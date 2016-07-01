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

### TODO
XMH is a file format that I am working on. It replaces the need of image file formats for heightmaps in video games. There needs to be a way to actually generate the XMH files, so I am making a terrain editor, featuring it. Once I get everything all sorted out, I am interested in making plugins for other applications to get the format out.
