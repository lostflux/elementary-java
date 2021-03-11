import java.util.Comparator;

/**
 * Class to compare frequency trees for the priority queue
 */
public class TreeComparator implements Comparator<FreqTree<Character>> {

    public TreeComparator() {
        super();
    }

    @Override
    public int compare(FreqTree tree1, FreqTree tree2) {

        // Get frequencies of both trees
        int frequency1 = tree1.getFrequency();
        int frequency2 = tree2.getFrequency();

        // if frequency1 >= frequency2 return 1  else return -1
        if (frequency1 > frequency2) return 1;
        else if (frequency1 == frequency2) return 0;
        else return -1;
    }
}