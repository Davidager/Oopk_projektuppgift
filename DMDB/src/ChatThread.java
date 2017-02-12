import java.lang.Thread;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
/**
 * Created by Dexter on 2017-02-09.
 */
public class ChatThread extends Thread{
    private int portNumber;
    private String name,textColor;
    private InetAddress hostIP;
    private Socket socket;

    public ChatThread(Socket socket, String name, String textColor){
        this.socket = socket;
        this.name = name;
        this.textColor = textColor;
    }
}
