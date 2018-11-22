
import java.net.*;
import java.io.*;

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
			ColourfulOutputs.log(TAG + " - " + currentThread.getName() + " initialising.", Process.SERVER);
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
			System.out.println(TAG + " - " + "Client Sent: " + inputLine);
			try {
				outputLine = bankTransactionState.processInput(inputLine);
				out.println(outputLine);
			} catch (NullPointerException e) {
				e.printStackTrace();
				break;
			}
			System.out.println(TAG + " - " + "Server is sending: " + outputLine);
			if (outputLine.contains("BYE!!!") || outputLine.contains("ERROR!!!"))
				break;

		}
		// Tidy up
		out.close();
		in.close();
		bankSocket.close();
	}

}