
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Random;

public class MainProgram {

	private static final String TAG = "MainProgram";

	public static void main(String[] args) {
		//        test();
		//ColourfulOutputs.produceLogFile(TAG);
		randomSelector();
		//        colourTest();
		//        initiateSingleBankClientThread("Client1");
		//        initiateSingleBankClientThread("Client2");
		//        initiateSingleBankClientThread("Client3");
		
	}


	private static void randomSelector() {
		int randomNum;
		for (int i = 0; i < 10; i++) {
			randomNum = new Random(new Date().getTime()).nextInt(3);
			actionClientInitiator(randomNum);
			randomNum = randomNum + 1;
			String message = i + ") New thread started. Bank Client is " + randomNum + ".";
			ColourfulOutputs.log(message, Process.CLIENT);
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


	private static void test(){
		File file = new File(TAG + ".txt"); //Your file
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assert fos != null;
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		System.out.println("This goes to out.txt");
	}

	private static void colourTest(){
		for (ValidColours validColours:ValidColours.values()) {
			ColourfulOutputs colourfulOutput = new ColourfulOutputs(validColours);
			colourfulOutput.outputColourfulText(validColours + "");
		}
	}

}
