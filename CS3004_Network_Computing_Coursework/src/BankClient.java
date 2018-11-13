import java.io.*;
import java.net.*;

public class BankClient extends Thread {
	String ActionClientID;

	public BankClient(String ActionClientID){
		this.ActionClientID = ActionClientID;
	}

	public void run() {
		try
		{
			// Set up the socket, in and out variables

			Socket ActionClientSocket = null;
			PrintWriter out = null;
			BufferedReader in = null;
			int ActionSocketNumber = 4545;
			String ActionServerName = "localhost";
		
			try {
				ActionClientSocket = new Socket(ActionServerName, ActionSocketNumber);
				out = new PrintWriter(ActionClientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(ActionClientSocket.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host: localhost ");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to: "+ ActionSocketNumber);
				System.exit(1);
			}

			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer;
			String fromUser = "Do my Action!";

			System.out.println("Initialised " + ActionClientID + " client and IO connections");

			// This is modified as it's the client that speaks first



			fromServer = in.readLine();
			System.out.println(ActionClientID + " received " + fromServer + " from BankServer");

			BankClientState bankClientState = new BankClientState(ActionClientID);

			fromUser = bankClientState.processInput(fromServer);

			if (fromUser != null) {
				System.out.println(ActionClientID + " sending " + fromUser + " to BankServer");
				out.println(fromUser);
			}
			fromServer = in.readLine();
			System.out.println(ActionClientID + " received " + fromServer + " from BankServer");

			// Tidy up - not really needed due to true condition in while loop
			out.close();
			in.close();
			stdIn.close();
			ActionClientSocket.close();


		} catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
}