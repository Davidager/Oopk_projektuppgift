/**
 * Created by Dexter on 2017-02-08.
 */
import java.io.*;
import javax.swing.*;
import java.lang.Thread;
import java.net.*;
import java.util.ArrayList;

public class ListeningThread extends Thread implements Runnable {
    private int portNumber;
    private String name,textColor;
    ServerSocket serverSocket;
    ArrayList[] threadList;
    Socket clientSocket;


    public ListeningThread(int portNumber, String name, String textColor){
        this.name = name;
        this.portNumber = portNumber;
        this.textColor = textColor;
        threadList = new ArrayList[10];

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + portNumber);
            System.exit(-1);
        }

        System.out.println("congrats man!");
        System.out.println(name);
        //createChatQuery();
        this.start();
        
    }
    public void run(){
        while (true){
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Accept failed: " + portNumber);
                System.exit(-1);
            }
            createChatQuery(clientSocket);
        }


    }

    public void createChatQuery(Socket clientSocket){
        JFrame frame = new JFrame();
        Object[] possibilities = {"ham", "spam", "yam"};

        String s = (String)JOptionPane.showInputDialog(frame, "Vill du ansluta?", "Chattförfrågan",JOptionPane.PLAIN_MESSAGE, null, possibilities, "ham");
        System.out.println(s);
    }
    /*public void addToThread(ServerChatThread serverChatThread){

    }*/
    public void createNewThread(Socket clientSocket, String name, String textColor){

    }
}
