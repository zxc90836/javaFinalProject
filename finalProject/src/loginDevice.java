import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.Format;
import java.util.Formatter;
import java.util.Scanner;

public class loginDevice {
    private Formatter output;//write something into the file
    private Scanner input;//
    private String fileName = "playerID.txt";
    public loginDevice(){
        openFileReader();
        if(input.nextInt() == 0)
        {
            //to enter basic Information
            enterPlayerInformation newUser = new enterPlayerInformation();
        }

    }
    public void openFileReader() {
        try
        {
            input = new Scanner(Paths.get(fileName));
        }
        catch (IOException ioException)
        {
            System.err.println("Error opening file. Terminating.");
            System.exit(1);
        }
    } // end method openFile
    public void closeFileReader() {
        if (input != null)
            input.close(); // close file
    } // end method closeFile
}
