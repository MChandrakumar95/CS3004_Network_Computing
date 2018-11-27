import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author 1442877 This class is based off the code on,
 *         http://www.santhoshreddymandadi.com/java/coloring-java-output-on-console.html
 *         Further implementation was done by Manoj Chandrakumar
 *
 *         This class could be used to format the output text on the console.
 *         The valid formatting input parameters could be found in the
 *         ValidColours enum.
 *
 */

class ColourfulOutputs {

	public ValidColours getColours() {
		return colours;
	}

	public void setColours(ValidColours colours) {
		this.colours = colours;
	}

	private ValidColours colours;

	/**
	 *
	 * @param message
	 *            String type: This is the test you want to display on the
	 *            console.
	 *
	 * @param Colour
	 *            ValidColour type: This the format or colour you want the
	 *            output text should be. This could be: - ValidColour.BOLD -
	 *            ValidColour.UNDERLINED - ValidColour.HIGHLIGHTED -
	 *            ValidColour.RED - ValidColour.GREEN - ValidColour.BLUE -
	 *            ValidColour.YELLOW - ValidColour.INDIGO -
	 *            ValidColour.TURQUOISE - ValidColour.GREY - ValidColour.NONE
	 *
	 **/

	public ColourfulOutputs() {
	}

	public ColourfulOutputs(ValidColours colour) {
		this.colours = colour;
	}

	void outputColourfulText(String message) {
		String outputText;
		switch (colours) {
		case BOLD:
			outputText = "\033[1m" + message + "\033[0m";
			break;
		case UNDERLINED:
			outputText = "\033[4m" + message + "\033[0m";
			break;
		case HIGHLIGHTED:
			outputText = "\033[7m" + message + "\033[0m";
			break;
		case RED:
			outputText = "\033[31m" + message + "\033[0m";
			break;
		case GREEN:
			outputText = "\033[32m" + message + "\033[0m";
			break;
		case BLUE:
			outputText = "\033[34m" + message + "\033[0m";
			break;
		case YELLOW:
			outputText = "\033[33m" + message + "\033[0m";
			break;
		case INDIGO:
			outputText = "\033[35m" + message + "\033[0m";
			break;
		case TURQUOISE:
			outputText = "\033[36m" + message + "\033[0m";
			break;
		case GREY:
			outputText = "\033[37m" + message + "\033[0m";
			break;
		case NONE:
			outputText = message;
			break;
		default:
			outputText = "\033[0m" + message + "\033[0m";
			break;
		}
		System.out.println(outputText);
	}

	public void helper(String TAG) {
		outputColourfulText(
				"Output Text from the " + TAG + " Class will be displayed in this font style... - " + colours + "");
	}

	public static void produceLogFile(String logFileName) {
		File file = new File(logFileName + ".txt"); // The log file.
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assert fileOutputStream != null;
		PrintStream ps = new PrintStream(fileOutputStream);
		System.setOut(ps);
		String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("This file was created on: " + date);
	}

	public static void readLogFile(String fileName) {
		// TODO Auto-generated method stub

	}

	public static synchronized void log(String message, Process processType) {
		PrintWriter printWriter = null;
		try {
			switch (processType) {
			case CLIENT:
				printWriter = new PrintWriter(new FileWriter("Client.log", true), true);
				break;
			case SERVER:
				printWriter = new PrintWriter(new FileWriter("Server.log", true), true);
				break;
			default:
				System.err.println("ERROR Invalid Process Type!!!");
				break;
			}
			Timestamp date = new Timestamp(new Date().getTime());
			printWriter.write(date + " - " + message + "\n");
			printWriter.close();
			System.out.println(date + " - " + message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("ERROR - Unable to write into the log file.");
		}

	}
}
