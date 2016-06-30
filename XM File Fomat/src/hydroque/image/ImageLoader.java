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
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import hydroque.texture.data.Image;
import hydroque.texture.data.Mipmap;

/*
 * Loads files into Image containers
 * 
 */
public class ImageLoader {

	/*
	 * Overload method for {@link loadImagePNG}
	 * 
	 */
	public static Image loadImagePNG(String location, ByteBuffer storage) throws IOException {
		return loadImagePNG(new File(location), storage);
	}
	
	/*
	 * loads data from file location into ByteBuffer storage using PNGDecoder, and an image is generated.
	 * Provided for compatibility.
	 * 
	 * @param location file to be loaded in
	 * @param storage a manditory buffer to load data into
	 * 
	 * @return Image generated
	 */
	public static Image loadImagePNG(File location, ByteBuffer storage) throws IOException {
		final FileInputStream fis = new FileInputStream(location);
		final PNGDecoder decoder = new PNGDecoder(fis);
		final int width = decoder.getWidth(), height = decoder.getHeight();
		decoder.decode(storage, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
		fis.close();
		return new Image(width, height, true, storage.array());
	}
	
	/*
	 * Overload method for {@link loadImageXMI}
	 * 
	 */
	public static Image loadImageXMI(String location) throws IOException {
		return loadImageXMI(new File(location));
	}
	
	/*
	 * loads data from file location and an image is generated
	 * 
	 * @param location file to be loaded in
	 * 
	 * @return Image generated
	 */
	public static Image loadImageXMI(File location) throws IOException {
		final FileInputStream fis = new FileInputStream(location);
		final byte[] header = new byte[5];
		fis.read(header);
		final boolean transparency = header[0] == 1;
		final int width = (header[2]&0xFF)<<8 | (header[1]&0xFF),
				 height = (header[4]&0xFF)<<8 | (header[3]&0xFF);
		final byte[] body = new byte[width * height * 4];
		fis.read(body);
		fis.close();
		return new Image(width, height, transparency, body);
	}
	
	/*
	 * Overload method for {@link loadImageXMM}
	 * 
	 */
	public static Mipmap loadImageXMM(String location) throws IOException {
		return loadImageXMM(new File(location));
	}
	
	/*
	 * loads data from file location and many images are generated, stored as a Mipmap object
	 * 
	 * @param location file to be loaded in
	 * 
	 * @return Mipmap generated
	 */
	public static Mipmap loadImageXMM(File location) throws IOException {
		final FileInputStream fis = new FileInputStream(location);
		final byte[] header = new byte[2];
		fis.read(header);
		final byte[] sizes = new byte[header[1]*2*2];
		fis.read(sizes);
		final Image[] images = new Image[header[1]];
		for (int i=0; i<header[1]; i++) {
			final boolean transparency = fis.read() == 1;
			final int width = (sizes[i*4+1]&0xFF)<<8 | (sizes[i*4]&0xFF),
					 height = (sizes[i*4+3]&0xFF)<<8 | (sizes[i*4+2]&0xFF);
			final byte[] body = new byte[width * height * 4];
			fis.read(body);
			images[i] = new Image(width, height, transparency, body);
		}
		fis.close();
		return new Mipmap(header[0] == 1, images);
	}
	
}
