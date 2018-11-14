
import java.util.Date;
import java.util.Random;

public class MainProgram {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
		randomSelector();
//        initiateSingleBankClientThread("Client1");
    }

    private static void randomSelector() {
        int randomNum;
        for (int i = 0; i < 10; i++) {
            randomNum = new Random(new Date().getTime()).nextInt(3);
            actionClientInitiator(randomNum);
            randomNum = randomNum + 1;
            System.out.println( i + ") New thread started. Bank Client is " + randomNum + ".");
        }
    }

    private static void actionClientInitiator(int i) {
        switch (i) {
            case 0:
                initiateSingleBankClientThread("Client1");
                break;
            case 1:
                initiateSingleBankClientThread("Client2");
                break;
            case 2:
                initiateSingleBankClientThread("Client3");
                break;
            default:
                break;
        }
    }

    private static void initiateSingleBankClientThread(String ClientID){
        new BankClient(ClientID).start();
    }


}
