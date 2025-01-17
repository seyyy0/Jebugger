import java.io.*;

//   Welcome to SDBugger (Seid's DeBugger)
//   The SDBugger class features the following:
//      1 - A constructor which is supposed to add a bit of flexibility to the program (regarding the .exe being debugged)
//      2 - All the required features:
//         a) specifying the debugger and executable
//         b) setting breakpoints (setBreakpoint())
//         c) printing the backtrace (getBacktrace())
//         d) resuming execution (cont())
//      3 - A method which allows for setting a new watch for a specific variable
//      4 - A method which allows the user to type in the (usual for GDB) keyword 'quit' to exit the program.
//   The class currently only works with GDB, which was a choice on my part.
//   It uses the ProcessBuilder and Process classes to create a GDB process on an executable, along with BufferedReader/Writer
//for I/O operations
//   Additionally I have also added the option to choose between an interactive version where all the input into GDB
//is entered by the user and the output from GDB is handled by a separate Thread.

public class SDBugger {


    //Initialization of the needed objects and boolean
    public static ProcessBuilder pb;
    public static Process p;
    public static BufferedWriter writer;
    public static BufferedReader reader;
    public static boolean CanRun = true;

    //Constructor
    public SDBugger(String executable) throws IOException {
        pb = new ProcessBuilder("gdb", executable);  //We create and start a GDB process, that will work with an executable
        p = pb.start();
        writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));  //We assign to the BufferedReader/Writer the I/O from our GDB process
        reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    //Method for handling output
    public static void startReading() {

        new Thread(() -> {  //We start a new thread so that we don't need to worry about reading output while we're busy sending in our input
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("exited normally")) {  //Detection of program completion
                        CanRun = false;
                    }
                    if (line.contains("value")) {  //Detection and highlighting of watched values/variables
                        System.out.println("\u001B[42m" + line + "\u001B[0m");
                    }
                    else {
                        System.out.println("\u001B[92m" + line + "\u001B[0m");
                    }
                }
            } catch (IOException e) {
                System.out.println("Process interrupted, exited with the following message: " + e + "\n");
            }
        }).start();
    }

    public static void setBreakpoint(String point) throws IOException, InterruptedException {

        writer.write("break " + point + "\r\n");
        writer.flush();
        Thread.sleep(50);
    }

    public static void setBreakpoint(int line) throws IOException, InterruptedException {

        writer.write("break " + line + "\r\n");
        writer.flush();
        Thread.sleep(50);
    }

    public static void newWatch(String variable) throws IOException, InterruptedException {

        writer.write("watch " + variable + "\r\n");
        writer.flush();
        Thread.sleep(50);
    }

    public static void runDebug() throws IOException, InterruptedException {

        writer.write("run" + "\r\n");
        writer.flush();
        Thread.sleep(50);
    }

    public static void getBacktrace() throws IOException, InterruptedException {

        writer.write("bt" + "\r\n");
        writer.flush();
        Thread.sleep(50);
    }

    public static void cont() throws IOException, InterruptedException {

        writer.write("c" + "\r\n");
        writer.flush();
        Thread.sleep(50);
    }

    public static void quit() {

        p.destroy();
        System.out.println("Program finished...");
    }
}