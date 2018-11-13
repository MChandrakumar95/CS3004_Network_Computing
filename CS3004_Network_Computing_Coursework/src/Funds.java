public class Funds{

	private double client1Funds = 100;
	private double client2Funds = 100;
	private double client3Funds = 100;
	private boolean accessing = false; // true a thread has a lock, false otherwise
	private int threadsWaiting = 0; // number of waiting writers


	//Attempt to acquire a lock

	public synchronized void acquireLock() throws InterruptedException{
		Thread me = Thread.currentThread(); // get a ref to the current thread
		System.out.println(me.getName()+" is attempting to acquire a lock!");	
		++threadsWaiting;
		while (accessing) {  // while someone else is accessing or threadsWaiting > 0
			System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
			//wait for the lock to be released - see releaseLock() below
			wait();
		}
		// nobody has got a lock so get one
		--threadsWaiting;
		accessing = true;
		System.out.println(me.getName()+" got a lock!"); 
	}

	// Releases a lock to when a thread is finished

	public synchronized void releaseLock() {
		//release the lock and tell everyone
		accessing = false;
		notifyAll();
		Thread me = Thread.currentThread(); // get a ref to the current thread
		System.out.println(me.getName()+" released a lock!");
	}

	private synchronized String addFunds(String clientID, double amountToAdd){
		String theOutput = null;
		switch (clientID) {
		case "Client1":
			client1Funds = client1Funds + amountToAdd;
			System.out.println(clientID + "'s funds has been updated to " + client1Funds);
			theOutput = "Transaction is complete. " + amountToAdd + " has been added to your account. \nYour Account Balance is " + client1Funds;
			break;
		case "Client2":
			client2Funds = client2Funds + amountToAdd;
			System.out.println(clientID + "'s funds has been updated to " + client2Funds);
			theOutput = "Transaction is complete. " + amountToAdd + " has been added to your account. \nYour Account Balance is " + client2Funds;
			break;
		case "Client3":
			client3Funds = client3Funds + amountToAdd;
			System.out.println(clientID + "'s funds has been updated to " + client3Funds);
			theOutput = "Transaction is complete. " + amountToAdd + " has been added to your account. \nYour Account Balance is " + client3Funds;
			break;
		default:
			System.out.println("Invalid Bank Account!!!");
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
			System.out.println(clientID + "'s funds has been updated to " + client1Funds);
			theOutput = "Transaction is complete. " + amountToWithdraw + " has been added to your account. \nYour Account Balance is " + client1Funds;
			break;
		case "Client2":
			client2Funds = client2Funds - amountToWithdraw;
			System.out.println(clientID + "'s funds has been updated to " + client2Funds);
			theOutput = "Transaction is complete. " + amountToWithdraw + " has been added to your account. \nYour Account Balance is " + client2Funds;
			break;
		case "Client3":
			client3Funds = client3Funds - amountToWithdraw;
			System.out.println(clientID + "'s funds has been updated to " + client3Funds);
			theOutput = "Transaction is complete. " + amountToWithdraw + " has been added to your account. \nYour Account Balance is " + client3Funds;
			break;
		default:
			System.out.println("Invalid Bank Account!!!");
			theOutput = "Invalid Bank Account!!!";
			break;
		}
		return theOutput;
	}
	
	private synchronized String transferFunds(String transferFromClientID, String tranferToClientID, double amountToTransfer){
		String theOutput = null;
		switch (transferFromClientID) {
		case "Client1":
			withdrawFunds(transferFromClientID, amountToTransfer);
			if (tranferToClientID.equalsIgnoreCase("Client2") || tranferToClientID.equalsIgnoreCase("Client3")) {
				addFunds(tranferToClientID, amountToTransfer);
				theOutput = "Transaction is complete. " + amountToTransfer + " has been transfered to " + tranferToClientID + "'s account. \nYour Account Balance is " + client1Funds;
			} else {
				System.out.println("Invalid tranfer account!!!");
				theOutput = "Invalid Tranfer Account!!!";
			}
			System.out.println("A transfer was done from " + transferFromClientID + " to " + tranferToClientID +" !!!");
			break;
		case "Client2":
			withdrawFunds(transferFromClientID, amountToTransfer);
			if (tranferToClientID.equalsIgnoreCase("Client1") || tranferToClientID.equalsIgnoreCase("Client3")) {
				addFunds(tranferToClientID, amountToTransfer);
				theOutput = "Transaction is complete. " + amountToTransfer + " has been transfered to " + tranferToClientID + "'s account. \nYour Account Balance is " + client2Funds;
			} else {
				System.out.println("Invalid tranfer account!!!");
				theOutput = "Invalid Tranfer Account!!!";
			}
			System.out.println("A transfer was done from " + transferFromClientID + " to " + tranferToClientID +" !!!");
			break;
		case "Client3":
			withdrawFunds(transferFromClientID, amountToTransfer);
			if (tranferToClientID.equalsIgnoreCase("Client1") || tranferToClientID.equalsIgnoreCase("Client2")) {
				addFunds(tranferToClientID, amountToTransfer);
				theOutput = "Transaction is complete. " + amountToTransfer + " has been transfered to " + tranferToClientID + "'s account. \nYour Account Balance is " + client3Funds;
			} else {
				System.out.println("Invalid tranfer account!!!");
				theOutput = "Invalid Tranfer Account!!!";
			}
			System.out.println("A transfer was done from " + transferFromClientID + " to " + tranferToClientID +" !!!");
			break;
		default:
			System.out.println("Invalid Bank Account!!!");
			theOutput = "Invalid Bank Account!!!";
			break;
		}
		return theOutput;
	}
	
	public synchronized String triggerTransactionJob(int transactionID, String currectClientID, double amount){
		String output = null;
		switch (transactionID) {
		case 1:
			output = addFunds(currectClientID, amount);
			break;

		case 2:
			output = withdrawFunds(currectClientID, amount);
			break;

		default:
			output = "Invalid Transaction!!!";
			break;
		}
		return output;
	}
	
	public synchronized String triggerTransactionJob(int transactionID, String currectClientID, String transferToClientID, double amount){
		String output = null;
		switch (transactionID) {
		case 1:
			output = addFunds(currectClientID, amount);
			break;

		case 2:
			output = withdrawFunds(currectClientID, amount);
			break;

		case 3:
			output = transferFunds(currectClientID, transferToClientID, amount);
			break;
			
		default:
			output = "Invalid Transaction!!!";
			break;
		}
		return output;
	}
	
	public synchronized String getClientBalance(String ClientID){
		String output = null;
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
			break;
		}
		return output;
	}
	
}
