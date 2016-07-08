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
import hydroque.Bitwork;
import hydroque.image.data.Image;

/*
 * Loads files into Image containers
 * 
 */
public class XMLoader {

	/*
	 * loads data from file location into ByteBuffer storage using PNGDecoder, and an image is generated.
	 * Provided for compatibility. It will iterate through the bytes to try to find transparency.
	 * 
	 * @param location file to be loaded in
	 * @param decoder PNGDecoder to utilize
	 * @param storage a mandatory buffer to load data into
	 * 
	 * @return Image generated
	 */
	public static Image loadImagePNG(PNGDecoder decoder, ByteBuffer storage) throws IOException {
		final int width = decoder.getWidth(), height = decoder.getHeight();
		decoder.decode(storage, decoder.getWidth() * 4);
		decoder.close();
		storage.flip();
		return new Image(width, height, Image.RGBA, Image.NONE, storage.array());
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
		final byte[] signiture = new byte[3];
		fis.read(signiture);
		if((char)signiture[0] != 'x' || (char)signiture[1] != 'm' || (char)signiture[2] != 'i') {
			fis.close();
			throw new IOException("File signiture is not XMI.");
		}
		final byte[] header = new byte[6];
		final byte[] count = new byte[4];
		fis.read(header);
		fis.read(count);
		final int byte_count = Bitwork.byteToInt(count);
		final byte[] body = new byte[byte_count];
		fis.read(body);
		fis.close();
		return new Image(
				Bitwork.byteToShort(header[1], header[0]),
				Bitwork.byteToShort(header[3], header[2]),
				header[4],
				header[5],
				body);
	}
	
	/*
	 * Overload method for {@link loadImageXMM}
	 * 
	 */
	public static Image[] loadImageXMM(String location) throws IOException {
		return loadImageXMM(new File(location));
	}
	
	/*
	 * loads data from file location and many images are generated, stored as a Mipmap object
	 * 
	 * @param location file to be loaded in
	 * 
	 * @return Mipmap generated
	 */
	public static Image[] loadImageXMM(File location) throws IOException {
		final FileInputStream fis = new FileInputStream(location);
		final byte[] signiture = new byte[3];
		fis.read(signiture);
		if((char)signiture[0] != 'x' || (char)signiture[1] != 'm' || (char)signiture[2] != 'm') {
			fis.close();
			throw new IOException("File signiture is not XMI.");
		}
		final byte[] header = new byte[3];
		fis.read(header);
		final Image[] images = new Image[header[0]];
		for (int i=0; i<header[0]; i++) {
			final byte[] count = new byte[4];
			final byte[] size = new byte[4];
			fis.read(count);
			fis.read(size);
			final int byte_count = Bitwork.byteToInt(count);
			final byte[] body = new byte[byte_count];
			fis.read(body);
			images[i] = new Image(Bitwork.byteToShort(size[1], size[0]), Bitwork.byteToShort(size[3], size[2]), header[1], header[2], body);
		}
		fis.close();
		return images;
	}
	
}
