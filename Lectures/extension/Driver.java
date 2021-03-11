import java.util.*;


interface IO {
    default void print(Object expr) {
        System.out.println(expr);
    }

    default int Int(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter an integer: ");
        return input.nextInt();
    }
}

interface Arithmetics {
    default int Add(int x, int y){
        return x + y;
    }

    default int Sub(int x, int y){
        return x - y;
    }

    default int Multi(int x, int y){
        return x * y;
    }

    default double Div(double x, double y){
        return x / y;
    }
}

class Dr implements Arithmetics, IO {

}

public class Driver{
    public static void main(String[] args) {

        Dr driver = new Dr();

        int num1 = driver.Int();
        int num2 = driver.Int();

//        Arithmetics driver = new Arithmetics();
//        Outputs driver = new Outputs();

        int sum = driver.Add(num1, num2);
        driver.print(num1 + " + " + num2 + " = " + sum );
//        System.out.println(num1 + " + " + num2 + " = " + sum );

        int difference = driver.Sub(num1, num2);
        driver.print(num1 + " - " + num2 + " = " + difference );

        int multi = driver.Multi(num1, num2);
        driver.print(num1 + " * " + num2 + " = " + multi );

        double quotient = driver.Div(num1, num2);
        driver.print(num1 + " / " + num2 + " = " + quotient );
        int a = 5;
        System.out.printf("This is a &d formatted string.", a);
    }
}
