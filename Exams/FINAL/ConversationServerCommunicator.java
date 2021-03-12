import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the same structure as usual.
 * I isolated a handleMessage whose body you need to fill in, to update the channel held by the server.
 */
public class ConversationServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private ConversationServer server;		// handling communication for

	public ConversationServerCommunicator(Socket sock, ConversationServer server) {
		this.sock = sock;
		this.server = server;		
	}

	/**
	 * Sends a message to the client
	 * @param msg message to be sent
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");

			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Keep getting and handling messages from the client
			String msg;
			while ((msg = in.readLine()) != null) {
				System.out.println("received:" + msg);
				handleMessage(msg);
			}
			System.out.println("hung up");

			// Clean up
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to handle messages server-side.
	 * Because we are doing two operations (updating channel in server, then broadcasting message),
	 * This method should be synchronized so a Thread does not get interrupted
	 * after adding message to server's Channel and fail to broadcast it
	 * (by the time the broadcast would happen,
	 * the state of the conversation might have changed significantly!)
	 * @param msg message to be broadcast
	 */
	public synchronized void handleMessage(String msg) {
		// TODO: your code here
		// Update channel instance variable in server
		server.channel.addMessage(msg);

		// broadcast message through server to all other clients
		server.broadcast(msg);
	}
}
