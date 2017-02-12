import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Dexter on 2017-02-12.
 */
public class ServerChatThread extends ChatThread {
    ArrayList<String> nameList;


    public ServerChatThread(Socket socket, String name, String textColor){
        super(socket, name, textColor);
        nameList = new ArrayList();
    }

    public String toString(){
        String retString = "Samtal med: ";
        for (int i=0 ; i<nameList.size()-1 ; i++){
            retString = retString + nameList.get(i) + ", ";
        }
        retString = retString + nameList.get(nameList.size()-1);
        return retString;
    }
}
