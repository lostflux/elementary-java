public class hashing {


    public static void main(String[] args) {
        String name = "Amittai";
        char a = 'a';
        char b = 'a';

        System.out.println(name.hashCode()%37);
        System.out.println(Character.hashCode(a));
    }

}

