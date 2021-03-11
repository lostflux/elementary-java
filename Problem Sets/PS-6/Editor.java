//import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
//import java.util.TreeMap;

import javax.swing.*;

/**
 * Client-server graphical editor
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; loosely based on CS 5 code by Tom Cormen
 * @author CBK, winter 2014, overall structure substantially revised
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 * @author CBK, spring 2016 and Fall 2016, restructured Shape and some of the GUI
 */

public class Editor extends JFrame {
	private static String serverIP = "localhost";			// IP address of sketch server
	// "localhost" for your own machine;
	// or ask a friend for their IP address

	private static final int width = 800, height = 800;		// canvas size

	// Current settings on GUI
	public enum Mode {
		DRAW, MOVE, RECOLOR, DELETE
	}
	private Mode mode = Mode.DRAW;				// drawing/moving/recoloring/deleting objects
	private String shapeType = "ellipse";		// type of object to add
	private Color color = Color.black;			// current drawing color

	// Drawing state
	// these are remnants of my implementation; take them as possible suggestions or ignore them
	private Shape curr = null;					// current shape (if any) being drawn
	private final Sketch sketch;						// holds and handles all the completed objects
	private int movingId = -1;					// current shape id (if any; else -1) being moved
	private Point drawFrom = null;				// where the drawing started
	private Point moveFrom = null;				// where object is as it's being dragged


	// Communication
	private final EditorCommunicator comm;			// communication with the sketch server

	public Editor() {
		super("Graphical Editor");

		sketch = new Sketch();

		// Connect to server
		comm = new EditorCommunicator(serverIP, this);
		comm.start();

		// Helpers to create the canvas and GUI (buttons, etc.)
		JComponent canvas = setupCanvas();
		JComponent gui = setupGUI();

		// Put the buttons and canvas together into the window
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(canvas, BorderLayout.CENTER);
		cp.add(gui, BorderLayout.NORTH);

		// Usual initialization
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Creates a component to draw into
	 */
	private JComponent setupCanvas() {
		JComponent canvas = new JComponent() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				drawSketch(g);
			}
		};
		
