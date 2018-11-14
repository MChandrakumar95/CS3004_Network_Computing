import java.util.Date;
import java.util.Random;

public class BankClientState {

    private String BankClientID;
    private int TransactionAmount = 0;
    private int TransactionID = 0;
    private String TranferClientID = null;

    private static final int IDENTIFICATION_STATE = 0;
    private static final int BALANCE_CHECK_STATE = 1;
    private static final int CLIENT_TRANSACTION_REQUEST_STATE = 2;
    private static final int CLIENT_REQUEST_STATE = 3;
    private static final int TRANSFER_TO_CLIENT_ID_STATE = 4;
    private static final int TRANSACTION_AMOUNT_STATE = 5;
    private static final int FURTHER_TRANSACTION_STATE = 6;
    private static final int TRANSACTION_COMPLETE = 7;
    private static final int TERMINATION_STATE = 8;

    private int currentState = IDENTIFICATION_STATE;

    private String[] responses = {"Thank you for the Balance", "Thank you"};



    public BankClientState(String BankClientID) {
        this.BankClientID = BankClientID;
    }

    public String processInput(String MessageFromServer) {
        String output = null;
        switch (currentState) {
            case IDENTIFICATION_STATE:
                if (MessageFromServer.equalsIgnoreCase("Identify Yourself")){
                    output = BankClientID;
                    currentState = BALANCE_CHECK_STATE;
                } else {
                    output = "ERROR";
                }
                break;
            case BALANCE_CHECK_STATE:
                if (MessageFromServer.contains("Your balance is ")) {
                    System.out.println(MessageFromServer);
                    output = responses[0];
                    currentState = CLIENT_TRANSACTION_REQUEST_STATE;
                } else {
                    output = "ERROR";
                }
                break;
            case CLIENT_TRANSACTION_REQUEST_STATE:
                if (MessageFromServer.equalsIgnoreCase("What do you want?")){
                    transactionDetails();
                    if (TransactionID == 3) {
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
                    if (MessageFromServer.contentEquals("Specified Amount has been added")
                            ||MessageFromServer.contentEquals("Specified Amount has been Transferred to "
                            + TranferClientID)
                            ||MessageFromServer.contentEquals("Specified Amount has been Withdrawn")){
                        output = "Thank you";
                        currentState = FURTHER_TRANSACTION_STATE;
                    } else {
                        output = "ERROR";
                    }
                }
                break;
            case TRANSFER_TO_CLIENT_ID_STATE:
                if (MessageFromServer.equalsIgnoreCase("Who do you want to transfer to?")) {
                    output = String.valueOf(TranferClientID);
                    currentState = CLIENT_REQUEST_STATE;
                }
                break;

            case TRANSACTION_COMPLETE:
                if (MessageFromServer.equalsIgnoreCase("Thank You")){
                    output = responses[4];
                    currentState = FURTHER_TRANSACTION_STATE;
                }
                break;

                case FURTHER_TRANSACTION_STATE:
                if (MessageFromServer.equalsIgnoreCase("Is there anything else?")) {
                    output = RandomConfirmer();
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

    private void transactionDetails() {
        TransactionID = RandomTransactionSelector();
        System.out.println("Transaction is " + getTransaction(TransactionID));
        if (TransactionID == 3){
            TranferClientID = RandomClientIDSelector();
        }
        TransactionAmount = RandomTransactionAmountGenerator();
        System.out.println("Transaction Amount is " + TransactionAmount);
    }

    private String getTransaction(int transactionID) {
        String transactionText;
        switch (transactionID){
            case 0:
                transactionText = "Add Funds";
                break;
            case 1:
                transactionText = "Withdrawal of Funds";
                break;
            case 2:
                transactionText = "Transfer Funds";
                break;
            default:
                transactionText = "ERROR";
                break;
        }
        return transactionText;

    }

    private int RandomTransactionSelector(){
        return new Random(new Date().getTime()).nextInt(3);
    }

    private String RandomClientIDSelector(){
        String transferClientID;
        switch (new Random(new Date().getTime()).nextInt(3)){
            case 0:
                transferClientID = "Client1";
                break;
            case 1:
                transferClientID = "Client2";
                break;
            case 2:
                transferClientID = "Client3";
                break;
            default:
                transferClientID = "Client1";
                break;
        }
        if (transferClientID.contentEquals(BankClientID)){
            RandomClientIDSelector();
        }
        return transferClientID;
    }

    private int RandomTransactionAmountGenerator(){
        return Math.abs(new Random(new Date().getTime()).nextInt());
    }

    private String RandomConfirmer(){
        String confirmation = null;
        int yesNo = new Random(new Date().getTime()).nextInt(2);
        switch (yesNo){
            case 0:
                confirmation = "NO";
                break;
            case 1:
                confirmation = "YES";
                break;
        }
        return confirmation;
    }

}

