import javax.swing.*;

/**
 * Created by Dexter on 2017-02-22.
 */
public class ChatFrame extends JFrame {
    private JButton sendButton,discButton,fileButton;
    private JTextField myText;
    private JTextPane chatText;
    private ChatThread chatThread;
    private JFrame chatFrame;

    public ChatFrame(ChatThread chatThread){
        this.chatThread=chatThread;

        //fortsätt här!
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(discButton);
        buttonPanel.add(fileButton);
    }
}
