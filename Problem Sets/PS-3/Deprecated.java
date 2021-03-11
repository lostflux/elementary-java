import java.io.IOException;

/**
 * A dump of all the deprecated code
 */
public class Deprecated {

//    private boolean checkOccurrence(Character ch) {
//        for (FreqTree<Character> tree : initialTrees) {
//            if (tree.getData() == ch) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean hasOccurrence(char ch) {
//        for (FreqTree<Character> tree : initialTrees) {
//            if (tree.getData() == ch) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private Tree<Character> getOccurrence(char ch) {
//
//        for (Tree<Character> tree : initialTrees) {
//            if (tree.getData() == ch) {
//                return tree;
//            }
//        }
//    }
//
//    private void buildList() throws IOException {
//
//        int nextCode = input.read();
//        while (nextCode != -1) {
//
//            Character nextChar = (char) nextCode;
//            if (initialTrees.size() == 0) {
//                initialTrees.add(new FreqTree<>(nextChar));
//            }
//            else {
//                for (FreqTree<Character> tree : initialTrees) {
//                    if (tree.getData() == nextChar) {
//                        tree.frequency++;
//                        return;
//                    }
//                }
//            }
//            nextCode = input.read();
//        }
//    }
//    private String encodeTree( String encoded, FreqTree<Character> tree) {
//        // Step 1: build chain of parents to help in parsing tree
//        //        tree.buildParents();
//
//        if (tree.hasData()) {
//            char data = tree.getData();
//            encoded = encoded + "|" + data;
//            if (tree.hasFrequency()) {
//                int frequency = tree.getFrequency();
//                encoded += frequency;
//            }
//        }
//        else if (tree.hasFrequency()) {
//            int frequency = tree.getFrequency();
//            encoded = encoded + "|" + frequency;
//        }
//        System.out.println(encoded);
//
//        if (tree.hasLeft()) {
//            encoded += "[";
//            String leftEncoding = encodeTree("", tree.left);
//            encoded += leftEncoding;
//        }
//        if (tree.hasRight()) {
//            encoded += "]";
//            String rightEncoding = encodeTree(encoded, tree.right);
//            encoded += rightEncoding;
//        }
//
//        System.out.println(encoded.length());
//        return encoded;
//        // if tree has parent, go up tree!
//    }

    // EXTRA CREDIT METHODS
//    public void encode() throws IOException {
//        String encoded = "";
//
//        FreqTree<Character> finalTree = buildTree();
//        encodeTree(encoded, finalTree);
//        System.out.println(encoded);
//    }
//    private void encodeTree( String encoded, FreqTree<Character> tree) {
//        // Step 1: build chain of parents to help in parsing tree
////        tree.buildParents();
//
//        String data = "";
//        int frequency = 0;
//
//
//        if (tree.hasData()) {
//            data += tree.getData();
//        }
//        if (tree.hasFrequency()) {
//            frequency = tree.getFrequency();
////            encoded = encoded + "|" + frequency;
//        }
//
////        System.out.println(encoded);
//        encoded = encoded + data + " " + frequency;
//
//        if (tree.hasLeft()) {
////            encoded += "[";
//            encoded = encoded + "(l ";
//            encodeTree(encoded, tree.left);
//            encoded += ")";
//        }
//        if (tree.hasRight()) {
////            encoded += "[";
//            encoded = encoded + "(r ";
//            encodeTree(encoded, tree.right);
//            encoded += ")";
//        }
//
//        if (!(tree.hasLeft() || tree.hasRight())) {
//            return;
//        }
//
//        System.out.println(encoded);
//        System.out.println(encoded.length());
//        return encoded;
        // if tree has parent, go up tree!
//    }
//    public void decode() throws IOException {
//        FreqTree<Character> generated = new FreqTree<>();
//        BufferedReader parseFile = new BufferedReader(new FileReader(sourceFile));
//        String strTree = "";
//        int cInt = parseFile.read();
//        while (cInt != -1) {
//            char c = (char) cInt;
//            if (c != '|' && c!= '[' && c != ']' ) {
//                strTree += c;
//            }
//            else if (c == '|') {
//                String[] elements = strTree.split("");
//                try {
//                    strTree.find
//                }
//                FreqTree<Character> currentTree = new FreqTree<>();
//            }
//
//
//            cInt = parseFile.read();
//
//        }
//    }


//    public boolean equals(FreqTree<E> otherTree) {
//
//        // Default values true
//        // In case BOTH lack an element, corresponding condition remains true
//        boolean dataEquals = true;
//        boolean freqEquals = true;
//        boolean leftEquals = true;
//        boolean rightEquals = true;
//
//        // Check data
//        if (this.hasData() && otherTree.hasData()) {
//            dataEquals = this.getData() == otherTree.getData();
//        }
//        else if (this.hasData() || otherTree.hasData()) {
//            dataEquals = false;
//        }
//
//        // Check frequency
//        if (this.hasFrequency() && otherTree.hasFrequency()) {
//            freqEquals = this.getData() == otherTree.getData();
//        }
//        else if (this.hasData() || otherTree.hasData()) {
//            freqEquals = false;
//        }
//
//        // Check left children
//        if (this.hasLeft() && otherTree.hasLeft()) {
//            leftEquals = this.getLeft().equals(otherTree.getLeft());
//        }
//        // else if child is existent in either or both of the trees, return false
//        else if (this.hasLeft() || otherTree.hasLeft()) {
//            leftEquals = false;
//        }
//
//        // Check right children
//        if (this.hasRight() && otherTree.hasRight()) {
//            rightEquals = this.getRight().equals(otherTree.getRight());
//        }
//        // else assert right child is existent in either or both of the trees, return false
//        else if (this.hasRight() || otherTree.hasRight()) {
//            rightEquals = false;
//        }
//
//        // Return a check of all conditions
//        return dataEquals && freqEquals && leftEquals && rightEquals;
//    }
}
