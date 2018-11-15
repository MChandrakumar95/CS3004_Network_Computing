
import java.net.*;
import java.io.*;


public class BankServerThread extends Thread {

	private static ColourfulOutputs colourfulOutputs = new ColourfulOutputs(ValidColours.GREY);
	private static final String TAG = "BankServerThread Class";


	private Socket bankSocket;
	private Funds bankFundsCurrentStateObject;
	private String myBankServerThreadName;


	//Setup the thread
	BankServerThread(Socket bankSocket, String BankServerThreadName, Funds BankFunds) {
		this.bankSocket = bankSocket;
		bankFundsCurrentStateObject = BankFunds;
		myBankServerThreadName = BankServerThreadName;
	}

	public void run() {

        colourfulOutputs.helper(TAG);

		try {
            colourfulOutputs.outputColourfulText(TAG + " - " + myBankServerThreadName + " initialising.");
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
        // says Bye.
        while ((inputLine = in.readLine()) != null) {
            colourfulOutputs.outputColourfulText(TAG + " - " + "Client Sent: " + inputLine);
             try {
				 outputLine = bankTransactionState.processInput(inputLine);
				 out.println(outputLine);
             } catch (NullPointerException e ){
             	e.printStackTrace();
             	break;
			 }
            colourfulOutputs.outputColourfulText(TAG + " - " + "Server is sending: " + outputLine);
            if (outputLine.contains("BYE!!!")||outputLine.contains("ERROR!!!"))
                break;

        }
         // Tidy up
         out.close();
         in.close();
		bankSocket.close();
	}


}