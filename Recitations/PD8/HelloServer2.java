import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple server that waits for someone to connect on port 4242,
 * and then repeatedly asks for their name and greets them.
 * Connect either by "telnet localhost 4242" or by running HelloClient.java
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author amittai, filled in solutions.
 */

public class HelloServer2 {

    private static final Map<String, String> dictionary = new HashMap<>();

    /**
     * Get a word's meaning from the dictionary.
     * Returns "unknown" if alien word.
     * @param word The word to get.
     * @return The meaning mapped to the word.
     */
    static String get(String word) {
        System.out.println("word: " + word);
        System.out.println("dictionary: " + dictionary);
        return dictionary.getOrDefault(word, "unknown");
    }

    /**
     * Set word meaning in the dictionary.
     * Overwrites old meaning, if any.
     * @param word word to set
     * @param meaning word meaning
     * @return old meaning, if any.
     */
    static String set(String word, String meaning) {
        return dictionary.put(word, meaning);
    }
    public static void main(String[] args) throws IOException {
        // Listen on a server socket for a connection
        System.out.println("waiting for someone to connect");
        ServerSocket listen = new ServerSocket(4242);
        // When someone connects, create a specific socket for them
        Socket sock = listen.accept();
        System.out.println("someone connected");

        // Now talk with them
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

        out.println("REQUEST?");

        String line;
        while ((line = in.readLine()) != null) {

            System.out.println("REQUEST: " + line);
            String[] tokens = line.split(" ");
            System.out.println("tokens: " + tokens.toString());
            if (tokens[0].equals("GET") && tokens.length == 2) {
                String meaning = get(tokens[1]);
                System.out.println(meaning);
                out.println(tokens[1] + ": " + meaning);
            } else if (tokens[0].equals("SET") && tokens.length > 2) {
                StringBuilder meaning = new StringBuilder();

                meaning.append(tokens[2]);

                for (int i = 3; i < tokens.length; i++) {
                    meaning.append(" ").append(tokens[i]);
                }

                String oldMeaning = set(tokens[1], meaning.toString());

                if (oldMeaning != null) {
                    out.println(
                        "Meaning for " + tokens[1] +
                        " changed from -- " + oldMeaning +
                        " -- to -- " + meaning
                    );
                } else {
                    out.println(
                        "Meaning for " + tokens[1] +
                        " set to -- " + meaning
                    );
                }
            } else if (tokens[0].equals("SHOW")) {
                out.println(
                    "SAVED: " + dictionary
                );
            } else {
                out.println(
                    "Invalid request. " +
                    "Please use one of the following formats:" +
                    "\tGET <word> \t" + " or " +
                    "\tPUT <word> <meaning>"
                );
            }
        }
        System.out.println("client hung up");

        // Clean up shop
        out.close();
        in.close();
        sock.close();
        listen.close();
    }
}