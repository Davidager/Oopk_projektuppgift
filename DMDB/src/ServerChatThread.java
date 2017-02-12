import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Dexter on 2017-02-12.
 */
public class ServerChatThread extends ChatThread {

    public ServerChatThread(Socket socket, String name, String textColor){
        super(socket, name, textColor);
    }
}
