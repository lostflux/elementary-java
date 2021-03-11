import java.util.*;

/**
 * Beginnings of a library for graph analysis code
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2017 (with some inspiration from previous terms)
 * 
 */
public class GraphLib {
	/**
	 * Orders vertices in decreasing order by their in-degree
	 * @param g		graph
	 * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
		List<V> vs = new ArrayList<V>();
		for (V v : g.vertices()) vs.add(v);
		vs.sort((v1, v2) -> g.inDegree(v2) - g.inDegree(v1));
		return vs;
	}

	/**
	 * Takes a random walk from a vertex, to a random one if its out-neighbors, to a random one of its out-neighbors
	 * Keeps going as along as a random number is less than "continueProb"
	 * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
	 * @param g			graph to walk on
	 * @param start		initial vertex (assumed to be in graph)
	 * @param keepOn		probability of continuing each time -- should be between 0 and 1 (non-inclusive)
	 * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list
	 * 			    null if start isn't in graph
	 */
	public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, double keepOn) {
		if (!g.hasVertex(start) || keepOn <= 0 || keepOn >= 1) return null;
		List<V> path = new ArrayList<V>();
		path.add(start);
		V curr = start;
		while (Math.random()<keepOn) {
			if (g.outDegree(curr) == 0) return path;
			// Pick a neighbor index
			int nbr = (int)(g.outDegree(curr) * Math.random());
			// Iterate through the out-neighbors the given number of times
			Iterator<V> iter = g.outNeighbors(curr).iterator();
			V next = iter.next();
			while (nbr > 0) {
				next = iter.next();
				nbr--;
			}
			// Got to the right neighbor; continue from there
			path.add(next);
			curr = next;
		}

		return path;
	}
	
	/**
	 * Takes a number of random walks from random vertices, keeping track of how many times it goes to each vertex
	 * Doesn't actually keep the walks themselves
	 * @param g			graph to walk on
	 * @param keepOn		probability of continuing each time -- should be between 0 and 1 (non-inclusive)
	 * @param numWalks	how many times to do that
	 * @return			vertex-hitting frequencies
	 */
	public static <V,E> Map<V,Integer> randomWalks(Graph<V,E> g, double keepOn, int numWalks) {
		if (keepOn <= 0 || keepOn >= 1) return null;
		
		// Initialize all frequencies to 0
		Map<V,Integer> freqs = new HashMap<V,Integer>();
		for (V v : g.vertices()) freqs.put(v, 0);
		
		for (int i=0; i<numWalks; i++) {
			// Pick a start index
			int start = (int)(g.numVertices()*Math.random());
			// Iterate through vertices till get there
			Iterator<V> iter = g.vertices().iterator();
			V curr = iter.next();
			while (start > 0) {
				curr = iter.next();
				start--;
			}
			while (Math.random()<keepOn && g.outDegree(curr)>0) {
				// Pick a neighbor index
				int nbr = (int)(g.outDegree(curr) * Math.random());
				// Iterate through the out-neighbors the given number of times
				iter = g.outNeighbors(curr).iterator();
				V next = iter.next();
				while (nbr > 0) {
					next = iter.next();
					nbr--;
				}
				// Keep frequency count
				freqs.put(next, 1+freqs.get(next));
				curr = next;
			}			
		}

		return freqs;
	}


	/**
	 * Orders vertices in decreasing order by their frequency in the map
	 * @param g		graph
	 * @return		list of vertices sorted by frequency, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByFrequency(Graph<V,E> g, Map<V,Integer> freqs) {
		List<V> vs = new ArrayList<V>();
		for (V v : g.vertices()) vs.add(v);
		vs.sort((v1, v2) -> freqs.get(v2) - freqs.get(v1));
		return vs;
	}
	
	// TODO: SA-7 Methods
	
	/**
	 * Identifies potential neighbors w for u, with w!=u, such that there's an edge u->v and an edge v->w
	 * @param g		graph
	 * @param u		vertex of interest
	 * @return		out-neighbors of out-neighbors of v (but not w itself)
	 */
	public static <V,E> Set<V> suggestions(Graph<V,E> g, V u) {
		// TODO: Your code here

		// Initialize set -- to hold unique neighbors
		Set<V> nearConnections = new HashSet<>();

		// Loop over all friend vertices
		for (V connectedVertex : g.outNeighbors(u)) {

			// Loop over all friends of friend vertex
			for (V secondVertex : g.outNeighbors(connectedVertex)) {

				// if connected vertex is not original vertex, add to set
				if (secondVertex != u) {
					nearConnections.add(secondVertex);
				}
			}
		}

		// Loop over all
		// return the set of all second-order neighbors
		return nearConnections;
	}
	
	/**
	 * Returns a flipped version of the graph; i.e., every edge u->v in g is v->u in the returned graph, with the same edge label
	 * @param g		graph
	 * @return		flipped version
	 */
	public static <V,E> Graph<V,E> flip(Graph<V,E> g) {
		// TODO: Your code here

		// Initialize the flipped Graph
		Graph<V,E> flippedGraph = new AdjacencyMapGraph<>();

		// Get each vertex from original graph and insert into flipped Graph
		for (V vertex : g.vertices()) {
			flippedGraph.insertVertex(vertex);
		}

		// Loop over vertex (to be treated as receptor of some connection)
		for (V receptor : g.vertices()) {

			// Loop over receptor's vertices who are pointing into the receptor
			for (V pointer : g.inNeighbors(receptor)) {

				// Get the label of the edge from the pointer to the receptor in original Graph
				E edge = g.getLabel(pointer, receptor);

				// Create a connection from receptor to pointer in flipped Graph
				flippedGraph.insertDirected(receptor, pointer, edge);

			}
		}
		// return flipped Graph
		return flippedGraph;
	}

	// TODO: PS4 Methods

	/**
	 * Function to build shortest-path tree
	 * @param g			// Graph
	 * @param source	// Vertex start-point in Graph
	 * @param <V>		// Vertex data type
	 * @param <E>		// Edge data type
	 * @return			A new Graph with the start-point vertex as center
	 */
	public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {

		// BFS
		HashMap<V,V> backTrack = new HashMap<>(); 	// initialize backTrack
		backTrack.put(source, null); 				// insert source into vertex map
		Set<V> visited = new HashSet<V>(); 			// Set to track which vertices have already been visited
		Queue<V> queue = new LinkedList<V>(); 		// queue to implement BFS

		queue.add(source); 		// enqueue start vertex
		visited.add(source); 	// add start to visited Set
		while (!queue.isEmpty()) { 	// loop until no more vertices
			V u = queue.remove(); 	// dequeue
			if (g.outNeighbors(u) != null) {
				for (V v : g.outNeighbors(u)) { // loop over out neighbors
					if (!visited.contains(v)) { // if neighbor not visited
						visited.add(v); 		// add neighbor to visited Set
						queue.add(v); 			// add neighbor to queue
						backTrack.put(v, u); 	// save path (current to discovered)
					}
				}
			}
		}
		// After BFS, start building new Graph
		Graph<V,E> map = new AdjacencyMapGraph<>();	// Initialize Graph

		// Insert every vertex in original Graph into new Graph if it's in the backTrack
		for (V vertex : g.vertices()) {
			if (backTrack.containsKey(vertex) || backTrack.containsValue(vertex)) {
				map.insertVertex(vertex);
			}
		}

		// Loop over generated list of connections from BFS and add them to new Graph
		for (V key : backTrack.keySet()) {
			V value = backTrack.get(key);
			if (value != null) { // Since source will have null value
				E edge = g.getLabel(key, value);
				map.insertDirected(key, value, edge); // insert directed edge

			}
		}
		// return the created Graph
		return map;
	}

	public static <V,E> List<V> getPath(Graph<V,E> tree, V v) {
		List<V> path = new ArrayList<>(); 		// Initialize list to hold path
		path.add(v);							// Add the final point to path

		// Initialize iteration over outNeighbors (should only be one)
		Iterator<V> iter = tree.outNeighbors(v).iterator();

		// while an outNeighbor exists, get, add to path,
		// and set iterator to its outNeighbors
		while (iter.hasNext()) {
			// This kept throwing exceptions -- with the null iterator of the central vertex
			try {
				V next = iter.next();
				path.add(next);
				iter = tree.outNeighbors(next).iterator();
			}
			catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		// Return created pathway list
		return path;

	}

	/**
	 * Function to find vertices in main Graph missing in sub graph
	 * @param graph Main Graph to check
	 * @param subgraph Smaller Graph containing subset of vertices in main Graph
	 * @param <V> Vertex Data type
	 * @param <E> Edge Data type
	 * @return Returns a set of vertices in main Graph missing in sub graph
	 */
	public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
		Set<V> missingVertices = new HashSet<V>();

		// Create list of vertices in subgraph
		List<V> subVertices = new ArrayList<>();
		for (V vertex : subgraph.vertices()) {
			subVertices.add(vertex);
		}

		// Loop over all vertices in original graph
		for (V v : graph.vertices()) {
			// If vertex not in list of vertices in subgraph, add to set
			if (!subVertices.contains(v)) {
				missingVertices.add(v);
			}
		}

		// return list of missing vertices
		return missingVertices;
	}

	/**
	 * Function to recursively compute the separation of each a vertex from root
	 * @param tree 		pathTree Graph to be analyzed for separation
	 * @param root 		The vertex at the center of the pathTree
	 * @param vertex	The vertex whose separation from center of Graph is to be found
	 * @param <V>		Vertex data type
	 * @param <E>		edge label data type
	 * @return 			double
	 */
	public static <V,E> int getSeparation(Graph<V,E> tree, V root, V vertex) {
		int separation = 0;
		if (vertex == root) {
			return separation;
		}
		// if vertex is not root, get next vertex in path
		// and recursively add its separation
		if (!vertex.equals(root)) {
			separation += 1;
			try {
				for (V next : tree.outNeighbors(vertex)) {
					separation += getSeparation(tree, root, next);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return separation;
	}

	/**
	 * Function to accumulate the separations for each vertex in the tree
	 * @param tree	The pathTree Graph to be analyzed for average separation
	 * @param root	The vertex at the center of the pathTree Graph
	 * @param <V>	Vertex data type
	 * @param <E>	Edge label data type
	 * @return		double
	 */
	public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {

		// Initialize total and count
		double total = 0;
		int count = 0;

		// For each connected vertex, find separation to root and accumulate
		for (V vertex : tree.vertices()) {
			if (!vertex.equals(root)) {
				total += getSeparation(tree, root, vertex);
				count += 1;
			}
		}
		// Finally, return the average, i.e. total/count
		return total/count;
	}
}
