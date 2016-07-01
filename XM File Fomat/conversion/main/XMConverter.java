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
package main;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

import de.matthiasmann.twl.utils.PNGDecoder;
import hydroque.compression.XMPacker;
import hydroque.image.XMLoader;
import hydroque.image.XMWriter;
import hydroque.image.data.Image;
import hydroque.image.data.Mipmap;

public class XMConverter {

	/**
	 * Args
	 * 0	1			2
	 * xmi destination {files, ...}
	 * xmi destination folder
	 * 
	 * xmm destination {files, ...}
	 * xmm destination folder
	 * 
	 * xmp destination {files, ...}
	 * xmp destination folder
	 * 
	 * dexmp source destination
	 * 
	 * Examples:
	 * xmi C:\Users\User\Desktop C:\Images\png\example.png
	 * xmi C:\Users\User\Desktop C:\Images\png\example.png C:\Images\png\example2.png
	 * xmi C:\Users\User\Desktop C:\Images\png
	 * 
	 * xmm C:\Users\User\Desktop C:\Images\png\example.png C:\Images\png\example2.png
	 * xmm C:\Users\User\Desktop C:\Images\png\
	 * 
	 * xmp C:\Users\User\Desktop\ C:\Images\xm\example.xmi
	 * xmp C:\Users\User\Desktop\ C:\Images\xm\example.xmi C:\Images\xm\example2.xmm
	 * xmp C:\Users\User\Desktop\ C:\Images\xm\
	 * 
	 * dexmp C:\Users\User\Desktop\packed_xm.xmp C:\Users\User\Desktop\output
	 * 
	 * vxmi C:\xm\example.xmi
	 * 
	 * vxmm C:\xm\example.xmm
	 * 
	 */
	
	public static void main(String[] args) {
		if(args.length < 1)
			args = new String[]{"?"};
		
		switch(args[0].toLowerCase()) {
		case "xmi": // convert PNG to XMI
			try {
				final File desti = new File(args[1]);
				if(!desti.exists() || !desti.isDirectory()) {
					System.err.println("Error: destination is not a valid directory. No files generated.");
					return;
				}
				final File source = new File(args[2]);
				if(!source.exists()) {
					System.err.println("Error: source is not a valid directory or file. No files generated.");
					return;
				}
				File[] files = null;
				if(source.isDirectory()) {
					files = source.listFiles((pathname) ->
							!pathname.isDirectory()
						&&  pathname.getName().split(Pattern.quote("."))[1].equalsIgnoreCase("png")
					);
				} else {
					files = new File[args.length - 2];
					for (int i=2; i<args.length; i++) {
						files[i-2] = new File(args[i]);
						if(!files[i-2].exists() || files[i-2].isDirectory()
								|| !args[i].split(Pattern.quote("."))[1].equalsIgnoreCase("png")) {
							System.err.println("Error: file " + args[i] + " is not a valid file. No fles generated.");
							return;
						}
					}
				}
				System.out.println("Files: " + files.length);
				for (final File f : files) {
					final PNGDecoder decoder = new PNGDecoder(f, PNGDecoder.Format.RGBA);
					final ByteBuffer bb = ByteBuffer.allocate(decoder.getWidth() * decoder.getHeight() * 4);
					final Image data = XMLoader.loadImagePNG(decoder, bb);
					XMWriter.writeXMI(data, new File(desti, f.getName() + ".xmi"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "xmm": // Convert PNGs or XMIs to XMM
			try {
				final File desti = new File(args[1]);
				if(!desti.exists() || !desti.isDirectory()) {
					System.err.println("Error: destination is not a valid directory. No files generated.");
					return;
				}
				final File source = new File(args[2]);
				if(!source.exists()) {
					System.err.println("Error: source is not a valid directory or file. No files generated.");
					return;
				}
				File[] files = null;
				if(source.isDirectory()) {
					files = source.listFiles((pathname) -> {
						final String[] split = pathname.getName().split(Pattern.quote("."));
						return !pathname.isDirectory() &&
								 	(
									split[1].equalsIgnoreCase("png")
								||	split[1].equalsIgnoreCase("xmi")
									);
					});
				} else {
					files = new File[args.length - 2];
					for (int i=2; i<args.length; i++) {
						files[i-2] = new File(args[i]);
						final String[] split = args[i].split(Pattern.quote("."));
						if(!files[i-2].exists() || files[i-2].isDirectory()
								|| (   !split[1].equalsIgnoreCase("png")
									&& !split[1].equalsIgnoreCase("xmi"))) {
							System.err.println("Error: file " + args[i] + " is not a valid file. No fles generated.");
							return;
						}
					}
				}
				final Image[] data = new Image[files.length];
				for (int i=0; i<files.length; i++) {
					final String file_type = files[i].getName().split(Pattern.quote("."))[1];
					if(file_type.equalsIgnoreCase("png")) {
						final PNGDecoder decoder = new PNGDecoder(files[i], PNGDecoder.Format.RGBA);
						final ByteBuffer bb = ByteBuffer.allocate(decoder.getWidth() * decoder.getHeight() * 4);
						data[i] = XMLoader.loadImagePNG(decoder, bb);
					}
					if(file_type.equalsIgnoreCase("xmi"))
						data[i] = XMLoader.loadImageXMI(files[i]);
				}
				XMWriter.writeXMM(data, new File(desti, files[0].getName() + ".xmm"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "xmp": // pack all given files into XMP
			try {
				final File desti = new File(args[1]);
				if(!desti.exists() || !desti.isDirectory()) {
					System.err.println("Error: destination is not a valid directory. No files generated.");
					return;
				}
				final File source = new File(args[2]);
				if(!source.exists()) {
					System.err.println("Error: source is not a valid directory or file. No files generated.");
					return;
				}
				File[] files = null;
				if(source.isDirectory()) {
					files = source.listFiles((pathname) -> !pathname.isDirectory());
				} else {
					files = new File[args.length - 2];
					for (int i=2; i<args.length; i++) {
						files[i-2] = new File(args[i]);
						if(!files[i-2].exists() || files[i-2].isDirectory()) {
							System.err.println("Error: file " + args[i] + " is not a valid file. No fles generated.");
							return;
						}
					}
				}
				XMPacker.pack(files, new File(desti, files[0].getName() + ".xmp"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "dexmp": // unpack given XMP
			try {
				XMPacker.unpack(args[2], args[1]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "vxmi": // verify XMI file
			try {
				final Image img = XMLoader.loadImageXMI(args[1]);
				System.out.println("Width: " + img.getWidth());
				System.out.println("Height: " + img.getHeight());
				System.out.println("Transparency: " + img.hasTransparency());
			} catch (IOException e) {
				System.err.println("Couldn't load/verify XMI file.");
				e.printStackTrace();
			}
			break;
		case "vxmm": // verify XMM file
			try {
				final Mipmap map = XMLoader.loadImageXMM(args[1]);
				System.out.println("Mipmap Levels: " + map.getLevels());
				System.out.println("Global Transparency: " + map.hasTransparency());
				for(int i=0; i<map.getImages().length; i++) {
					final Image sub = map.getImages()[i];
					System.out.println("Sub Image " + i);
					System.out.println("Width: " + sub.getWidth());
					System.out.println("Height: " + sub.getHeight());
					System.out.println("Local Transparency: " + sub.hasTransparency());
				}
			} catch (IOException e) {
				System.err.println("Couldn't load/verify XMM file.");
				e.printStackTrace();
			}
			break;
		default:
			System.out.println("Invalid arguments/incorrect operation.");
			break;
		}
	}
	
}
