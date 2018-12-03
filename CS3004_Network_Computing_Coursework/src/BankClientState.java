import java.util.Random;

/**
 * @author 1442877
 * 
 *         This class will provide the client with the required output to sent
 *         to the server. The processInput method will require the input message
 *         from the server. Depending on the state and the message from the
 *         server an appropriate output message is produced. In order to
 *         automate a bank client user a randomiser is implemented to randomly
 *         select a transaction, amount and transfer client ID.
 * 
 */

class BankClientState {

	private static final String TAG = "BankClientState_Class";

	private String BankClientID;
	private int TransactionAmount = 0;
	private String TransactionID = "Add_Funds";
	private String TransferClientID = null;

	// The different client states.
	private static final int IDENTIFICATION_STATE = 0;
	private static final int BALANCE_CHECK_STATE = 1;
	private static final int CLIENT_TRANSACTION_REQUEST_STATE = 2;
	private static final int CLIENT_REQUEST_STATE = 3;
	private static final int TRANSFER_TO_CLIENT_ID_STATE = 4;
	private static final int TRANSACTION_AMOUNT_STATE = 5;
	private static final int FURTHER_TRANSACTION_STATE = 6;
	private static final int TERMINATION_STATE = 7;

	private int currentState = IDENTIFICATION_STATE;

	BankClientState(String BankClientID, String TransactionID, int TransactionAmount, String TransferClientID) {
		this.BankClientID = BankClientID;
		this.TransactionID = TransactionID;
		this.TransactionAmount = TransactionAmount;
		this.TransferClientID = TransferClientID;
	}

	String processInput(String MessageFromServer) {
		String output = null;
		switch (currentState) {
		case IDENTIFICATION_STATE:
			if (MessageFromServer.equalsIgnoreCase("Identify Yourself")) {
				output = BankClientID;
				currentState = BALANCE_CHECK_STATE;
			} else {
				output = "ERROR";
			}
			break;
		case BALANCE_CHECK_STATE:
			if (MessageFromServer.contains("Your balance is ")) {
				WindowLogger.log(TAG + " - " + MessageFromServer, Process.CLIENT);
				output = "Thank you for the Balance";
				currentState = CLIENT_TRANSACTION_REQUEST_STATE;
			} else {
				output = "ERROR";
			}
			break;
		case CLIENT_TRANSACTION_REQUEST_STATE:
			if (MessageFromServer.equalsIgnoreCase("What do you want?")) {
				if (TransactionID.contains("Transfer_Funds")) {
					output = String.valueOf(TransactionID);
					currentState = TRANSFER_TO_CLIENT_ID_STATE;
				} else {
					output = String.valueOf(TransactionID);
					currentState = CLIENT_REQUEST_STATE;
				}

			} else {
				output = "ERROR";
			}
			break;
		case CLIENT_REQUEST_STATE:
			if (MessageFromServer.equalsIgnoreCase("What is the Transaction Amount?")) {
				output = String.valueOf(TransactionAmount);
				currentState = TRANSACTION_AMOUNT_STATE;
			} else {
				output = "ERROR";
			}
			break;
		case TRANSACTION_AMOUNT_STATE:
			if (MessageFromServer != null) {
				if (MessageFromServer.contains("Transaction is complete.")) {
					output = "Thank you";
					currentState = FURTHER_TRANSACTION_STATE;
				} else {
					output = "ERROR";
				}
			}
			break;
		case TRANSFER_TO_CLIENT_ID_STATE:
			if (MessageFromServer.equalsIgnoreCase("Who do you want to transfer to?")) {
				output = String.valueOf(TransferClientID);
				currentState = CLIENT_REQUEST_STATE;
			}
			break;

		case FURTHER_TRANSACTION_STATE:
			if (MessageFromServer.equalsIgnoreCase("Is there anything else?")) {
				output = RandomConfirm();
				if (output.contentEquals("YES")) {
					currentState = BALANCE_CHECK_STATE;
				} else if (output.contentEquals("NO")) {
					currentState = TERMINATION_STATE;
				} else {
					output = "BYE!!!";
				}
			}
			break;

		case TERMINATION_STATE:
			if (MessageFromServer.equalsIgnoreCase("BYE!!!")) {
				output = "BYE!!!";
				currentState = IDENTIFICATION_STATE;
			}
			break;

		default:
			output = "ERROR!!!";
			break;
		}
		return output;
	}

	private synchronized String RandomConfirm() {
		String confirmation = null;
		int yesNo = new Random(System.nanoTime()).nextInt(2);
		switch (yesNo) {
		case 0:
			confirmation = "NO";
			break;
		case 1:
			confirmation = "YES";
			BankClient bankClient = new BankClient(BankClientID);
			bankClient.randomTransactionDetailsGenerator();
			TransactionAmount = bankClient.getTransactionAmount();
			TransactionID = bankClient.getTransactionID();
			TransferClientID = bankClient.getTransferClientID();
			break;
		}
		return confirmation;
	}

}
