import java.util.PriorityQueue;

public class TestCompare {
    public static void main(String[] args) {
        PriorityQueue<Double> test = new PriorityQueue<>((o1, o2) -> Double.compare(o2, o1));

        test.add(1.7);
        test.add(2.2);
        System.out.println(test.toString());
    }
}
