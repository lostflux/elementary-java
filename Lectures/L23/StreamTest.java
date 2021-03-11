import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {

	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
		List<Integer> twoEvenSquares = numbers.stream()
	           .filter(n -> {
	                    System.out.println("filtering " + n); 
	                    return n % 2 == 0; //returns true if n is even
	                  })
	           .map(n -> {
	                    System.out.println("mapping " + n);
	                    return n * n; //square n
	                  })
	           .limit(2) //only return the first two squared evens
	           .collect(Collectors.toList()); //save in List
		System.out.println(twoEvenSquares);
	}

}
