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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import hydroque.Bitwork;
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
		final byte[] width_b = Bitwork.intToByte(image.getWidth()),
				height_b = Bitwork.intToByte(image.getHeight());
		fos.write(new byte[]{(byte)'x', (byte)'m', (byte)'i'});
		fos.write(new byte[]{
				width_b[0],
				width_b[1],
				height_b[0],
				height_b[1]
			});
		fos.write(image.getColorModel());
		fos.write(image.getCompression());
		fos.write(Bitwork.intToByte(image.getBody().length));
		fos.write(image.getBody());
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
		fos.write(new byte[]{(byte)'x', (byte)'m', (byte)'m'});
		fos.write(images.length);
		fos.write(images[0].getColorModel());
		fos.write(images[0].getCompression());
		for (final Image img : images) {
			final byte[] width_b = Bitwork.intToByte(img.getWidth()),
					height_b = Bitwork.intToByte(img.getHeight());
			fos.write(Bitwork.intToByte(img.getBody().length));
			fos.write(new byte[]{
					width_b[0],
					width_b[1],
					height_b[0],
					height_b[1]
				});
			fos.write(img.getBody());
		}
		fos.flush();
		fos.close();
	}
	
}
