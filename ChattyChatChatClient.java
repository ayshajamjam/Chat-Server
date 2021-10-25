import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChattyChatChatClient{
	public static void main(String[] args) {
		
		int port = Integer.parseInt(args[1]);
		String hostname = args[0];
		Socket socket = null;
		
		try {

			// Establish connection with server on port 9876
			socket = new Socket(hostname, port);
			
			// Performs a loop of receiving a message from the keyboard + sends to the server
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			BufferedReader userIn = new BufferedReader(new InputStreamReader(
					System.in));
			
			
			// Welcome message from server
			System.out.println(in.readLine());
			
			// Server to terminal part
			Runnable r = new ServerToTerminalRunnable(socket);
			Thread t = new Thread(r);
			t.start();
			
			// Keyboard to server part
			boolean done = false;
			while(!done) {
				
				String userMsg = userIn.readLine();
				if(userMsg == null || userMsg.equals("/quit")) {done = true;}
				out.println(userMsg);
				
			}
			
			//server.close();
			
		}
		catch(IOException e) {
			System.out.println("Error with socket");
		}
		finally {
			if(socket != null) {
				try {socket.close();}
				catch(IOException e) {}
			}
		}
		
		return;
	
	}
}