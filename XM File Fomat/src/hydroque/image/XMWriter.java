package hydroque.image;

import static hydroque.Util.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import hydroque.image.data.Image;

public class XMWriter {

	public static void writeXMI(Image image, String destination) throws IOException {
		writeXMI(image, new File(destination));
	}
	
	public static void writeXMI(Image image, File destination) throws IOException {
		destination.createNewFile();
		final FileOutputStream fos = new FileOutputStream(destination);
		fos.write(image.hasTransparency() ? 0b1 : 0b0);
		fos.write(new byte[]{
				(byte) ((image.getWidth()&0xFF) >>> 8),
				(byte) ((image.getWidth()&0xFF) >>> 0),
				(byte) ((image.getHeight()&0xFF) >>> 8),
				(byte) ((image.getHeight()&0xFF) >>> 0)
			});
		fos.write(image.getPixels());
		fos.close();
	}
	
	public static void writeXMM(Image[] image, String destination) throws IOException {
		writeXMM(image, new File(destination));
	}
	
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
		fos.close();
	}
	
}
