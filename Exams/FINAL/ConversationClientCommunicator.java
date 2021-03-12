// TODO: Note Commented out code because of non-implemented Class
//  ConversationClientCommunicator

//import java.io.*;
//import java.net.*;
//import java.util.*;
//
///**
// * This is the same structure as usual.
// * I created a Channel instance variable to hold the state.
// * I isolated a handleMessage whose body you need to fill in, to update the channel.
// */
//public class ConversationClientCommunicator extends Thread {
//    private ConversationClient client;		// for which this is handling communication
//    private BufferedReader in;				// from server
//    private PrintWriter out;				// to server
//    public Channel channel;					// *** the conversations
//
//    /**
//     * Establishes connection and in/out pair
//     */
//    public ConversationClientCommunicator(String serverIP, ConversationClient client) {
//        this.client = client;
//        System.out.println("connecting to " + serverIP + "...");
//        try {
//            Socket sock = new Socket(serverIP, 4242);
//            out = new PrintWriter(sock.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//            System.out.println("...connected");
//        }
//        catch (IOException e) {
//            System.err.println("couldn't connect");
//            System.exit(-1);
//        }
//        channel = new Channel();		// *** initialize the instance
//    }
//
//    /**
//     * Sends message to the server
//     */
//    public void send(String msg) {
//        out.println(msg);
//    }
//
//    /**
//     * Keeps listening for and handling messages from the server
//     */
//    public void run() {
//        try {
//            String msg;
//            // Handle messages
//            while ((msg = in.readLine()) != null) {
//                System.out.println(msg);
//                handleMessage(msg);
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally {
//            System.out.println("server hung up");
//        }
//    }
//
//    public void handleMessage(String msg) {
//        // TODO: your code here
//        // Add message to channel
//        channel.addMessage(msg);        // Channel.addMessage() method is already synchronized
//    }
//}
