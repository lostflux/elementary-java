//import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 *  This is an Abstract Data Type that implements an extensible array
 * @param <T>   --> Data Type
 * @author Amittai Joel Siavava Wekesa, Winter 2021
 */
public class GrowingList<T> implements SimpleList<T>, Comparable<T> {
    private T[] array;
    private int size;
    private static final int initCap = 10;

    public GrowingList() {
        array = (T[]) new Object[initCap];
        size = 0;
//        length = initCap;
    }

    public GrowingList(int size) {
        array = (T[]) new Object[size];
        this.size = 0;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int idx, T item) throws Exception {
        if (idx < 0){
            idx = idx-size;
            if (idx < 0) throw new Exception ("Invalid index");
        }
        if (idx > size) throw new Exception ("Invalid index");
        if (size == array.length){
            T[] temp = (T[]) new Object[array.length * 2];
            System.arraycopy(array, 0, temp, 0, array.length);
            array = temp;

        }
        array[size] = item;
        size++;
    }

    public void add(T item) throws Exception {
        int idx = size;
        this.add(idx, item);
    }

    public void concat(GrowingList<T> otherList) throws Exception {
        this.append(otherList);
    }

    public void append(T item) throws Exception {
        this.add(size, item);
    }

    @SafeVarargs
    public final void append(T... items) throws Exception {
        for (T item : items) {
            if (item != null) {
                this.append(item);
            }
        }
    }

    public void append(GrowingList<T> items) throws Exception {
        this.append(items.array);
    }

    public String toString() {
        GrowingList<T> temp = this.removeNulls();
        return Arrays.toString(temp.array);
    }

    @Override
    public void remove(int idx) throws Exception {

    }

    public void sort() {
        GrowingList<T> temp = removeNulls();
        Arrays.sort(temp.array);
        array = temp.array;
    }

    private GrowingList<T> removeNulls() {
        GrowingList<T> temp = new GrowingList<>(size);
        for (T item : array) {
            if (item != null) {
                try {
//                    System.out.println(item.getClass());
                    temp.add(item);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return temp;
    }

    @Override
    public T get(int idx) throws Exception {
        if (idx < 0){
            idx = size - idx;
            if (idx < 0) {
                throw new Exception ("Index out of range");
            }
        }
        if (idx > size){
            throw new Exception ("Index out of range");
        }
        return array[idx];
    }

    @Override
    public void set(int idx, T item) throws Exception {


    }

//    @Override
    public int compareTo(T o) {
        return 0;
    }
}
