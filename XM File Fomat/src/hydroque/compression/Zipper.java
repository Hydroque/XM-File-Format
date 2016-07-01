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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*
 * Responsible for inflation and deflation of files.
 * 
 */
public class Zipper {

	/*
	 * Overload method for {@link zip}
	 * 
	 */
	public static long zip(String[] files, String destination) throws IOException {
		return zip(files, new File(destination));
	}

	/*
	 * Overload method for {@link zip}
	 * Converts String[] files into File objects first.
	 * 
	 */
	public static long zip(String[] files, File destination) throws IOException {
		final File[] file_store = new File[files.length];
		for (int i=0; i<files.length; i++)
			file_store[i] = new File(files[i]);
		return zip(file_store, destination);
	}

	/*
	 * Overload method for {@link zip}
	 * 
	 */
	public static long zip(File[] files, String destination) throws IOException {
		return zip(files, new File(destination));
	}

	/*
	 * Opens the destination into a checked output stream for checksum generation. Data is loaded in
	 * from the files. Has a hard buffer of 4096. This is a batch method.
	 * 
	 * @param files the files to be inflated
	 * @param destination the zip destination for the inflated files
	 * 
	 * @return the checksum generated
	 */
	public static long zip(File[] files, File destination) throws IOException {
		final CheckedOutputStream checksum = new CheckedOutputStream(new FileOutputStream(destination), new Adler32());
		final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
		
		byte[] data = new byte[4096];
		for (int i=0; i<files.length; i++) {
			final BufferedInputStream origin = new BufferedInputStream(new FileInputStream(files[i]), 4096);
			final ZipEntry entry = new ZipEntry(files[i].getAbsolutePath());
			out.putNextEntry(entry);
			int count;
			while((count = origin.read(data)) != -1)
				out.write(data, 0, count);
			origin.close();
		}
		
		out.close();
		checksum.close();
		return checksum.getChecksum().getValue();
	}
	
	/*
	 * Opens the destination into a checked output stream for checksum generation. Data is loaded in
	 * from the files. Has a hard buffer of 4096. This is not a batch method.
	 * 
	 * @param input the files to be inflated
	 * @param destination the zip destination for the inflated files
	 * 
	 * @return the checksum generated
	 */
	public static long zip(File input, File destination) throws IOException {
		final CheckedOutputStream checksum = new CheckedOutputStream(new FileOutputStream(destination), new Adler32());
		final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
		
		byte[] data = new byte[4096];
		final BufferedInputStream origin = new BufferedInputStream(new FileInputStream(input), 4096);
		final ZipEntry entry = new ZipEntry(input.getAbsolutePath());
		out.putNextEntry(entry);
		int count;
		while((count = origin.read(data)) != -1)
			out.write(data, 0, count);
		origin.close();
		
		out.close();
		checksum.close();
		return checksum.getChecksum().getValue();
	}
	
	/*
	 * Overload method for {@link unzip}
	 * 
	 */
	public static long unzip(File source, String destination, boolean takeone) throws IOException {
		return unzip(source, new File(destination), takeone);
	}
	
	/*
	 * Overload method for {@link unzip}
	 * 
	 */
	public static long unzip(String source, String destination, boolean takeone) throws IOException {
		return unzip(source, new File(destination), takeone);
	}
	
	/*
	 * Overload method for {@link unzip}
	 * 
	 */
	public static long unzip(String source, File destination, boolean takeone) throws IOException {
		return unzip(new File(source), destination, takeone);
	}
	
	/*
	 * Opens the source into a checked output stream for checksum generation. Data is loaded out
	 * to destination. Has a hard buffer of 4096. This can be a batch method for the extraction using
	 * the takeone boolean.
	 * 
	 * @param source the file to be deflated
	 * @param destination the folder destination for the deflated files
	 * @param takeone controls whether only one file should be deflated and relocated into destination
	 * 
	 * @return the checksum generated
	 */
	public static long unzip(File source, File destination, boolean takeone) throws IOException {
		final CheckedInputStream checksum = new CheckedInputStream(new FileInputStream(source), new Adler32());
		final ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
		
		byte[] data = new byte[4096];
		
		ZipEntry entry;
		while((entry = zis.getNextEntry()) != null) {
			BufferedOutputStream dest = null;
			if(takeone) dest = new BufferedOutputStream(new FileOutputStream(destination));
			else dest = new BufferedOutputStream(new FileOutputStream(
					new File(destination, entry.getName().substring(entry.getName().lastIndexOf("\\")))), 4096);
			
			int count;
			while((count = zis.read(data)) != -1)
				dest.write(data, 0, count);
			
			dest.flush();
			dest.close();
			
			if(takeone) break;
		}
		
		zis.close();
		checksum.close();
		return checksum.getChecksum().getValue();
	}
	
}
