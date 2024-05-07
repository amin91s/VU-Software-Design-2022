package softwaredesign;

public interface Plugin {

    String getCommandName();
    String executeCommand(String input);
    Integer getPrecedence();
    Boolean isLeftAssociative();
    Integer getNumParameters();

}
