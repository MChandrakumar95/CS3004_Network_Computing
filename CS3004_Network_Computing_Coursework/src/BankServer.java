import java.net.*;
import java.io.*;





public class BankServer {
  public static void main(String[] args) throws IOException {

	ServerSocket BankServer = null;
    boolean listening = true;
    String BankServerName = "BankServer";
    int BankServerPortNumber = 4545;

    //Create the shared object in the global scope...
    
    Funds funds = new Funds();
        
    // Make the server socket

    try {
      BankServer = new ServerSocket(BankServerPortNumber);
    } catch (IOException e) {
      System.err.println("Could not start " + BankServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(BankServerName + " started");

    //Got to do this in the correct order with only four clients!  Can automate this...
    
    while (listening){
      new BankServerThread(BankServer.accept(), "Client1", funds).start();
      new BankServerThread(BankServer.accept(), "Client2", funds).start();
      new BankServerThread(BankServer.accept(), "Client3", funds).start();
      System.out.println("New " + BankServerName + " thread started.");
    }
    BankServer.close();
  }
}