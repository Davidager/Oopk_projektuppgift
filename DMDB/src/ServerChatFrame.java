import javax.swing.*;
import java.awt.*;

/**
 * Created by David on 23-Feb-17.
 */
public class ServerChatFrame extends ChatFrame{
    private ServerChatThread serverChatThread;
    private int serverNumber;

    public ServerChatFrame( String myName, Color myColor) {
        super(myName, myColor);
        serverNumber = MainFrame.atomicInteger.get();
        this.setTitle("Server chat " + serverNumber);
        //this.serverChatThread = serverChatThread;

    }

    @Override
    protected void disconnect() {

    }

    public int getServerNumber() {
        return serverNumber;
    }

    @Override
    protected void submitText() {
        String textMessage = submitHelper();
        if (textMessage.isEmpty()) return;
        serverChatThread.sendText(textMessage);
    }

    public void setServerChatThread(ServerChatThread serverChatThread) {
        this.serverChatThread = serverChatThread;
    }
}
