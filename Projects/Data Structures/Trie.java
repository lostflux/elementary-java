import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ListIterator;

public class Trie {
    private final Node root;

    protected static class Node {
        private final Character data;
        private final Array<Node> next;
        boolean isWord = false;

        public Node(char data) {
            this.data = data;
            next = new Array<>();
        }

        public char getData() {
            return this.data;
        }

        public boolean contains(char c) {
            for (Node n : next) {
                if (n.getData() == c) {
                    return true;
                }
            }
            return false;
        }

        public Node put(char c) {
            Node n = null;
            for (@NotNull ListIterator<Node> it = next.listIterator(); it.hasNext(); ) {
                Node node = it.next();
                if (node.equals(c)) {
                    n = node;
                }
            }
            if (n == null) {
                n = new Node(c);
                next.add(n);
            }
            return n;
        }

//        public Node put(Node n) {
//            int index = next.getIndex(n);
//            Node toInsert;
//            if (index == -1) {
//                toInsert = n;
//            }
//            else {
//                toInsert = next.get(index);
//            }
//            if (!next.contains(n)) {
//                next.add(n);
//            }
//            return n;
//        }

        public Node get(char c) {
            return next.get(c);
        }

        public void setWord() {
            isWord = true;
        }

        public boolean isWord() {
            return isWord;
        }

        public boolean equals(char c) {
            for (Node n : next) {
                if (n.getData() == c) {
                    return true;
                }
            }
            return false;
        }

        public Array<Node> getNext() {
            return next;
        }
    }

    public Trie() {
        this.root = new Node('/');
    }

    public void insert(String sentence) {

        String[] words = sentence.split(" ");
        for (String word : words) {
            Node currentNode = root;
            for (int i=0; i < word.length(); i++) {
                currentNode = currentNode.put(word.charAt(i));
                System.out.println("Added: " + word.charAt(i));
                System.out.println(currentNode.getNext());
//                System.out.println(this);
            }
            currentNode.setWord();
        }
    }

    public String toString() {
        String parsed = "";
        buildString(parsed, root, 0);
        return parsed;
    }

    public void buildString(String prev, Node n, int level) {
        StringBuilder holder = new StringBuilder(" ");
        if (n != null) {
            holder.append(" ".repeat(Math.max(0, level)));
            prev += holder + " -> " + n.getData() + "\n";
//            System.out.println(prev);
            int nextLevel = level + 1;
            if (n.getNext().size() != 0) {
                for (Node nextNode : n.getNext()) {
                    buildString(prev, nextNode, nextLevel);
                }
            }
        }
        System.out.println(prev);
    }
}
