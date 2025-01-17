import java.io.IOException;
import java.util.Scanner;

@SuppressWarnings("ALL")
public class Main {

    //The hard-coded debugging process works in the following way:
    //   1 - We instantiate the SDBugger class as "driver" which we will use to control the debug process
    //   2 - We immediately start the thread which is supposed to take care of the output from GDB so that we dont miss anything
    //   3 - We set 2 breakpoints in the program, one at the main function and one at a (randomly selected) line
    //   4 - We run the debug, and set a watch for the variable "sum"
    //   5 - We tell the program to keep on continuing (while getting the backtrace) for as long as it can
    //before GDB is done with our .exe
    //   6 - Finally, we quit the program.

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("Hello and welcome to Seid's DeBugger (SDB)! o/");
        System.out.print("Please choose between doing a [1] hard-coded debug or an [2] interactive one: ");
        int choice = sc.nextInt();

        if (choice == 1) {

            System.out.println("\u001B[34m   You have selected the hard-coded debug option!");

            SDBugger driver = new SDBugger("sum.exe");

            driver.startReading();
            driver.setBreakpoint("main");
            driver.setBreakpoint(14);
            driver.runDebug();
            driver.newWatch("sum");
            while (driver.CanRun) {
                driver.cont();
                driver.getBacktrace();
            }
            driver.quit();
        }

        else if (choice == 2) {

            System.out.println("\u001B[34m   You have selected the interactive debug option, enjoy!");
            try {
                Process gdbProcess = SDBuggerInteractive.processStart("sum.exe");
                System.out.println("\u001B[34m   You can now start inputting commands for the running gdb process below. \n   Enter 'quit' if you want the program to exit.");
                SDBuggerInteractive.InputOutput(gdbProcess);
            } catch (InterruptedException e) {
                System.out.println("Process interrupted, exited with the following message: " + e + "\n");
            }
        }


    }
}