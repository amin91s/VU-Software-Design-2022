package softwaredesign;

import java.util.ArrayList;


public class Calculator {

    public static ArrayList<Plugin> plugins = new ArrayList<>();
    public static ArrayList<String> history = new ArrayList<>();
    public static int historyIndex = -1;

    public static void main (String[] args) throws Exception {
        System.out.println("----Beginning Calculator Program----");

        // create PluginPort object
        PluginPort pluginport = PluginPort.getInstance();
        // load all plugins
        try{
            pluginport.loadPlugins();
        }catch (Exception e){
            System.out.println("could not load plugins.\nError: " + e.getMessage());
        }


        IO io = IO.getInstance();
        String input ;
        io.promptUser();
        input = io.readInput();
        while(!input.equals("quit")){
            if (input.equals("undo")){
                io.printUndo(--historyIndex);
            }
            else if (input.equals("redo")){
                io.printRedo(++historyIndex);
            }
            else {
                try {
                    String res = pluginport.calculate(input);
                    io.printResult(res);
                    history.add(res);
                    historyIndex++;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.print("type another command: ");
            input = io.readInput();
        }


        System.out.println("----Ending Calculator Program----");
    }
}
