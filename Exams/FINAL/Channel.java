import java.util.*;

public class Channel {
    // Used a set since conversations need not be ordered
    // And there is no preferential access
    private final Set<Conversation> conversations;

    /**
     * Inner Post class
     */
    private class Post {
        String sender;      // who posted something
        String content;     // what they posted

        /**
         * Constructor for Post subclass
         * @param sender Who sent the message
         * @param content Message content
         */
        public Post(String sender, String content) {
            this.sender = sender;
            this.content = content;
        }
    } // End of Post class


    /**
     * Inner Conversation class
     */
    private class Conversation {
        private final int conversationId;
        private final List<Post> posts;    // the messages in the conversation

        /**
         * Constructor for Conversation subclass
         */
        public Conversation(int id) {
            conversationId = id;
            posts = new ArrayList<>();
        }

        public void addPost(Post post) {
            posts.add(post);
        }
        private int getId() {
            return conversationId;
        }
    } // End of Channel class


    /**
     * Constructor for Channel class
     */
    public Channel() {
        conversations = new HashSet<>();
    }

    /**
     * Method to check if channel contains a conversation by specified conversation ID
     * @param id Conversation ID
     * @return true or false
     */
    public boolean contains(int id) {
        for (Conversation conversation : conversations) {
            if (conversation.getId() == id) return true;    // comparing int
        }
        return false;
    }

    /**
     * Getter for a Conversation of a specific ID
     * @param id ID of Conversation tobe found
     * @return Conversation matching specified ID
     * @throws Exception Item not found
     */
    public Conversation getConversation(int id) throws Exception {
        if (this.contains(id)) {
            for (Conversation conv : this.conversations) {
                if (conv.getId() == id) return conv;
            }
        }
        throw new Exception("Item not found!");
    }

    /**
     * Method to remove a conversation with a specified ID
     * @param id Conversation ID
     */
    public void removeConversation(int id) {
        if (this.contains(id)) {
            this.conversations.removeIf(conv -> conv.getId() == id);
        }
    }

    /**
     * Method to add conversation to Channel
     * @param conv Conversation to add
     */
    public void addConversation(Conversation conv) {
        conversations.add(conv);
    }

    /**
     * Method to find next ID in Channel
     * NOTE: since this method finds a common ID,
     * synchronizing it ensures two threads do not generate IDs
     * at the same time and end up getting same or invalid IDs
     *
     * Generating IDs in the Channel (as opposed to server) should not be problematic
     * because all senders & recipients are on same single Channel
     * @return integer value of 1 + current maximum Conversation ID in Channel
     */
    private synchronized int nextId() {
        int conversationId = 0;     // Initialize ID

        // Find highest ID in current channel
        for (Conversation conv : conversations) {
            if (conv.getId() > conversationId) conversationId += 1;
        }
        return conversationId + 1;      // return highest + 1
    }

    /**
     * Method to add a message into a specified conversation
     * Since we are updating a shared resource (conversations),
     * This method is synchronized to avoid different threads interrupting each other mid-execution
     * @param msg  message (un-split)
     */
    public synchronized void addMessage(String msg) {
        // TODO: Parse message and split into tokens
        //  message format: "conversationId | sender | message"
        //  NOTE: To start a new conversation, incoming conversationId is set to 0
        //  I implemented nextId() method in Channel that
        //  finds the highest ID in current channel and returns that ID + 1
        String[] tokens = msg.split("\\|");
        int id = Integer.parseInt(tokens[0]);
        String sender = tokens[1];
        String message = tokens[2];

        Post newPost = new Post(sender, message);   // Create post
        Conversation conv;                          // Initialize conversation
        if (id == 0) id = this.nextId();            // determine if ID is 0 (new conv and generate next ID)

        try {
            conv = this.getConversation(id);        // Get conv with ID -- Method implemented in Channel
            this.removeConversation(id);            // remove conv -- Method implemented in Channel
                                                    // (we shall re-insert after updating)
            /*
             Note: It iss particularly important to synchronize this method
             since we are REMOVING the conversation, updating it, and re-inserting it into the Set.
             If a given run is interrupted, the conversation will be left in limbo and messages
             to the same conversation will create a new, empty conversation
             and overwrite current instance.
             */
        }
        catch (Exception e) {
            // If conversation does not exist, create new one with the given ID
            conv = new Conversation(id);
        }

        conv.addPost(newPost);          // Add post to conversation
        this.addConversation(conv);     // re-insert conversation to channel
    }
}
