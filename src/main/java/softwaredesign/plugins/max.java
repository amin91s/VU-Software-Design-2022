package softwaredesign.plugins;

import softwaredesign.Plugin;

public class max implements Plugin {

    public String getCommandName(){
        return "max";
    }

    public String executeCommand(String input) {
        String[] op = input.split(" ");
        double lhs = Double.parseDouble(op[0]);
        double rhs = Double.parseDouble(op[1]);
        double result = Math.max(lhs,rhs);
        return String.valueOf(result);
        //return String.format("%.4f", result);
    }
    public Integer getPrecedence(){return 4;}
    public Boolean isLeftAssociative(){return false;}
    public Integer getNumParameters(){return 2;}
}
