package softwaredesign;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;




public class IO {

    private static IO instance;
    private IO() {}
    public static IO getInstance() {
        if(instance == null) {
            instance = new IO();
        }

        return instance;
    }

    public void promptUser() {
    // print list of commands
        System.out.print("Welcome to your jCalculator. Available commands are:\nquit, undo, redo");
        for(Plugin p : Calculator.plugins){
            System.out.print(" , "+p.getCommandName());
        }
        System.out.print("\nplease type in a command: ");
    }

    public String readInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }




    public void printResult(String result) {
        System.out.println("result = " + result);
    }

    public void printUndo(int previousResult) {
        if (previousResult >= 0) {
            System.out.println("Retrieving previous result: " + Calculator.history.get(previousResult));
        }
        else{
            System.out.println("No previous answers to retrieve.");
        }
    }

    public void printRedo(int nextResult) {
        if (nextResult <= Calculator.history.size()-2) {
            System.out.println("Restoring result: " + nextResult);
        }
        else{
            System.out.println("No answers to restore.");
        }
    }


}
