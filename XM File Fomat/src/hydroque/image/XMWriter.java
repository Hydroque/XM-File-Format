package hydroque.image;

import static hydroque.Util.*;

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
