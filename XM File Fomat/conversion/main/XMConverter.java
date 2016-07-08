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

public class XMConverter {

	/**
	 * Args
	 * 0	1		2			3			4
	 * xmi	format	compression	destination	{files, ...}
	 * xmi	format	compression	destination	folder
	 * 
	 * -- not supported --
	 * xmm	format	compression	destination {files, ...}
	 * xmm	format	compression	destination	folder
	 * 
	 * xmp destination {files, ...}
	 * xmp destination folder
	 * 
	 * dexmp destination source
	 * 
	 * Examples:
	 * xmi C:\Users\User\Desktop C:\Images\png\example.png
	 * xmi C:\Users\User\Desktop C:\Images\png\example.png C:\Images\png\example2.png
	 * xmi C:\Users\User\Desktop C:\Images\png
	 * 
	 * -- not supported --
	 * xmm C:\Users\User\Desktop C:\Images\png\example.png C:\Images\png\example2.png
	 * xmm C:\Users\User\Desktop C:\Images\png\
	 * 
	 * xmp C:\Users\User\Desktop\ C:\Images\xm\example.xmi
	 * xmp C:\Users\User\Desktop\ C:\Images\xm\example.xmi C:\Images\xm\example2.xmm
	 * xmp C:\Users\User\Desktop\ C:\Images\xm\
	 * 
	 * dexmp C:\Users\User\Desktop\output C:\Users\User\Desktop\packed_xm.xmp
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
				final File desti = new File(args[3]);
				if(!desti.exists() || !desti.isDirectory()) {
					System.err.println("Error: destination is not a valid directory. No files generated.");
					return;
				}
				final File source = new File(args[4]);
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
					files = new File[args.length - 4];
					for (int i=4; i<args.length; i++) {
						files[i-4] = new File(args[i]);
						if(!files[i-4].exists() || files[i-4].isDirectory()
								|| !args[i].split(Pattern.quote("."))[1].equalsIgnoreCase("png")) {
							System.err.println("Error: file " + args[i] + " is not a valid file. No fles generated.");
							return;
						}
					}
				}
				for (final File f : files) {
					final PNGDecoder decoder = new PNGDecoder(f, PNGDecoder.Format.RGBA);
					final ByteBuffer bb = ByteBuffer.allocate(decoder.getWidth() * decoder.getHeight() * 4);
					final Image data = XMLoader.loadImagePNG(decoder, bb);
					
					final int size = data.getWidth() * data.getHeight();
					Image out = null;
					byte[] actual_body = null;
					if(args[1].equals("1")) {
						actual_body = new byte[size];
						for (int i=0; i<size * 4; i+=4)
							actual_body[i/4] = data.getBody()[i];
						out = new Image(data.getWidth(), data.getHeight(), Image.MONOCHROME, Image.NONE, actual_body);
					}
					if(args[1].equals("3")) {
						actual_body = new byte[size * 3];
						int a = 0;
						for (int i=0; i<size * 4; i++) {
							actual_body[a++] = data.getBody()[i++];
							actual_body[a++] = data.getBody()[i++];
							actual_body[a++] = data.getBody()[i++];
						}
						out = new Image(data.getWidth(), data.getHeight(), Image.RGB, Image.NONE, actual_body);
					}
					if(args[1].equals("4"))
						out = data;
					XMWriter.writeXMI(out, new File(desti, f.getName() + ".xmi"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "xmm": // Convert PNGs or XMIs to XMM
			try {
				final File desti = new File(args[3]);
				if(!desti.exists() || !desti.isDirectory()) {
					System.err.println("Error: destination is not a valid directory. No files generated.");
					return;
				}
				final File source = new File(args[4]);
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
					files = new File[args.length - 4];
					for (int i=4; i<args.length; i++) {
						files[i-4] = new File(args[i]);
						final String[] split = args[i].split(Pattern.quote("."));
						if(!files[i-4].exists() || files[i-4].isDirectory()
								|| (   !split[1].equalsIgnoreCase("png")
									&& !split[1].equalsIgnoreCase("xmi"))) {
							System.err.println("Error: file " + args[i] + " is not a valid file. No fles generated.");
							return;
						}
					}
				}
				final Image[] images = new Image[files.length];
				for (int i=0; i<files.length; i++) {
					final String file_type = files[i].getName().split(Pattern.quote("."))[1];
					if(file_type.equalsIgnoreCase("png")) {
						final PNGDecoder decoder = new PNGDecoder(files[i], PNGDecoder.Format.RGBA);
						final ByteBuffer bb = ByteBuffer.allocate(decoder.getWidth() * decoder.getHeight() * 4);
						
						final Image data = XMLoader.loadImagePNG(decoder, bb);
						final int size = data.getWidth() * data.getHeight();
						byte[] actual_body = null;
						if(args[1].equals("1")) {
							actual_body = new byte[size];
							for (int a=0; a<size * 4; a+=4)
								actual_body[a/4] = data.getBody()[a];
							images[i] = new Image(data.getWidth(), data.getHeight(), Image.MONOCHROME, Image.NONE, actual_body);
						}
						if(args[1].equals("3")) {
							actual_body = new byte[size * 3];
							int a = 0;
							for (int j=0; j<size * 4; j++) {
								actual_body[a++] = data.getBody()[j++];
								actual_body[a++] = data.getBody()[j++];
								actual_body[a++] = data.getBody()[j++];
							}
							images[i] = new Image(data.getWidth(), data.getHeight(), Image.RGB, Image.NONE, actual_body);
						}
						if(args[1].equals("4"))
							images[i] = data;
					}
					if(file_type.equalsIgnoreCase("xmi"))
						images[i] = XMLoader.loadImageXMI(files[i]);
				}
				XMWriter.writeXMM(images, new File(desti, files[0].getName() + ".xmm"));
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
							System.err.println("Error: file " + args[i] + " is not a valid file. No files generated.");
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
				System.out.println("ColorModel: " + img.getColorModel());
				System.out.println("Compression: " + img.getCompression());
				System.out.println("Data Size: " + img.getBody().length);
			} catch (IOException e) {
				System.err.println("Couldn't load/verify XMI file.");
				e.printStackTrace();
			}
			break;
		case "vxmm": // verify XMM file
			try {
				final Image[] map = XMLoader.loadImageXMM(args[1]);
				System.out.println("Mipmap Levels: " + map.length);
				System.out.println("Definitive color model: " + map[0].getColorModel());
				System.out.println("Definitive compression: " + map[0].getCompression());
				for(int i=0; i<map.length; i++) {
					final Image sub = map[i];
					System.out.println("Sub Image " + i);
					System.out.println("Width: " + sub.getWidth());
					System.out.println("Height: " + sub.getHeight());
					System.out.println("Data size: " + sub.getBody().length);
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
