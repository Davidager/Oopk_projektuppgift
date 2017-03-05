import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

/**
 * Created by Dexter on 2017-03-04.
 */
public class ReceiveFileFrame extends JFrame implements ActionListener{
    private ChatThread chatThread;
    private String fileInfo, fileName, fileSize, name;
    private JButton accept,deny;
    private JTextField infoResponse;
    private Socket socket;

    public ReceiveFileFrame(ChatThread chatThread, String fileInfo, String name, String fileName, String fileSize){
        this.chatThread = chatThread;
        this.fileInfo = fileInfo;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.name = name;

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        JLabel lab = new JLabel("Avsändare: " + name);
        JLabel lab2 = new JLabel("Filnamn: " + fileName);
        JLabel lab3 = new JLabel("Filstorlek: " + fileSize + " bytes");
        JTextField infoResponse = new JTextField("Skicka ett svar till " + name);
        infoResponse.setPreferredSize(new Dimension(300,50));
        JButton accept = new JButton("Acceptera fil");
        JButton deny = new JButton("Neka fil");
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3,1));
        infoPanel.add(lab);
        infoPanel.add(lab2);
        infoPanel.add(lab3);
        add(infoPanel);

        //add(lab);
        //add(lab2);
        //add(lab3);
        add(infoResponse);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(accept);
        buttonPanel.add(deny);
        add(buttonPanel);

        accept.addActionListener(this);
        deny.addActionListener(this);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == accept){
            String s = infoResponse.getText();
            String fileMessage = "";
            int port = 4455;
            fileMessage = "<message sender=\"" + myName + "\"><fileresponse reply=\"" + file.getName() + "\" port=\"" + Integer.toString(port) + "\">" + s + "</fileresponse></message>";

            chatThread.sendText(s);
            new FileThread(s, chatThread);
        }
        if (e.getSource() == deny){

        }
    }

    public static void main(String[] args) {
        new ReceiveFileFrame("a", "b", "c", "d");
    }
}
