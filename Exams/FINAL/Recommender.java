import java.util.*;

public class Recommender {
    public Map<String, Map<String, List<String>>> messages;  // from -> to -> [ message ] -- assume already initialized

    // declare friendStrength
     private Map<String, Map<String, Double>> friendStrength; // person -> friend -> friend strength

    /**
     * Constructor
     * @param messages Map of Sender -> Recipient -> [ message ]
     */
     public Recommender(Map<String, Map<String, List<String>>> messages) {
         this.messages = messages;
     }

    /**
     * Based on messages, computes friendStrength as the log of the relative frequency of messages
     */
    public void computeFriendStrength() {
        // Initialize Map of friend strengths
        friendStrength = new HashMap<>();

        // For each sender in sent messages
        for (String sender : messages.keySet()) {
            // Initialize count of total messages the sender sent
            int messageCount = 0;

            // Initialize Map of Strengths for current sender
            Map<String, Double> senderStrengths = new HashMap<>();              // recipient -> friend strength

            // Get Map of messages sender sent to others
            Map<String, List<String>> allMessages = messages.get(sender);       // recipient -> list of messages

            // For each message recipient
            for (String recipient : allMessages.keySet()) {

                // Get List of messages sender sent to current recipient
                List<String> sentMessages = allMessages.get(recipient);         // [ messages ]

                // Add number of messages to Map of sender strengths
                senderStrengths.put(recipient, (double) sentMessages.size());   // recipient -> friend strength

                // Also increment total messages --> add count of all messages sent to current recipient
                messageCount += sentMessages.size();
            } // exit for-loop for recipients

            // compute logs
            for (String recipient : senderStrengths.keySet()) {     // recipient -> friend strength
                double count = senderStrengths.get(recipient);      // friend strength (count of messages)
                double probability = Math.log(count/messageCount);  // log of (count / total count)
                senderStrengths.put(recipient, probability);        // replace value in Map
            }

            // Finally, insert map of senderStrengths to instance variable,
            // mapped to current sender
            friendStrength.put(sender, senderStrengths);

            // For-loop repeats for other senders in messages Map
        }
    }

    /**
     * Based on friendStrength, returns a list of suggested friends for person,
     * such that person has not yet sent any message to them,
     * ranked in order of total of friendStrength from person -> intermediate and intermediate -> suggested
     */
    public List<String> recommendations(String person) {
        // Initialize Map to hold computed probabilities to likely friends
        Map<String, Double> likelyFriends = new HashMap<>();    // likely friend -> sum of probabilities to likely friend

        // Initialize priority queue to sort Map Entries
        PriorityQueue<Map.Entry<String, Double>> recommendations = new PriorityQueue<>(
                // Comparator reversed o1 and o2 so higher values go to front of queue
                (o1, o2) -> Double.compare(o2.getValue(), o1.getValue())
        );

        // Get Map of all friend-strengths of person
        Map<String, Double> personStrengths = friendStrength.get(person);      // friend -> friend-strength

        // For each friend
        for (String friend : personStrengths.keySet()) {

            // Get friend-strength of specific friend
            double strengthToFriend = personStrengths.get(friend);

            // Get Map of friend-strengths of friend to other people
            Map <String, Double> probableFriends = friendStrength.get(friend);

            // for each second-rank connection
            for (String second : probableFriends.keySet()) {

                // if second connection is not current person
                // and has no correspondence from current person
                if (!second.equals(person) && !personStrengths.containsKey(second)) {

                    // Get strength of second connection from first connection
                    double friendToSecond = probableFriends.get(second);

                    // Get total likely-hood of second connection to person
                    double total = strengthToFriend + friendToSecond;

                    // Add second connection and their total probability from friend
                    // to map of likely friends
                    likelyFriends.put(second, total);       // probable friend -> total probability
                }
            }
        }

        // Add all ** Map Entries to priority queue so they're sorted
        recommendations.addAll(likelyFriends.entrySet());

        // Get Entries from priority queue and add their names (keys) to List
        // in order of removal
        List<String> recommended = new ArrayList<>();
        while (recommendations.size() > 0) {
            Map.Entry<String, Double> next = recommendations.remove();
            String name = next.getKey();
            recommended.add(name);
        }

        // return list of recommended friends, sorted in order of removal from priority queue
        return recommended;
    }
}
