import java.util.stream.IntStream;

public class SA0 {
    public static void main(String[] args){
        int sum = IntStream.rangeClosed(0, 100).sum();
        System.out.println("The sum of all integers from 1 to 100 is: " + sum);
    }
}
