import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

/* Put protocol in this class */

public class ServerRunnable implements Runnable{
	
	private int clientNumber;
	private Socket client;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private String clientName = "";
	private Vector<ServerRunnable> clientList;
	
	public ServerRunnable(Vector<ServerRunnable> list, int n, Socket s) throws IOException {
		clientList = list;
		clientNumber = n;
		client = s;
		
		out = new PrintWriter(client.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	}
	
	@Override
	public void run() {
		
		try {
			
			out.println("Welcome client " + clientNumber);

			boolean done = false;
			while(!done) {
				
				// Read in input from client
				String input = in.readLine();
				
				// Separate message into components
				String[] message = input.split(" ");
				
				// Protocol for valid message
				if(input != null) {
					// Quit
					if(message[0].equals("/quit")) {done = true;}
					// Nickname: assign client's name to nickname
					if(message[0].equals("/nick")) {
							clientName = message[1];
					}
					// DM: check for direct message qualification
					else if(message[0].equals("/dm")) {
						String output = "";
						for(int i = 0; i < clientList.size(); i++) {
							if(clientList.get(i).clientName.equals(message[1])) {
								for(int j = 2; j < message.length; j++) {
									output += (message[j] + " ");
								}
								clientList.get(i).out.println(output);
							}
						}
						
					}
					// Normal message
					else {
						for(ServerRunnable cl: clientList) {
							cl.getWriter().println(input);
						}
					}
					
				}
				System.out.println("Sent \"" + input + "\" to client " + clientNumber);
				
			}
			
		}
		
		catch (IOException e) {
			System.out.println("Error with client " + clientNumber);
			System.out.println("Exception: " + e.toString() );
		}
		finally {
			System.out.println("Closing connection to client " + clientNumber);
			try { client.close(); }
			catch (Exception e) { }
		}
	}

	public PrintWriter getWriter() {
		return out;
	}
	
	public String getName() {
		return clientName;
	}
	
	public void setName(String nameInput) {
		clientName = nameInput;
	}
}



