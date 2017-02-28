/**
 * Created by Dexter on 2017-02-08.
 */
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.lang.Thread;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ListeningThread extends Thread implements Runnable {
    private int portNumber;
    private String name;
    private Color textColor;
    private ServerSocket serverSocket;
    private ArrayList<ServerChatThread> threadList;
    private Socket clientSocket;



    public ListeningThread(int portNumber, String name, Color textColor){
        this.name = name;
        this.portNumber = portNumber;
        this.textColor = textColor;
        threadList = new ArrayList();

        try {
            serverSocket = new ServerSocket(portNumber, 0, InetAddress.getByName(null));
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + portNumber);
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
        for (int i=1; i<threadList.size() + 1; i++){
            System.out.println(threadList.get(i-1));
            possibilities[i] = threadList.get(i-1);
        }


        String requestString = null;
        try {
            requestString = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())).readLine();
        } catch (IOException e) {
            System.out.println("woops");
            System.exit(0);
        }
        String infoText = "En enklare klient vill ansluta.";
        if (requestString != null) {
            String parsedString = XmlParser.parseRequest(requestString);
            if (!parsedString.equals("")) {
                infoText = parsedString;
            }
        }

        System.out.println(possibilities[possibilities.length-1] + "12");

        Object obj = JOptionPane.showInputDialog(
                frame, infoText + "\nVill du ansluta?", "Chattförfrågan",
                JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[0]);
        //System.out.println(s);
        System.out.println(obj);
        if (obj.equals(newChat)){
            MainFrame.atomicInteger.incrementAndGet();
            threadList.add(createNewThread(clientSocket, name, textColor));
            System.out.println(threadList.get(0));
        }else {
            ServerChatThread serverChatThread = (ServerChatThread)obj;
            addToThread(serverChatThread, clientSocket);
        }
    }
    public void addToThread(ServerChatThread serverChatThread, Socket clientSocket){
        serverChatThread.addToSocketList(clientSocket); // Vi lägger till den nya socketen till tråden
    }
    public ServerChatThread createNewThread(Socket clientSocket, String name, Color textColor){
        ServerChatThread svc = new ServerChatThread(clientSocket, name, textColor);

        svc.start();
        return svc;
    }
}
