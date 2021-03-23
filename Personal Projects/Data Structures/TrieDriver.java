public class TrieDriver {
    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("This is an example sentence. Will it work? I don't know!");
        System.out.println(trie);
    }
}
