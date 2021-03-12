import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Bacon {
    private Graph<String, HashSet<String>> movieNetwork;    // Absolute Graph of the movie network
    private Graph<String, HashSet<String>> centeredNetwork; // Graph of movie network re-centered around a specific actor
    private String centralActor = "";                       // Instance variable to hold current central actor


    /**
     * Default constructor configured with test files
     * @throws IOException  Error reading from file
     */
    public Bacon () throws IOException {
        // Initialize the names to default test files start a new instance
        String actorsFile = "inputs/bacon/actorsTest.txt";
        String moviesSource = "inputs/bacon/moviesTest.txt";
        String actorsInMovies = "inputs/bacon/movie-actorsTest.txt";
        new Bacon(actorsFile, moviesSource, actorsInMovies);
    }

    /**
     * Constructor with source file names
     * @param actorsSourceFile  File source of actors data
     * @param moviesSourceFile  File source of movies data
     * @param actorsInMovies    File source of actors in movies data
     * @throws IOException      Error reading from file
     */
    public Bacon(String actorsSourceFile, String moviesSourceFile, String actorsInMovies) throws IOException {

        // Build the Graph of movies, actors, and connections
        this.buildMovieNetwork(actorsSourceFile, moviesSourceFile, actorsInMovies );

        // if in test mode, print out the Graph
//        System.out.println(this.movieNetwork);

        // Call the function to play the game
        this.play();
    }

    /**
     * Constructor with specified mode for using test data or actual data
     * @param mode          The game mode ("test" or "play"), each configured with the appropriate source files
     * @throws IOException  Error reading from file
     */
    public Bacon(String mode) throws IOException {
        switch (mode) {
            case "test" -> {
                String actorsFile = "inputs/bacon/actorsTest.txt";
                String moviesSource = "inputs/bacon/moviesTest.txt";
                String actorsInMovies = "inputs/bacon/movie-actorsTest.txt";
                new Bacon(actorsFile, moviesSource, actorsInMovies);
            }
            case "play" -> {
                String actorsFile = "inputs/bacon/actors.txt";
                String moviesSource = "inputs/bacon/movies.txt";
                String actorsInMovies = "inputs/bacon/movie-actors.txt";
                new Bacon(actorsFile, moviesSource, actorsInMovies);
            }
        }
    }

    /**
     * Method to load actor data from file
     * @param fileName Name of file with actor data
     * @return HashMap mapping actor codes to actor names
     */
    private HashMap<Integer, String> loadActors(String fileName) {
        // Load the actors file
        HashMap<Integer, String> actorNames = new HashMap<>();  // Initialize HashMap of actor codes to names
        BufferedReader actorsSource;                            // Initialize BufferedReader
        try {
            actorsSource = new BufferedReader(new FileReader(fileName));
            String nextLine = actorsSource.readLine();              // Read first line in file
            while(nextLine != null) {
                String[] elements = nextLine.split("\\|");      // Split line into elements using "|"
                int actorCode = Integer.parseInt(elements[0]);          // Parse first element into int
                String actorName = elements[1];                         // Get name --> second element
                actorNames.put(actorCode, actorName);                   // Add to HashMap
                nextLine = actorsSource.readLine();                     // Read next line and repeat loop
            }
        } catch(IOException e) {
            System.err.println("Error reading from " + fileName + " or invalid data.");
        }

        // Return built HashMap of actor codes to actor names
        return actorNames;
    }

    /**
     * Method to load data on actors cast in movies
     * @param fileName Name of file containing actors in movies data
     * @return HashMap mapping a movie codes to actor codes for actors cast in each movie
     */
    private HashMap<Integer, HashSet<Integer>> loadMovieActors(String fileName) {
        // Load the movies and actors file
        BufferedReader movieActorsSource;
        HashMap<Integer, HashSet<Integer>> movieActors = new HashMap<>();
        try {
            movieActorsSource = new BufferedReader(new FileReader(fileName));   // Initialize BufferedReader
            String nextLine = movieActorsSource.readLine();         // Read first line into variable
            while(nextLine != null) {                               // While line read is not null
                String[] elements = nextLine.split("\\|");   // Split line by "|"
                int movieCode = Integer.parseInt(elements[0]);      // First element is movie code
                int actorCode = Integer.parseInt(elements[1]);      // Second element is actor code
                HashSet<Integer> actorsInMovie;                     // Initialize set to hold codes for actors in each movie

                // if hashmap contains current movie already,
                // Get set of actors in the movie and add current actor code
                if (movieActors.containsKey(movieCode)) {
                    actorsInMovie = movieActors.get(movieCode);
                    actorsInMovie.add(actorCode);               // add current actor to the set
                }
                // else, initialize set for actors in current movies and insert into HashMap
                else {
                    actorsInMovie = new HashSet<>();
                    actorsInMovie.add(actorCode);               // add current actor to the set
                    movieActors.put(movieCode, actorsInMovie);  // Put set in the HashMap
                }

                nextLine = movieActorsSource.readLine();    // Finally, read the next line and repeat while loop
            }
        }
        catch(IOException e) {
            System.err.println("Error reading from " + fileName + " or invalid data.");
        }
        return movieActors;
    }

    /**
     * Method to load movie data
     * @param fileName Name of file with movie data
     * @return HashMap mapping movie codes to movie names
     */
    private HashMap<Integer, String> loadMovies(String fileName) {

        BufferedReader readMovies;                              // Initialize BufferedReader for movies file
        HashMap<Integer, String> movieNames = new HashMap<>();  // Initialize HashMap to hold mappings of movie codes to names
        try {
            readMovies = new BufferedReader(new FileReader(fileName));
            String nextLine = readMovies.readLine();                // Get first line
            while(nextLine != null) {                               // While line is not null
                String[] elements = nextLine.split("\\|");   // Split line into elements
                int movieCode = Integer.parseInt(elements[0]);      // Cast first element into integer --> movie code
                String movieName = elements[1];                     // Second element is movie name
                movieNames.put(movieCode, movieName);               // Add mapping of movie code to movie name in the HashMap
                nextLine = readMovies.readLine();                   // Get next line and repeat loop
            }
        }
        catch(IOException e) {
            System.err.println("Error reading from " + fileName + " or incorrect data.");
        }

        // Finally, return the built mapping of movie codes to movie names
        return movieNames;
    }

    /**
     * Method to build the Graph network of movies, actors, and connections
     * @param actorsSource The source file for actors data -- actor code and name
     * @param moviesSource Source file for movies data -- movie code and name
     * @param connectionsSource Source file for connections -- movies and cast actors
     */
    private void buildMovieNetwork(String actorsSource, String moviesSource, String connectionsSource) {
        // Initialize Graph
        movieNetwork = new AdjacencyMapGraph<String, HashSet<String>>();

        // Parse the data from the sourceFile into convenient HashMaps
        HashMap<Integer, String> actorNames = loadActors(actorsSource);
        HashMap<Integer, String> movieNames = loadMovies(moviesSource);
        HashMap<Integer, HashSet<Integer>> movieActors = loadMovieActors(connectionsSource);

        // Add vertices for all actor names
        for (String actorName : actorNames.values()) {
            movieNetwork.insertVertex(actorName);
        }

        // Loop over each actor in the HashMap of actor names
        for (int actorCode : actorNames.keySet()) {

            // Get name of actor
            String actorName = actorNames.get(actorCode);

//            HashSet<String> actorMovies = new HashSet<>();

            // Loop over each movie in the list of actor movies
            for (int mov : movieActors.keySet()) {

                // Check if actor code is in list of actors for the movie
                if (movieActors.get(mov).contains(actorCode)) {

                    // Loop over every actor in the same movie
                    for (int otherActorCode : movieActors.get(mov)) {

                        // if other actor is current actor, skip current otherActor
                        if (otherActorCode == actorCode) continue;

                        // Get other actor name
                        String otherActorName = actorNames.get(otherActorCode);

                        // Get movie name
                        String movieName = movieNames.get(mov);

                        // If Graph has edge between actors, get edge label (a set)
                        // and add current movie name
                        if (movieNetwork.hasEdge(actorName, otherActorName)) {
                            // Get set representing connection label
                            movieNetwork.getLabel(actorName, otherActorName).add(movieName);
                            movieNetwork.getLabel(otherActorName, actorName).add(movieName);
                        }
                        // else, initialize set to be label of connection
                        // and insert new undirected connection
                        else {
                            // initialize set to be label of connection
                            HashSet<String> label = new HashSet<>();
                            label.add(movieName);

                            // insert undirected edge into Graph
                            movieNetwork.insertUndirected(actorName, otherActorName, label);
                        }

                    } // exit inner FOR loop
                } // exit outer IF check
            } // exit outer FOR loop
        } // exit master FOR loop
    }

    /**
     * Method to do an action based on a specified choice
     * @param mode What action the player wants to do
     */
    private void doAction(String mode) {
        String[] elements;      // List to hold split elements of the user's input

        // TODO: if mode is b... print analytics for current central actor.
        if (mode.toLowerCase().startsWith("b")) {
            if (centralActor.equals("")) {
                System.err.println("Central actor is not yet initialized!");
                return;
            }
            // find average separation of pathTree
            double averageSeparation = GraphLib.averageSeparation(centeredNetwork, centralActor);
            // Find degree of current central actor
            int degree = movieNetwork.outDegree(centralActor);
            // calculate size of pathTree
            int treeSize = -1;
            for (String vertex : centeredNetwork.vertices()) treeSize++;
            System.out.println("The current central actor is: " + centralActor);
            System.out.println(centralActor + " has an average separation of: " + averageSeparation);
            System.out.println(centralActor + " is connected to " + degree + " other actors.");
            System.out.println(centralActor + "'s network has a total of " + treeSize + " other actors.");

        }


        // TODO: if mode is c... find top <#> or bottom <#> centers of the universe
        else if (mode.toLowerCase().startsWith("c")) {
            elements = mode.split(" ");
            int num;        // number of items to fetch
            try {           // Input might not be as long as it should be, or might not be a number
                num = Integer.parseInt(elements[1]);
            }
            catch (Exception e){ // set a default of 5, print error message
                System.err.println("Error: " + elements[1] + " is not a number. Defaulted to 5.");
                num = 5;
//                e.printStackTrace();
            }
            // Initialize HashMap to hold average connection mappings to vertices
            HashMap<Double, HashSet<String>> separationToVertices = new HashMap<>();
            // initialize priority queue
            String descriptor = "";
            PriorityQueue<Double> priority;
            if (num < 0) {
                descriptor = "The " + Math.abs(num) +
                        " bottom (highest average separation) centers of the universe are: \n";
                priority = new PriorityQueue<>((d1, d2) -> Double.compare(d2, d1));
            }
            else {
                descriptor = "The " + Math.abs(num) +
                        " top (lowest average separation) centers of the universe are: \n";
                priority = new PriorityQueue<>((d1, d2) -> Double.compare(d1, d2));
            }
            // Initialize priority queue to automatically sort inputs


            // for each actor, get the average separation of their pathTree Graph
            // and add in priority queue
            for (String actor : centeredNetwork.vertices()) {
                Graph<String, HashSet<String>> connections = GraphLib.bfs(movieNetwork, actor);
                double averageSeparation = GraphLib.averageSeparation(connections, actor);
                priority.add(averageSeparation);
                // if already exists, get set at key and add actor there
                if (separationToVertices.containsKey(averageSeparation)) {
                    separationToVertices.get(averageSeparation).add(actor);
                }
                else { // else create a new set for the level of separation,
                    // add value, and add set into HashMap
                    HashSet<String> temp = new HashSet<>();
                    temp.add(actor);
                    separationToVertices.put(averageSeparation, temp);
                }
            }

            if (priority.size() < 10) { // Easier to visualize but traumatizing for larger datasets
                System.out.println("Priority Queue: " + priority);
                System.out.println("Map: " + separationToVertices);
            }

            // in case user specifies a larger size than there are actors,
            // we may want to instead return the maximum actors
            int sizeOfQueue = priority.size();
            ArrayList<String> topActors = new ArrayList<>();
            // add the specified number of items into a temporary list, removing items from priority queue
            for (int i=0; i<Math.min(sizeOfQueue, Math.abs(num)); i++) {
                double separation = priority.remove();
                HashSet<String> vertices = separationToVertices.get(separation);
                String random = (String) vertices.toArray()[0];
                topActors.add(random);
                vertices.remove(random);
            }

            // Finally, print out the list plus a basic descriptor of what it contains
            System.out.println(descriptor + topActors);
        }


        // TODO: if mode is d... list actors by degree in the given range <low> to <high>
        else if (mode.toLowerCase().startsWith("d")) {
            elements = mode.split(" ");
            int low, high;     // initialize variables
            try { // input might be too short
                low = Integer.parseInt(elements[1]);
                high = Integer.parseInt(elements[2]);
            }
            catch(Exception e) {
                System.err.println(elements[1] + " and " + elements[2] + " are not valid numbers, defaulted to 0, 5");
                low = 0;
                high = 5;
            }

            // Initialize mapping of degrees to vertices
            HashMap<Integer, HashSet<String>> degreesToVertices = new HashMap<>();

            // initialize priority queue to sort degrees
            PriorityQueue<Integer> degrees = new PriorityQueue<>(Comparator.comparingInt(num -> num));

            // For each actor in the move network, get the number of connections they have,
            // if it falls within range, save it to priority queue and create mapping of degree
            // to actor name in the HashMap
            for (String actor : movieNetwork.vertices()) {
                int degree = movieNetwork.inDegree(actor);
                if (degree >= low && degree <= high) {
                    degrees.add(degree);
                    if (degreesToVertices.containsKey(degree)) {
                        degreesToVertices.get(degree).add(actor);
                    }
                    else {
                        HashSet<String> temp = new HashSet<>();
                        temp.add(actor);
                        degreesToVertices.put(degree, temp);
                    }
                }
            }

            // Initialize list of actors whose degrees are in range
            ArrayList<String> actorsInRange = new ArrayList<>();

            // While priority queue is not empty, keep getting the front item
            // and adding corresponding movie name to list
            while (!degrees.isEmpty()) {
                int degree = degrees.remove();                              // remove degree at front of priority queue

                HashSet<String> vertices = degreesToVertices.get(degree);   // Get set corresponding to each degree
                String random = (String) vertices.toArray()[0];             // get an element from set, cast to String
                actorsInRange.add(random);                                  // Add element to actors in the range
                // remove the element from the original set.
                // In case of multiple values in set, next check will return different value.
                vertices.remove(random);
            }
            // Finally, print  the sorted list of movies
            System.out.println("Actors with degree between " + low + " and " + high + " in increasing order: ");
            System.out.println(actorsInRange);
        }


        // TODO: if mode is i... list infinitely separated actors from central actor
        else if (mode.equalsIgnoreCase("i")) {
            if (centralActor.equals("")) {
                System.err.println("Central actor has not yet been initialized!");
                System.err.println("Please initialize a central actor before checking infinite separation.");
                return;
            }
            Set<String> infiniteActors = GraphLib.missingVertices(movieNetwork, centeredNetwork);
            System.out.println("These actors have infinite separation to " + centralActor + ": " + infiniteActors);
        }


        // TODO: if mode is p... -> get and print path to other actor
        else if (mode.toLowerCase().startsWith("p")) {
            elements = mode.split(" ");          // Split input into elements
            String otherActor;                          // initialize name of other actor
            try {                                       // User input might be too short
                otherActor = elements[1];               // Get first name
                for (int i=2; i<elements.length; i++) { // In case actor has multiple names, get other names
                    otherActor += " " + elements[i];
                }
            }
            catch(Exception e) {
                System.err.println(mode + " does not contain a valid actor name." );
                return; // break! Cannot find path to an incorrect actor name
            }
            // Get path from centralActor to other actor in pathTree --> call to GraphLib
            List<String> path;
            try {
                path = GraphLib.getPath(centeredNetwork, otherActor);
            }
            catch(Exception e) {
                System.err.println(otherActor + " does not exist in Graph!");
                return; // break! cannot continue with incorrect actor name.
            }

            // Get separation of other actor from central actor in pathTree --> call to GraphLib
            double separation = GraphLib.getSeparation(centeredNetwork, this.centralActor, otherActor);

            System.out.println(otherActor + "'s number is " + separation);

            // Loop over each actor in path, print entire path
            for (int index = 0; index < path.size() - 1; index++ ) {
                String actor1 = path.get(index); // Get name of actor 1
                String actor2 = path.get(index+1);  // Get name of actor 2

                // Get label of connection between the two actors
                Set<String> commonMovie = centeredNetwork.getLabel(actor1, actor2);

                // Print the names of the two and their connection label
                System.out.println(actor1 + " appeared in " + commonMovie + " with " + actor2);
            }
        }


        // TODO: if mode is s... get sorted list of actors from range <low> to <high>
         else if (mode.toLowerCase().startsWith("s")) {
            int low, high;     // initialize variables for low and high limit params
            try { // try to read in low and high params from user input
                elements = mode.split(" ");
                low = Integer.parseInt(elements[1]);
                high = Integer.parseInt(elements[2]);
            }
            catch ( Exception e){
                System.err.println("Error: " + mode + " does not contain valid low and high number parameters.");
                System.err.println("These were defaulted to low = 0, high = 10.");
                low = 0;
                high = 10;
            }
            // Initialize variables to hold a mapping of separations to vertices
            // and a priority queue of separations
            HashMap<Integer, HashSet<String>> separationToVertices = new HashMap<>();
            PriorityQueue<Integer> separations = new PriorityQueue<>(Comparator.comparingInt(num -> num));

            // For every actor not at the center of the movie universe,
            // Get their separation from central actor.
            // If separation falls in range, save it to queue and add mapping to HashMap
            for (String otherActor : this.centeredNetwork.vertices()) {
                int separation = GraphLib.getSeparation(centeredNetwork, this.centralActor, otherActor);
                if (separation >= low && separation <= high) {
                    if (separationToVertices.containsKey(separation)) {
                        separationToVertices.get(separation).add(otherActor);
                    }
                    else {
                        HashSet<String> temp = new HashSet<>();
                        temp.add(otherActor);
                        separationToVertices.put(separation, temp);
                    }
                    separations.add(separation);
                }
            }

            // Finally, get separation values in order from the queue,
            // add corresponding actor names to temporary array in order then print the array
            ArrayList<String> actorsInRange = new ArrayList<>();
            while (!separations.isEmpty()) {
                int separation = separations.remove();
                HashSet<String> vertices = separationToVertices.get(separation);
                String random = (String) vertices.toArray()[0];
                actorsInRange.add(random);
                vertices.remove(random);
            }
            System.out.println("Separations, going farther from " + centralActor + ", range "
                    + low + " to " + high + " : " + actorsInRange);
            /*
            Note: Using a start range of 0 prints actor at center of Graph too.
            I thought about excluding this but I decided against it.
             */
        }


        // TODO if mode is u...
        else if (mode.toLowerCase().startsWith("u")) { // Make actor center of game universe
            elements = mode.split(" "); // Split String into elements
            // Initialise name of actor
            String actorName = elements[1];
            // Concatenate name  --> some actors have two names separated by a space,
            // so name will be split
            for (int i=2; i<elements.length; i++) {
                actorName += " " + elements[i];
            }
            // Finally, build a re-centered Graph around the actor
            // and set it to the instance variable of the game
            try {
                this.centeredNetwork = GraphLib.bfs(this.movieNetwork, actorName);
                this.centralActor = actorName;
            }
            catch (Exception e){
                System.err.println(actorName + " does not exist in this database!");
                return;
            }
            int miniNetSize = centeredNetwork.numVertices();
            double avgSeparation = GraphLib.averageSeparation(this.centeredNetwork, this.centralActor);

            System.out.println(centralActor + " is now the center of the acting universe, " +
                    "connected to " + (miniNetSize-1) +
                    " other actors with average separation " + avgSeparation);
        }

        // TODO: if mode is q...
        else if (mode.equalsIgnoreCase("q")) {
            // Exit game
            System.out.println("Thank you for playing, please come back later!");
            System.exit(0);
        }
        else {
            System.err.println("Invalid input! Please try again.");
        }

    }

    /**
     * Function to read in user's input, check if it's valid, and do an action with the input
     * @throws IllegalStateException input does not match a valid option
     */
    public void play() throws IllegalStateException {
        Scanner input = new Scanner(System.in);

        // String of commands in case needed to print
        String commands = """
        b: analytics for current center of universe
        c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation 
        d <low> <high>: list actors sorted by degree, with degree between low and high 
        i: list actors with infinite separation from the current center 
        p <name>: find path from <name> to current center of the universe
        s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high 
        u <name>: make <name> the center of the universe 
        q: quit game""";

        System.out.println("Here are the valid commands for this game:");
        System.out.println(commands);

        // infinity loop until broken
        // noinspection InfiniteLoopStatement
        while (true) {
            if (!this.centralActor.equals("")) {
                System.out.println(centralActor + " game >");
            }
            else {
                System.out.println("Please select a central actor!");
            }
            String feedback = input.nextLine();
            // Make sure feedback starts with a viable command
            if (feedback.toLowerCase().matches("^[bcdipsuq].*")) {
                doAction(feedback);
            }
            else {
                // Call out invalid action and if user wants to see viable commands, retry, or quit
                System.err.println("Invalid option!");
                System.err.println("Press 'C' to view viable commands, 'R' to retry, or 'Q' to exit");

                // Get next input
                String action = input.next();
                switch (action.toUpperCase()) {
                    case "C" -> System.out.println(commands);   // print all viable commands
                    case "R" -> {                               // do nothing, continue to next iteration
                        ;
                    }
                    case "Q" -> doAction("q");           // Quit game
                    default -> throw new IllegalStateException("Unexpected value: " + action);
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
//        String mode = "test";       // To use test data
        String mode = "play";       // To use actual data
        new Bacon(mode);
    }
}
