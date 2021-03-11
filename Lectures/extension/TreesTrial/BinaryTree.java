package TreesTrial;

import java.util.ArrayList;

public class BinaryTree<E> {
    private BinaryTree<E> left;
    private BinaryTree<E> right;
    private E data;

    private class Element {

    }

    public BinaryTree() {
        left = null;
        right = null;
        data = null;
    }

    public BinaryTree(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public BinaryTree(BinaryTree<E> left, BinaryTree<E> right, E data) {
        this.left = left;
        this.right = right;
        this.data = data;
    }

    public boolean isLeaf() {
        return (!this.hasLeft()) && (!this.hasRight());
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public int getHeight() {
        if (this.isLeaf()) {
            return 0;
        } else {
            int height = 0;
            height = Math.max(height, left.getHeight());
            height = Math.max(height, right.getHeight());
            height++;
            return height;
        }
    }

    public ArrayList<BinaryTree<E>> getLeaves() {
        ArrayList<BinaryTree<E>> leaves = new ArrayList<>();
        if (this.isLeaf()) {
            leaves.add(this);
        } else {
            if (this.hasLeft()) left.getLeaves();
            if (this.hasRight()) right.getLeaves();
        }
        return leaves;
    }

    public int getSize() {
        int size = 1;
        if (hasLeft()) size += left.getSize();
        if (hasRight()) size += right.getSize();
        return size;
    }

    public int getWidth() {
        return getWidth('m');
    }

    private int getWidth(char mode) {
        switch (mode) {
            case 'm' -> {
                int width = 1;
                if (hasLeft()) width += left.getWidth('l');
                if (hasRight()) width += right.getWidth('r');
                return width;
            }
            case 'l' -> {
                int width = 1;
                if (hasLeft()) width += left.getWidth(mode);
                return width;
            }
            case 'r' -> {
                int width = 1;
                if (hasRight()) width += right.getWidth(mode);
                return width;
            }
        }
        return 0;
    }

    public ArrayList<E> getFringe() {
        ArrayList<E> fringe = new ArrayList<>();
        addToFringe(fringe);
        return fringe;
    }

    private void addToFringe(ArrayList<E> fringe) {
        if (isLeaf()) fringe.add(data);
        else {
            if (hasLeft()) left.addToFringe(fringe);
            if (hasRight()) right.addToFringe(fringe);
        }
    }


    /**
     * Returns a string representation of the tree
     */
    public String toString() {
        return toStringHelper("");
    }

    /**
     * Recursively constructs a String representation of the tree from this node,
     * starting with the given indentation and indenting further going down the tree
     */
    public String toStringHelper(String indent) {
        String res = indent + data + "\n";
        if (hasLeft()) res += left.toStringHelper(indent+"  ");
        if (hasRight()) res += right.toStringHelper(indent+"  ");
        return res;
    }

    public BinaryTree<E> copyToDepth(int d) {
        if (d==0) {
            return new BinaryTree<E>(data);
        }
        else {
            if (hasLeft()) {
                left = left.copyToDepth(d-1);
            }
            if (hasRight()) {
                right = right.copyToDepth(d-1);
            }
        }
        return this;
    }

    public void traverse() {
        System.out.println(data);
        if (hasLeft()) left.traverse();
        if (hasRight()) right.traverse();
    }

    public static void main(String[] args) {
        BinaryTree<String> root = new BinaryTree<>("A");
        root.left = new BinaryTree<String>("B");
        root.right = new BinaryTree<String>("C");
        BinaryTree<String> temp = root.left;
        temp.left = new BinaryTree<String>("D");
        temp.right = new BinaryTree<String>("E");
        temp = root.right;
        temp.left = new BinaryTree<String>("F");
        temp.right = new BinaryTree<String>("G");
        System.out.println(root);
        System.out.println(root.getFringe());
        System.out.println(root.getSize());
        System.out.println(root.getHeight());
        System.out.println(root.copyToDepth(1));

    }
}
