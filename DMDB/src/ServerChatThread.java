import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Dexter on 2017-02-12.
 */
public class ServerChatThread extends ChatThread implements Runnable {
    private ArrayList<String> nameList;
    private Socket socket;
    private ArrayList<Socket> socketList;
    private ServerChatFrame serverChatFrame;


    public ServerChatThread(Socket socket, String name, Color textColor){
        //super(socket, name, textColor);
        this.socket = socket;
        this.name = name;
        this.textColor = textColor;
        System.out.println(socket);

        try{
            outText = new PrintWriter(socket.getOutputStream(), true);
            inText = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (UnknownHostException e){
            System.out.println("bam");
        }catch (IOException f){
            System.out.println("b88oom");
        }

        nameList = new ArrayList();
        socketList = new ArrayList();
        socketList.add(socket);
        serverChatFrame = new ServerChatFrame(name, textColor);


    }

    @Override
    public void run() {
        serverChatFrame.setServerChatThread(this);
        done = false;
        while(!done){
            try{
                String s = inText.readLine();
                System.out.println(s);
                if (s==null){
                    System.out.println("Server disconnect");
                    done = true;
                }else {
                    //ta hand om xml-strängen och sedan lägga in texten med korrekt namn och färg i chatText
                    //XmlParser.parse(s) returnerar lista med 0. text 1. namn 2. färg 3. indikator
                    serverChatFrame.writeToChat(s, "sdf", Color.RED);
                }
            }catch (IOException e){

            }
        }
    }

    public String toString(){
        String retString = "Samtal nr " + serverChatFrame.getServerNumber();
        /*for (int i=0 ; i<nameList.size()-1 ; i++){
            retString = retString + nameList.get(i) + ", ";
        }
        retString = retString + nameList.get(nameList.size()-1);*/
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
