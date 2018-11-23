class Funds{

	private static final String TAG = "Funds_Class";

	private double client1Funds = 100;
	private double client2Funds = 100;
	private double client3Funds = 100;
	private boolean accessing = false; // true a thread has a lock, false otherwise
	private int threadsWaiting = 0; // number of waiting writers


	//Attempt to acquire a lock

	synchronized void acquireLock() throws InterruptedException{
		String currentThread = Thread.currentThread().getName(); // Get the name of the current thread.
		ColourfulOutputs.log(TAG + " - " + currentThread + " is attempting to acquire a lock!", Process.SERVER);
		++threadsWaiting;
		while (accessing) {  // while someone else is accessing or threadsWaiting > 0
			ColourfulOutputs.log(TAG + " - " + currentThread +" waiting to get a lock as someone else is accessing...", Process.SERVER);
			//wait for the lock to be released - see releaseLock() below
			wait();
		}
		// nobody has got a lock so get one
		--threadsWaiting;
		accessing = true;
		ColourfulOutputs.log(TAG + " - " + currentThread + " got a lock!", Process.SERVER);
	}

	// Releases a lock to when a thread is finished

	synchronized void releaseLock() {
		//release the lock and tell everyone
		accessing = false;
		notifyAll();
		Thread me = Thread.currentThread(); // get a ref to the current thread
		ColourfulOutputs.log(TAG +" - " + me.getName()+" released a lock!", Process.SERVER);
	}

	private synchronized String addFunds(String clientID, double amountToAdd){
		String theOutput;
		switch (clientID) {
		case "Client1":
			client1Funds = client1Funds + amountToAdd;
			ColourfulOutputs.log(TAG +" - " + clientID + "'s funds has been updated to " + client1Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToAdd + " has been added to your account. Your Account Balance is " + client1Funds;
			break;
		case "Client2":
			client2Funds = client2Funds + amountToAdd;
			ColourfulOutputs.log(TAG +" - " + clientID + "'s funds has been updated to " + client2Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToAdd + " has been added to your account. Your Account Balance is " + client2Funds;
			break;
		case "Client3":
			client3Funds = client3Funds + amountToAdd;
			ColourfulOutputs.log(TAG +" - " + clientID + "'s funds has been updated to " + client3Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToAdd + " has been added to your account. Your Account Balance is " + client3Funds;
			break;
		default:
			ColourfulOutputs.log("Invalid Bank Account!!!", Process.SERVER);
			theOutput = "Invalid Bank Account!!!";
			break;
		}
		return theOutput;
	}

	private synchronized String withdrawFunds(String clientID, double amountToWithdraw){
		String theOutput = null;
		switch (clientID) {
		case "Client1":
			client1Funds = client1Funds - amountToWithdraw;
			ColourfulOutputs.log(TAG +" - " + clientID + "'s funds has been updated to " + client1Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToWithdraw + " has been added to your account. Your Account Balance is " + client1Funds;
			break;
		case "Client2":
			client2Funds = client2Funds - amountToWithdraw;
			ColourfulOutputs.log(TAG +" - " + clientID + "'s funds has been updated to " + client2Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToWithdraw + " has been added to your account. Your Account Balance is " + client2Funds;
			break;
		case "Client3":
			client3Funds = client3Funds - amountToWithdraw;
			ColourfulOutputs.log(TAG +" - " + clientID + "'s funds has been updated to " + client3Funds, Process.SERVER);
			theOutput = "Transaction is complete. " + amountToWithdraw + " has been added to your account. Your Account Balance is " + client3Funds;
			break;
		default:
			ColourfulOutputs.log(TAG +" - " + "Invalid Bank Account!!!", Process.SERVER);
			theOutput = "Invalid Bank Account!!!";
			break;
		}
		return theOutput;
	}

	private synchronized String transferFunds(String transferFromClientID, String transferToClientID, double amountToTransfer){
		String theOutput = null;
		switch (transferFromClientID) {
		case "Client1":
			withdrawFunds(transferFromClientID, amountToTransfer);
			if (transferToClientID.contains("Client2") || transferToClientID.contains("Client3")) {
				addFunds(transferToClientID, amountToTransfer);
				theOutput = "Transaction is complete. " + amountToTransfer + " has been transferred to " + transferToClientID + "'s account. Your Account Balance is " + client1Funds;
			} else {
				ColourfulOutputs.log(TAG +" - " + "Invalid transfer account!!!", Process.SERVER);
				theOutput = "Invalid Transfer Account!!!";
			}
			ColourfulOutputs.log(TAG +" - " + "A transfer was done from " + transferFromClientID + " to " + transferToClientID +" !!!", Process.SERVER);
			break;
		case "Client2":
			withdrawFunds(transferFromClientID, amountToTransfer);
			if (transferToClientID.contains("Client1") || transferToClientID.contains("Client3")) {
				addFunds(transferToClientID, amountToTransfer);
				theOutput = "Transaction is complete. " + amountToTransfer + " has been transferred to " + transferToClientID + "'s account. Your Account Balance is " + client2Funds;
			} else {
				ColourfulOutputs.log(TAG +" - " + "Invalid transfer account!!!", Process.SERVER);
				theOutput = "Invalid Transfer Account!!!";
			}
			ColourfulOutputs.log(TAG +" - " + "A transfer was done from " + transferFromClientID + " to " + transferToClientID +" !!!", Process.SERVER);
			break;
		case "Client3":
			withdrawFunds(transferFromClientID, amountToTransfer);
			if (transferToClientID.contains("Client1") || transferToClientID.contains("Client2")) {
				addFunds(transferToClientID, amountToTransfer);
				theOutput = "Transaction is complete. " + amountToTransfer + " has been transferred to " + transferToClientID + "'s account. Your Account Balance is " + client3Funds;
			} else {
				ColourfulOutputs.log(TAG +" - " + "Invalid transfer account!!!", Process.SERVER);
				theOutput = "Invalid Transfer Account!!!";
			}
			ColourfulOutputs.log(TAG +" - " + "A transfer was done from " + transferFromClientID + " to " + transferToClientID +" !!!", Process.SERVER);
			break;
		default:
			ColourfulOutputs.log("Invalid Bank Account!!!", Process.SERVER);
			theOutput = "Invalid Bank Account!!!";
			break;
		}
		return theOutput;
	}

	synchronized String triggerTransactionJob(int transactionID, String currentClientID, String transferToClientID, double amount){
		String output;
		switch (transactionID) {
		case 0:
			output = addFunds(currentClientID, amount);
			break;

		case 1:
			output = withdrawFunds(currentClientID, amount);
			break;

		case 2:
			output = transferFunds(currentClientID, transferToClientID, amount);
			break;

		default:
			output = "Invalid Transaction!!!";
			break;
		}
		return output;
	}

	synchronized String getClientBalance(String ClientID){
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
