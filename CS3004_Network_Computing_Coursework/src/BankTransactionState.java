import java.net.*;
import java.io.*;

public class BankTransactionState {

	private static final int IDENTIFICATION_STATE = 0;
	private static final int BALANCE_CHECK_STATE = 1;
	private static final int CLIENT_TRANSACTION_REQUEST_STATE = 2;
	private static final int CLIENT_REQUEST_STATE = 3;
	private static final int TRANSFER_TO_CLIENT_ID_STATE = 4;
	private static final int TRANSACTION_AMOUNT_STATE = 5;
	private static final int FURTHER_TRANSACTION_STATE = 6;
	private static final int TRANSACTION_COMPLETE = 7;


	private int state = IDENTIFICATION_STATE;

	private String ClientID = null;
	private String TransferToClientID = null;
	private int transactionID;
	private int transactionAmount;

	private String[] responses = {"Identify Yourself", "What do you want?", "What is the Transaction Amount?",
			"Who do you want to transfer to?", "Is there anything else?", "Specified Amount has been added",
			"Specified Amount has been Withdrawn", "Specified Amount has been Transferred"};

	private Funds bankFundsCurrentStateObject;
	private PrintWriter out;

	public BankTransactionState(Funds BankFunds, PrintWriter OutputStream){
		this.bankFundsCurrentStateObject = BankFunds;
		this.out = OutputStream;
	}



	public String processInput(String InputData) {
		String output = null;
		switch (state) {
			case IDENTIFICATION_STATE:
				output = responses[0];
				state = BALANCE_CHECK_STATE;
				break;
			case BALANCE_CHECK_STATE:
				if (InputData.equalsIgnoreCase("Client1")||InputData.equalsIgnoreCase("Client2")||InputData.equalsIgnoreCase("Client3")) {
					ClientID = InputData;
					output = balanceCheck();
				}
				break;
			case CLIENT_TRANSACTION_REQUEST_STATE:
				if (InputData.equalsIgnoreCase("Thank you for the Balance")){
					output = responses[1];
					state = CLIENT_REQUEST_STATE;
				}
				break;
			case CLIENT_REQUEST_STATE:
				if (InputData.equalsIgnoreCase("1")||InputData.equalsIgnoreCase("2")||InputData.equalsIgnoreCase("3")) {
					try {
						transactionID = Integer.parseInt(InputData);

						if (transactionID != 3) {
							output = responses[2];
							state = TRANSACTION_AMOUNT_STATE;
						} else {
							output = responses[3];
							state = TRANSFER_TO_CLIENT_ID_STATE;
						}
					} catch (NumberFormatException e) {
						output = "Invalid Request";
					}


				}
				break;
			case TRANSACTION_AMOUNT_STATE:
				if (InputData != null) {
					try {
						transactionAmount = Integer.parseInt(InputData);
						transaction();
						state = TRANSACTION_COMPLETE;

					} catch (NumberFormatException e) {
						output = "Invalid Request";
					}
				}
				break;
			case TRANSFER_TO_CLIENT_ID_STATE:
				if (InputData.equalsIgnoreCase("Client1")||InputData.equalsIgnoreCase("Client2")||InputData.equalsIgnoreCase("Client3")) {
					if (InputData.equalsIgnoreCase(ClientID)){
						output = "You cannot send money to yourself!!!";
					} else {
						TransferToClientID = InputData;
						output = responses[2];
						state = TRANSACTION_AMOUNT_STATE;
					}
				}
				break;
			case TRANSACTION_COMPLETE:
				if (InputData.equalsIgnoreCase("Thank You")){
					output = responses[4];
					state = FURTHER_TRANSACTION_STATE;
				}
				break;
			case FURTHER_TRANSACTION_STATE:
				if (InputData.equalsIgnoreCase("YES")){
					output = balanceCheck();
					state = CLIENT_TRANSACTION_REQUEST_STATE;
				} else {
					state = IDENTIFICATION_STATE;
					output = "BYE!!!";
				}
				break;
			default:
				output = "ERROR!!!";
				break;
		}
		return output;
	}

	private String balanceCheck() {
		String output = null;
		try {
			bankFundsCurrentStateObject.acquireLock();
			output = bankFundsCurrentStateObject.getClientBalance(ClientID);
			bankFundsCurrentStateObject.releaseLock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		state = CLIENT_TRANSACTION_REQUEST_STATE;
		return output;
	}

	private void transaction(){
		try {
			// Get a lock first
			bankFundsCurrentStateObject.acquireLock();
			String outputLine = bankFundsCurrentStateObject.triggerTransactionJob(transactionID, ClientID, TransferToClientID, transactionAmount);
			out.println(outputLine);
			bankFundsCurrentStateObject.releaseLock();
			System.out.println(outputLine);
		}
		catch(InterruptedException e) {
			System.err.println("Failed to get lock when reading:"+e);
		}
	}
}
