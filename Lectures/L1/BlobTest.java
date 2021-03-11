public class BlobTest {
    public static void main(String[] args) {
        Blob bob = new Blob();
        Blob alice = new Blob(10, 100);
        System.out.println(bob.howBigAreYou());

        bob.grow();
        bob.grow();
        bob.grow();
        bob.grow();
        int i = 0;
        while (i < 10){
            alice.grow();
            bob.grow();
            i += 1;
        }
        System.out.println(bob.howBigAreYou());
        System.out.println(bob + " vs " + alice);

    }
}
