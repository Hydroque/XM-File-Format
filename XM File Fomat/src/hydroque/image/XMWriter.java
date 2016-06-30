/*
 * Copyright (C) 2016  Polite Kiwi Programs
 * 
 * This file is part of a free library: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation, either version 3 of the
 * License.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * See the GNU General Public License for more details.
 * <http://www.gnu.org/licenses/>
 */
package hydroque.image;

import static hydroque.Bitwork.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import hydroque.image.data.Image;

/*
 * Responsible for writing xmi and xmm files
 * 
 */
public class XMWriter {

	/*
	 * Overload method for {@link writeXMI}
	 * 
	 */
	public static void writeXMI(Image image, String destination) throws IOException {
		writeXMI(image, new File(destination));
	}
	
	/*
	 * takes image data and writes out an XMI format file to destination
	 * 
	 * @param image the image data to write
	 * @param destination the output destination to write to
	 * 
	 */
	public static void writeXMI(Image image, File destination) throws IOException {
		destination.createNewFile();
		final FileOutputStream fos = new FileOutputStream(destination);
		fos.write(image.hasTransparency() ? 0b1 : 0b0);
		fos.write(new byte[]{
				(byte) ((image.getWidth() >>> 0) &0xFF),
				(byte) ((image.getWidth() >>> 8) &0xFF),
				(byte) ((image.getHeight()>>> 0) &0xFF),
				(byte) ((image.getHeight()>>> 8) &0xFF)
			});
		fos.write(image.getPixels());
		fos.flush();
		fos.close();
	}
	
	/*
	 * Overload method for {@link writeXMM}
	 * 
	 */
	public static void writeXMM(Image[] images, String destination) throws IOException {
		writeXMM(images, new File(destination));
	}
	
	/*
	 * takes multiple image data and writes out an XMM format file to destination
	 * 
	 * @param images the images data to write
	 * @param destination the output destination
	 * 
	 */
	public static void writeXMM(Image[] images, File destination) throws IOException {
		destination.createNewFile();
		final FileOutputStream fos = new FileOutputStream(destination);
		boolean transparency = false;
		for (final Image img : images)
			if((transparency = img.hasTransparency()))
				break;
		fos.write(transparency ? 0b1 : 0b0);
		fos.write(images.length);
		for (final Image img : images) {
			fos.write(intShortToByte(img.getWidth()));
			fos.write(intShortToByte(img.getHeight()));
		}
		for (final Image img : images) {
			fos.write(img.hasTransparency() ? 0b1 : 0b0);
			fos.write(img.getPixels());
		}
		fos.flush();
		fos.close();
	}
	
}
