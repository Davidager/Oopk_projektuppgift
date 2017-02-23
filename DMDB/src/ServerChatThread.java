import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Dexter on 2017-02-12.
 */
public class ServerChatThread extends ChatThread implements Runnable {
    ArrayList<String> nameList;
    private Socket socket;
    ArrayList<Socket> socketList;


    public ServerChatThread(Socket socket, String name, String textColor){
        super(socket, name, textColor);
        nameList = new ArrayList();
        socketList = new ArrayList();
        socketList.add(socket);
    }

    public String toString(){
        String retString = "Samtal med: ";
        for (int i=0 ; i<nameList.size()-1 ; i++){
            retString = retString + nameList.get(i) + ", ";
        }
        retString = retString + nameList.get(nameList.size()-1);
        return retString;
    }

    public void disconnect(Socket socket){
        for (int i=0;i<socketList.size();i++){
            if (socket==socketList.get(i)){
                socketList.remove(i);
            }
        }
    }

    public void addToSocketList(Socket socket){
        socketList.add(socket);
    }
}
