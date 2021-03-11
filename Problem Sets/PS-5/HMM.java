import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HMM {
    public static boolean debug = false;                    // debugging flag. Enable to print steps to console
    public static final double unseenPenalty = -100;        // Penalty for when a word is missing in a given tag state
    HashMap<String, HashMap<String, Double>> states;        // map of all states to their map of words and their probabilities
    HashMap<String, HashMap<String, Double>> transitions;   // map of all states to their map of transitions and probabilities

    /**
     * Default Constructor
     * @throws Exception Error reading from/writing to file
     */
    public HMM() throws Exception {
        String defaultSource = "inputs/example-hmm.csv";
        new HMM(defaultSource);
    }

    /**
     * Constructor with file source of a previously constructed HMM
     * @param dataFileName csv file source of HMM
     * @throws Exception Error reading from file
     */
    public HMM(String dataFileName) throws Exception {
        loadData(dataFileName);     // Load HMM data
        if (debug) { // if in debug mode, print loaded map of states and transitions
            System.out.println("this.states: " + states);
            System.out.println("this.transitions: " + transitions);
        }
    }

    /**
     * Constructor with two files, first for text and second for tags
     * @param text Text source file
     * @param tags Tags source file
     * @throws IOException Error reading from file
     */
    public HMM(String text, String tags) throws IOException {
        // Initialize HashMaps and call train with the file names
        this.transitions = new HashMap<>();
        this.states = new HashMap<>();
        train(text, tags);
    }

    /**
     * Method to do viterbi decoding of a given sentence of text
     * @param sentence Sentence of text to be checked
     * @return list of strings --> tags of each word in sentence, in order
     */
    public String[] viterbi(String sentence) {
        sentence = sentence.toLowerCase();                      // Cast to lower case
        String[] words = sentence.split(" ");             // Split sentence into words

        // TODO: Debugging activity, print list of words
        if (debug) {
            System.out.println("Words: " + Arrays.toString(words));
        }

        HashMap<String, String> backTrace = new HashMap<>();    // Initialize Map of each state to state that led to it
        int observations = words.length;                        // Number of observations (words) in sentence
        if (debug) {
            System.out.println("Observations present: " +  observations);
        }
        HashSet<String> currStates = new HashSet<>();           // Set of current states
        HashMap<String, Double> currScores = new HashMap<>();   // Map of scores to states in current states
        currStates.add("#");                                    // Add start state to current states
        currScores.put("#", 0.0);                               // add score to start state, = 0

        // Initialize temporary map to hold outcomes of final observation
        HashMap<Double, String> finalObservations = new HashMap<>();

        // For each observation
        for (int i=0; i<observations; i++) {
            // TODO: Debugging activity, print current step/iteration
            if (debug) System.out.println("Current step: "+i);

            // Initialize set to hold each next state as it is processed
            // !! Irrelevant to use queue since order does not matter
            HashSet<String> nextStates = new HashSet<>();

            // Initialize map for each processed next state to it's score
            HashMap<String, Double> nextScores = new HashMap<>();


            // for each current state
            for (String currState : currStates) {
                if (debug) System.out.println("currState: " + currState);

                // Get map of transitions from the current state to another state
                HashMap<String, Double> currTransitions = transitions.getOrDefault(currState, new HashMap<>());
                if (debug) System.out.println(i + " " + currState + " transitions: " + currTransitions);

                // For each possible next state in the map of transitions,
                for (String nextStep : currTransitions.keySet()) {

                    String nextState = nextStep.split(" -> ")[1];
                    // add the next state to set of next states
                    nextStates.add(nextState);

                    // compute score from START to NEXT state after current

                    // STEP 1 : Get saved score up to current state
                    double precedingScore = currScores.get(currState);

                    // STEP 2: Get saved score for transition current to next, or default to -10
                    double transitionScore = currTransitions.get(
                            currState + " -> " + nextState);

                    // STEP 3: Get saved observation score for word in next state, or default to -10
                    double nextObservationScore = states.get(
                            nextState).getOrDefault(words[i], unseenPenalty);

                    // Step 4: Get total = preceding score + transition score + next Score
                    double nextScore = precedingScore + transitionScore + nextObservationScore; // full score = sum of the above

                    if (debug) {
                        System.out.println(currState + " preceding score: " + precedingScore);
                        System.out.println(currState + " -> " + nextState + " transition score: " + transitionScore);
                        System.out.println(nextState + " observation score: " + nextObservationScore);
//                        System.out.println(currState + " preceding score: " + precedingScore);
                    }

                    // check map of saved next scores:
                    // if it does not contain next score or next score has lower score, save or replace respectively
                    if ((!nextScores.containsKey(nextState)) || nextScore > nextScores.get(nextState)) {
                        nextScores.put(nextState, nextScore);

                        // save back trace to current ... with a reference to the step since same transition might recur!
                        backTrace.put(nextState+" "+ i, backTrace.getOrDefault(
                                currState + " " + (i - 1), currState)+" -> "+nextState);
                        if (debug) System.out.println(backTrace.get(nextState+" "+ i) + " : " + nextScore);
                        // if final observation, save observations and scores
                        if (i == observations - 1) {
                            finalObservations.put(nextScore, nextState + " " + i);
                        }
                    }
                    if (debug) System.out.println("backTrace: " + backTrace);

                }
            }
            // Finally, swap  current states, current scores with next states, next scores
            // so that next iteration of i takes a step forward in Graph
            currStates = nextStates;
            currScores = nextScores;

        }
        // Get best score for final observation
        if (debug) System.out.println("Final Observations: " + finalObservations);

        // GEt highest score and associated value
        double maxScore = Collections.max(finalObservations.keySet());
        String highestFinalObservation = finalObservations.get(maxScore);

        if (debug) {
            System.out.println("Highest final observation: " + highestFinalObservation + " with score: " + maxScore);
        }
        // rebuild path into list
        String[] path = backTrace.getOrDefault(highestFinalObservation, " ").split(" -> ");
        return Arrays.copyOfRange(path, 1, path.length);

    }

    /**
     * Method to load HMM from file.
     * Credit: modified from incomplete wrapper code by Prof. Chris Bailey-Kellogg
     * @author Chris Bailey-Kellogg
     * @author Amittai Siavava
     * @param filename Name of file to find HMM from
     * @throws Exception Error reading from or writing to file
     */
    public void loadData(String filename) throws Exception {
        if (debug) System.out.println("loading data from "+filename);
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line;
        boolean gettingObservations = true;
        // Initialize HashMaps
        HashMap<String, HashMap<String, Double>> states = new HashMap<>();
        HashMap<String, HashMap<String, Double>> transitions = new HashMap<>();
        while ((line = in.readLine()) != null) {
            if (debug) System.out.println(line);
            if (line.equals("Observations")) gettingObservations = true;
            else if (line.equals("Transitions")) gettingObservations = false;
            else {
                String[] parts = line.split(",");
                if (parts.length==0) continue;
                String state = parts[0];
                for (int i=1; i<parts.length; i+=2) {
                    double score = Double.parseDouble(parts[i+1]);
                    if (gettingObservations) {
                        String word = parts[i];
                        if (debug) System.out.println("observation "+state+" "+word+" "+score);
                        HashMap<String, Double> temp = states.getOrDefault(state, new HashMap<>());
                        temp.put(word, score);
                        states.put(state, temp);
                    }
                    else {
                        String next = parts[i];
                        if (debug) System.out.println("transition "+state+" "+next+" "+score);
                        HashMap<String, Double> temp = transitions.getOrDefault(state, new HashMap<>());
                        temp.put(state+" -> "+next, score);
                        transitions.put(state, temp);
                    }
                }
            }
        }
        in.close();
        this.states = states;
        this.transitions = transitions;
        System.out.println("Load data from directory " + filename + " complete!!");
    }

    private void train (String textSourceFile, String tagsSourceFile) throws IOException {
        BufferedReader textStream;      // Declare stream of text (sentences)
        BufferedReader tagsStream;      // Declare stream of tags (corresponding to a sentence)
        HashMap<String, HashMap<String, Double>> states = new HashMap<>();      // Initialize map of states
        HashMap<String, HashMap<String, Double>> transitions = new HashMap<>(); // Initialize map of transitions

        // Try to initialize textStream. IF file not found, print error, exit
        try {
            textStream = new BufferedReader(new FileReader(textSourceFile));
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found: " + textSourceFile);
            return;
        }

        // Try to initialize tagsStream. IF file not found, print error, exit
        try {
            tagsStream = new BufferedReader(new FileReader(tagsSourceFile));
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found: " + tagsSourceFile);
            return;
        }

        // Get first sentence from text Stream
        String sentence = textStream.readLine();

        // Get first line of tags from tagsStream
        String sentenceTags = tagsStream.readLine();

        // While sentence is not null and sentenceTags is not null
        // (What would happen at the end of either file)
        while (sentence != null && sentenceTags != null) {

            // cast each to lower case --> convenient for consistency
            sentence = sentence.toLowerCase();
            sentenceTags = sentenceTags.toLowerCase();

            // get list of words in sentence
            String[] words = sentence.split(" ");

            // get list of tags in sentenceTags
            String[] tags = sentenceTags.split(" ");

            // if number of words and number of tags don't match,
            // print error message and exit
            if (words.length != tags.length) {
                System.err.println("Mismatch! \n" + "Sentence: " + sentence + "\n" + "Tags: " + sentenceTags);
                return;
            }

            // TODO: First... Handle the "#" tag & transition at start of sentence
            String firstTag = "#";              // First tag is "#"
            String secondTag = tags[0];         // second tag in HMM is first tag in sentence
            String transition = firstTag + " -> " + secondTag;  // Transition

            // Get map of transitions for "#" tag
            HashMap<String, Double> firstTransitions = transitions.getOrDefault(firstTag, new HashMap<>());

            // Add or increment count of transitions from "#" to first element in current sentence
            firstTransitions.put(transition, firstTransitions.getOrDefault(transition, 0.0) + 1);

            // reinsert map of transitions for "#" into map of all transitions
            transitions.put(firstTag, firstTransitions);

            // TODO: Update the other tags and states
            // For each index corresponding to a word and a tag
            for (int i=0; i<words.length; i++) {
                String word = words[i];     // Get word at index
                String tag = tags[i];       // Get corresponding tag

                // Get map of strings for current tag
                HashMap<String, Double> currentState = states.getOrDefault(tag, new HashMap<>());
                // Increment (or add) count of current word
                currentState.put(word, currentState.getOrDefault(word, 0.0) + 1);

                // replace map of words for the current state
                states.put(tag, currentState);

                // For previous Tag, update transition to current tag
                String previousTag;
                if (i == 0) previousTag = "#";
                else previousTag = tags[i-1];             // Get previous tag
                transition = previousTag + " -> " + tag;    // Generate label of transition

                // Get map of transitions for previous tag
                HashMap<String, Double> possibleTransitions = transitions.getOrDefault(previousTag, new HashMap<>());

                // Get (or generate) value for current transition and increment it
                possibleTransitions.put(transition, possibleTransitions.getOrDefault(transition, 0.0) + 1);

                // replace value
                transitions.put(previousTag, possibleTransitions);
            } // end of for-loop over sentence
            // Read next lines and repeat loop
            sentence = textStream.readLine();
            sentenceTags = tagsStream.readLine();
        }
        // Close streams
        textStream.close();
        tagsStream.close();

        // Calculate totals and logarithmic probabilities for words in each state
        for (String state : states.keySet()) {
            HashMap<String, Double> words = states.get(state);
            double total = 0;       // variable to count total word occurrences

            // for each word, add it's occurrences to total
            for (double value : words.values()) {
                total += value;
            }

            // for each word, compute the log of it's occurrence divided by total occurrences
            for (String word : words.keySet()) {
                words.put(word, Math.log(words.get(word) / total));
            }

            // and replace original value (which was just the count of words)
            states.put(state, words);
        }

        // Calculate totals and logarithmic probabilities for each transition
        for (String state : transitions.keySet()) {
            HashMap<String, Double> stateTransitions = transitions.get(state);
            double total = 0;       // Initialize total count
            // For each transition, add it's count to total
            for (double value : stateTransitions.values()) {
                total += value;
            }

            // For each transition, find log of count divided by total count
            for (String transition : stateTransitions.keySet()) {
                stateTransitions.put(transition, Math.log(stateTransitions.get(transition)/total));
            }

            // replace map of transitions for this state in the full map
            // of all transitions for all states
            transitions.put(state, stateTransitions);
        }

        System.out.println("TRAINING DONE!!!");
        if (debug) {
            for (String key: states.keySet()) {
                System.out.println(key + ":      -->" + states.get(key) + "");
            }
            for (String key : transitions.keySet()) {
                System.out.println(key + ":      -->" + transitions.get(key) + "");
            }
        }

        // set states and transitions to this.states, this.transitions
        // NB: I avoided modifying them directly because this.states and this.transitions
        // might have been initialized to a different state (containing some values)
        // if the HMM class is first initialized with some data then trained... which might lead to
        // a slightly different data model --> and different results
        // if we just check whether they have stuff and add stuff inside as opposed to overwriting them.
        this.states = states;
        this.transitions = transitions;
    }

    /**
     * Method to test text entered in the console
     * @throws Exception Error reading from file
     */
    public static void testConsole() throws Exception {

        HMM engine; // declare
        HMM.debug = false;

        // Request user to select a mode (to train an HMM or manually load an HMM)
        System.out.println("Please type in a mode --> 'train' or 'manual'");
        Scanner input = new Scanner(System.in);
        String mode = input.nextLine();

        // Initialize engine based on user option
        if (mode.equals("manual")) {
            String source = "inputs/example-hmm.csv";
            engine = new HMM(source);

        }
        else if (mode.equals("train")) {

            System.out.println("TYPE '0' to use simple training dataset, '1' to use brown training dataset");
            String source = input.next();
            String textSource = "";
            String tagsSource = "";
            if (source.equals("0")) {
                textSource = "inputs/example-sentences.txt";
                tagsSource = "inputs/example-tags.txt";
            }
            else {
                textSource = "inputs/brown-train-sentences.txt";
                tagsSource = "inputs/brown-train-tags.txt";
            }
            engine = new HMM(textSource, tagsSource);
        }
        else {
            engine = new HMM();
        }


        // Keep infinite loop running, prompt user for text and test it:
        // noinspection InfiniteLoopStatement
        while (true) {
            System.out.println("Please type in a sentence to check: ");
            String line = input.nextLine();
            String[] tags = engine.viterbi(line);

            String output = "";
            for (int i=0; i <= tags.length-1; i++) {
                output += line.split(" ")[i] + "/" + tags[i] + " ";
            }
            System.out.println(output);
        }
    }

    /**
     * Method to test from files
     * @throws Exception Error reading from files
     */
    public static void testFile() throws Exception {
        HMM engine;
        HMM.debug = false;
        BufferedReader text;
        BufferedReader tags;

        System.out.println("Please type in a mode --> 'train' or 'manual'");
        Scanner input = new Scanner(System.in);
        String mode = input.nextLine();
        if (mode.equalsIgnoreCase("train")) {
            System.out.println("TYPE '0' to use simple training dataset, '1' to use brown training dataset");
            String source = input.next();
            String testText = "",testTags = "";
            String trainText = "", trainTags = "";

            // Depending on user feedback, set up source files
            if (source.equals("0")) {
                testText = "inputs/simple-test-sentences.txt";
                testTags = "inputs/simple-test-tags.txt";
                trainText = "inputs/simple-train-sentences.txt";
                trainTags = "inputs/simple-train-tags.txt";
            }
            else {
                testText = "inputs/brown-test-sentences.txt";
                testTags = "inputs/brown-test-tags.txt";
                trainText = "inputs/brown-train-sentences.txt";
                trainTags = "inputs/brown-train-tags.txt";
            }
            engine = new HMM(trainText, trainTags);
            // Open text source file, quit if exception
            try {
                text = new BufferedReader(new FileReader(testText));
            }
            catch (Exception e) {
                System.err.println("File not found or faulty: " + testText);
                return;
            }

            // Open tags source file, quit if exception
            try {
                tags = new BufferedReader(new FileReader(testTags));
            }
            catch (Exception e) {
                System.err.println("File not found or faulty: " + testTags);
                return;
            }
            String textLine = text.readLine();
            String tagsLine = tags.readLine();
            int correct = 0;
            int total = 0;
            // Keep doing until end of file
            while (textLine != null && tagsLine != null) {
                String[] tagsInLine = tagsLine.split(" ");
                String[] computedTags = engine.viterbi(textLine);
                if (tagsInLine.length != computedTags.length) {
                    System.err.println("Mismatch!! \n Text: " + textLine + "\n Tags: " + Arrays.toString(tagsInLine) +
                            "\n" + "Computed: " + Arrays.toString(computedTags));
                }
                if (debug) {
                    System.out.println("Text: " + textLine + "\n Tags: " + Arrays.toString(tagsInLine) +
                            "\n" + "Computed: " + Arrays.toString(computedTags));
                }
                for (int i = 0; i < tagsInLine.length; i++) {
                    total += 1;
                    if (computedTags[i].equalsIgnoreCase(tagsInLine[i])) correct += 1;
                }
                textLine = text.readLine();
                tagsLine = tags.readLine();
            }
            System.out.println("Correct tags: " + correct);
            System.out.println("Incorrect tags: " + (total - correct));
            System.out.println("Total tags: " + total);
        }
    }


    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Test from Console ('0') or Test from file ('1')?");
        String mode = input.next();
        if (mode.equalsIgnoreCase("0")) {
            testConsole();
        }
        else if (mode.equalsIgnoreCase("1")) {
            testFile();
        }

    }
}
