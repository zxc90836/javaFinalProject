import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PlayerInfoReader {
	private Scanner input;
	private String fileName; // target file name

	public PlayerInfoReader(String fileName) {
		this.fileName = fileName;
	}

	public ArrayList<Player> readAllPlayers() {
		openFile();
		ArrayList<Player> list = readRecords();
		closeFile();

		return list;
	}

	public void openFile() {
		try {
			input = new Scanner(Paths.get(fileName));
		} catch (IOException ioException) {
			System.err.println("Error opening file. Terminating.");
			System.exit(1);
		}
	}

	// Read all records and return an ArrayList of Player Objects
	public ArrayList<Player> readRecords() {
		ArrayList<Player> list = new ArrayList<Player>();
		RankTypeHandler rankTypeHandler= new RankTypeHandler();
		//System.out.printf("%-12s%-12s%10s%n", "First Name", "Last Name", "Balance");

		try {
			while (input.hasNext()) // while there is more to read
			{
				//TODO
				//1. 0 or 1 2.名稱 3.牌位 4.均傷
				int trash = input.nextInt();
				Player e = new Player(input.next()
						,rankTypeHandler.produceRank(input.next()),input.nextDouble());
				/*Player e = new Player(input.next(), input.nextInt(), input.nextInt()
						,new Score(input.nextDouble(),input.nextInt()));*/
				list.add(e);
			   
			}
		} catch (NoSuchElementException elementException) {
			System.err.println("File reader improperly formed. Terminating.");
		} catch (IllegalStateException stateException) {
			System.err.println("Error reading from file. Terminating.");
		}

		return list;
	} // end method readRecords

	// close file and terminate application
	public void closeFile() {
		if (input != null)
			input.close();
	}

}
