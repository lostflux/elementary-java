import java.io.*;
import java.net.Socket;

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg Message to be sent
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			// TODO: YOUR CODE HERE
			Sketch master = server.getSketch();										// Get the master sketch
			if (!master.isEmpty()) {												// If master sketch is not empty,
				for (int id : master.getKeys()) {									// Get each shape and broadcast it
					Shape shape = master.getShape(id);
					server.broadcast("draw " + shape.toString() + " " + id);
				}
			}

			// Keep getting and handling messages from the client
			// TODO: YOUR CODE HERE
			try {
				String msg;									// Initialize message
				while((msg = in.readLine()) != null) {		// Get message

					// if message is draw, generate random ID and append to message
					if (msg.startsWith("draw")) {
						int shapeId = server.getShapeId();
						msg += " " + shapeId;
					}

					// Update master sketch
					MessageParser.parse(server.getSketch(), msg);

					// Broadcast message to everyone else
					server.broadcast(msg);
				}
			}
			catch (IOException e) {
				// In case of error reading from stream of incoming messages
				e.printStackTrace();
			}

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