		canvas.setPreferredSize(new Dimension(width, height));

		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				handlePress(event.getPoint());
			}

			public void mouseReleased(MouseEvent event) {
				handleRelease();
			}
		});		

		canvas.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent event) {
				handleDrag(event.getPoint());
			}
		});
		
		return canvas;
	}

	/**
	 * Creates a panel with all the buttons
	 */
	private JComponent setupGUI() {
		// Select type of shape
		String[] shapes = {"ellipse", "rectangle", "segment"};
		JComboBox<String> shapeB = new JComboBox<String>(shapes);
		shapeB.addActionListener(e -> shapeType = (String)((JComboBox<String>)e.getSource()).getSelectedItem());

		// Select drawing/recoloring color
		// Following Oracle example
		JButton chooseColorB = new JButton("choose color");
		JColorChooser colorChooser = new JColorChooser();
		JLabel colorL = new JLabel();
		colorL.setBackground(Color.black);
		colorL.setOpaque(true);
		colorL.setBorder(BorderFactory.createLineBorder(Color.black));
		colorL.setPreferredSize(new Dimension(25, 25));
		JDialog colorDialog = JColorChooser.createDialog(chooseColorB,
				"Pick a Color",
				true,  //modal
				colorChooser,
				e -> { color = colorChooser.getColor(); colorL.setBackground(color); },  // OK button
				null); // no CANCEL button handler
		chooseColorB.addActionListener(e -> colorDialog.setVisible(true));

		// Mode: draw, move, recolor, or delete
		JRadioButton drawB = new JRadioButton("draw");
		drawB.addActionListener(e -> mode = Mode.DRAW);
		drawB.setSelected(true);
		JRadioButton moveB = new JRadioButton("move");
		moveB.addActionListener(e -> mode = Mode.MOVE);
		JRadioButton recolorB = new JRadioButton("recolor");
		recolorB.addActionListener(e -> mode = Mode.RECOLOR);
		JRadioButton deleteB = new JRadioButton("delete");
		deleteB.addActionListener(e -> mode = Mode.DELETE);
		ButtonGroup modes = new ButtonGroup(); // make them act as radios -- only one selected
		modes.add(drawB);
		modes.add(moveB);
		modes.add(recolorB);
		modes.add(deleteB);
		JPanel modesP = new JPanel(new GridLayout(1, 0)); // group them on the GUI
		modesP.add(drawB);
		modesP.add(moveB);
		modesP.add(recolorB);
		modesP.add(deleteB);

		// Put all the stuff into a panel
		JComponent gui = new JPanel();
		gui.setLayout(new FlowLayout());
		gui.add(shapeB);
		gui.add(chooseColorB);
		gui.add(colorL);
		gui.add(modesP);
		return gui;
	}

	/**
	 * Getter for the sketch instance variable
	 */
	public Sketch getSketch() {
		return sketch;
	}

	/**
	 * Draws all the shapes in the sketch,
	 * along with the object currently being drawn in this editor (not yet part of the sketch)
	 */
	public void drawSketch(Graphics g) {
		// TODO: YOUR CODE HERE
		// For each Shape in the sketch, draw it
		if (curr != null) curr.draw(g);
		for (int shapeId : sketch.getKeys()){
			// If a drawing is in progress, draw it.
			sketch.getShape(shapeId).draw(g);
		}
	}

	// Helpers for event handlers
	
	/**
	 * Helper method for press at point
	 * In drawing mode, start a new object;
	 * in moving mode, (request to) start dragging if clicked in a shape;
	 * in recoloring mode, (request to) change clicked shape's color
	 * in deleting mode, (request to) delete clicked shape
	 */
	private void handlePress(Point p) {
		// TODO: YOUR CODE HERE
		switch (mode) {
			case DRAW -> {
				// In drawing mode start creating shape
				if (drawFrom == null) drawFrom = p;

				// NB: Don't send to server yet until shape is completed

			}
			case MOVE -> {
				// In moving mode, if pointer clicked in a shape, find it's shape ID
				// And set it to movingId so it can be dragged as mouse is dragged

				// TODO: I had this check just to be sure I don't re-set the movingId while moving a shape.
				//  turned out I didn't need it but just left it in case things broke last-minute.
//				if (movingId == -1) {

					// Check which shape contains cursor
					for (int shapeId : sketch.getKeysReverse()) { // Get latest keys first
						if (sketch.getShape(shapeId).contains(p.x, p.y)) {
							// Set cursor position to moveFrom point
							moveFrom = p;

							// Set shape ID to moving ID
							movingId = shapeId;
							break;
						}
					}
//				}
			}
			case RECOLOR -> {
				// TODO: In recoloring mode, change the shape's color if clicked in it

				// Initialize recolorId with an invalid value
				// so we can detect if it is not changed
				int recolorId = -1;

				// Loop over shapes in sketch and find one that contains cursor point
				for (int shapeId : sketch.getKeysReverse()) {
					Shape currentShape = sketch.getShapesMap().get(shapeId);	// Get shape
					if (currentShape.contains(p.x, p.y)) {						// If shape contains cursos,
						recolorId = shapeId;				// Select it to recolor
						break;
					}
				}
				// If a shape was identified (i.e. targetId is not -1), send instruction to server to recolor shape
				if (recolorId != -1) {
					// Generate random color
					Random random = new Random();
					float r = random.nextFloat();			// RED 		--> 0 < r < 1
					float g = random.nextFloat();			// GREEN	--> 0 < g < 1
					float b = random.nextFloat();			// BLUE		--> 0 < b < 1
					Color color = new Color(r, g, b);		// Create color

					comm.send("recolor " + recolorId +" "+ color.getRGB()); // send message
				}
				else { // if shapeId is still -1, no shape was selected. Send feedback to user
					System.err.println("No shape selected! Please click inside a shape.");
				}

			}
			case DELETE -> {
				// Initialize deleteId with an invalid value
				// so we can later detect if it was changed or not
				int deleteId = -1;

				// Loop over all shapes in sketch checking which one contains cursor
				for (int shapeId : sketch.getKeysReverse()) {
					Shape currentShape = sketch.getShapesMap().get(shapeId);
					if (currentShape.contains(p.x, p.y)) {
						deleteId = shapeId; // set deleteId to shapeId and break loop
						break;
					}
				}
				// If a shape has been selected, send message to server to delete it.
				if (deleteId != -1) {
					comm.send("delete " + deleteId);
				}
				else { // if shapeId is still -1, no shape was selected. Send feedback to user
					System.err.println("No shape selected! Please click inside a shape.");
				}
			}
		}
	}

	/**
	 * Helper method for drag to new point
	 * In drawing mode, update the other corner of the object;
	 * in moving mode, (request to) drag the object
	 */
	private void handleDrag(Point p) {
		// TODO: YOUR CODE HERE

		switch (mode) {
			case DRAW -> {

				// Just to be safe, check if drawFrom is null and set to current cursor point.
				// This should not run since the point is set at mouse press, but who knows?
				if (drawFrom == null) drawFrom = p;

				// Keep updating the shape being drawn
				switch (shapeType) {
					case "rectangle" -> curr = new Rectangle(drawFrom.x, drawFrom.y, p.x, p.y, color);
					case "segment" -> curr = new Segment(drawFrom.x, drawFrom.y, p.x, p.y, color);
					case "ellipse" -> curr = new Ellipse(drawFrom.x, drawFrom.y, p.x, p.y, color);
				}
			}
			case MOVE -> {
				// Find target shape, if none selected
				if (movingId == -1) {
					for (int shapeId : sketch.getShapesMap().keySet()) {
						Shape currentShape = sketch.getShapesMap().get(shapeId);
						if (currentShape.contains(p.x, p.y)) {
							movingId = shapeId;
							break;
						}
					}
				}

				// If movingId is still -1, user didn't click in any shape.
				// Print error message
				if (movingId == -1) System.err.println("Error! No shape selected. Please click inside a shape.");
				else {
					// find params for movement and send message to server
					int dx = p.x - moveFrom.x;
					int dy = p.y - moveFrom.y;
					comm.send("move " + movingId + " " + dx + " " + dy);

					// Adjust the moveFrom point to current cursor point
					moveFrom = p;
				}
			}
		}
	}

	/**
	 * Helper method for release
	 * In drawing mode, pass the add new object request on to the server;
	 * in moving mode, release it		
	 */
	private void handleRelease() {
		// TODO: YOUR CODE HERE
		// Set moveFrom and drawFrom to null
		switch(mode) {
			case DRAW -> {
				// If current shape is not null, send it to server
				// Set current shape to null
				if (curr != null) {
					// send shape to server
					comm.send("draw " + curr.toString());

					// Reset current shape and drawFrom point
					curr = null;
					drawFrom = null;
				}
			}
			case MOVE -> {
				// Reset movingId, moveFrom
				movingId = -1;
				moveFrom = null;
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Editor();
			}
		});
	}
}
