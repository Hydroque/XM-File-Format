# XM Files Format
Simple files. Faster loading. Better storage. Video game file formats and compression.

This library is aimed at video games, but does have uses outside of video games - like normal images. Default support for these file types are unheard of. Don't be expecting to load these files in programs that don't use this library, yet. The plan is to have plugins for editors. It also features file compression.

### Featured file formats
XMI - image file format

XMM - mipmap file format

XMP - compressed XM-file pack

### Purpose
XMI is a raw image file format. Utilizing it in applications, you reduce your deflate (and load anyways) overhead.

XMM is a compilation of XMI format images, ordered in a way to utilize XMI for custom user-generated mipmapping.

XMP is a neat way to transfer files. You pack XM files into one file for fast transfer, then unpack on install. This allows for the ability to give the user the option to keep the files packed, too.

### Quick Start Guide

The classes you will be using are XMLoader, XMWriter, XMPacker, and the resulting return containers Image and Mipmap. XMLoader takes in the path of the file to load (XMI, XMM) and generates either an Image or Mipmap, depending on the file format. XMWriter takes in an Image or Mipmap and outputs to their respective files.

The container Image holds the width, height, ordered byte array of pixels, and a boolean to whether any alpha pixel is, but translucent. The container Mipmap holds a 'global boolean' for all files to indicate if at least one of them is but translucent, and an array of said Image container. The order of mipmap resolution isn't 0-length where index 'length' is the highest resolution. Its user defined or machine made. It can be length-0.

XMPacker takes in a bunch of files, or a directory of files, and packs them all together. Simple enough. Reversed with the unpack method. It uses the Zipper class to do the compression using Zip Streams.

You will need both jars 'ImageLoader.jar' and 'XMConverter.jar'. I take it you probably don't want to make an XMConverter like I did. That was a nice bit of code. Utilize XMConverter to generate XMI and XMM files from PNG files. XMConverter uses ImageLoader. You will need to have both jars next to each other, or you need to modify XMConverter's MANIFEST to point to the ImageLoader jar. XMConverter is a command line application. Look below for a reference to the commands.

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

### TODO
XMH is a file format that I am working on. It replaces the need of image file formats for heightmaps in video games. There needs to be a way to actually generate the XMH files, so I am making a terrain editor, featuring it. Once I get everything all sorted out, I am interested in making plugins for other applications to get the format out.
