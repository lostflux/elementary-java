import java.util.List;

/**
 * Hard-coded tests for point quadtrees, Dartmouth CS 10, Fall 2017
 * 
 * @author Chris Bailey-Kellogg, Fall 2017, extracted from other code, augmented
 * @author CBK, Winter 2021, minor improvements
 *
 */
public class QuadtreeTest {
	/**
	 * Is the tree of the expected size, both from size() and from allPoints()?
	 * @param tree
	 * @param size
	 * @return
	 */
	private static int testSize(PointQuadtree<Dot> tree, int size) {
		int errs = 0;
		
		if (tree.size() != size) {
			errs++;
			System.err.println("wrong size: got "+tree.size()+" but expected "+size);
		}

		List<Dot> points = tree.allPoints();
		if (points.size() != size) {
			errs++;
			System.err.println("wrong points size: got "+points.size()+" but expected "+size);
		}

		return errs;
	}
	
	/**
	 * A simple testing procedure, making sure actual is expected, and printing a message if not
	 * @param x		query x coordinate
	 * @param y		query y coordinate
	 * @param r		query circle radius
	 * @param expectedCircleRectangle	how many times Geometry.circleIntersectsRectangle is expected to be called
	 * @param expectedInCircle			how many times Geometry.pointInCircle is expected to be called
	 * @param expectedHits				how many points are expected to be found
	 * @return  0 if passed; 1 if failed
	 */
	private static int testFind(PointQuadtree<Dot> tree, int x, int y, int r, int expectedCircleRectangle, int expectedInCircle, int expectedHits) {
		Geometry.resetNumInCircleTests();
		Geometry.resetNumCircleRectangleTests();
		int errs = 0;
		int num = tree.findInCircle(x, y, r).size();
		String which = "find near ("+x+","+y+") with radius "+r;

		//TODO Added code here
		checkAll(tree, x, y, r);


		if (Geometry.getNumCircleRectangleTests() != expectedCircleRectangle) {
			errs++;
			System.err.println(which+": wrong # circle-rectangle, got "+Geometry.getNumCircleRectangleTests()+" but expected "+expectedCircleRectangle);
		}
		else {
			System.out.println("Correct number of circle-rectangle checks, got "+Geometry.getNumCircleRectangleTests()+", expected "+expectedCircleRectangle);
		}
		if (Geometry.getNumInCircleTests() != expectedInCircle) {
			errs++;
			System.err.println(which+": wrong # in circle, got "+Geometry.getNumInCircleTests()+" but expected "+expectedInCircle);
		}
		else {
			System.out.println("Correct number of point in circle checks, got "+Geometry.getNumInCircleTests()+", expected "+expectedInCircle);

		}
		if (num != expectedHits) {
			errs++;
			System.err.println(which+": wrong # hits, got "+num+" but expected "+expectedHits);
		}
		else {
			System.out.println("Got correct number of hits: " + num);
		}
		return errs;
	}

	// TODO: Added this function to query the points using the relevant PointQuadTree method and only accumulate where condition is passed
	private static void checkAll(PointQuadtree<Dot> tree, int x, int y, int r) {
		if (tree.intersectsCircle(x, y, r)) {
			int x1 = tree.getX1();
			int x2 = tree.getX2();
			int y1 = tree.getY1();
			int y2 = tree.getY2();

			boolean geoRect = Geometry.circleIntersectsRectangle(x, y, r, x1, y1, x2, y2);

//			if (!geoRect) System.out.println("Mismatch! tree: true, Geometry: false");
			if (tree.isInCircle(x, y, r)) {
				int px = tree.getX();
				int py = tree.getY();
				Geometry.pointInCircle(px, py, x, y, r);
			}
			for (int quadrant=1; quadrant<=4; quadrant++) {
				if (tree.hasChild(quadrant)) {
					PointQuadtree<Dot> child = tree.getChild(quadrant);
					checkAll(child, x, y, r);
				}
			}
		}
	}
	
	/**
	 * test tree 0 -- first three points from figure in handout
	 * hardcoded point locations for 800x600
	 */
	private static void test0() {
		PointQuadtree<Dot> tree = new PointQuadtree<Dot>(new Dot(300,400, "A"), 0,0,800,600); // start with A
		tree.insert(new Dot(150,450, "B"));
		tree.insert(new Dot(250,550, "C"));
		int bad = 0;
		bad += testSize(tree, 3);
		bad += testFind(tree, 0,0,900,3,3,3);		// rect for all; circle for all; find all
		bad += testFind(tree, 300,400,10,3,2,1);		// rect for all; circle for A,B; find A
		bad += testFind(tree, 150,450,10,3,3,1);		// rect for all; circle for all; find B
		bad += testFind(tree, 250,550,10,3,3,1);		// rect for all; circle for all; find C
		bad += testFind(tree, 150,450,150,3,3,2);	// rect for all; circle for all; find B, C
		bad += testFind(tree, 140,440,10,3,2,0);		// rect for all; circle for A,B; find none
		bad += testFind(tree, 750,550,10,2,1,0);		// rect for A,B; circle for A; find none
		if (bad==0) System.out.println("test 0 passed!");
		else System.out.println("test 0 failed! "  + bad + " errors.");
	}

