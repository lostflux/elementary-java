import java.net.*;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A server to handle sketches: getting requests from the clients,
 * updating the overall state, and passing them on to the clients
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 */
public class SketchServer {
	private ServerSocket listen;						// for accepting connections
	private final ArrayList<SketchServerCommunicator> comms;	// all the connections with clients
	private Sketch sketch;								// the state of the world
	private int shapeId = 0;							// current shapeId
	
	public SketchServer(ServerSocket listen) {
		this.listen = listen;
		sketch = new Sketch();
		comms = new ArrayList<>();
	}

	public Sketch getSketch() {
		return sketch;
	}

	/**
	 * Getter for shapeId
	 * @return int value
	 */
	public int getShapeId() {
		shapeId++;			// Each time queried, increment shapeId
		return shapeId;
	}
	
	/**
	 * The usual loop of accepting connections and firing off new threads to handle them
	 */
	@SuppressWarnings("InfiniteLoopStatement")
	public void getConnections() throws IOException {
		System.out.println("server ready for connections");

		while (true) {
			SketchServerCommunicator comm = new SketchServerCommunicator(listen.accept(), this);
			comm.setDaemon(true);
			comm.start();
			addCommunicator(comm);
		}
	}

	/**
	 * Adds the communicator to the list of current communicators
	 */
	public synchronized void addCommunicator(SketchServerCommunicator comm) {
		comms.add(comm);
	}

	/**
	 * Removes the communicator from the list of current communicators
	 */
	public synchronized void removeCommunicator(SketchServerCommunicator comm) {
		comms.remove(comm);
	}

	/**
	 * Sends the message from the one communicator to all (including the originator)
	 */
	public synchronized void broadcast(String msg) {
		for (SketchServerCommunicator comm : comms) {
			comm.send(msg);
		}

//		List<SketchServerCommunicator> newComms = new ArrayList<>();

		List<SketchServerCommunicator> newComms = comms.stream().toList();

		IntStream stream = IntStream.range(0, 9);
		stream.forEach(System.out::println);

		List<Integer>	numbers	=	Arrays.asList(1, 2,	3,	4,	5,	6,	7,	8);
		List<Integer>	twoEvenSquares	=
				numbers.stream()
				.filter(n	->	{
					System.out.println("filtering	"	+	n);
					return	n	%	2	==	0;
				})
				.map(n	->	{
					System.out.println("mapping	"	+	n);
					return	n	*	n;
				})
				.limit(2)
				.collect(Collectors.toList());
	}
	
	public static void main(String[] args) throws Exception {
		new SketchServer(new ServerSocket(4242)).getConnections();
	}
}
