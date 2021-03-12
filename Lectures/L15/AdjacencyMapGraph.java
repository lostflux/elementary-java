import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Adjancency Map implementation of the Graph interface
 * Edge labels are stored in nested maps: { v1 -> { v2 -> edge } }
 * Inspired by and loosely based on Goodrich & Tamassia
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 */

public class AdjacencyMapGraph<V,E> implements Graph<V,E> {
	protected Map<V, Map<V, E>> out;		// from v1 to v2: { v1 -> { v2 -> edge } }
	protected Map<V, Map<V, E>> in;		// to v1 from v2: { v1 -> { v2 -> edge } }

	/**
	 * Default constructor, creating an empty graph 
	 */
	public AdjacencyMapGraph() {
		in = new HashMap<>();
		out = new HashMap<>();
	}

	public int numVertices() {
		return out.keySet().size();
	}

	public boolean hasOut(V v) {
		return out.containsKey(v);
	}

	public int numEdges() {
		// Don't actually keep an edge list around, so compute this on the fly
		int n = 0;
		for (V v : vertices()) n += outDegree(v);
		return n;
	}

	public Iterable<V> vertices() {
		return out.keySet();
	}

	public boolean hasVertex(V v) {
		return out.containsKey(v);
	}

	public int outDegree(V v) {
		return out.get(v).size();
	}

	public int inDegree(V v) {
		return in.get(v).size();
	}

	public Iterable<V> outNeighbors(V v) {
//		System.out.println(v);
		return out.get(v).keySet();
	}

	public Iterable<V> inNeighbors(V v) {
		return in.get(v).keySet();
	}

	public boolean hasEdge(V u, V v) {
		return out.get(u).containsKey(v);
	}

	public E getLabel(V u, V v) {
		return out.get(u).get(v);
	}

	public void insertVertex(V v) {
		if (!out.containsKey(v)) {
			out.put(v, new HashMap<>());		// edges from v
			in.put(v, new HashMap<>());			// edges to v
		}
	}

	public void insertDirected(V u, V v, E e) {
		out.get(u).put(v, e);
		in.get(v).put(u, e);
	}

	public void insertUndirected(V u, V v, E e) {
		// insert in both directions
		insertDirected(u, v, e);
		insertDirected(v, u, e);
	}

	public void removeVertex(V v) {
		if (!out.containsKey(v)) return;
		// remove v from all adjacency lists for other vertices
		for (V u : inNeighbors(v)) { // u has an edge to v
			out.get(u).remove(v);
		}
		for (V w : outNeighbors(v)) { // w has an edge from v
			in.get(w).remove(v);
		}
		in.remove(v);
		out.remove(v);
	}

	public void removeDirected(V u, V v) {
		in.get(v).remove(u);
		out.get(u).remove(v);
	}
	
	public void removeUndirected(V u, V v) {
		// remove in both directions
		removeDirected(u, v);
		removeDirected(v, u);
	}

	/** 
	 * Returns a string representation of the vertex and edge lists.
	 */
	public String toString() {
		return "Vertices: " + out.keySet().toString() + "\nOut edges: " + out.toString();
	}
}
