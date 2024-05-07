package softwaredesign.plugins;

import softwaredesign.Plugin;

public class subtraction implements Plugin {

    public String getCommandName(){
        return "-";
    }

    public String executeCommand(String input) {
        String[] op = input.split(" ");
        int lhs = Integer.parseInt(op[0]);
        int rhs = Integer.parseInt(op[1]);
        int result = Math.subtractExact(lhs,rhs);
        return String.valueOf(result);
    }
    public Integer getPrecedence(){return 1;}
    public Boolean isLeftAssociative(){return true;}

    public Integer getNumParameters(){return 2;}
}
