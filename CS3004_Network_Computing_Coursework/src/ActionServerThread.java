
import java.net.*;
import java.io.*;


public class ActionServerThread extends Thread {


	private Socket actionSocket = null;
	private SharedActionState mySharedActionStateObject;
	private String myActionServerThreadName;
	private double mySharedVariable;

	//Setup the thread
	public ActionServerThread(Socket actionSocket, String ActionServerThreadName, SharedActionState SharedObject) {

		//	  super(ActionServerThreadName);
		this.actionSocket = actionSocket;
		mySharedActionStateObject = SharedObject;
		myActionServerThreadName = ActionServerThreadName;
	}

	public void run() {
		try {
			System.out.println(myActionServerThreadName + " initialising.");
			PrintWriter out = new PrintWriter(actionSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(actionSocket.getInputStream()));
			String inputLine, outputLine;

			while ((inputLine = in.readLine()) != null) {
				// Get a lock first
				try { 
					mySharedActionStateObject.acquireLock();  
					outputLine = mySharedActionStateObject.processInput(myActionServerThreadName, inputLine);
					out.println(outputLine);
					mySharedActionStateObject.releaseLock();  
				} 
				catch(InterruptedException e) {
					System.err.println("Failed to get lock when reading:"+e);
				}
			}

			out.close();
			in.close();
			actionSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double getMySharedVariable() {
		return mySharedVariable;
	}

	public void setMySharedVariable(double mySharedVariable) {
		this.mySharedVariable = mySharedVariable;
	}
	
	private void triggerTransactionJob(int transactionID, String clientID, int amount){
		switch (transactionID) {
		case 1:
			
			break;
			
		case 2:
			break;
		case 3:
			
			break;

		default:
			break;
		}
	}
	
	
}