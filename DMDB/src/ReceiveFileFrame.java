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
    private ServerChatThread serverChatThread;
    private String fileInfo, fileName, fileSize, name, cryptoType, cryptoKey;
    private JButton accept,deny;
    private JTextField infoResponse;
    private Socket socket;
    private boolean chatThreadYes;
    private ServerChatThread.ReceivingThread receivingThread;

    public ReceiveFileFrame(String fileInfo, String name, String fileName, String fileSize, String cryptoType, String cryptoKey){
        this.fileInfo = fileInfo;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.name = name;
        this.cryptoType = cryptoType;
        this.cryptoKey = cryptoKey;

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        JLabel lab = new JLabel("Avsändare: " + name);
        JLabel lab2 = new JLabel("Filnamn: " + fileName);
        JLabel lab3 = new JLabel("Filstorlek: " + fileSize + " bytes");
        infoResponse = new JTextField("Skicka ett svar till " + name);
        infoResponse.setPreferredSize(new Dimension(300,50));
        accept = new JButton("Acceptera fil");
        deny = new JButton("Neka fil");
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
    public ReceiveFileFrame(ChatThread chatThread, String fileInfo, String name, String fileName, String fileSize, String cryptoType, String cryptoKey) {
        this(fileInfo, name, fileName, fileSize, cryptoType, cryptoKey);
        chatThreadYes = true;
        this.chatThread = chatThread;

    }

    public ReceiveFileFrame(ServerChatThread serverChatThread, ServerChatThread.ReceivingThread receivingThread, String fileInfo, String name, String fileName, String fileSize, String cryptoType, String cryptoKey) {
        this(fileInfo, name, fileName, fileSize, cryptoType, cryptoKey);
        chatThreadYes = false;
        this.serverChatThread = serverChatThread;
        this.receivingThread = receivingThread;
    }

    public void actionPerformed(ActionEvent e){
        String fileMessage;
        String s = infoResponse.getText();
        int port = 4555;
        if (chatThreadYes) {
            System.out.println("check5");
            if (e.getSource() == accept) {
                System.out.println("check3");

                fileMessage = "<message sender=\"" + chatThread.getMyName() + "\"><fileresponse reply=\"" +
                        "yes" + "\" port=\"" + Integer.toString(port) + "\">" + s + "</fileresponse></message>";
                //chatThread.sendText(fileMessage);
                chatThread.sendText("<message sender=\"" + "System" + "\"><text color=\""
                        + "#7c7777" + "\">" + "Startar filöverföring" + "</text></message>");
                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }*/


                chatThread.sendText(fileMessage);
                FileThread fileThread = new FileThread(chatThread.getSocket());
                fileThread.setCryptoInfo(cryptoType, cryptoKey);
                fileThread.startReceivingFile(port, fileSize, fileName);
            }
            if (e.getSource() == deny) {
                fileMessage = "<message sender=\"" + chatThread.getMyName() + "\"><fileresponse reply=\"" +
                        "no" + "\" port=\"" + Integer.toString(port) + "\">" + s + "</fileresponse></message>";
                chatThread.sendText(fileMessage);
            }
        } else {
            System.out.println("check4");
            if (e.getSource() == accept) {
                System.out.println("check1");

                fileMessage = "<message sender=\"" + serverChatThread.getMyName() + "\"><fileresponse reply=\"" +
                        "yes" + "\" port=\"" + Integer.toString(port) + "\">" + s + "</fileresponse></message>";
                receivingThread.sendToOne("<message sender=\"" + "System" + "\"><text color=\""
                        + "#7c7777" + "\">" + "Startar filöverföring" + "</text></message>");
                receivingThread.sendToOne(fileMessage);
                FileThread fileThread = new FileThread(receivingThread.getThreadSocket());
                fileThread.setCryptoInfo(cryptoType, cryptoKey);
                fileThread.startReceivingFile(port, fileSize, fileName);
            }
            if (e.getSource() == deny) {

                fileMessage = "<message sender=\"" + serverChatThread.getMyName() + "\"><fileresponse reply=\"" +
                        "no" + "\" port=\"" + Integer.toString(port) + "\">" + s + "</fileresponse></message>";
                receivingThread.sendToOne(fileMessage);
            }
        }
        setVisible(false);
        dispose();
    }

    //public static void main(String[] args) {
        //new ReceiveFileFrame("a", "b", "c", "d");
    //}
}
