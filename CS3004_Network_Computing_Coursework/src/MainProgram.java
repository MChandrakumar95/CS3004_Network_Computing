import java.util.Random;

public class MainProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		randomSelector();
	}

	private static void randomSelector() {
		Random randomGen = new Random();
		int randomNum = 0; 
		for (int i = 0; i < 10; i++) {
			randomNum = randomGen.nextInt(4);
			actionClientInitiator(randomNum);
			randomNum = randomNum + 1;
		      System.out.println( i + ") New thread started. Action Client is " + randomNum + ".");
		}
	}
	
	private static void actionClientInitiator(int i) {
		switch (i) {
		case 0:
			new ActionClient("ActionClient1").start();
			break;
		case 1:
			new ActionClient("ActionClient2").start();
			break;
		case 2:
			new ActionClient("ActionClient3").start();
			break;
		case 3:
			new ActionClient("ActionClient4").start();
			break;
		default:
			break;
		}
	}
	
}
