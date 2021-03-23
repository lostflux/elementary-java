import net.datastructures.Tree;

import java.io.*;
import java.util.*;

/**
 * Compressor class
 * Create an instance of this class to compress and decompress files
 */
public class Compressor {

    // Convenient to store input and output file names for the current instance
    private String sourceFile;                        // Original file to compress
    private String compressedFileName;                // Name of compressed file
    private String decompressedFileName;        // Name of decompressed file
    private HashMap<Character, String> dictionary;    // Container of bit dictionary for this compression


    /**
     * Initialize with file name
     * @param fileName Name of file to be compressed/decompressed
     * @throws IOException Error reading from or writing to file
     */
    public Compressor(String fileName) throws IOException {
        updateFileNames(fileName);
    }

    /**
     * Initialize with file and dictionary
     * @param fileName File to be compressed/decompressed
     * @param dictionary HashMap containing character mappings to binary -- specific to file
     * @throws IOException Error reading from or writing to file
     */
    public Compressor(String fileName, HashMap<Character, String> dictionary) throws IOException {
        updateFileNames(fileName);
        this.dictionary = dictionary;
    }

    /**
     * Method to auto-generate the name of the compressed & decompressed files from name of original file
     * @param fileName Name of original file
     */
    private void updateFileNames(String fileName) {
        sourceFile = fileName;
        compressedFileName = sourceFile.split("\\.")[0] + "_compressed.txt";
        decompressedFileName = sourceFile.split("\\.")[0] + "_decompressed.txt";
    }

    public Tree<Character> getFinalTree() throws IOException {
        Tree<Character> finalTree = buildTree();
        if (finalTree == null) {
            System.err.println("Error building tree");
        }
        return finalTree;
    }

