import java.util.Random;

public class MainProgram {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//		randomSelector();
        simpletest();
    }

    private static void randomSelector() {
        Random randomGen = new Random();
        int randomNum;
        for (int i = 0; i < 10; i++) {
            randomNum = randomGen.nextInt(4);
            actionClientInitiator(randomNum);
            randomNum = randomNum + 1;
            System.out.println( i + ") New thread started. Bank Client is " + randomNum + ".");
        }
    }

    private static void actionClientInitiator(int i) {
        switch (i) {
            case 0:
                new BankClient("Client1").start();
                break;
            case 1:
                new BankClient("Client2").start();
                break;
            case 2:
                new BankClient("Client3").start();
                break;
            default:
                break;
        }
    }

    private static void simpletest(){
        new BankClient("Client1").start();
    }

}