	/**
	 * test tree 1 -- figure in handout
	 * hardcoded point locations for 800x600
	 */
	private static void test1() {
		PointQuadtree<Dot> tree = new PointQuadtree<Dot>(new Dot(300,400, "A"), 0,0,800,600); // start with A
		tree.insert(new Dot(150,450, "B"));
		tree.insert(new Dot(250,550, "C"));
		tree.insert(new Dot(450,200, "D"));
		tree.insert(new Dot(200,250, "E"));
		tree.insert(new Dot(350,175, "F"));
		tree.insert(new Dot(500,125, "G"));
		tree.insert(new Dot(475,250, "H"));
		tree.insert(new Dot(525,225, "I"));
		tree.insert(new Dot(490,215, "J"));
		tree.insert(new Dot(700,550, "K"));
		tree.insert(new Dot(310,410, "L"));
		int bad = 0;
		bad += testSize(tree, 12);
		bad += testFind(tree, 150,450,10,6,3,1); 	// rect for A [D] [E] [B [C]] [K]; circle for A, B, C; find B
		bad += testFind(tree, 500,125,10,8,3,1);		// rect for A [D [G F H]] [E] [B] [K]; circle for A, D, G; find G
		bad += testFind(tree, 300,400,15,10,6,2);	// rect for A [D [G F H]] [E] [B [C]] [K [L]]; circle for A,D,E,B,K,L; find A,L
		bad += testFind(tree, 495,225,50,10,6,3);	// rect for A [D [G F H [I [J]]]] [E] [B] [K]; circle for A,D,G,H,I,J; find H,I,J
		bad += testFind(tree, 0,0,900,12,12,12);		// rect for all; circle for all; find all
		if (bad==0) System.out.println("test 1 passed!");
		else System.out.println("test 1 failed!");
	}
	
	// TODO: YOUR CODE HERE -- additional test case(s)

	/**
	 * Test for rectangles intersections and circle intersections (see "image01.jpg")
	 */
	private static void test2() {
		// Create cascading tree with each child in 4th quadrant of parent
		PointQuadtree<Dot> tree = new PointQuadtree<Dot>(new Dot(100,100, "A"), 0,0,800,800); // start with A
		tree.insert(new Dot(200,200, "B"));
		tree.insert(new Dot(300,300, "C"));
		tree.insert(new Dot(400,400, "D"));
		tree.insert(new Dot(500,500, "E"));
		tree.insert(new Dot(600,600, "F"));
		tree.insert(new Dot(700,700, "G"));

		// Check
		int bad = 0;
		bad += testSize(tree, 7);
		bad+= testFind(tree, 100, 100, 10, 2, 1, 1);
		bad+= testFind(tree, 200, 200, 10, 3, 1, 1);
		bad+= testFind(tree, 300, 300, 10, 4, 1, 1);
		bad+= testFind(tree, 400, 400, 10, 5, 1, 1);
		bad+= testFind(tree, 500, 500, 10, 6, 1, 1);
		bad+= testFind(tree, 600, 600, 10, 7, 1, 1);
		bad+= testFind(tree, 700, 700, 10, 7, 1, 1);
		System.out.println(bad);
	}


	/**
	 * @deprecated
	 */
	private static void test3() {
		PointQuadtree<Dot> tree = new PointQuadtree<Dot>(new Dot(300,400, "A"), 0,0,800,600); // start with A
		tree.insert(new Dot(150,450, "B"));
		tree.insert(new Dot(250,550, "C"));
		System.out.println(tree.size());
		System.out.println(tree.allPoints());
		System.out.println(tree.intersectsCircle(400, 400, 200));
		System.out.println(tree.findInCircle(400, 400, 200));
	}

	/**
	 * @deprecated
	 */
	public static void test4() {
		PointQuadtree<Dot> tree = new PointQuadtree<Dot>(new Dot(400,400, "A"), 0,0,800,800); // start with A
		tree.insert(new Dot(200,200, "B"));
		tree.insert(new Dot(200,600, "C"));
		tree.insert(new Dot(600, 200, "D"));
		tree.insert(new Dot(600,600, "E"));
		testSize(tree, 5);

		System.out.println(tree.getName());
		for (int quadrant=1; quadrant <=4; quadrant++){
			if (tree.hasChild(quadrant)){
				System.out.println(tree.getChild(quadrant).getName());
			}
		}
	}

	/**
	 * Additional functionality added in DotTreeGUI for testing
	 * Also see submitted image
	 */
	public static void test5() {
		String drawMode = "boxes";
		new DotTreeGUI(drawMode);
	}

	/**
	 * Additional functionality added in DotTreeGUI for testing
	 * Also see submitted image
	 */
	public static void test6() {
		String drawMode = "connections";
		new DotTreeGUI(drawMode);
	}
	
	public static void main(String[] args) {
//		test0();
//		test1();
		// TODO: YOUR CODE HERE -- call additional test case(s)

		// Test 2: cascading tree test
//		test2();

		// Test 3 and 4: I don't remember what they were doing but
//		test3();
//		test4();

		// Test 5: Boxes around nodes -- visualize boxes around trees
//		test5();

		//NOTE: Don't run test 5 and test 6 at the same time since
		// they both ultimately call DotTreeGUI with a modification of the same parameter --
		// and therefore interfere with each other's result.

		// Test 6: Connections -- visualize connections between parent and child trees
		test6();
	}
}
