import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author 1442877
 *         <p>
 *         This class handles the transaction process for each client request.
 *         Once a client connect to the Bank Server this class will be called.
 */

public class BankClientThread extends Thread {

	// This private variable stores the Bank Client's ID. e.g "Client1" or
	// "Client2" or "Client3"
	private String BankClientID;
	private String TransactionID;
	private int TransactionAmount;
	private String TransferClientID;

	private static final String TAG = "BankClient_Class";

	// Bank Client Thread Constructor
	BankClientThread(String BankClientID, String TransactionID, int TransactionAmount, String TransferClientID) {
		this.BankClientID = BankClientID;
		this.TransactionID = TransactionID;
		this.TransactionAmount = TransactionAmount;
		this.TransferClientID = TransferClientID;
	}

	public void run() {
		try {
			// Set up the socket, in and out variables
			Socket BankClientSocket = null;
			PrintWriter out = null;
			BufferedReader in = null;
			int BankSocketNumber = 4545;
			String BankServerName = "localhost";

			try {
				BankClientSocket = new Socket(BankServerName, BankSocketNumber);
				out = new PrintWriter(BankClientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(BankClientSocket.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host: localhost ");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to: " + BankSocketNumber);
				System.exit(1);
			}

			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer;
			String fromUser;

			WindowLogger.log(TAG + " - " + "Initialised " + BankClientID + " client and IO connections",
					Process.CLIENT);

			// Print out what the server says then take what the client's
			// response and send it back to the server.
			// Make the client's internal communication state object
			BankClientState bankClientState = new BankClientState(BankClientID, TransactionID, TransactionAmount,
					TransferClientID);

			while ((fromServer = in.readLine()) != null) {
				WindowLogger.log(TAG + " - " + BankClientID + " received " + fromServer + " from BankServer",
						Process.CLIENT);
				if (fromServer.equals("BYE!!!") || fromServer.equals("ERROR"))
					break;

				fromUser = bankClientState.processInput(fromServer);

				if (fromUser != null && !fromUser.contentEquals("ERROR")) {
					WindowLogger.log(TAG + " - " + BankClientID + " sending " + fromUser + " to BankServer",
							Process.CLIENT);
					out.println(fromUser);
				} else {
					break;
				}
			}

			// Tidy up - not really needed due to true condition in while loop
			out.close();
			in.close();
			stdIn.close();
			BankClientSocket.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}