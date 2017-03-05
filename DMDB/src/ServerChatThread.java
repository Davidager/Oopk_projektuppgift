import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Dexter on 2017-02-12.
 */
public class ServerChatThread extends ChatThread implements Runnable {
    private ArrayList<String> nameList;
    private Socket socket;
    private ServerChatFrame serverChatFrame;
    private ArrayList<ServerChatThread> threadList;

    private Hashtable<Socket, PrintWriter> socketPrintWriterHashtable;
    private Hashtable<Socket, String> socketandNameHashTable;

    public ServerChatThread(Socket socket, String name, Color textColor, ArrayList<ServerChatThread> threadList){
        this.socket = socket;   // Todo: används detta?
        this.name = name;
        this.textColor = textColor;
        this.threadList = threadList;


        nameList = new ArrayList<>();

        /*try{
            outText = new PrintWriter(socket.getOutputStream(), true);
            inText = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (UnknownHostException e){
            System.out.println("bam");
        }catch (IOException f){
            System.out.println("b88oom");
        }*/

        socketPrintWriterHashtable = new Hashtable<>();
        socketandNameHashTable = new Hashtable<>();

        //socketPrintWriterHashtable.put(socket, outText);
        serverChatFrame = new ServerChatFrame(name, textColor);
    }

    @Override
    public void run() {    // TODO: ska nog tas bort på något sätt, körs aldrig som det är nu
        done = false;
        while(!done){
            try{
                String s = inText.readLine();
                System.out.println(s);
                if (s==null){
                    System.out.println("Server disconnect");
                    done = true;
                }else {
                    String[] parsedArray = XmlParser.parse(s);
                    serverChatFrame.writeToChat(parsedArray[0], parsedArray[1], Color.decode(parsedArray[2]));
                }
            }catch (IOException e){

            }
        }
    }

    public int getNumberConnected () {
        return socketPrintWriterHashtable.size();
    }

    public Hashtable<Socket, PrintWriter> getSocketPrintWriterHashtable() {
        return socketPrintWriterHashtable;
    }

    public Hashtable<Socket, String> getSocketandNameHashtable() {
        return socketandNameHashTable;
    }


    public void createInputListenerThread() {
        //ny socket med mera
    }

    public void setServerChatFramesServerChatThread() {
        serverChatFrame.setServerChatThread(this);
    }

    public String toString(){
        String retString = "Samtal nr " + serverChatFrame.getServerNumber();
        return retString;
    }

    public void removeSocket(Socket removeSocket){
        socketPrintWriterHashtable.remove(removeSocket);
        if (socketandNameHashTable.containsKey(removeSocket)) {
            socketandNameHashTable.remove(removeSocket);
        }
    }

    private void removeFromThreadList() {
        threadList.remove(this);
    }

    @Override
    public void closeThread() {
        try{
            removeFromThreadList();
            for (Socket key : socketPrintWriterHashtable.keySet()) {
                key.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendText(String str) {
        for (Socket key : socketPrintWriterHashtable.keySet()) {
            socketPrintWriterHashtable.get(key).println(str);

        }
    }


    public void addToSocketList(Socket socket){
        ReceivingThread receivingThread = new ReceivingThread(socket);
        receivingThread.start();
    }

    protected class ReceivingThread extends Thread implements Runnable {
        private Socket threadSocket;
        BufferedReader myInText;
        PrintWriter myOutText;

        ReceivingThread(Socket threadSocket) {
            this.threadSocket = threadSocket;
            try{
                myInText = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
                myOutText = new PrintWriter(threadSocket.getOutputStream(), true);
            }catch (UnknownHostException e){
                System.out.println("bam");
            }catch (IOException f){
                System.out.println("b88oom");
            }
            socketPrintWriterHashtable.put(threadSocket, myOutText);
        }

        @Override
        public void run() {
            done = false;
            while(!done){
                try{
                    String s = myInText.readLine();
                    System.out.println(s + "1");
                    if (s==null){
                        System.out.println("Server disconnect in receivThread");
                        removeSocket(threadSocket);
                        if (socketPrintWriterHashtable.isEmpty()) {
                            serverChatFrame.frameClose();
                            removeFromThreadList();
                        }
                        done = true;
                    }else {
                        for (Socket key : socketPrintWriterHashtable.keySet()) {
                            if (!key.equals(threadSocket)) {
                                socketPrintWriterHashtable.get(key).println(s);
                            }
                        }
                        String[] parsedArray = XmlParser.parse(s);
                        if (parsedArray[0].equals("text")){
                            if (!socketandNameHashTable.containsKey(threadSocket)) {   // lägger in namn!
                                socketandNameHashTable.put(threadSocket, parsedArray[1]);
                            }
                            serverChatFrame.writeToChat(parsedArray[1], parsedArray[2], Color.decode(parsedArray[3]));
                        } else if (parsedArray[0].equals("filerequest")) {
                            new ReceiveFileFrame(ServerChatThread.this, this,
                                    parsedArray[1], parsedArray[2], parsedArray[3], parsedArray[4]);

                        } else if (parsedArray[0].equals("keyrequest")) {
                            sendToOne("<message sender=\"" + name + "\"><text color=\""
                                    + String.format("#%02X%02X%02X", textColor.getRed(),
                                    textColor.getGreen(), textColor.getBlue()) + "\">" +
                                    "Detta program skickar ingen nyckel!" + "</text></message>");
                        }


                    }
                }catch (IOException e){

                }
            }
        }

        protected Socket getThreadSocket() {
            return threadSocket;
        }

        public void sendToOne(String message) {
            System.out.println("check2");
            myOutText.println(message);

        }
    }
}
