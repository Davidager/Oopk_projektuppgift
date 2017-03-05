import java.net.InetAddress;

/**
 * Created by Dexter on 2017-03-05.
 */
public class FileThread extends Thread implements Runnable {
    private String infoResponse;
    private ChatThread chatThread;

    public FileThread(String infoResponse, ChatThread chatThread){
        this.infoResponse = infoResponse;
        this.chatThread = chatThread;


    }

    public void run(){

    }
}
