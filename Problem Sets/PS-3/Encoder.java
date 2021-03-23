import net.datastructures.Tree;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Encoder {
    static int currentIndex = 0;

    public Encoder() {

    }
    public ArrayList<String> encodeTree(Tree<Character> finalTree) {
        String preorder = "";
        String inorder = "";
        preorder += precodeTree(finalTree);
        inorder += incodeTree(finalTree);

        ArrayList<String> encoded = new ArrayList<>();
        encoded.add(preorder);
        encoded.add(inorder);

        return encoded;
    }

    public String precodeTree(Tree<Character> tree) {
        FreqTree<Character> castTree;
        if (tree instanceof FreqTree) {
            castTree = (FreqTree<Character>) tree;
        }
        else return null;
        String name;
        String preorder = "";
        if (castTree.hasData()) {
            name = String.valueOf(castTree.getData());
        }
        else {
            name = castTree.getFrequency() + "F";
        }
        preorder += name + "///";

        if (castTree.hasLeft()) {
            preorder += precodeTree(castTree.getLeft());
        }
        if (castTree.hasRight()) {
            preorder += precodeTree(castTree.getRight());
        }
        return preorder;
    }
    public String incodeTree(Tree<Character> tree) {
        FreqTree<Character> castTree;
        if (tree instanceof FreqTree) {
            castTree = (FreqTree<Character>) tree;
        }
        else return null;

        String name;
        String inorder = "";

        if (castTree.hasData()) {
            name = String.valueOf(castTree.getData());
        }
        else {
            name = castTree.getFrequency() + "F";
        }
        if (castTree.hasLeft()) {
            inorder += incodeTree(castTree.getLeft());
        }

        inorder = inorder + "///" + name;

        if (castTree.hasRight()) {
            inorder += incodeTree(castTree.getRight());
        }

        return inorder;
    }

    public Tree<Character> decodeTree(String preorder, String inorder) {
        String[] preordered = preorder.split("///");
        String[] inordered = inorder.split("///");

//        int currentIndex = 1;       // skip 0 because it has the "PR" or "IN" indicator
        int startRange = 0;
        int endRange  = preordered.length;

        return addTree(preordered, inordered);
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

    public static ArrayList<String> encode (Tree<Character> tree) {
        Encoder en = new Encoder();

        return en.encodeTree(tree);
    }

    public static Tree<Character> decode(String preordered, String inordered) {
        Encoder en = new Encoder();
        return en.decodeTree(preordered, inordered);
    }
}
