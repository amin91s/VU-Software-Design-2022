package softwaredesign.plugins;

import softwaredesign.Plugin;

public class addition implements Plugin {

    public Integer getPrecedence(){return 1;}
    public Boolean isLeftAssociative(){return true;}

    public String getCommandName(){
        return "+";
    }

    public String executeCommand(String input) {
        String[] op = input.split(" ");
        double lhs = Double.parseDouble(op[0]);
        double rhs = Double.parseDouble(op[1]);
        double result = Double.sum(lhs,rhs);
        return String.valueOf(result);
    }
    public Integer getNumParameters(){return 2;}
}
