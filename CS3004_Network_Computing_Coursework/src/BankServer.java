import java.io.IOException;
import java.net.ServerSocket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 1442877
 *         <p>
 *         This is the Bank server class which accepts connection requests from
 *         the client. Once a connection has been made a new thread is triggered
 *         to handle the transaction request. Threads are used to handle
 *         multiple client transactions at the same time.
 */

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

	// Main method
	public static void main(String[] args) throws IOException {

		String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
		WindowLogger.log(TAG + " - Server Application started at: " + timestamp, Process.SERVER);

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
		WindowLogger.log(TAG + " - " + BankServerName + " started", Process.SERVER);

		// Listens to new connections and starts a new thread to process the
		// request.
		while (listening) {
			new BankServerThread(BankServer.accept(), funds).start();
			WindowLogger.log(TAG + " - " + "New " + BankServerName + " thread started.", Process.SERVER);
		}
		BankServer.close();

	}
}