import javax.swing.*;
import java.awt.*;

/**
 * Created by David on 23-Feb-17.
 */
public class ServerChatFrame extends ChatFrame{
    private ServerChatThread serverChatThread;

    public ServerChatFrame(ServerChatThread serverChatThread, String myName, Color myColor) {
        super(myName, myColor);
        this.serverChatThread = serverChatThread;

    }

    @Override
    public void disconnect() {

    }
}
