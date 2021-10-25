import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

// Purpose: read from server, print to terminal 

public class ServerToTerminalRunnable implements Runnable{
	
	Socket socket;
	BufferedReader in = null;
	
	public ServerToTerminalRunnable(Socket s) throws IOException {
		socket = s;
	}
	
	@Override
	public void run() {
		try {
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			boolean done = false;
			while(!done) {
				String msg = in.readLine();
				if(msg == null || msg.equals("/quit")) {done = true;}
				else{
					System.out.println(msg);
				}
			}

		}
		catch (IOException e) {
			System.out.println("Error with sending to client ");
			System.out.println("Exception: " + e.toString() );
		}
		finally {
			System.out.println("Closing connection to client ");
			try { socket.close(); }
			catch (Exception e) { }
		}
	}
}

