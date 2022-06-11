package Network;

public class Message {

    String[] message;

    public Message(String[] strings){
        message = strings.clone();
    }

    public Message(String string){
        message = new String[]{string};
    }

    public String[] getMessage(){
        return message;
    }

}
