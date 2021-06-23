import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.FormatterClosedException;

public class PlayerInfoWriter {
	private static Formatter output; // outputs text to a file
	private String fileName; // target file name

	public PlayerInfoWriter(String fileName) {
		this.fileName = fileName;
	}

	public void addPlayer( String Name, String rank, int averageDamage) {
		openFile();
		addRecord(Name, rank, averageDamage);
		closeFile();
	}

	public void openFile() {
		try {
			FileWriter fw = new FileWriter(fileName, true);
			output = new Formatter(fw);
			//output = new Formatter(fileName);
		} catch (SecurityException securityException) {
			System.err.println("Write permission denied. Terminating.");
			System.exit(1); // terminate the program
		} catch (FileNotFoundException fileNotFoundException) {
			System.err.println("Error opening file. Terminating.");
			System.exit(1); // terminate the program
		} catch (IOException e) {
			System.err.println("I/O error. Terminating.");
			System.exit(1); // terminate the program
		}
	}

	// add records to file
	public void addRecord(String Name, String rank, int averageDamage) {
		try {
			// output new record to file; assumes valid input
			// TODO
			output.format("%s %s %d%n",Name, rank, averageDamage);
		} catch (FormatterClosedException formatterClosedException) {
			System.err.println("Error writing to file. Terminating.");
		}
	}

	// close file
	public static void closeFile() {
		if (output != null)
			output.close();
	}
}