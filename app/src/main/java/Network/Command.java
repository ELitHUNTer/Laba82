package Network;

public class Command {

    private CommandType type;
    private String[] args;
    public static String LOGIN_OK = "Вход выполнен";

    public Command(CommandType type, String[] args){
        this.type = type;
        this.args = args;
    }

    public CommandType getType() {
        return type;
    }

    public String[] getArgs() {
        return args;
    }
}


