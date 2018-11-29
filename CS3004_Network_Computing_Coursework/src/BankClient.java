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

    public int getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(int transactionID) {
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

    private int TransactionID;
    private int TransactionAmount;
    private String TransferClientID;

    private static final String TAG = "BankClient_Class";

    // BankClient Constructor
    BankClient(String BankClientID) {
        this.BankClientID = BankClientID;
        randomTransactionDetailsGenerator();
    }

    public void triggerBankClientThread() {
        new BankClientThread(BankClientID, 0, TransactionAmount, TransferClientID).start();
    }

    private synchronized int RandomTransactionSelector() {
        return new Random(System.nanoTime()).nextInt(3);
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
        return Math.abs(new Random(System.nanoTime()).nextInt());
    }

    public void randomTransactionDetailsGenerator() {
        TransactionID = RandomTransactionSelector();
        ColourfulOutputs.log(TAG + " - " + "Transaction is " + getTransaction(TransactionID), Process.CLIENT);
        if (TransactionID == 2) {
            TransferClientID = TransferClientIDGenerator();
        }
        TransactionAmount = RandomTransactionAmountGenerator();
        displayTransactionAmountMessage();
    }

    private void forceAddFunds() {
        TransactionID = 0;
        TransactionAmount = RandomTransactionAmountGenerator();
        displayTransactionAmountMessage();
    }

    private void forceWithdrawFunds() {
        TransactionID = 1;
        TransactionAmount = RandomTransactionAmountGenerator();
        displayTransactionAmountMessage();
    }

    private void forceTransferFunds() {
        TransactionID = 2;
        TransferClientID = TransferClientIDGenerator();
        TransactionAmount = RandomTransactionAmountGenerator();
        displayTransactionAmountMessage();
    }

    private void displayTransactionAmountMessage() {
        ColourfulOutputs.log(TAG + " - " + "Transaction Amount is " + TransactionAmount, Process.CLIENT);
    }

    private String getTransaction(int transactionID) {
        String transactionText;
        switch (transactionID) {
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

}