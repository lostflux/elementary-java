import java.util.Comparator;

public class maxCompareActors implements Comparator<Double> {


    public maxCompareActors() {

    }
    @Override
    public int compare(Double o1, Double o2) {
        if (o1 < o2) return 1;
        else if (o1.equals(o2)) return 0;
        else return -1;
    }
}
