/**
 * Created by Dexter on 2017-02-08.
 */
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ListeningThread extends Thread {
    private int portNumber;
    private String name,textColor;
    ServerSocket serverSocket;
    ArrayList[] threadList;
    Socket clientSocket;

    public ListeningThread(int portNumber, String name, String textColor){
        System.out.println("congrats man!");
        System.out.println(name);
    }
}
