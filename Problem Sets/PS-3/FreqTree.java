import net.datastructures.*;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Nested class for Tree
 * @param <E> Generic data type
 */
public class FreqTree<E> implements Tree<E> {

    private final E data;
    public FreqTree<E> left;
    public FreqTree<E> right;
    public int frequency;
    public FreqTree<E> lparent = null;  // default value = null
    public FreqTree<E> rparent = null;  // default value = null

    // basic constructor with no argument
    public FreqTree() {
        data = null;
        frequency = 0;
        left = null;
        right = null;
    }

    // basic constructor for data, no frequency
    public FreqTree(E data) {
        this.data = data;
        left = null;
        right = null;
    }

    // constructor for data and frequency
    public FreqTree(E data, int frequency) {
        this.data = data;
        this.frequency = frequency;
        left = null;
        right = null;
    }

    // constructor for frequency alone
    public FreqTree(int frequency) {
        this.data = null;
        this.frequency = frequency;
        left = null;
        right = null;
    }

    // constructor for frequency, left, and right
    public FreqTree(int frequency, FreqTree<E> left, FreqTree<E> right) {
        this.data = null;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    // Setters, Getters, and Checks
    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public void setLeft(FreqTree<E> left) {
        this.left = left;
    }

    public void setRight(FreqTree<E> right) {
        this.right = right;
    }

    public FreqTree<E> getLeft() {
        return left;
    }

    public FreqTree<E> getRight() {
        return right;
    }

    public boolean hasData() {
        return this.data != null;
    }

    public E getData() {
        return this.data;
    }

    public boolean hasFrequency() {
        return this.frequency != 0;
    }

    /**
     * Getter for the frequency of current tree
     * @return integer value of current frequency
     */
    public int getFrequency() {
        return this.frequency;
    }

    /**
     * Method to check for equality of current tree and other tree
     * @param otherTree Other tree to compare to this tree
     * @return Boolean value of equality
     */
    public boolean equals(FreqTree<E> otherTree) {

        // Default values true
        // In case BOTH lack an element, corresponding condition remains true
        boolean dataEquals = true;
        boolean freqEquals = true;
        boolean leftEquals = true;
        boolean rightEquals = true;

        // Check data
        if (this.hasData() && otherTree.hasData()) {
            dataEquals = this.getData() == otherTree.getData();
            System.out.println("Data: " + dataEquals);
        }

        // Check frequency
        if (this.hasFrequency() && otherTree.hasFrequency()) {
            freqEquals = this.getData() == otherTree.getData();
            System.out.println("Data: " + freqEquals);
        }

        // Check left children
        if (this.hasLeft() && otherTree.hasLeft()) {
            leftEquals = this.getLeft().equals(otherTree.getLeft());
            System.out.println("Left: " + leftEquals);
        }
        // else if child is existent in either or both of the trees, return false
//        else if (this.hasLeft() || otherTree.hasLeft()) {
//            leftEquals = false;
////            System.out.println("Second left: " + leftEquals);
//        }

        // Check right children
        if (this.hasRight() && otherTree.hasRight()) {
            rightEquals = this.getRight().equals(otherTree.getRight());
//            System.out.println("Right: " + rightEquals);
        }
        // else assert right child is existent in either or both of the trees, return false
//        else if (this.hasRight() || otherTree.hasRight()) {
//            rightEquals = false;
////            System.out.println("Second right: " + rightEquals);
//        }

        // Return a check of all conditions
        return dataEquals && freqEquals && leftEquals && rightEquals;
    }

    public String toString() {
        return this.buildString(0);
    }

    private String buildString(int level) {
        String name = " ";
        String basicHolder = "  ";
        String currentHolder = "";

        for (int i=0; i<= level; i++) {
            currentHolder += basicHolder;
        }
        name = name + currentHolder + "Tree: " + this.data + " " + this.frequency;
        int left = level + 1;
        int right = left;
        if (this.hasLeft()) name += "\n l" + level + this.left.buildString(left);
        if (this.hasRight()) name += "\n r" + level + this.right.buildString(right);

        return name;
    }

    @Override
    public int size() {
        int size = 1;
        if (this.hasLeft()) size += left.size();
        if (this.hasRight()) size += right.size();
        return size;
    }

    public void buildParents() {
        if (this.hasLeft()) {
            this.left.rparent = this;
            left.buildParents();
        }
        if (this.hasRight()) {
            this.right.lparent = this;
            right.buildParents();
        }
    }

    public boolean hasLeftParent() {
        return this.lparent != null;
    }
    public boolean hasRightParent() {
        return this.rparent != null;
    }

    public FreqTree<E> getParent() {
        if (this.lparent != null) {
            return this.lparent;
        }
        else if (this.rparent != null) {
            return this.rparent;
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return this.getData() == null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Iterable<Position<E>> positions() {
        return null;
    }

    @Override
    public E replace(Position<E> position, E e) throws InvalidPositionException {
        return null;
    }

    @Override
    public Position<E> root() throws EmptyTreeException {
        return null;
    }

    @Override
    public Position<E> parent(Position<E> position) throws InvalidPositionException, BoundaryViolationException {
        return null;
    }

    @Override
    public Iterable<Position<E>> children(Position<E> position) throws InvalidPositionException {
        return null;
    }

    @Override
    public boolean isInternal(Position<E> position) throws InvalidPositionException {
        return false;
    }

    @Override
    public boolean isExternal(Position<E> position) throws InvalidPositionException {
        return false;
    }

    @Override
    public boolean isRoot(Position<E> position) throws InvalidPositionException {
        return false;
    }
}
