import java.util.Random;

public class BankClientState {

    private String BankClientID;

    private static final int IDENTIFICATION_STATE = 0;
    private static final int BALANCE_CHECK_STATE = 1;
    private static final int CLIENT_TRANSACTION_REQUEST_STATE = 2;
    private static final int CLIENT_REQUEST_STATE = 3;
    private static final int TRANSFER_TO_CLIENT_ID_STATE = 4;
    private static final int TRANSACTION_AMOUNT_STATE = 5;
    private static final int FURTHER_TRANSACTION_STATE = 6;
    private static final int TRANSACTION_COMPLETE = 7;

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
                }
                break;
            case BALANCE_CHECK_STATE:
                if (MessageFromServer.contains("Your balance is ")) {
                    System.out.println(MessageFromServer);
                    output = responses[0];
                }
                break;
            case CLIENT_TRANSACTION_REQUEST_STATE:
                if (MessageFromServer.equalsIgnoreCase("What do you want?")){
                    output = responses[1];
                    currentState = CLIENT_REQUEST_STATE;
                }
                break;
            case CLIENT_REQUEST_STATE:
                if (MessageFromServer.equalsIgnoreCase("1")||MessageFromServer.equalsIgnoreCase("2")||MessageFromServer.equalsIgnoreCase("3")) {
                    try {
                        transactionID = Integer.parseInt(MessageFromServer);

                        if (transactionID != 3) {
                            output = responses[2];
                            currentState = TRANSACTION_AMOUNT_STATE;
                        } else {
                            output = responses[3];
                            currentState = TRANSFER_TO_CLIENT_ID_STATE;
                        }
                    } catch (NumberFormatException e) {
                        output = "Invalid Request";
                    }


                }
                break;
            case TRANSACTION_AMOUNT_STATE:
                if (MessageFromServer != null) {
                    try {
                        transactionAmount = Integer.parseInt(MessageFromServer);
                        transaction();
                        currentState = TRANSACTION_COMPLETE;

                    } catch (NumberFormatException e) {
                        output = "Invalid Request";
                    }
                }
                break;
            case TRANSFER_TO_CLIENT_ID_STATE:
                if (MessageFromServer.equalsIgnoreCase("Client1")||MessageFromServer.equalsIgnoreCase("Client2")||MessageFromServer.equalsIgnoreCase("Client3")) {
                    if (MessageFromServer.equalsIgnoreCase(ClientID)){
                        output = "You cannot send money to yourself!!!";
                    } else {
                        TransferToClientID = MessageFromServer;
                        output = responses[2];
                        currentState = TRANSACTION_AMOUNT_STATE;
                    }
                }
                break;
            case TRANSACTION_COMPLETE:
                if (MessageFromServer.equalsIgnoreCase("Thank You")){
                    output = responses[4];
                    currentState = FURTHER_TRANSACTION_STATE;
                }
                break;
            case FURTHER_TRANSACTION_STATE:
                if (MessageFromServer.equalsIgnoreCase("YES")){
                    output = balanceCheck();
                    currentState = CLIENT_TRANSACTION_REQUEST_STATE;
                } else {
                    currentState = IDENTIFICATION_STATE;
                    output = "BYE!!!";
                }
                break;
            default:
                output = "ERROR!!!";
                break;
        }
        return output;
    }

    private int RandomTransactionSelector(){
        return new Random().nextInt(3);
    }

    private int RandomTransactionAmountGenerator(){
        return new Random().nextInt();
    }

}

