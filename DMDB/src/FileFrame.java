import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

/**
 * Created by Dexter on 2017-03-02.
 */
public class FileFrame extends JFrame implements ActionListener {
    private JButton selectFile, sendFile, cancel;
    private JToggleButton aesButton, caesarButton;
    private File selectedFile;
    private JComboBox selectFileRecipient;
    private JTextPane fileInfo;
    private ChatFrame chatFrame;
    private ServerChatFrame serverChatFrame;
    private boolean chatFrameYes;

    public FileFrame() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        selectFile = new JButton("Välj fil");
        sendFile = new JButton("Skicka");
        cancel = new JButton("Avbryt");
        aesButton = new JToggleButton("AES");
        caesarButton = new JToggleButton("Caesar");
        JLabel label = new JLabel("OBS! Filer kan endast skickas till servern.");
        fileInfo = new JTextPane();
        fileInfo.setPreferredSize(new Dimension(300,50));
        JPanel pan = new JPanel();
        pan.setLayout(new GridLayout(2, 1));
        pan.add(selectFile);
        pan.add(label);
        add(pan);

        //add(label);
        //add(selectFile);
        //add(selectFileRecipient);
        JPanel pan2 = new JPanel();
        pan2.setLayout(new GridLayout(2, 2));
        add(fileInfo);
        pan2.add(aesButton);
        pan2.add(caesarButton);
        pan2.add(cancel);
        pan2.add(sendFile);

        add(pan2);

        //add(sendFile);

        selectFile.addActionListener(this);
        sendFile.addActionListener(this);
        cancel.addActionListener(this);
        aesButton.addActionListener(this);
        caesarButton.addActionListener(this);


    }

    public FileFrame(ChatFrame chatFrame){
        this();
        chatFrameYes = true;
        this.chatFrame = chatFrame;
        pack();
        setVisible(true);





    }


    public FileFrame(ServerChatFrame serverChatFrame, ServerChatThread serverChatThread) {
        this();
        chatFrameYes = false;
        this.serverChatFrame = serverChatFrame;
        Hashtable<Socket, PrintWriter> socketPrintTable = serverChatThread.getSocketPrintWriterHashtable();
        Hashtable<Socket, String> socketNameTable = serverChatThread.getSocketandNameHashtable();



        Object[] items = new Object[socketPrintTable.size()];
        int temp = 0;
        for (Socket key : socketPrintTable.keySet()) {
            if (socketNameTable.containsKey(key)) {
                items[temp] = new PossibilityHelper(key, socketNameTable.get(key));
            } else {
                items[temp] = new PossibilityHelper(key, "Person med okänt namn");
            }
            temp++;
        }
        selectFileRecipient = new JComboBox(items);
        add(selectFileRecipient);
        pack();
        setVisible(true);

    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == selectFile){
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        }
        if (e.getSource() == sendFile){
            //System.out.println(selectedFile.getName());
            //String s = "Förfrågan om att skicka följande fil: " + selectedFile.getName() + ". Vill du mottaga denna fil?";
            //chatFrame.writeToChat(s, chatFrame.getMyName(), chatFrame.getMyColor());
            try {

                if (chatFrameYes){
                    if (aesButton.isSelected()){
                        chatFrame.submitFile(selectedFile, fileInfo.getText(), "AES");
                        //System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT filemfrae");
                    } else if (caesarButton.isSelected()){
                        chatFrame.submitFile(selectedFile, fileInfo.getText(), "caesar");
                    } else{
                        chatFrame.submitFile(selectedFile, fileInfo.getText(), "");
                    }
                }
                else{
                    PossibilityHelper posHelper = (PossibilityHelper)selectFileRecipient.getSelectedItem();
                    if (aesButton.isSelected()){
                        //System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW  fileframe");
                        serverChatFrame.submitFile(selectedFile, fileInfo.getText(), posHelper.getSocket(), "AES");
                    } else if (caesarButton.isSelected()){
                        serverChatFrame.submitFile(selectedFile, fileInfo.getText(), posHelper.getSocket(), "caesar");
                    } else{
                        serverChatFrame.submitFile(selectedFile, fileInfo.getText(), posHelper.getSocket(), "");
                    }
                }
                setVisible(false);
                dispose();

            } catch (NullPointerException e1){
                JOptionPane.showMessageDialog(this, "Vänligen välj en fil!");
            }
            //chatFrame.submitFile(selectedFile, fileInfo.getText());

        }
        if (e.getSource() == cancel){
            setVisible(false);
            dispose();
        }
        if (e.getSource() == aesButton) {

            if (caesarButton.isSelected()){
                aesButton.setSelected(false);
                caesarButton.doClick();
                aesButton.setSelected(true);

            }

        }
        if (e.getSource() == caesarButton) {
            if (aesButton.isSelected()) {
                caesarButton.setSelected(false);
                aesButton.doClick();
                caesarButton.setSelected(true);

            }

        }
    }
}

class PossibilityHelper {
    String connectedName;
    public Socket socket;

    public PossibilityHelper(Socket socket, String connectedName) {
        this.socket = socket;
        this.connectedName = connectedName;
    }

    public Socket getSocket() {
        return socket;
    }

    public String toString() {
        return connectedName;
    }
}
