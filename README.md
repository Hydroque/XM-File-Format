# XM Files Format
Simple files. Faster loading. Better storage. Video game quality file formats and compression.

This library is aimed at video games, but does have uses outside of video games - like normal images. Default support for these file types are unheard of. Don't be expecting to load these files in programs that don't use this library, yet. The plan is to have plugins for editors. It also features file compression. Working on Paint.NET plugin, soon Photoshop.

### Featured file formats
XMI - image file format

XMM - mipmap file format

XMP - compressed XM-file pack

### Purpose
XMI is a smart image file format. Utilizing it in applications, you reduce you have more control of your data - whether you just want a 1-byte stream, 3-byte stream, or 4 byte stream. Comes with an indicator of which compression method it uses for you to decompress - implement any compression method you want!

XMM is a compilation of XMI format images, ordered in a way to utilize XMI for custom, user-generated mipmapping. This saves on a lot of blank space in texture atlases and the overhead of calculating the bounds. If you don't like auto-generated mipmaps, this is something of interest.

XMP is a neat way to transfer files. You pack XM files into one file for fast transfer, then unpack on install. This allows for the ability to give the user the option to keep the files packed, too. Packing files compresses them using inflate/deflate. This isn't your typical PNG compression method on a subset of data, this is the WHOLE file - add as many non-xm files you want as long as they are less than Integer.MAX_VALUE.

### Quick Start Guide

The classes you will be using are XMLoader, XMWriter, XMPacker, and the resulting return container Image. XMLoader takes in the path of the file to load (XMI, XMM, PNG) and generates an Image or Image[], depending on the file format. XMWriter takes in an Image or Image[] and outputs to their respective files.

The container Image holds the width, height, ordered byte array of pixels, and a number indicating the number of color channels (monochrome 1, RGB 3, RGBA 4). In an array of Image[], it is assume that index 0 is the base, or highest resolution. It is also assumed the rest of the Image follow the same color model and compression, although never explicitly dictated.

XMPacker takes in a bunch of files, or a directory of files, and packs them all together. Simple enough. Reversed with the unpack method. It uses the Zipper class to do the compression using Zip Streams.

You will need both jars 'ImageLoader.jar' and 'XMConverter.jar'. I take it you probably don't want to make an XMConverter like I did. That was a nice bit of ugly, spewed code. Utilize XMConverter to generate XMI, XMM, and XMP files from PNG (or XMI too in the case of XMM) files. XMConverter uses ImageLoader. You will need to have both jars next to each other, or you need to modify XMConverter's MANIFEST to point to the ImageLoader jar. XMConverter is a command line application. Look below for a reference to the commands. Placing the source from ImageLoader into XMConverter.jar works entirely, for the packing is the same. This makes it able to occlude ImageLoader jar, but you'll need it anyways.

	/**
	 * Args
	 * 0	1		2			3			4
	 * xmi	format	compression	destination	{files, ...}
	 * xmi	format	compression	destination	folder
	 * 
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


### TODO
XMH is a file format that I am working on. It replaces the need of image file formats for heightmaps in video games. There needs to be a way to actually generate the XMH files, so I am making a terrain editor, featuring it. Once I get everything all sorted out, I am interested in making plugins for other applications to get the format out.

XMV is a file format that has the full functionality of GIF. It is going to go in development sometime in the future. It is time we had a conservative motion image, especially for games! RIP AVI.

I am working on a plugin to directly export and load XMI files. It may be possible to use layers for XMM files. In the future, looking at photoshop for this stuff.
