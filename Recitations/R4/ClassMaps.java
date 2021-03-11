public class ClassMaps <T> {
    String value;
    final int factor = 37;
    T[] storedValues;

    // Default, use int
    public void classMaps(T newValue) {

    }

    private int getHashCode(String string) {
        int sum = 0;

        for (int index=0; index<string.length(); index++) {
            sum += sum * factor + string.charAt(index);
        }
        return sum % factor;
    }

    public void add(T item, String string) {
        int index = getHashCode(string);
        storedValues[index] = item;
    }
}
