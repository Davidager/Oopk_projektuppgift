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
    private ServerSocket serverSocket;
    private ArrayList<ServerChatThread> threadList;
    private Socket clientSocket;



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
        //threadList.add("Tråd 1");  //dummies som ska tas bort
        //threadList.add("Tråd 2");
        String newChat = "Ny chat";
        Object[] possibilities = new Object[1 + threadList.size()];
        possibilities[0] = newChat;
        for (int i=1; i<threadList.size();i++){
            possibilities[i] = threadList.get(i-1);
        }

        Object obj = JOptionPane.showInputDialog(frame, "Vill du ansluta?", "Chattförfrågan",JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[0]);
        //System.out.println(s);

        if (obj.equals(newChat)){
            threadList.add(createNewThread(clientSocket, name, textColor));
        }else {
            ServerChatThread serverChatThread = (ServerChatThread)obj;
            addToThread(serverChatThread, clientSocket);
        }
    }
    public void addToThread(ServerChatThread serverChatThread, Socket clientSocket){
        serverChatThread.addToSocketList(clientSocket); // Vi lägger till den nya socketen till tråden
    }
    public ServerChatThread createNewThread(Socket clientSocket, String name, String textColor){
        ServerChatThread svc = new ServerChatThread(clientSocket, name, textColor);
        return svc;
    }
}
