
public class Test {
    public static void main(String[] args) {
        int x = 0;
        int y = 0;
        String who = "That person";
        System.out.println(who);
        while (x < 5){
            y = x - y;
            System.out.println(x + "" + y + " ");
            x += 1;
        }

        boolean flag = false;
        char ch = 'A';
        byte b = 12;
        short s = 24;
        int i = 257;
        long l = 890L;  //Note use of "L"
        float f = 3.1415F;
        double d = 2.1828;
        System.out.println("flag = " + flag);
        System.out.println("ch =" + ch);
        System.out.println("b = " + b);
        System.out.println("s = " + s);
        System.out.println("i = " + i);
        System.out.println("l = " + l);
        System.out.println("f = " + f);
        System.out.println("d = " + d);

        /*
        Block comments
        are kinda
        dumb
        TBH
         */


    }
}
