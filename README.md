Tinybacterus
============

A simple shooter game that I made for the Ludum Dare 23 competition.

Requirements
============

To run the game you need the slick2d library with updated lwjgl files. That means; download slick2d and lwjgl separately. Then overwrite the old lwjgl files that come with slick2d, with the newer ones from the lwjgl site.

How-to
======

Create a folder with the native files for your operating system. Your java library path have to point to that. So set the following as a VM option:

-Djava.library.path=nativefolder/

Then you have to add three jar's to the classpath. The files that need to be included are slick.jar and lwjgl.jar.