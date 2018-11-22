import java.net.*;
import java.io.*;

public class BankServer {

	// Declaring all the variables.
	private static boolean listening = true;
	// This is the port number that the server listens on.
	private static int BankServerPortNumber = 4545;
	// This is the server socket that the client connects on.
	private static ServerSocket BankServer = null;
	// Class name for logging purposes.
	private static final String TAG = "BankServer_Class";
	// Server name for logging purposes.
	private static final String BankServerName = "BankServer";
	// This is the funds object that holds all the clients' funds.
	private static Funds funds = new Funds();
	
	private static final Boolean saveToLogFile = false;
	

	// Main method
	public static void main(String[] args) throws IOException {
		
		//This saves all the console outputs into a log file.
		if (saveToLogFile) {
			// This produces a log file.
			ColourfulOutputs.produceLogFile(TAG);
			
		}
		// Makes the server socket using the port number specified above.
		try {
			BankServer = new ServerSocket(BankServerPortNumber);
		}
		// Catches any IO exceptions and prints the error message then kills the
		// program/server.
		catch (IOException e) {
			System.err.println("Could not start " + BankServerName + " at specified port.");
			e.printStackTrace();
			System.exit(-1);
		}
		// Logs that the server has started on the console.
		ColourfulOutputs.newLog(TAG + " - " + BankServerName + " started", Process.SERVER);

		// Got to do this in the correct order with only four clients! Can
		// automate this...

		while (listening) {
			new BankServerThread(BankServer.accept(), funds).start();
			ColourfulOutputs.newLog(TAG + " - " + "New " + BankServerName + " thread started.", Process.SERVER);
		}
		BankServer.close();
		
		if (saveToLogFile){
			ColourfulOutputs.readLogFile(TAG);
		}
		
	}
}