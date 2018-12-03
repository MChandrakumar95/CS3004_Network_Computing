import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

/**
 * @author 1442877
 * <p>
 * This class is used to run the client side code.
 */

public class MainProgram {

    private static final String TAG = "MainProgram_Class";

    public static void main(String[] args) {
        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        ColourfulOutputs.log(TAG + " - Automated Client Application started at: " + timestamp, Process.CLIENT);
        randomSelector();
//        singleBankClientInitiator(0);
//        singleBankClientInitiator(1);
//        singleBankClientInitiator(2);
    }

    private static void randomSelector() {
        for (int i = 0; i < 10; i++) {
            randomBankClientInitiator(i);
        }
    }

    private static synchronized void randomBankClientInitiator(int i) {
        int randomNum = new Random(System.nanoTime()).nextInt(3);
        singleBankClientInitiator(randomNum);
        randomNum = randomNum + 1;
        String message = TAG + " - " + i + ") New thread started. Bank Client is " + randomNum + ".";
        ColourfulOutputs.log(message, Process.CLIENT);
    }

    private static void singleBankClientInitiator(int i) {
        switch (i) {
            case 0:
                new BankClient("Client1").triggerBankClientThread();
                break;
            case 1:
                new BankClient("Client2").triggerBankClientThread();
                break;
            case 2:
                new BankClient("Client3").triggerBankClientThread();
                break;
            default:
                break;
        }
    }

}
