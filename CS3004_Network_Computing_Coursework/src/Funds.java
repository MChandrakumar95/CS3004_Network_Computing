class Funds{

	private static ColourfulOutputs colourfulOutputs = new ColourfulOutputs(ValidColours.TURQUOISE);
	private static final String TAG = "Funds Class";

	private double client1Funds = 100;
	private double client2Funds = 100;
	private double client3Funds = 100;
	private boolean accessing = false; // true a thread has a lock, false otherwise
	private int threadsWaiting = 0; // number of waiting writers


	//Attempt to acquire a lock

	synchronized void acquireLock() throws InterruptedException{
		Thread me = Thread.currentThread(); // get a ref to the current thread
		colourfulOutputs.outputColourfulText(TAG + " - " + me.getName() + " is attempting to acquire a lock!");
		++threadsWaiting;
		while (accessing) {  // while someone else is accessing or threadsWaiting > 0
			colourfulOutputs.outputColourfulText(TAG + " - " + me.getName()+" waiting to get a lock as someone else is accessing...");
			//wait for the lock to be released - see releaseLock() below
			wait();
		}
		// nobody has got a lock so get one
		--threadsWaiting;
		accessing = true;
		colourfulOutputs.outputColourfulText(TAG + " - " + me.getName()+" got a lock!");
	}

	// Releases a lock to when a thread is finished

	synchronized void releaseLock() {
		//release the lock and tell everyone
		accessing = false;
		notifyAll();
		Thread me = Thread.currentThread(); // get a ref to the current thread
		colourfulOutputs.outputColourfulText(TAG +" - " + me.getName()+" released a lock!");
	}

	private synchronized String addFunds(String clientID, double amountToAdd){
		String theOutput;
		switch (clientID) {
			case "Client1":
				client1Funds = client1Funds + amountToAdd;
				colourfulOutputs.outputColourfulText(TAG +" - " + clientID + "'s funds has been updated to " + client1Funds);
				theOutput = "Transaction is complete. " + amountToAdd + " has been added to your account. Your Account Balance is " + client1Funds;
				break;
			case "Client2":
				client2Funds = client2Funds + amountToAdd;
				colourfulOutputs.outputColourfulText(TAG +" - " + clientID + "'s funds has been updated to " + client2Funds);
				theOutput = "Transaction is complete. " + amountToAdd + " has been added to your account. Your Account Balance is " + client2Funds;
				break;
			case "Client3":
				client3Funds = client3Funds + amountToAdd;
				colourfulOutputs.outputColourfulText(TAG +" - " + clientID + "'s funds has been updated to " + client3Funds);
				theOutput = "Transaction is complete. " + amountToAdd + " has been added to your account. Your Account Balance is " + client3Funds;
				break;
			default:
				System.err.println("Invalid Bank Account!!!");
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
				colourfulOutputs.outputColourfulText(TAG +" - " + clientID + "'s funds has been updated to " + client1Funds);
				theOutput = "Transaction is complete. " + amountToWithdraw + " has been added to your account. Your Account Balance is " + client1Funds;
				break;
			case "Client2":
				client2Funds = client2Funds - amountToWithdraw;
				colourfulOutputs.outputColourfulText(TAG +" - " + clientID + "'s funds has been updated to " + client2Funds);
				theOutput = "Transaction is complete. " + amountToWithdraw + " has been added to your account. Your Account Balance is " + client2Funds;
				break;
			case "Client3":
				client3Funds = client3Funds - amountToWithdraw;
				colourfulOutputs.outputColourfulText(TAG +" - " + clientID + "'s funds has been updated to " + client3Funds);
				theOutput = "Transaction is complete. " + amountToWithdraw + " has been added to your account. Your Account Balance is " + client3Funds;
				break;
			default:
				System.err.println(TAG +" - " + "Invalid Bank Account!!!");
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
					System.err.println(TAG +" - " + "Invalid transfer account!!!");
					theOutput = "Invalid Transfer Account!!!";
				}
				colourfulOutputs.outputColourfulText(TAG +" - " + "A transfer was done from " + transferFromClientID + " to " + transferToClientID +" !!!");
				break;
			case "Client2":
				withdrawFunds(transferFromClientID, amountToTransfer);
				if (transferToClientID.contains("Client1") || transferToClientID.contains("Client3")) {
					addFunds(transferToClientID, amountToTransfer);
					theOutput = "Transaction is complete. " + amountToTransfer + " has been transferred to " + transferToClientID + "'s account. Your Account Balance is " + client2Funds;
				} else {
					System.err.println(TAG +" - " + "Invalid transfer account!!!");
					theOutput = "Invalid Transfer Account!!!";
				}
				colourfulOutputs.outputColourfulText(TAG +" - " + "A transfer was done from " + transferFromClientID + " to " + transferToClientID +" !!!");
				break;
			case "Client3":
				withdrawFunds(transferFromClientID, amountToTransfer);
				if (transferToClientID.contains("Client1") || transferToClientID.contains("Client2")) {
					addFunds(transferToClientID, amountToTransfer);
					theOutput = "Transaction is complete. " + amountToTransfer + " has been transferred to " + transferToClientID + "'s account. Your Account Balance is " + client3Funds;
				} else {
					System.err.println(TAG +" - " + "Invalid transfer account!!!");
					theOutput = "Invalid Transfer Account!!!";
				}
				colourfulOutputs.outputColourfulText(TAG +" - " + "A transfer was done from " + transferFromClientID + " to " + transferToClientID +" !!!");
				break;
			default:
				System.err.println("Invalid Bank Account!!!");
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
