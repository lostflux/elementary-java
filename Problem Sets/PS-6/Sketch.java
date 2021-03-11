import java.util.*;

public class Sketch {
    private TreeMap<Integer, Shape> shapesMap;
//    private TreeMap<Integer, Shape> shapeTreeMap;

    public Sketch(){
        shapesMap = new TreeMap<>();
    }

    /**
     * Method to add a new shape into a sketch
     * @param newShape shape to be added into Sketch
     */
    public void push(Integer id, Shape newShape) {
        shapesMap.put(id, newShape);
    }

    /**
     * Method to get Iterable over all shapes in a sketch
     * @return iterator over shapes in sketch
     */
    public TreeMap<Integer, Shape> getShapesMap() {
        return shapesMap;
    }

    /**
     * Method to clear all shapes in current Sketch
     */
    public void clearSketch() {
        shapesMap = null;
    }

    /**
     * Method to replace all shapes in current sketch
     * @param shapesList new set of shapes
     */
    public void replaceSketch(TreeMap<Integer, Shape> shapesList) {
        this.shapesMap = shapesList;
    }

    /**
     * Method to remove a specific shape from Sketch
     * @param objectId ID of shape to be removed
     */
    public void remove (int objectId) {
        shapesMap.remove(objectId);
    }

    /**
     * Method to get shape from sketch
     * @param shapeId ID of shape
     * @return Shape object
     */
    public Shape getShape(int shapeId) {
        return shapesMap.get(shapeId);
    }

    /**
     * Method to check if sketch is empty
     * @return boolean
     */
    public boolean isEmpty() {
        return shapesMap.isEmpty();
    }

    /**
     * Method to get iterable of IDs, normal order
     * @return Iterable set
     */
    public Iterable<Integer> getKeys() {
        return shapesMap.keySet();
    }

    /**
     * Method to get Iterable of IDs, reverse order
     * @return Iterable set
     */
    public Iterable<Integer> getKeysReverse() {
        return shapesMap.descendingKeySet();
    }

    /**
     * Method to get Iterable of shapes in Sketch
     * @return Iterable set
     */
    public Iterable<Shape> getShapesIterator() {
        return this.shapesMap.values();
    }
}
