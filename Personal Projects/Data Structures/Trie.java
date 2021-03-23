public class Trie {
    private final Node root;

    protected class Node {
        Character data;
        Array<Node> next;
        boolean word = false;

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
            return this.put(new Node(c));
        }

        public Node put(Node n) {
            int index = next.getIndex(n);
            Node toInsert;
            if (index == -1) {
                toInsert = n;
            }
            else {
                toInsert = next.get(index);
            }
            if (!next.contains(n)) {
                next.add(n);
            }
            return n;
        }

        public Node get(char c) {
            return next.get(c);
        }

        public void setWord() {
            word = true;
        }

        public boolean isWord() {
            return word;
        }

        public boolean equals(Character c) {
            for (Node n : next) {
                if (n.data.equals(c)) {
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

        String[] words = sentence.split("[!._,'@?/s ]");
        for (String word : words) {
            Node currentNode = root;
            for (int i=0; i < word.length(); i++) {
                currentNode = currentNode.put(word.charAt(i));
            }
            currentNode.setWord();
        }
    }

    public String toString() {
        String parsed = "";
        buildString(parsed, root, 0);
//        System.out.println(parsed);
        return parsed;
    }

    public void buildString(String prev, Node n, int level) {
        String holder = " ";
        if (n != null) {
            for (int i=0; i<level; i++) {
                holder += " ";
            }
            prev += holder + " -> " + n.getData() + "\n";
            int nextLevel = level + 1;
            if (n.getNext().size() != 0) {
                for (Node nextNode : n.getNext()) {
                    buildString(prev, nextNode, nextLevel);
                }
            }
            System.out.println(prev);
        }
    }
}
