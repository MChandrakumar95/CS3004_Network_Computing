import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author 1442877
 *         <p>
 *         This is the server side thread class. This class handles the requests
 *         from the client. Each time a client connects to the server one of
 *         these threads are triggered. The class will interrogates the client
 *         for all the information it requires to perform the transaction.
 */

public class BankServerThread extends Thread {

	private static final String TAG = "BankServerThread_Class";

	private Socket bankSocket;
	private Funds bankFundsCurrentStateObject;

	// Setup the thread
	// Constructor
	BankServerThread(Socket bankSocket, Funds BankFunds) {
		this.bankSocket = bankSocket;
		bankFundsCurrentStateObject = BankFunds;
	}

	// The run method. This method gets executed when this thread is triggered.
	public void run() {
		try {
			Thread currentThread = Thread.currentThread();
			WindowLogger.log(TAG + " - " + currentThread.getName() + " initialising.", Process.SERVER);
			gettingAllRequiredDetails();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void gettingAllRequiredDetails() throws IOException {
		// Connect the input and the output to and from the socket
		PrintWriter out = new PrintWriter(bankSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(bankSocket.getInputStream()));
		String inputLine, outputLine;
		// Make the server internal state object
		BankTransactionState bankTransactionState = new BankTransactionState(bankFundsCurrentStateObject);
		// Get the first message
		outputLine = bankTransactionState.processInput(null);
		// And send it to the client
		out.println(outputLine);
		// Repeatedly loop getting input from the client
		// check it with the state object for the response
		// and send it to the client until the client
		// says BYE!!! or ERROR.
		while ((inputLine = in.readLine()) != null) {
			WindowLogger.log(TAG + " - " + "Client Sent: " + inputLine, Process.SERVER);
			try {
				outputLine = bankTransactionState.processInput(inputLine);
				out.println(outputLine);
			} catch (NullPointerException e) {
				e.printStackTrace();
				break;
			}
			WindowLogger.log(TAG + " - " + "Server is sending: " + outputLine, Process.SERVER);
			if (outputLine.contains("BYE!!!") || outputLine.contains("ERROR!!!"))
				break;

		}
		// Tidy up
		out.close();
		in.close();
		bankSocket.close();
	}

}