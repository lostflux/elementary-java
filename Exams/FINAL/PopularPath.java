import java.util.*;

public class PopularPath<V, E> {
    private Graph<V, E> g;                              // the network being analyzed -- assume already set
    private List<Map<V, Map<V, Integer>>> backCounts;   // for each path length (# steps away from start)
                                                        // the backtrace count: v -> u ->how many times the u->v edge from g was used
                                                        // in a path of that length

    /**
     * Constructor for PopularPath
     * @param g Graph to be analyzed
     */
    public PopularPath(Graph<V, E> g) {
        this.g = g;
    }

    /**
     * Returns a random neighbor of u in g.
     * Extracted from version in lecture notes.
     * Already implemented and used correctly; just here in case you wondered what was going on.
     */
    public V randomNeighbor(V u) {
        // A random number from 0 (inclusive) to # neighbors (not inclusive)
        int nbr = (int) (g.outDegree(u) * Math.random());
        // Iterate through the out-neighbors the given number of times
        Iterator<V> iter = g.outNeighbors(u).iterator();
        V v = iter.next();
        while (nbr > 0) {
            v = iter.next();
            nbr--;
        }
        return v;
    }

    /**
     * Takes numExplorations random walks from start
     * For each step in in the walk, chooses whether to continue or not with probability keepOn
     * (if no out edge, then necessary stops)
     * Stores edge use counts in instance variable backCounts
     */
    public void exploreFrom(V start, double keepOn, int numExplorations) {
        // If start vertex does not exist in Graph or keepOn value not in wanted state, return
        if (!g.hasVertex(start) || keepOn <= 0 || keepOn >= 1) return;

        // Initialize backCounts instance variable
        backCounts = new ArrayList<>();

        // The random walk structure from lecture is here; you just need to augment it to keep the counts
        for (int exploration=0; exploration<numExplorations; exploration++) {
            V curr = start;                        // Current vertex
            while (Math.random()<keepOn) {
                if (g.outDegree(curr) == 0) break; // end of the line
                V next = randomNeighbor(curr);     // choose a random neighbor

                //Initialize Map of Maps for steps taken at current step
                Map<V, Map<V, Integer>> currentStep;

                // Get Map at current step (index) and remove it from List
                try {
                     currentStep = backCounts.get(exploration);
                     backCounts.remove(exploration);
                }
                catch (IndexOutOfBoundsException e) {
                    // If step does not exist in List,
                    // initialize new Map for current step
                    currentStep = new HashMap<>();
                }

                // Get map of transitions curr -> next in currentStep,
                // or Initialize a new Map if vertex does not exist
                Map<V, Integer> stepToNext = currentStep.getOrDefault(next, new HashMap<>());

                // Increment frequency of transition currentVertex -> nextVertex
                // If key does not exist in Map, initialize value to 0 and increment.
                stepToNext.put(curr, stepToNext.getOrDefault(curr, 0) + 1);

                // Re-insert into Map of all vertices visited at current step
                currentStep.put(next, stepToNext);

                // Re-insert Map of steps at current step into List of back counts at the specific index
                backCounts.add(exploration, currentStep);

                // Swap current with next and continue while loop
                curr = next;
            }
        }
    }

    /**
     * Finds a path from end back to the start used in exploreFrom according to the stats in backCounts:
     * using the smallest number of steps by which end can be reached,
     * such that each step from v back to some u cho0ses as u the most-frequent predecessor for v.
     * Throws an exception if there is no path to end.
     */
    public List<V> path(V end) throws Exception {

        // Initialize path List
        List<V> pathToEnd = new ArrayList<>();

        // Step through Maps in List of backCounts, in order of indexes
        for (Map<V, Map<V, Integer>> backCount : backCounts) {
            /*
             * If Map contains end key, then end was reached at step (index)
             * We can then get the predecessor and break.
             * However, if step is > 0 then we need to
             * recursively find path of predecessor and append
             */
            if (backCount.containsKey(end)) { // if backCount Map contains vertex,
                // Get map of transitions to vertex in backCount
                Map<V, Integer> transitions = backCount.get(end);

                int maxFreq = -1;               // Initialize highest frequency
                V mostPopularPred = null;       // Initialize most popular predecessor

                // Find highest frequency in Map of transitions
                for (int freq : transitions.values()) {
                    if (freq > maxFreq)  maxFreq = freq;
                }

                // Get vertex mapped to highest frequency
                for (V vertex : transitions.keySet()) {
                    if (transitions.get(vertex) == maxFreq) {
                        mostPopularPred = vertex;
                        break;
                    }
                }

                // Add most popular predecessor to path
                pathToEnd.add(mostPopularPred);

                /*
                 * If most popular predecessor is not null,
                 * Recursively Get path to most popular predecessor
                 * and add to path of current
                 */
                if (mostPopularPred != null ) {
                    pathToEnd.addAll(path(mostPopularPred));    // Recursive call
                }

                /*
                 *Finally, break the loop since
                 * if path exists at current step,
                 * it's fewer steps than all possible paths in later steps
                 */
                break;
            }
        }
        /*
         * If path to end vertex is empty, then vertex is not reachable.
         * Throw Exception.
         */
        if (pathToEnd.size() == 0) {
            throw new Exception("Path not found!");
        }

        // If path is not empty, return path
        return pathToEnd;
    }
}
