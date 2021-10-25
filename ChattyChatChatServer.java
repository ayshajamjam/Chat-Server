import java.net.Socket;
import java.util.Vector;
import java.net.ServerSocket;
import java.io.IOException;

public class ChattyChatChatServer{
	
	public static Vector<ServerRunnable> clientList = new Vector<ServerRunnable>();
	
	public static void main(String[] args) throws IOException {
		
		int port = Integer.parseInt(args[0]);
		ServerSocket listener = null;
		Socket client = null;
		int clientNumber = 0;
		
		try {
			// Establish ServerSocket
			listener = new ServerSocket(port); 
			System.out.println("ServerSocket established on port " + port);
			
			while(true) {
				try {
					// Listens for connections on port 9876
					System.out.println("Listening for connection...");
					client = listener.accept();
					System.out.println("Connection established to client " + clientNumber + ": " + client);
					
					////////////////////////
					
					ServerRunnable r = new ServerRunnable(clientList, clientNumber, client);
					Thread t = new Thread(r);
					t.start();
					
					clientList.add(r);
					clientNumber++;
					
				}
				catch (IOException e) {
					System.out.println("Error connecting to client " + clientNumber);
					System.out.println("Exception: " + e.toString());
				}
				
			} // End while
		}
		catch (IOException e) {
			System.out.println("Error with listener");
			System.out.println("Exception: " + e.toString());
		}
		finally {
			if(listener != null) {
				try {listener.close();}
				catch(Exception e) {}
			}
		}
		
		return;
		
	}
}