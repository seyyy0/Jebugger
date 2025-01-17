import java.io.*;
import java.util.Scanner;

//   Welcome to SDBuggerInteractive (Seid's DeBugger (Interactive version))
//   The SDBuggerInteractive class features a cool method that handles input from the user and output from the process
//in two different threads
//   The interactive version, compared to the non-interactive, due to its nature (of taking user input directly) has more
//functionalities, because this way the user can work with any commands the process (GDB in this case) features, whereas
//the non-interactive version relies on pre-baked methods to extend its functionality.
//   The interactive version also relies on classes Process and ProcessBuilder to get GDB up and running, and also uses
//BufferedReader/Writer to handle I/O operations, but it also uses our good old friend Scanner to get user input

public class SDBuggerInteractive {

    //Method for initialising our process
    public static Process processStart(String executable) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gdb", executable);
        pb.redirectErrorStream(true);
        return pb.start();
    }

    //Method that handles our input and output from the terminal and the process
    public static void InputOutput(Process SDBprocess) throws IOException, InterruptedException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(SDBprocess.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(SDBprocess.getInputStream()));
        Scanner userReader = new Scanner(System.in);

        Thread output = new Thread(() -> {  //We start a new thread so that we don't need to worry about reading output while we're busy sending in our input
            try {
                String line;
                while ((line = reader.readLine()) != null) {  //To 'line' we assign the current line from the GDB process and check if its null, if not, we print it out
                    System.out.println("\u001B[93m" + line);
                }
            } catch (IOException e) {
                System.out.println("Process interrupted, exited with the following message: " + e + "\n");
            }
        });
        output.start();

        String input;
        while ((input = userReader.nextLine()) != null) {  //To 'input' we assign whatever the user inputs into the terminal using our old friend Scanner
            if (input.equals("quit")) {  //Detection of the 'quit' command
                System.out.println("Program finished...");
                System.exit(0);
            }
            writer.write(input + "\n");  //Actually inputting the input from the user into the GDB process using BufferedWriter
            writer.flush();
        }

        writer.close();
        reader.close();
    }
}