    /**
     * Method to count the occurrences of all characters in file
     * @return A hashMap of characters and their frequencies
     * @throws IOException Error reading from or writing to file
     */
    private HashMap<Character, Integer> logFrequencies() throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(sourceFile));
        HashMap<Character, Integer> frequencyTable = new HashMap<>();
        int cInt = input.read();
        while (cInt != -1) {
            char c = (char) cInt;
            frequencyTable.put(c, frequencyTable.getOrDefault(c, 0) + 1);
            cInt = input.read();
        }
        input.close();
        return frequencyTable;
    }

    /**
     * Method to create Frequency trees for each character and it's frequency
     * @param frequencyTable HashMap containing characters and their frequencies in file
     * @return A list of frequency trees
     */
    private List<Tree<Character>> createInitTrees(HashMap<Character, Integer> frequencyTable) throws IOException {
        // TODO: Error Handling
        //  If frequency table for current instance is empty, attempt rebuild
        while (frequencyTable.size() == 0) {
            logFrequencies();

            // If still empty, prompt user for action
            System.err.println("Error building the Frequency tree.");
            System.out.println("Retry? Please type Y or N");
            Scanner input = new Scanner(System.in);
            String feedback = input.next();
            if (feedback.equals("N")){
                System.err.println("Routine terminated!");
                return null;
            }
            else if (!feedback.equals("Y")) {
                System.err.println("Invalid input. Please try again");
            }
        }

        // If frequency tree is validated successfully,

        // Create list instance to hold initial trees
        List<Tree<Character>> initialTrees = new ArrayList<>();

        // For each character, create a frequency tree and put into list
        for (char key : frequencyTable.keySet()) {
            initialTrees.add(new FreqTree<>(key, frequencyTable.get(key)));
        }

        return initialTrees;
    }

    /**
     * Method to construct priority queue from all the sub lists
     * @return PriorityQueue  containing characters sorted by increasing frequency
     * @throws IOException Error reading from or writing to file
     */
    public PriorityQueue<Tree<Character>> buildPriorityQueue() throws IOException {

        // Build frequency table
        HashMap<Character, Integer> frequencyTable = logFrequencies();

        // Create initial trees
        List<Tree<Character>> initialTrees = createInitTrees(frequencyTable);

        // Instantiate TreeComparator class to be used in sorting the priority queue
        TreeComparator<Tree<Character>> comparator = new TreeComparator<>();

        // Instantiate the priority queue instance to be updated
        PriorityQueue<Tree<Character>> priorityQueue = new PriorityQueue<>(comparator);

        // Dump all initial trees into the priority queue
        priorityQueue.addAll(initialTrees);

        return priorityQueue;
    }

    /**
     * Method to construct the tree
     * @return single tree object
     * @throws IOException Error reading from or writing to file
     */
    public Tree<Character> buildTree() throws IOException {

        // Construct the priority queue
        PriorityQueue<Tree<Character>> priorityQueue = buildPriorityQueue();

        // While priority queue has more than one tree, get the two trees with least frequency
        // and chain them into one tree, then add the one tree back into priority queue
        while (priorityQueue.size() > 1) {
            // Get the two trees with lowest frequency
            FreqTree<Character> T1 = (FreqTree<Character>) priorityQueue.remove();
            FreqTree<Character> T2 = (FreqTree<Character>) priorityQueue.remove();

            // Get their respective frequencies
            int freq1 = T1.getFrequency();
            int freq2 = T2.getFrequency();

            // Build new tree with frequency as their sum frequencies and the two trees as children
            Tree<Character> newTree = new FreqTree<>(freq1+freq2, T1, T2);

            // Add new tree into Priority Queue
            priorityQueue.add(newTree);
        }

        // TODO: ERROR HANDLING
        //  If final priority queue does not have a single element, something went wrong.
        //  Print error message, prompt for action.

        while (true) {
            if(priorityQueue.size() != 1) {
                System.err.println("The Mapping is empty. Please check the code and try again.");
                System.out.println("Retry? Y or N" );
                Scanner input = new Scanner(System.in);
                String feedback = input.next();
                if (feedback.equals("Y")) buildTree();
                else if (feedback.equals("N")) return null;
                else System.err.println("Invalid input. Please try again");
            }
            else break;
        }
        // return the tree at front of priority queue
        return priorityQueue.peek();
    }

    /**
     * Function to log the entries of the final Tree built for this text file
     */
    private void buildDictionary() throws IOException {
//        HashMap<Character, String> dictionary = new HashMap<>();
        Tree<Character> finalTree = buildTree();

        dictionary = new HashMap<>();

        // ERROR HANDLING
        // if finalTree is null, cannot run on incorrect structure. Try to rebuild
        while (true){
            if (finalTree == null) {
                System.err.println("The final Tree is null. Something went wrong. Please check code and retry.");
                System.out.println("Retry? Y or N" );
                Scanner input = new Scanner(System.in);
                String feedback = input.next();
                if (feedback.equals("Y")) finalTree = buildTree();
                else if (feedback.equals("N")) return;
                else System.err.println("Invalid input. Please try again");
            }
            else break;
        }

        // initialize the path and start the addToDictionary routine
        String path = "";
        addToDictionary(finalTree, path);
    }

    /**
     * Helper function to add current tree to dictionary
     * @param tree current tree
     * @param path path leading to current tree
     */
    private void addToDictionary(Tree<Character> tree, String path) {
        FreqTree<Character> castTree = (FreqTree<Character>) tree;
        // If current tree node has data, save it and the path to the dictionary
        if (!tree.isEmpty()) {
            dictionary.put(castTree.getData(), path);
        }

        // if tree node has left child, recurse with the left child, updating the path accordingly
        if (castTree.hasLeft()) {
            addToDictionary(castTree.getLeft(), path+0);
        }

        // if tree node has right child, recurse with the right child, updating the path accordingly
        if (castTree.hasRight()) {
            addToDictionary(castTree.getRight(), path+1);
        }
    }


    /**
     * Method to compress file contents
     * @throws IOException Exception reading from or writing into file
     */
    private void compress() throws IOException {

        // TODO: ERROR HANDLING
        //  if file name is empty, prompt user for file name
        while(sourceFile.equals("")) {
            System.err.println("Source file name is empty. Please type in the name & directory of a new file.");
            Scanner input = new Scanner(System.in);
            String newName = input.next();
            updateFileNames(newName);
        }

        // start reading from file
        BufferedReader parseFile = new BufferedReader(new FileReader(sourceFile));
        BufferedBitWriter bitDump = new BufferedBitWriter(compressedFileName);

        // TODO: Test the file
        parseFile.mark(3);
        for (int i=0; i<3; i++) {
            int getFromFile = parseFile.read();
            if (getFromFile == -1) {
                System.err.println("Sorry, file too short!");
                System.out.println("Please select another file.");
                Scanner input = new Scanner(System.in);
                String newName = input.next();
                updateFileNames(newName);
                compress();
                break;
            }
        }
        parseFile.reset();

        // TODO: if dictionary is null, rebuild
        if (dictionary == null) {
            buildDictionary();
        }


        int cInt = parseFile.read();

        // While the read number is not -1 -- (-1 is end of file)
        while(cInt != -1) {
            char c = (char) cInt;                   // Cast to character
            String cBits = dictionary.get(c);       // Get corresponding bit sequence from dictionary
            char[] bits = cBits.toCharArray();      // Split String of bit sequence to list of characters

            // For each character in file,
            for (char cBit : bits) {
                boolean bitValue = cBit == '1';     // Convert 0 to false, 1 to true
                bitDump.writeBit(bitValue);         // Write into file
            }
            cInt = parseFile.read();
        }
        bitDump.close();
        parseFile.close();
    }

    /**
     * Method compress with explicit file name declaration
     * @param filename Name of source file
     * @throws IOException Exception reading from or writing into file
     */
    private void compress(String filename) throws IOException {
        this.sourceFile = filename;
        compress();
    }

    /**
     * Method to decompress a compressed file
     * @throws IOException Exception reading from or writing into file
     */
    private void deCompress() throws IOException {

        // Initialize the file reader and writer
        while (compressedFileName == null) {
            System.err.println("file");
        }

        BufferedBitReader parseBits = new BufferedBitReader(compressedFileName);
        BufferedWriter output = new BufferedWriter(new FileWriter(decompressedFileName));

        String bitStream = "";  // String to hold stream of bits from file

        // If dictionary is nonexistent, reconstruct it
        // TODO: !! Note that this depends on the original file being accessible
        System.err.println("Building dictionary...");
        buildDictionary();

        while (dictionary == null) {
            System.err.println("Invalid dictionary! Do you want to reconstruct? Y or N");
            Scanner input = new Scanner(System.in);
            String feedback = input.next();
            if (feedback.equals("Y")) buildDictionary();
            else return;
        }

        // Keep reading new bits until end of file
        while(parseBits.hasNext()) {
            // Read in the next bit as a boolean
            boolean cBool = parseBits.readBit();

            // Convert true to 1, false to 0 ... and add to the stream of bits
            if (cBool) bitStream += "1";
            else bitStream += "0";

            // Check the dictionary if the current bitstream corresponds to a character
            for (Map.Entry<Character, String> entry : dictionary.entrySet()) {
                if (entry.getValue().equals(bitStream)) {
                    char letter = entry.getKey();
                    output.write(letter);

                    // Print out the current later
                    System.out.println(letter +": " + bitStream);
                    bitStream = "";
                    break;
                }
            }
        }
        parseBits.close();
        output.close();
    }

    /**
     * Method decompress with explicit source file declaration
     * @param filename  Name of file to decompress
     * @throws IOException Exception reading from or writing into file
     */
    private void deCompress(String filename) throws IOException {
        this.compressedFileName = filename;
        deCompress();
    }


    /**
     * Static method to run compression - decompression engine
     * @param fileName source file to be compressed/decompressed
     * @throws IOException Exception reading from or writing into file
     */
    public static void engine(String fileName) throws IOException {
        Compressor compressEngine = new Compressor(fileName);
        compressEngine.compress();
    }

    /**
     * Static method to run the compression - decompression engine
     * @param fileName The name of the file to be compressed or decompressed
     * @param mode "compress" or "decompress"
     * @throws IOException Exception reading from or writing into file
     */
    public static void engine(String fileName, String mode) throws IOException {
        Compressor compressEngine = new Compressor(fileName);
        mode = mode.toLowerCase();      // To be safe, cast mode to lower case
        switch (mode) {
            case "compress" -> compressEngine.compress();
            case "decompress" -> compressEngine.deCompress();

            // TODO: debugging
            case "debug" -> {
                // Test compress
                compressEngine.compress();
                System.out.println("Tree: \n" + compressEngine.buildTree() + "\n");
                System.out.println("Dictionary: \n" + compressEngine.dictionary + "\n");

                // Test decompress
                compressEngine.deCompress();

                // Test source and source and decompressed files to make sure they are the same:
                BufferedReader readOriginal = new BufferedReader(new FileReader(fileName));
                String decompressedName = fileName.split("\\.")[0] + "_decompressed.txt";
                BufferedReader readDecompressed = new BufferedReader(new FileReader(decompressedName));
                boolean statusCheck = true;
                int originalInt = readOriginal.read();
                int decompressedInt = readDecompressed.read();
                System.err.println("Running validity check...");
                while (originalInt != -1 && decompressedInt != -1) {
                    statusCheck = originalInt == decompressedInt;
                    if (!statusCheck) {
                        System.err.println("The decompressed file is not correct!");
                        break;
                    }
                    originalInt = readOriginal.read();
                    decompressedInt = readDecompressed.read();
                }
                if (statusCheck) {
                    System.err.println("Validity check passed!");
                }
                else {
                    System.err.println("Validity check failed.");
                }
            }


            // TODO: Ignore these, I had attempted EC stuff but didn't finish.
            //  the encoder works, but the decoder builds a faulty tree.
            case "encode" -> {
                Tree<Character> finalTree = compressEngine.getFinalTree();
                compressEngine.compress();
                ArrayList<String> encoded = Encoder.encode(finalTree);
                System.out.println(encoded.get(0));
                System.out.println(encoded.get(1));

            }
            case "decode" -> {
                Compressor.engine(fileName, "encode");
                Tree<Character> finalTree = compressEngine.getFinalTree();
                ArrayList<String> encoded = Encoder.encode(finalTree);
                String preordered = encoded.get(0);
//                System.out.println("final" + preordered);
                String inordered = encoded.get(1);
                Tree<Character> decodedFinalTree = Encoder.decode(preordered, inordered);
                System.out.println(decodedFinalTree.equals(finalTree));
                String finalPR = Encoder.encode(decodedFinalTree).get(0);

                System.out.println("\n pre-encoded tree:\n " + preordered + "\n");
                System.out.println("\n Re-encoded  tree:\n " + finalPR + "\n");
                System.out.println("\n Tree regeneration == original tree: " + decodedFinalTree.equals(finalTree));
                System.out.println("\n Pre-encoding == re-encoding: " + finalPR.equals(preordered));

//                System.out.println(finalTree);
                System.out.println(decodedFinalTree);


                // TODO test
//                FreqTree<Character> tree = new FreqTree<Character>('A', 1);
//                tree.left = new FreqTree<Character>('B', 2);
//                tree.right = new FreqTree<Character>('C', 3);
//                tree.left.left = new FreqTree<Character>('D', 4);
//                tree.left.right = new FreqTree<Character>('E', 5);
//                tree.right.left = new FreqTree<Character>('F', 6);
//                tree.right.right = new FreqTree<Character>('G', 7);
////                tree.left.left.left = new FreqTree<Character>('H', 8);
////                tree.left.left.right = new FreqTree<Character>('I', 9);
//                ArrayList<String> encoded = Encoder.encode(tree);
//                String preordered = encoded.get(0);
//                String inordered = encoded.get(1);
//                FreqTree<Character> decodedFinalTree = Encoder.decode(preordered, inordered);
//
//                System.out.println(decodedFinalTree);
//                System.out.println("----------------------------------------------");
//                System.out.println(tree);

            }
            default -> compressEngine.compress();
        }
    }

    /**
     * Static method to run the compression - decompression engine if dictionary is provided
     * @param fileName  Source file to be compressed or decompressed
     * @param mode  "compress" or "decompress"
     * @param dictionary The dictionary (if already built)
     * @throws IOException  Exception reading from or writing into file
     */
    public static void engine(String fileName, String mode, HashMap<Character, String> dictionary) throws IOException {
        mode = mode.toLowerCase();      // To be safe, cast mode to lower case
        Compressor compressEngine = new Compressor(fileName, dictionary);
        switch (mode) {
            case "compress" -> compressEngine.compress();
            case "decompress" -> compressEngine.deCompress();
            default -> compressEngine.compress();
        }
    }
}
