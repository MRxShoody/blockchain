package blockchain.commandPattern;

public class Invoker {
    command command;

    public void setCommand(command command){
        this.command = command;
    }

    public void executeCommand(){
        command.execute();
    }
}
