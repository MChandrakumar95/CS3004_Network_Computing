/**
 * @author 1442877
 *         <p>
 *         This is the Funds class. This is the class that actually conducts the
 *         transaction whether it be add, withdraw or transfer funds, this class
 *         handles it all. This class has all the information about the client's
 *         funds. Locks are used to prevent deadlocking.
 */

class Funds {

	private static final String TAG = "Funds_Class";

	private int client1Funds = 100;
	private int client2Funds = 100;
	private int client3Funds = 100;
	// This is true only when a lock has been acquired otherwise it will be
	// false.
	private boolean accessing = false;

	// Attempt to acquire a lock
	synchronized void acquireLock() throws InterruptedException {
		// Get the name of the current thread.
		String currentThread = Thread.currentThread().getName();
		WindowLogger.log(TAG + " - " + currentThread + " is attempting to acquire a lock!", Process.SERVER);
		while (accessing) { // while someone else is accessing
			WindowLogger.log(TAG + " - " + currentThread + " waiting to get a lock as someone else is accessing...",
					Process.SERVER);
			// wait for the lock to be released - see releaseLock() below
			wait();
		}
		// nobody has got a lock so get one
		accessing = true;
		WindowLogger.log(TAG + " - " + currentThread + " got a lock!", Process.SERVER);
	}

	// Releases a lock to when a thread is finished
	synchronized void releaseLock() {
		// release the lock and tell everyone
		accessing = false;
		notifyAll();
		Thread me = Thread.currentThread(); // get a ref to the current thread
		WindowLogger.log(TAG + " - " + me.getName() + " released a lock!", Process.SERVER);
	}

