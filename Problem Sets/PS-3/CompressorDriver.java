import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Scanner;

/**
 * Driver for the compression algorithm.
 * Rather than manually create instances, I added a static method in Compressor class
 * that handles the instance creations and calls to methods.
 * All that's needed to is to call Compressor.engine() with the filename and the mode.
 *
 * For decompression, the engine will try to re-access the dictionary that was built during compression.
 * To run decompression on a not-recently-compressed file, make sure the original is in the "inputs/" directory
 * so that the system can rebuild the dictionary
 */
public class CompressorDriver {
    public static void main(String[] args) throws IOException {
        String constitution = "inputs/USConstitution.txt";
        String testFile = "inputs/testfile.txt";
        String emptyFile = "inputs/empty.txt";
        String singleCharFile = "inputs/onechar.txt";
//        String warpeace = "inputs/WarAndPeace.txt";

        // TODO: Compress
//        Compressor.engine(constitution, "compress");

        // TODO: Decompress
        //  NOTE
        //  For the decompression part, pass in the name of the compressed file without "_compressed" tag.
        //  It will automatically refactor the name and add the tag.
        //  Adding "_compressed" will make the system look for "filename_compressed_compressed.txt"
//        Compressor.engine(constitution, "decompress");


        // TODO Debugging
        //  debug mode tests compress and decompress, and compares the decompressed file against the original file.
        System.out.println("Which test do you want to run? please type in an integer... 1 to 6");
        Scanner input = new Scanner(System.in);
        int test = input.nextInt();
        switch (test) {
            case 1 -> Compressor.engine("", "debug");      // test with empty filename
            case 2 -> Compressor.engine(emptyFile, "debug");        // test with empty file
            case 3 -> Compressor.engine(singleCharFile, "debug");   // test with single char file
            case 4 -> Compressor.engine(testFile, "debug");         // test with a proper (but shorter) file
            case 5 -> Compressor.engine(constitution, "debug");     // test with US constitution
            case 6 -> {     // enter name of file to test
                System.out.println("Please type in the name & directory of the file you wish to test");
                String name = input.next();
                Compressor.engine(name, "debug");
            }
        }



        /*
         * I had attempted some EC stuff but didn't have time to finish -- just ignore the below
         * I plan to finish it sometime in the future as a challenge, that's why I didn't delete the code
         */

        // TODO Encode
//        Compressor.engine(fileName, "encode");

        // TODO decode
//        Compressor.engine(fileName, "decode");
    }
}
