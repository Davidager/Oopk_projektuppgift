/**
 * Created by Dexter on 2017-02-08.
 */
import java.io.*;
import javax.swing.*;
import java.lang.Thread;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ListeningThread extends Thread implements Runnable {
    private int portNumber;
    private String name,textColor;
    ServerSocket serverSocket;
    ArrayList threadList;
    Socket clientSocket;
    ServerChatThread serverChatThread;


    public ListeningThread(int portNumber, String name, String textColor){
        this.name = name;
        this.portNumber = portNumber;
        this.textColor = textColor;
        threadList = new ArrayList();

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
        //Object[] possibilities = {"ham", "spam", "yam"};
        threadList.add("Tråd 1");  //dummies som ska tas bort
        threadList.add("Tråd 2");
        Object newChat = "Ny chat";
        ArrayList optionsList = new ArrayList(1 + threadList.size());
        optionsList.add(0, newChat);
        for (int i=0; i<threadList.size();i++){
            optionsList.add(i+1, threadList.get(i));
        }
        Object[] possibilities = optionsList.toArray();

        Object obj = JOptionPane.showInputDialog(frame, "Vill du ansluta?", "Chattförfrågan",JOptionPane.PLAIN_MESSAGE, null, possibilities, "Ny chat");
        //System.out.println(s);

        if (obj == newChat){
            createNewThread(clientSocket, name, textColor);
            threadList.add(obj);
        }if (obj == null){
            System.out.println("kaoz");
        }else {
            serverChatThread = (ServerChatThread)obj;
            addToThread(serverChatThread);
        }
    }
    public void addToThread(ServerChatThread serverChatThread){

    }
    public void createNewThread(Socket clientSocket, String name, String textColor){
        new ServerChatThread(clientSocket, name, textColor);
    }
}
