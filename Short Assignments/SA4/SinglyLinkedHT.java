/**
 * A singly-linked list implementation of the SimpleList interface
 * Now with tail pointer
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012, 
 * based on a more feature-ful version by Tom Cormen and Scot Drysdale
 * @author CBK, Spring 2016, cleaned up inner class, extended testing
 */
public class SinglyLinkedHT<T> implements SimpleList<T> {
	private Element head;	// front of the linked list
	private Element tail;	// end
	private int size;		// # elements in the list

	/**
	 * The linked elements in the list: each has a piece of data and a next pointer
	 */
	private class Element {
		private T data;
		private Element next;

		private Element(T data, Element next) {
			this.data = data;
			this.next = next;
		}
	}

	public SinglyLinkedHT() {
		// TODO: this is just copied from SinglyLinked; modify as needed
		// initialize head and tail to null, size to 0
		head = null;
		tail = null;
		size = 0;
	}

	public int size() {
		return size;
	}

	/**
	 * Helper function, advancing to the nth Element in the list and returning it
	 * (exception if not that many elements)
	 */
	private Element advance(int n) throws Exception {
		Element e = head;
		while (n > 0) {
			// Just follow the next pointers
			e = e.next;
			if (e == null) throw new Exception("invalid index");
			n--;
		}
		return e;
	}

	public void add(int idx, T item) throws Exception {        
		// TODO: this is just copied from SinglyLinked; modify as needed
		if (idx == 0) {
			// Insert at head
			if (size == 0) {
				head = new Element(item, null);
			}
			else {
				Element after = head;
				head = new Element(item, after);
			}
			size++;
			tail = advance(size - 1);
		}
		else {
			// It's the next thing after element # idx-1
			Element e = advance(idx-1);
			Element after = e.next;
			// Splice it in
			e.next = new Element(item, e.next);
			e.next.next = after;
			size++;
			tail = advance(size - 1);

		}
	}

	public void remove(int idx) throws Exception {
		// TODO: this is just copied from SinglyLinked; modify as needed
		if (idx == 0) {
			// Just pop off the head
			if (head == null) throw new Exception("invalid index");
			head = head.next;
		}
		else {
			// It's the next thing after element # idx-1
			Element e = advance(idx-1);
			if (e.next == null){
				throw new Exception("invalid index");
			}
			// Splice it out
			else if (e.next.next == null){
				e.next = null;
				tail = e;
			}
			else{
				e.next = e.next.next;
			}
		}
		size--;
	}

	public T get(int idx) throws Exception {
		Element e = advance(idx);
		return e.data;
	}

	public void set(int idx, T item) throws Exception {
		Element e = advance(idx);
		e.data = item;
	}

	/**
	 * Appends other to the end of this in constant time, by manipulating head/tail pointers
	 */
	public void append(SinglyLinkedHT<T> other) {
		// TODO: YOUR CODE HERE
		// I used "this" references to make the code clearer -- but it's not needed.

		if (other.size != 0){				// if list to be appended is not empty
			if (this.tail == null){				// if list to append to is empty
				this.head = other.head;
				this.size = other.size;
			}
			else{
				this.tail.next = other.head;	// if list to append to is NOT empty
				this.size += other.size;
			}
			this.tail = other.tail;
		}
		else { // if list to be appended is empty --> do nothing
			System.out.println("List " + other + " is empty.");
		}
	}
	
	public String toString() {
		String result = "";
		for (Element x = head; x != null; x = x.next)
			result += x.data + "->";
		result += "[/]";

		return result;
	}

	public static void main(String[] args) throws Exception {
		SinglyLinkedHT<String> list1 = new SinglyLinkedHT<String>();
		SinglyLinkedHT<String> list2 = new SinglyLinkedHT<String>();


		String randomString = """
				These are the test cases from the provided code
				""";
		System.out.println(randomString);
		System.out.println(list1 + " + " + list2);
		list1.append(list2);
		System.out.println(" = " + list1);

		list2.add(0, "a");
		System.out.println(list1 + " + " + list2);
		list1.append(list2);
		System.out.println(" = " + list1);
		System.out.println("head: " + list1.head.data + ", tail: " + list1.tail.data + ", size: " + list1.size + "\n");
		
		list1.add(1, "b");
		list1.add(2, "c");
		SinglyLinkedHT<String> list3 = new SinglyLinkedHT<String>();
		System.out.println(list1 + " + " + list3);
		list1.append(list3);
		System.out.println(" = " + list1);
		System.out.println("head: " + list1.head.data + ", tail: " + list1.tail.data + ", size: " + list1.size + "\n");
		
		SinglyLinkedHT<String> list4 = new SinglyLinkedHT<String>();
		list4.add(0, "z");
		list4.add(0, "y");
		list4.add(0, "x");		
		System.out.println(list1 + " + " + list4);
		list1.append(list4);

		System.out.println(" = " + list1);
		System.out.println("head: " + list1.head.data + ", tail: " + list1.tail.data + ", size: " + list1.size + "\n");
		
		list1.remove(5);
		list1.remove(4);
		SinglyLinkedHT<String> list5 = new SinglyLinkedHT<String>();
		list5.add(0, "z");
		list5.add(0, "y");
		System.out.println(list1 + " + " + list5);
		list1.append(list5);
		System.out.println(" = " + list1);
		System.out.println("head: " + list1.head.data + ", tail: " + list1.tail.data + ", size: " + list1.size + "\n");
		System.out.println("\n ---------------------------------------------------------------------------------- \n");

		// New tests for case from exam instructions
		String randomString2 = """
				These are the test cases from the SA-4 Assignment page on Canvas
				""";
		System.out.println(randomString2);
		SinglyLinkedHT<String> list8 = new SinglyLinkedHT<>();
		System.out.println("SinglyLinkedHT<String> list8 = new SinglyLinkedHT<>();");
		System.out.println("list8.add(0, \"a\");");
		list8.add(0, "a");
		System.out.println(list8);
		System.out.println("head: " + list8.head.data + ", tail: " + list8.tail.data + ", size: " + list8.size + "\n");

		list8.add(1, "b");
		System.out.println("list8.add(1, \"b\");");
		System.out.println(list8);
		System.out.println("head: " + list8.head.data + ", tail: " + list8.tail.data + ", size: " + list8.size + "\n");

		list8.add(0, "c");
		System.out.println("list8.add(0, \"c\");");
		System.out.println(list8);
		System.out.println("head: " + list8.head.data + ", tail: " + list8.tail.data + ", size: " + list8.size + "\n");

		list8.remove(0);
		System.out.println("list8.remove(0);");
		System.out.println(list8);
		System.out.println("head: " + list8.head.data + ", tail: " + list8.tail.data + ", size: " + list8.size + "\n");

		list8.remove(1);
		System.out.println("list8.remove(1);");
		System.out.println(list8);
		System.out.println("head: " + list8.head.data + ", tail: " + list8.tail.data + ", size: " + list8.size + "\n");

		list8.remove(0);
		System.out.println("list8.remove(0);");
		System.out.println(list8);
//		System.out.println("head: " + list8.head.data + ", tail: " + list8.tail.data + ", size: " + list8.size + "\n");

	}
}
