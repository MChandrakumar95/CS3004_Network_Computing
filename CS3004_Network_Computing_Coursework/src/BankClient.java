import java.util.Random;

/**
 * @author 1442877
 * <p>
 * This is the Bank client class. This class will generate the information required for a transaction. It will also trigger the client given current ClientID.
 */
class BankClient {

    private String BankClientID;

    public String getBankClientID() {
        return BankClientID;
    }

    public void setBankClientID(String bankClientID) {
        BankClientID = bankClientID;
    }

    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String transactionID) {
        TransactionID = transactionID;
    }

    public int getTransactionAmount() {
        return TransactionAmount;
    }

    public void setTransactionAmount(int transactionAmount) {
        TransactionAmount = transactionAmount;
    }

    public String getTransferClientID() {
        return TransferClientID;
    }

    public void setTransferClientID(String transferClientID) {
        TransferClientID = transferClientID;
    }

    private String TransactionID;
    private int TransactionAmount;
    private String TransferClientID;

    private static final String TAG = "BankClient_Class";

    // BankClient Constructor
    BankClient(String BankClientID) {
        this.BankClientID = BankClientID;
        randomTransactionDetailsGenerator();
    }

    public void triggerBankClientThread() {
        new BankClientThread(BankClientID, TransactionID, TransactionAmount, TransferClientID).start();
    }

    private synchronized String RandomTransactionSelector() {
    	String transactionID;
//        int temptransactionID = new Random(System.nanoTime()).nextInt(3);
    	int temptransactionID = 0;
        switch (temptransactionID) {
            case 0:
            	transactionID = "Add_Funds";
                break;
            case 1:
            	transactionID = "Withdraw_Funds";
                break;
            case 2:
                transactionID = "Transfer_Funds";
                break;
            default:
            	transactionID = "Add_Funds";
                break;
        }
        ColourfulOutputs.log(TAG + " - " + "Random Transaction selected: " + transactionID, Process.CLIENT);
        return transactionID;
    }

    private synchronized String RandomClientIDSelector(long Seed) {
        String transferClientID;
        int clientID = new Random(Seed).nextInt(3);
        ColourfulOutputs.log(TAG + " - " + "Temp Transfer ClientID: " + clientID, Process.CLIENT);
        switch (clientID) {
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
        return transferClientID;
    }

    private synchronized String TransferClientIDGenerator() {
        long timestamp = System.nanoTime();
        String transferClientID = RandomClientIDSelector(timestamp);
        long count = timestamp;
        while (transferClientID.equals(BankClientID)) {
            count++;
            transferClientID = RandomClientIDSelector(count);
            if (transferClientID.contains(BankClientID)) {
            } else {
                break;
            }
        }
        return transferClientID;
    }

    private synchronized int RandomTransactionAmountGenerator() {
        return Math.abs(new Random(System.nanoTime()).nextInt(500));
    }

    public void randomTransactionDetailsGenerator() {
        TransactionID = RandomTransactionSelector();
        ColourfulOutputs.log(TAG + " - " + "Transaction is " + getTransaction(TransactionID), Process.CLIENT);
        if (TransactionID.contains("Transfer_Funds")) {
            TransferClientID = TransferClientIDGenerator();
        }
        TransactionAmount = RandomTransactionAmountGenerator();
        displayTransactionAmountMessage();
    }

    private void forceAddFunds() {
        TransactionID = "Add_Funds";
        TransactionAmount = RandomTransactionAmountGenerator();
        displayTransactionAmountMessage();
    }

    private void forceWithdrawFunds() {
        TransactionID = "Withdraw_Funds";
        TransactionAmount = RandomTransactionAmountGenerator();
        displayTransactionAmountMessage();
    }

    private void forceTransferFunds() {
        TransactionID = "Transfer_Funds";
        TransferClientID = TransferClientIDGenerator();
        TransactionAmount = RandomTransactionAmountGenerator();
        displayTransactionAmountMessage();
    }

    private void displayTransactionAmountMessage() {
        ColourfulOutputs.log(TAG + " - " + "Transaction Amount is " + TransactionAmount, Process.CLIENT);
    }

    private String getTransaction(String transactionID) {
        String transactionText;
        switch (transactionID) {
            case "Add_Funds":
                transactionText = "Add Funds";
                break;
            case "Withdraw_Funds":
                transactionText = "Withdrawal of Funds";
                break;
            case "Transfer_Funds":
                transactionText = "Transfer Funds";
                break;
            default:
                transactionText = "ERROR";
                break;
        }
        return transactionText;

    }

}