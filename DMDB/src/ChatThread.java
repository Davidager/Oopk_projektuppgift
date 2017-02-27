import java.awt.*;
import java.io.*;
import java.lang.Thread;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
/**
 * Created by Dexter on 2017-02-09.
 */
public class ChatThread extends Thread implements Runnable{
    private int hostPortNumber;
    private String name,textColor;
    private InetAddress hostIP;
    private Socket socket;
    private ChatFrame chatFrame;
    private BufferedReader inText;
    private PrintWriter outText;
    private InputStream inFile;
    private OutputStream outFile;
    private Thread receivingThread;
    private Boolean done;

    public ChatThread(Socket socket, String name, String textColor){
        this.socket = socket;
        this.name = name;
        this.textColor = textColor;


        try{
            outText = new PrintWriter(socket.getOutputStream(), true);
            inText = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (UnknownHostException e){
            System.out.println("bam");
        }catch (IOException f){
            System.out.println("boom");
        }

        chatFrame = new ChatFrame("sdf", Color.RED);
        receivingThread = new Thread(this);
        receivingThread.start();

    }

    public void run(){
        done = false;
        while(!done){
            try{
                String s = inText.readLine();
                if (s==null){
                    System.out.println("Server disconnect");
                    done = true;
                }else {
                    //ta hand om xml-strängen och sedan lägga in texten med korrekt namn och färg i chatText
                    //XmlParser.parse(s) returnerar lista med 0. text 1. namn 2. färg
                    chatFrame.writeToChat(s, "sdf", Color.RED);
                }
            }catch (IOException e){

            }
        }
    }

    public void closeThread(){
        done = true;    //hur funkar detta med reveivingthread?
        receivingThread.stop();  //hur göra?

    }

    public void sendText(String str){
        outText.print(str);
    }

    public void receiveText(String str){

    }

}
