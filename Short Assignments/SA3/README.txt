PROJECT AUTHOR: AMITTAI JOEL WEKESA
ORIGINAL AUTHOR: PROF. CHRIS BAILEY-KELLOGG
DATE: JANUARY 19, 2021
PURPOSE: COSC 10, SHORT ASSIGNMENT 3

WORK DONE:
Implemented a method that reads the RGB values of each pixel within a 10-by-10 square of the cursor,
rescales the values, and repaints the pixels with the monochrome version of the color.
Essentially, it turns brushed areas from color to monochrome.

INSTRUCTIONS:

1. Please copy the two modified files (provided) into the package or project containing the other files.
    The two files have dependencies on the others.
    Just in case, I have included the other files in a folder in the zip file.


2.  * To start the program, run ImageProcessingGUI0.java

3. Once running, please check that the target image is in a folder/package named "pictures" inside the project.
    By default, the code is set to look for an image titled "baker.jpg" inside the pictures folder.
    If you would like to change this, please do the following:
     (i). Open ImageProcessingGUI0.java
     (ii). Find line 90 --> inside the "main" method
     (iii). Change final String filename from "pictures/baker.jpg" to whichever desired filename and path in the project.

     Disclaimer: Please make sure you get the path right, or the file will not be found.

4. Once running, the controls are as follows:
    (i). Press the Space Bar to activate or deactivate the brush.
    (ii). Press 'm' to activate monochrome mode.

    Note: pressing 'm' again won't deactivate monochrome mode (as with the space-bar).
    To deactivate monochrome mode, press any other key that is not SPACE or m
