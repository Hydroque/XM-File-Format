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
package hydroque.compression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMPacker {

	/* 
	 * Compacts 4 bytes into an int
	 * 
	 * @return the result
	 */
	private static int byteToInt(byte a, byte b, byte c, byte d) {
		return ((a&0xFF) << 24 | (b&0xFF) << 16 | (c&0xFF) << 8 | (d&0xFF));
	}
	
	/*
	 * Overload method for {@link byteToInt}
	 * 
	 * @param a the list of bytes to be converted
	 * 
	 * @return the result of {@link byteToInt}
	 */
	private static int byteToInt(byte[] a) {
		return byteToInt(a[3], a[2], a[1], a[0]);
	}
	
	/*
	 * Creates a new byte array with the 4 byte segments of an int
	 * 
	 * @return the result
	 */
	private static byte[] intToByte(int a) {
		return new byte[] {
			(byte) (a >>> 0),
			(byte) (a >>> 8),
			(byte) (a >>> 16),
			(byte) (a >>> 24)
		};
	}
	
	/*
	 * Converts a byte array to an int array. It is manditory that: a.length % 4 == 0
	 * 
	 * @param the list of bytes
	 * 
	 * @return a int array, where each int is 4 bytes of the array. See {@link byteToInt}
	 */
	private static int[] byteRangeToInt(byte[] a) {
		final int[] out = new int[a.length/4];
		int pointer = 0;
		for (int i=0; i<a.length; i+=4)
			out[pointer++] = byteToInt(a[i], a[i+1], a[i+2], a[i+3]);
		return out;
	}
	
	/*
	 * Overload method for {@link pack}
	 * 
	 */
	public static void pack(String[] files, String destination) throws IOException {
		pack(files, new File(destination));
	}
	
	/*
	 * Overload method for {@link pack}
	 * Converts String[] list into File objects first.
	 * 
	 */
	public static void pack(String[] files, File destination) throws IOException {
		final File[] file_store = new File[files.length];
		for (int i=0; i<files.length; i++)
			file_store[i] = new File(files[i]);
		pack(file_store, destination);
	}
	
	/*
	 * Overload method for {@link pack}
	 */
	public static void pack(File[] files, String destination) throws IOException {
		pack(files, new File(destination));
	}
	
	/*
	 * Takes in a list of files, which are packed into a temp file. Then it is sent through deflation.
	 * The result is stored in the destination variable.
	 * 
	 * @param files Files to be gathered and deflated.
	 * @param destination Output file
	 * 
	 * @return the result
	 */
	public static void pack(File[] files, File destination) throws IOException {
		final File temp = File.createTempFile("xmp_gen" + System.currentTimeMillis(), ".tmp");
		temp.createNewFile();
		final FileOutputStream temps = new FileOutputStream(temp);
		temps.write(intToByte(files.length));
		for (int i=2; i<files.length; i++) {
			if(files[i].length() > Integer.MAX_VALUE) {
				System.err.println("Error: File size > Integer.MAX_VALUE! No file generated.");
				temps.close();
				temp.delete();
				return;
			}
			temps.write(intToByte((int) files[i].length()));
			temps.write(intToByte(files[i].getName().getBytes().length));
		}
		byte[] buffer = new byte[4096];
		for (int i=0; i<files.length; i++) {
			final FileInputStream fis = new FileInputStream(files[i]);
			temps.write(files[i].getName().getBytes());
			int count;
			while((count = fis.read(buffer)) != -1)
				temps.write(buffer, 0, count);
			fis.close();
		}
		temps.flush();
		temps.close();
		destination.createNewFile();
		Zipper.zip(temp, destination);
		temp.delete();
	}
	
	/*
	 * Overload method for {@link unpack}
	 * 
	 */
	public static void unpack(File source, String destination) throws IOException {
		unpack(source, new File(destination));
	}
	
	/*
	 * Overload method for {@link unpack}
	 * 
	 */
	public static void unpack(String source, String destination) throws IOException {
		unpack(new File(source), new File(destination));
	}
	
	/*
	 * Overload method for {@link unpack}
	 * 
	 */
	public static void unpack(String source, File destination) throws IOException {
		unpack(new File(source), destination);
	}
	
	/*
	 * Given that the source points to an XMP file, source is deflated into a temp file. The temp file
	 * is read and each entry will be unpacked into new files in root destination.
	 * 
	 * @param source
	 * @param destination
	 * 
	 */
	public static void unpack(File source, File destination) throws IOException {
		final File vtemp = File.createTempFile("xmp_gen" + System.currentTimeMillis(), ".tmp");
		vtemp.createNewFile();
		Zipper.unzip(source, vtemp, true);
		byte[] one = new byte[4];
		final FileInputStream sfis = new FileInputStream(vtemp);
		sfis.read(one);
		final int files = byteToInt(one);
		final byte[] lenheader = new byte[files * 8];
		sfis.read(lenheader);
		final int[] lenints = byteRangeToInt(lenheader);
		for (int i=0; i<files; i++) {
			final byte[] name = new byte[lenints[i*2+1]], fbytes = new byte[lenints[i*2]];
			sfis.read(name);
			sfis.read(fbytes);
			final File out = new File(destination, new String(name));
			final FileOutputStream xmpfos = new FileOutputStream(out);
			xmpfos.write(fbytes);
			xmpfos.flush();
			xmpfos.close();
		}
		sfis.close();
		vtemp.delete();
	}
	
}
