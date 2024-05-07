package softwaredesign.plugins;

import softwaredesign.Plugin;

public class sin implements Plugin {

    public String getCommandName(){
        return "sin";
    }

    public String executeCommand(String input) {

        double num = Double.parseDouble(input);
        double result = Math.sin(num);
        return String.valueOf(result);
        //return String.format("%.4f", result);
    }
    public Integer getPrecedence(){return 4;}
    public Boolean isLeftAssociative(){return false;}
    public Integer getNumParameters(){return 1;}
}
