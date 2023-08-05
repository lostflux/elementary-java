import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * A point quadtree: stores an element at a 2D position, 
 * with children at the subdivided quadrants
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, explicit rectangle
 * @author CBK, Fall 2016, generic with Point2D interface
 * 
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// lower-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children
	private String name;

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		if (point instanceof Dot) {
			Dot newDot = (Dot) point;
			name = newDot.getName();
			this.name = name + " at " + point.getX() + ", " + point.getY() + " scoping " + x1 + ", " + y1 + " to " + x2 + ", " + y2;
		}

		// Handling weirdness
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}

		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters
	
	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public int getY() {
		return (int) this.point.getY();
	}

	public int getX()  {
		return (int) this.point.getX();
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Function to determine what quadrant a point's position is relative to current point
	 * @param point the point to check
	 * @return integer value of quadrant
	 */
	private int getQuadrant(E point) {	// determine quadrant of a point relative to current point
		int quadrant;
		int pointX = (int) point.getX(), pointY = (int) point.getY(); // Get x and y coordinates of point

		// Determine quadrant
		if (pointX>this.point.getX() && pointY<=this.point.getY()){
			quadrant = 1;
		}
		else if (pointX<=this.point.getX() && pointY<=this.point.getY()){
			quadrant = 2;
		}
		else if (pointX<=this.point.getX() && pointY>this.point.getY()){
			quadrant = 3;
		}
		else if (pointX>this.point.getX() && pointY>this.point.getY()) {
			quadrant = 4;
		}
		else {
			System.err.println("Point is current tree!");
			quadrant = 0;
		}
		return quadrant;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		return switch (quadrant) {
			case 1 -> c1;
			case 2 -> c2;
			case 3 -> c3;
			case 4 -> c4;
			default -> null;
		};
//		if (quadrant==1) return c1;
//		if (quadrant==2) return c2;
//		if (quadrant==3) return c3;
//		if (quadrant==4) return c4;
//		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}



	/**
	 * Inserts the point into the tree
	 */
	public void insert(E p2) {
		// TODO: YOUR CODE HERE

		// Determine which quadrant the current point is in
		int quadrant = this.getQuadrant(p2);

		// If quadrant is zero, point is exactly where current point is. Don't add.
		if (quadrant == 0){
			return;
		}

		// check if tree has child in the given quadrant, recursively insert into child
		if (hasChild(quadrant)){
			getChild(quadrant).insert(p2);
		}
		// if node has no child in quadrant,
		// get coordinates of new scope, create new PointQuadtree,
		// and insert it as child of current node in the given quadrant
		else {
			int tX1, tY1, tX2, tY2;	// temporary variable to hold coordinates of new node's scope
			switch (quadrant) {
				case 1 -> {	// insert into quadrant 1
					tX1 =  (int) this.point.getX();
					tY1 = this.getY1();
					tX2 = this.getX2();
					tY2 = (int) this.point.getY();
					this.c1 = new PointQuadtree<>(p2, tX1, tY1, tX2, tY2);
				}
				case 2 -> {	// insert into quadrant 2
					tX1 = this.getX1();
					tY1 = this.getY1();
					tX2 = (int) this.point.getX();
					tY2 = (int) this.point.getY();
					this.c2 = new PointQuadtree<>(p2, tX1, tY1, tX2, tY2);
				}
				case 3 -> {	// insert into quadrant 3
					tX1 = this.getX1();
					tY1 = (int) this.point.getY();
					tX2 = (int) this.point.getX();
					tY2 = this.getY2();
					this.c3 = new PointQuadtree<>(p2, tX1, tY1, tX2, tY2);
				}
				case 4 -> {	// insert into quadrant 4
					tX1 = (int) this.point.getX();
					tY1 = (int) this.point.getY();
					tX2 = this.getX2();
					tY2 = this.getY2();
					this.c4 = new PointQuadtree<>(p2, tX1, tY1, tX2, tY2);
				}
				default -> throw new IllegalStateException("Unexpected value: Quadrant " + quadrant);
			}
//			System.out.println(tX1 + " " + tX2 + " " + tY1 + " " + tY2);
		}
	}

	
	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		// TODO: YOUR CODE HERE -- compute directly, using only numbers not lists (i.e., don't just call allPoints() and return its size)
		int size = 1; // current node -> this is the base case

		// Loop over all 4 quadrants, check for child and add size of child
		for (int quadrant=1; quadrant<=4; quadrant++) {
			if (hasChild(quadrant)) size += getChild(quadrant).size();
		}

		// finally return the size
		return size;
	}
	
	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 * @return List of all points in the tree
	 */
	public List<E> allPoints() {
		// TODO: YOUR CODE HERE -- efficiency matters!
		List<E> buffer = new ArrayList<>();
		this.getPoints(buffer); 		// call to helper function that adds points to list
		return buffer;
	}

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		// TODO: YOUR CODE HERE -- efficiency matters!

		// Instantiate a set to hold points in circle -- used a set to avoid duplicates
		Set<E> buffer = new HashSet<>();

		// Call the helper function to add points into the set
		getPointsInCircle(buffer, cx, cy, cr);

		// Cast the set into an array and return it
		return new ArrayList<>(buffer);
	}



	// TODO: YOUR CODE HERE for any helper methods

	/**
	 * Helper function to add points in current Quadtree to list
	 * @param buffer	ArrayList to hold points
	 */
	private void getPoints(List<E> buffer) {
		buffer.add(this.point);
		for (int quadrant=1; quadrant<=4; quadrant++) {
			if (hasChild(quadrant)) {
				getChild(quadrant).getPoints(buffer);
			}
		}
	}

	/**
	 * Function to take an ArrayList and a circle then add all points that are in the circle into the provided list
	 * @param buffer	Array
	 * @param cx		center x
	 * @param cy		center y
	 * @param cr		radius
	 */
	private void getPointsInCircle(Set<E> buffer, double cx, double cy, double cr) {

		// if scope of current PointQuadtree intersects circle:
		if (this.intersectsCircle(cx, cy, cr)){

			// if current point is in circle, add point to list
			// for each quadrant with a child, recurse with the child
			if (this.isInCircle(cx, cy, cr)){
				buffer.add(point);
			}

			// Loop over the 4 quadrants.
			// if tree has child in quadrant, recurse with the child.

			for (int quadrant=1; quadrant<=4; quadrant++) {
				if (hasChild(quadrant)) {
					getChild(quadrant).getPointsInCircle(buffer, cx, cy, cr);
				}
			}
		}
	}

	/**
	 * Function to determine if a provided point is inside a specified circle
	 * @param cx	center x
	 * @param cy	center y
	 * @param cr	radius
	 * @return		returns a boolean status on if point lies inside circle
	 */
	public boolean isInCircle(double cx, double cy, double cr) {
		double pointX = point.getX();
		double pointY = point.getY();

		// check if square of distance from point to center of circle is less than square of radius
		return ((pointX-cx)*(pointX-cx) + (pointY-cy)*(pointY-cy) <= (cr * cr));
	}

	/**
	 * Function to determine if a circle intersects with a tree's rectangle scope
	 * @param cy	circle center y
	 * @param cr	circle radius
	 * @return		returns a boolean depending on intersection
	 */
	public boolean intersectsCircle(double cx, double cy, double cr){

		/*
		 * I saw Prof's version of this check and thought
		 * it would be a challenge to conceptualize my own check from scratch
		 * So I did!
		 */

		// Step 1: Check if cx is within range (left to right, within distance cr before to distance cr after rectangle)
 		if ((cx >= x1-cr) && (cx <= x2+cr)) {

			// Step 2: check if cy is within range (top-to-bottom, within distance cr above to distance cr below rectangle)
			return (cy >= y1-cr) && (cy <= y2+cr);
		}

		/*
		 *if first check fails, then second check won't run
		 * BUT we still need to return something!
		 */

		// return false
		else return false;
	}

	/**
	 * Function to find parentCoordinates of a given point inside a another tree
	 * @param point The point where child to find is.
	 * @param parentCoordinates List for coordinates of parentCoordinates to be added in.
	 */
	public void findParent(E point, int[] parentCoordinates) {

		// Step 1: determine what quadrant of current tree the point would be in
		int quadrant = getQuadrant(point);

		// Step 2: check if current tree has child in that quadrant -- typically evaluates to true
		if (this.hasChild(quadrant)) {

			// Step 2.1: get child
			PointQuadtree<E> child = this.getChild(quadrant);

			// Check if child's coordinates match coordinates of given point
			// Step 2.2: IF true:
			if (child.point.getX() == point.getX() && child.point.getY() == point.getY()) {
				parentCoordinates[0] = (int) this.point.getX();
				parentCoordinates[1] = (int) this.point.getY();
			}

			// Step 2.3: If false:
			else {
				// then recurse with the child and test if for parentage
				child.findParent(point, parentCoordinates);
			}
		}

		// Step 3: if no parent found, print error message
		if (parentCoordinates.length == 0) {
			System.err.println("No parentCoordinates found!");
		}
	}
}
