public class BlobTrial {
    double x, y;
    double r = 1.0;

    public BlobTrial(){

    }

    public BlobTrial(double initX, double initY){
        x = initX;
        y = initY;
    }

    public void grow(){
        r += 1;
    }

    public void moveTo(double newX, double newY){
        x = newX;
        y = newY;
    }

    public double howBigAreYou(){
        return r;
    }

    public String toString(){
        return "Blob at " + x + ", " + y + " of size " + r; //Start concatenation with an actual String
    }
}
