public class BlobHash extends Blob {

    public BlobHash(int x, int y, int r) {
        super(x, y, r);
    }

    public BlobHash() {
        super();
    }

    @Override
    public boolean equals(Object otherBlob) {
        if (otherBlob instanceof Blob) {
            Blob other = (Blob) otherBlob;
            return x == other.x && y == other.y && r == other.r;
        }
        return false;
    }
}

