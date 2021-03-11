import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Encoder {
    static int currentIndex = 0;

    public Encoder() {

    }
    public ArrayList<String> encodeTree(FreqTree<Character> finalTree) {
        String preorder = "";
        String inorder = "";
        preorder += precodeTree(finalTree);
        inorder += incodeTree(finalTree);

        ArrayList<String> encoded = new ArrayList<>();
        encoded.add(preorder);
        encoded.add(inorder);

        return encoded;
    }

    public String precodeTree(FreqTree<Character> tree) {
        String name;
        String preorder = "";
        if (tree.hasData()) {
            name = String.valueOf(tree.getData());
        }
        else {
            name = tree.getFrequency() + "F";
        }
        preorder += name + "///";

        if (tree.hasLeft()) {
            preorder += precodeTree(tree.getLeft());
        }
        if (tree.hasRight()) {
            preorder += precodeTree(tree.getRight());
        }
        return preorder;
    }
    public String incodeTree(FreqTree<Character> tree) {
        String name;
        String inorder = "";

        if (tree.hasData()) {
            name = String.valueOf(tree.getData());
        }
        else {
            name = tree.getFrequency() + "F";
        }
        if (tree.hasLeft()) {
            inorder += incodeTree(tree.getLeft());
        }

        inorder = inorder + "///" + name;

        if (tree.hasRight()) {
            inorder += incodeTree(tree.getRight());
        }

        return inorder;
    }

    public FreqTree<Character> decodeTree(String preorder, String inorder) {
        String[] preordered = preorder.split("///");
        String[] inordered = inorder.split("///");

//        int currentIndex = 1;       // skip 0 because it has the "PR" or "IN" indicator
        int startRange = 0;
        int endRange  = preordered.length;

        FreqTree<Character> constructedTree = addTree(preordered, inordered);
        return constructedTree;
    }

    public FreqTree<Character> addTree(String[] preordered, String[] inordered) {

        int start = 0;
        int end = inordered.length;

//        if (start > end) return null;
        if (currentIndex == preordered.length) return null;

        String treeData = preordered[currentIndex++];

        FreqTree<Character> newTree;                                // initialize tree

        // To check if it's a frequency (appended "F")
        boolean isFrequency = treeData.endsWith("F") && treeData.length() > 1;

        if (isFrequency) {
//            System.out.println(treeData);
            treeData = treeData.substring(0, treeData.length()-1);
            newTree = new FreqTree<>(Integer.parseInt(treeData)); // initialize with integer frequency
        }
        else {
            Character data = treeData.charAt(0);
            newTree = new FreqTree<>(data); // initialize with Character data
            System.out.println(newTree);
        }

        if (start == end || currentIndex == end) {
            return newTree;
        }

        int indexInInorder = search(inordered, treeData);
        String[] left_inordered = Arrays.copyOfRange(inordered, 0, indexInInorder);
        String[] right_inordered = Arrays.copyOfRange(inordered, indexInInorder, end);


//        if (currentIndex == indexInInorder-1 || currentIndex == indexInInorder+1) {
//            return newTree;
//        }
//        System.out.println(treeData + ": " + indexInInorder);

        if (currentIndex <= indexInInorder) {
        newTree.left = addTree(preordered, left_inordered);
        }

//        if (currentIndex >= indexInInorder) {
        newTree.right = addTree(preordered, right_inordered);
//        }

        return newTree;
    }
    private int search(String[] list, String value) {
        int index;
        for (index=0; index<list.length; index++) {
            if (list[index].equals(value)) {
                return index;
            }
        }
        return index;
    }

    public static ArrayList<String> encode (FreqTree<Character> tree) {
        Encoder en = new Encoder();

        return en.encodeTree(tree);
    }

    public static FreqTree<Character> decode(String preordered, String inordered) {
        Encoder en = new Encoder();
        return en.decodeTree(preordered, inordered);
    }
}
