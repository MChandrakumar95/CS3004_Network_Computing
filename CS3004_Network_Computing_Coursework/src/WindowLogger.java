import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author 1442877 This class is can be used to produce a log file for the
 *         server and client. The log messages will also be displayed on the
 *         console window.
 */

class WindowLogger {

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
			e.printStackTrace();
			System.err.println("ERROR - Unable to write into the log file.");
		}
	}
}
