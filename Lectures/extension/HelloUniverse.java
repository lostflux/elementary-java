
public class HelloUniverse {
    public static void main(String[] args) {
        System.out.println("Hello, Universe!");

        int x = 0;
        int sum = 0;
        while (x < 100){
            sum += x;
            x += 1;
        }
        System.out.println("The sum of 0 to 100 is " + sum);
    }
}
