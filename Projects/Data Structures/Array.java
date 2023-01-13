
import java.util.*;

/**
 *
 * @param <T> Generic type of Array contents
 */
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

    public String toString() {
        return Arrays.toString(this.toArray());
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
            System.out.println("Checking: " + o + " at: " + i + " -> " + size);
            if (array[i].equals(o)) return true;
        }
        return false;
    }

    /**
     * Get iterator over items in list
     * @return Iterator over data items in Array
     */
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
    @Override
    public Object[] toArray() {
        return Arrays.copyOfRange(array, 0, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
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
            System.err.println(e.getMessage());
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
    public boolean containsAll(Collection<?> c) {
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
        for (int idx=0; idx < size; idx++) {
            System.out.println("Index: " + idx + " size: " + size);
            if (array[idx].equals(item)) {
                return idx;
            }
        }
        return -1;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
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
    public boolean addAll(int index, Collection<? extends T> c) {
        for (T item : c) {
            add(item);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        try {
            for (Object item : c) {
                this.remove(item);
            }
            return true;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        try {
            array = (T[]) new Object[initCap];
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public T get(int index) {
        return array[index];
    }

    @Override
    public T set(int index, T element) {
        if (size + 1 - index >= 0) {
            System.arraycopy(array, index, array, index + 1, size + 1 - index);
        }
        T prev = array[index];
        array[index] = element;
        return prev;
    }

    @Override
    public void add(int index, T element) {
        if (size + 1 - index >= 0) {
            System.arraycopy(array, index, array, index + 1, size + 1 - index);
        }
        array[index] = element;
    }

    /**
     * remove item at index
     * @param idx int index
     * @return removed item
     */
    public T remove(int idx) {
        T removed = null;
        try {
            if (size - idx >= 0) {
                removed = array[idx];
                System.arraycopy(array, idx + 1, array, idx, size - idx);
                size--;
            }
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
        return removed;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        int lastIndex = -1;
        for (int i=0; i<size; i++) {
            if (this.array[i].equals(o)) lastIndex = i;
        }
        return lastIndex;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIterator<>() {
            int index = 0;
            private boolean recentChange = false;
            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public T next() {
                recentChange = false;
                return array[index++];
            }

            @Override
            public boolean hasPrevious() {
                return index > 0;
            }

            @Override
            public T previous() {
                recentChange = false;
                return array[--index];
            }

            @Override
            public int nextIndex() {
                return index + 1;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() throws UnsupportedOperationException {
                if (!recentChange) {
                    System.arraycopy(array, index+1, array, index, size - index);
                    recentChange = true;
                }
                else {
                    throw new UnsupportedOperationException();
                }
            }

            @Override
            public void set(T t) throws UnsupportedOperationException{
                if (!recentChange) {
                    array[index] = t;
                    recentChange = true;
                }
                else {
                    throw new UnsupportedOperationException();
                }
            }

            @Override
            public void add(T t) {
                System.arraycopy(array, index, array, index+1, array.length - index);
                array[index] = t;
                index++;
            }
        };
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIterator<>() {
            int startIndex = index;
            int currentIndex = index;
            private boolean recentChange = false;
            @Override
            public boolean hasNext() {
                return currentIndex < array.length;
            }

            @Override
            public T next() {
                recentChange = false;
                return array[currentIndex++];
            }

            @Override
            public boolean hasPrevious() {
                return currentIndex > startIndex;
            }

            @Override
            public T previous() {
                recentChange = false;
                return array[--currentIndex];
            }

            @Override
            public int nextIndex() {
                return currentIndex + 1;
            }

            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            @Override
            public void remove() throws UnsupportedOperationException {
                if (!recentChange) {
                    System.arraycopy(array, currentIndex +1, array, currentIndex, size - currentIndex);
                    recentChange = true;
                }
                else {
                    throw new UnsupportedOperationException();
                }
            }

            @Override
            public void set(T t) throws UnsupportedOperationException{
                if (!recentChange) {
                    array[currentIndex] = t;
                    recentChange = true;
                }
                else {
                    throw new UnsupportedOperationException();
                }
            }

            @Override
            public void add(T t) {
                System.arraycopy(array, currentIndex, array, currentIndex+1, array.length - currentIndex);
                array[currentIndex] = t;
                currentIndex++;
            }
        };
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        int size = toIndex - fromIndex;
        T[] temp = (T[]) new Object[size];
        System.arraycopy(array, fromIndex, temp, 0, size);
        return new Array<>(temp);
    }
}
