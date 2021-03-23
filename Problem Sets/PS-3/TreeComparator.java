import java.util.Comparator;

/**
 * Class to compare frequency trees for the priority queue
 */
public class TreeComparator<T> implements Comparator<T> {

    public TreeComparator() {
        super();
    }

    @Override
    public int compare(T tree1, T tree2) {

        FreqTree<Character> t1;
        FreqTree<Character> t2;
        if (tree1 instanceof FreqTree && tree2 instanceof FreqTree) {
            t1 = (FreqTree<Character>) tree1;
            t2 = (FreqTree<Character>) tree2;
        }
        else {
            return 0;
        }

        // Get frequencies of both trees
        int frequency1 = t1.getFrequency();
        int frequency2 = t2.getFrequency();

        // if frequency1 >= frequency2 return 1  else return -1
        return Integer.compare(frequency1, frequency2);
    }
}