	private synchronized String addFunds(String clientID, int amountToAdd) {
		String theOutput;
		switch (clientID) {
		case "Client1":
			client1Funds = client1Funds + amountToAdd;
			WindowLogger.log(TAG + " - " + clientID + "'s funds has been updated to " + client1Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToAdd
					+ " has been added to your account. Your Account Balance is " + client1Funds;
			break;
		case "Client2":
			client2Funds = client2Funds + amountToAdd;
			WindowLogger.log(TAG + " - " + clientID + "'s funds has been updated to " + client2Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToAdd
					+ " has been added to your account. Your Account Balance is " + client2Funds;
			break;
		case "Client3":
			client3Funds = client3Funds + amountToAdd;
			WindowLogger.log(TAG + " - " + clientID + "'s funds has been updated to " + client3Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToAdd
					+ " has been added to your account. Your Account Balance is " + client3Funds;
			break;
		default:
			WindowLogger.log("Invalid Bank Account!!!", Process.SERVER);
			theOutput = "Invalid Bank Account!!!";
			break;
		}
		return theOutput;
	}

	private synchronized String withdrawFunds(String clientID, int amountToWithdraw) {
		String theOutput = null;
		switch (clientID) {
		case "Client1":
			client1Funds = client1Funds - amountToWithdraw;
			WindowLogger.log(TAG + " - " + clientID + "'s funds has been updated to " + client1Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToWithdraw
					+ " has been withdrawn from your account. Your Account Balance is " + client1Funds;
			break;
		case "Client2":
			client2Funds = client2Funds - amountToWithdraw;
			WindowLogger.log(TAG + " - " + clientID + "'s funds has been updated to " + client2Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToWithdraw
					+ " has been withdrawn from your account. Your Account Balance is " + client2Funds;
			break;
		case "Client3":
			client3Funds = client3Funds - amountToWithdraw;
			WindowLogger.log(TAG + " - " + clientID + "'s funds has been updated to " + client3Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToWithdraw
					+ " has been withdrawn from your account. Your Account Balance is " + client3Funds;
			break;
		default:
			WindowLogger.log(TAG + " - " + "Invalid Bank Account!!!", Process.SERVER);
			theOutput = "Invalid Bank Account!!!";
			break;
		}
		return theOutput;
	}

	private synchronized String transferFunds(String transferFromClientID, String transferToClientID,
			int amountToTransfer) {
		String theOutput = null;
		switch (transferFromClientID) {
		case "Client1":
			if (transferToClientID == null || transferToClientID.isEmpty()) {
				WindowLogger.log(TAG + " - " + "Cannot Find Transfer Client ID - " + transferToClientID,
						Process.SERVER);
				theOutput = "Transfer Account Not Found!!!";
			} else {
				if (transferToClientID.contains("Client2") || transferToClientID.contains("Client3")) {
					withdrawFunds(transferFromClientID, amountToTransfer);
					addFunds(transferToClientID, amountToTransfer);
					theOutput = "Transaction is complete. " + amountToTransfer + " has been transferred to "
							+ transferToClientID + "'s account. Your Account Balance is " + client1Funds;
					WindowLogger.log(TAG + " - " + "A transfer was done from " + transferFromClientID + " to "
							+ transferToClientID + " !!!", Process.SERVER);
				} else {
					WindowLogger.log(TAG + " - " + "Invalid transfer account!!!", Process.SERVER);
					theOutput = "Invalid Transfer Account!!!";
				}
			}
			break;
		case "Client2":
			if (transferToClientID == null || transferToClientID.isEmpty()) {
				WindowLogger.log(TAG + " - " + "Cannot Find Transfer Client ID - " + transferToClientID,
						Process.SERVER);
				theOutput = "Transfer Account Not Found!!!";
			} else {
				if (transferToClientID.contains("Client1") || transferToClientID.contains("Client3")) {
					withdrawFunds(transferFromClientID, amountToTransfer);
					addFunds(transferToClientID, amountToTransfer);
					theOutput = "Transaction is complete. " + amountToTransfer + " has been transferred to "
							+ transferToClientID + "'s account. Your Account Balance is " + client2Funds;
					WindowLogger.log(TAG + " - " + "A transfer was done from " + transferFromClientID + " to "
							+ transferToClientID + " !!!", Process.SERVER);
				} else {
					WindowLogger.log(TAG + " - " + "Invalid transfer account!!!", Process.SERVER);
					theOutput = "Invalid Transfer Account!!!";
				}
			}
			break;
		case "Client3":
			if (transferToClientID == null || transferToClientID.isEmpty()) {
				WindowLogger.log(TAG + " - " + "Cannot Find Transfer Client ID - " + transferToClientID,
						Process.SERVER);
				theOutput = "Transfer Account Not Found!!!";
			} else {
				if (transferToClientID.contains("Client1") || transferToClientID.contains("Client2")) {
					withdrawFunds(transferFromClientID, amountToTransfer);
					addFunds(transferToClientID, amountToTransfer);
					theOutput = "Transaction is complete. " + amountToTransfer + " has been transferred to "
							+ transferToClientID + "'s account. Your Account Balance is " + client3Funds;
					WindowLogger.log(TAG + " - " + "A transfer was done from " + transferFromClientID + " to "
							+ transferToClientID + " !!!", Process.SERVER);
				} else {
					WindowLogger.log(TAG + " - " + "Invalid transfer account!!!", Process.SERVER);
					theOutput = "Invalid Transfer Account!!!";
				}
			}
			break;
		default:
			WindowLogger.log("Invalid Bank Account!!!", Process.SERVER);
			theOutput = "Invalid Bank Account!!!";
			break;
		}
		return theOutput;
	}

	synchronized String triggerTransactionJob(String transactionID, String currentClientID, String transferToClientID,
			int amount) {
		String output;
		switch (transactionID) {
		case "Add_Funds":
			output = addFunds(currentClientID, amount);
			break;

		case "Withdraw_Funds":
			output = withdrawFunds(currentClientID, amount);
			break;

		case "Transfer_Funds":
			output = transferFunds(currentClientID, transferToClientID, amount);
			break;

		default:
			output = "Invalid Transaction!!!";
			break;
		}
		return output;
	}

	synchronized String getClientBalance(String ClientID) {
		String output;
		switch (ClientID) {
		case "Client1":
			output = "Your balance is " + client1Funds;
			break;
		case "Client2":
			output = "Your balance is " + client2Funds;
			break;
		case "Client3":
			output = "Your balance is " + client3Funds;
			break;
		default:
			output = "Invalid Client ID Provided";
			break;
		}
		return output;
	}

}
