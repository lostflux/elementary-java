import java.util.HashSet;
import java.util.Set;

public class BasicGraphTest {
    public static void main(String[] args) {
        Graph<String, HashSet<String>> movies = new AdjacencyMapGraph<>();
        // Add vertices
        movies.insertVertex("Kevin Bacon");
        movies.insertVertex("Bob");
        movies.insertVertex("Alice");
        movies.insertVertex("Charlie");
        movies.insertVertex("Dartmouth (Earl thereof)");
        movies.insertVertex("Nobody");
        movies.insertVertex("Nobody's Friend");

        // Add undirected connections
        movies.insertUndirected("Kevin Bacon", "Bob", new HashSet<>(){{add("A Movie");}});
        movies.insertUndirected("Kevin Bacon", "Alice", new HashSet<>(){{add("A Movie"); add("E Movie");}});
        movies.insertUndirected("Bob", "Alice", new HashSet<>(){{add("A Movie");}});
        movies.insertUndirected("Bob", "Charlie", new HashSet<>(){{add("C Movie");}});
        movies.insertUndirected("Alice", "Charlie", new HashSet<>(){{add("D Movie");}});
        movies.insertUndirected("Charlie", "Dartmouth (Earl thereof)", new HashSet<>(){{add("B Movie");}});
        movies.insertUndirected("Nobody", "Nobody's Friend", new HashSet<>(){{add("F Movie");}});

        System.out.println(movies);

        Graph<String, HashSet<String>> shortestPaths = GraphLib.bfs(movies, "Kevin Bacon");
        System.out.println(shortestPaths);
        System.out.println(GraphLib.getPath(shortestPaths, "Dartmouth (Earl thereof)"));
//        System.out.println(movies.outNeighbors("Charlie"));
        System.out.println("Shortest path Alice to Kevin Bacon: " + GraphLib.getPath(shortestPaths, "Alice"));
        System.out.println("Full Graph: " + movies.vertices());
        System.out.println("Sub Graph:" + shortestPaths.vertices());
        System.out.println("Missing: " + GraphLib.missingVertices(movies, shortestPaths));

        System.out.println("Average Separation: " + GraphLib.averageSeparation(shortestPaths, "Kevin Bacon"));
    }
}
