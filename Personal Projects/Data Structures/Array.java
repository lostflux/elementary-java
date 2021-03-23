import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Array<T> implements List<T> {
    private T[] array;
    private static final int initCap = 10;
    private int size;

    /**
     * Constructor for Array class
     */
    public Array() {
        array = (T[]) new Object[initCap];
        size = 0;
    }

    /**
     * Constructor with a primitive array
     * @param data data to set to current array instance
     */
    public Array(T[] data) {
        this.array = data;
        this.size = data.length;
    }

    /**
     * Get size
     * @return int size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Check if empty
     * @return boolean isEmpty
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Check if it contains a given Object
     * @param o Object to check for
     * @return boolean contains()
     */
    @Override
    public boolean contains(Object o) {
        if (size == 0) return false;
        else for (int i=0; i<size; i++) {
            System.out.println(size);
            if (array[i].equals(o)) return true;
        }
        return false;
    }

    /**
     * Get iterator over items in list
     * @return Iterator over data items in Array
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {

        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return array.length > index;
            }

            @Override
            public T next() {
                return array[index++];
            }
        };
    }

    /**
     * Cast Array instance to a primitive array
     * @return T[]
     */
    @NotNull
    @Override
    public Object[] toArray() {
        return Arrays.copyOfRange(array, 0, size);
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return null;
    }

    /**
     * Add new item to list
     * @param data item to add
     * @return true if item was added
     */
    public boolean add(T data) {
        try {
            if (size > array.length) {
                T[] copy = (T[]) new Object[size*2];
                for (int i=0; i<size; i++) {
                    copy[i] = array[i];
                    array = copy;
                }
            }
            array[size++] = data;
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * remove specified object from Array
     * @param o Object to remove
     * @return true if remove successful
     */
    @Override
    public boolean remove(Object o) {
        for (int index=0; index < size; index++) {
            if (array[index].equals(o)) {
                System.arraycopy(array, index+1, array, index, size);
                return true;
            }
        }
        return false;
    }

    /**
     * check if all items in given collection are in Array
     * @param c collection of items
     * @return true if all items are in Array
     */
    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object item : c) {
            if (!this.contains(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * get index of first occurrence of item
     * @param item item to get index of
     * @return int index
     */
    public int getIndex(T item) {
        for (int idx=0; idx < array.length; idx++) {
            if (array[idx].equals(item)) return idx;
        }
        return -1;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        try {
            for (T item : c) {
                this.add(item);
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public T get(int index) {
        return null;
    }

    public T get(T item) throws Exception {
        for (T data : array) {
            if (data.equals(item)) {
                return data;
            }
        }
        throw new Exception("Item Not Found!");
    }

    @Override
    public T set(int index, T element) {
        return null;
    }

    @Override
    public void add(int index, T element) {

    }

    /**
     * remove item at index
     * @param idx int index
     * @return removed item
     */
    public T remove(int idx) {
        T removed = array[idx];
        if (size - idx >= 0) System.arraycopy(array, idx + 1, array, idx, size - idx);
        return removed;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        int size = toIndex - fromIndex;
        T[] temp = (T[]) new Object[size];
        System.arraycopy(array, fromIndex, temp, 0, size);
        return new Array<>(temp);
    }
}
