import java.net.*;
import java.io.*;

public class BankTransactionState {

    private static final int INITIAL_CONNECTION = 0;
    private static final int BALANCE_RESPONSE = 1;
    private static final int TRANSACTION_OPTION = 2;
    private static final int AMOUNT = 3;
    private static final int TRANSFER_USER_ID = 4;
    private static final int MORE = 5;
    
    private String[] responses = {"Identify Yourself", "What do you want?", "What is the Transaction Amount?", "“Is there anything else?"};
    
	private Funds bankFundsCurrentStateObject;
	
	public BankTransactionState(Funds BankFunds){
		this.bankFundsCurrentStateObject = BankFunds;
	}
    


	public String processInput(String InputData) {
		String output = null;
		switch (state) {
		case value:
			
			break;

		default:
			break;
		}
		return output;
	}
    
    private void transaction(PrintWriter out, String outputLine){
		try { 
			// Get a lock first
			bankFundsCurrentStateObject.acquireLock();  
			outputLine = bankFundsCurrentStateObject.triggerTransactionJob(transactionID, currectClientID, transferToClientID, amount);
			out.println(outputLine);
			bankFundsCurrentStateObject.releaseLock();  
		} 
		catch(InterruptedException e) {
			System.err.println("Failed to get lock when reading:"+e);
		}
	}
}
