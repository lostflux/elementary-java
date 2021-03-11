import java.util.Comparator;

public class minCompareActors implements Comparator<Double> {

    public minCompareActors() {

    }

    @Override
    public int compare(Double o1, Double o2) {
        return Double.compare(o1, o2);
    }
}
