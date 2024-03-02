Reflection Plotter

-----------------------------------------------
-----------------------------------------------
-----------------------------------------------

Code to convert the compressed Reflection .svg files that use fonts and libraries
to plotter friendly .svg files where all line art is written out.

-----------------------------------------------
-----------------------------------------------
-----------------------------------------------

There are 2 java classes that read in an SVG file
and output 16 layers that can be plotted on a plotter printer

The code is written by an artist (me), so please excuse all
the anti-patterns. Would definitely welcome a refactor that improves
it, or even ports it into python.

The 2 classes are

SplitSvgIntoLayers.java which: 
  1: reads in a Reflection .svg file
  2: Parses it into the 16 layers
  3: Sends each layer into MakeBackgroundsFromClipmask.java to get the line art as svg.
  4: saves each layer as a standalone .svg file that can be easily read by plotters

MakeSvgBackgroundFromClipmask.java which:
  1: takes in svg that was compressed to be onchain
  2: uncompresses it by filling in the data (often in fonts/libraries) with actual line art
  3: returns the line art as svg

Important notes:

1: MakeSvgBackgroundFromClipmask does not return the exact hex/binary or original,
   but thats the cool part. 
2: I am improving as needed and adding layers as needed, so conversion is not complete yet.

-----------------------------------------------
-----------------------------------------------
-----------------------------------------------

Folder svgsamples contains 3 Reflection .svg files saved off the contract
Folder outputlayers is sample of what the output should look like
