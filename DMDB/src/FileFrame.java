import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Dexter on 2017-03-02.
 */
public class FileFrame extends JFrame implements ActionListener {
    private JButton selectFile, sendFile, cancel;
    private File selectedFile;
    private JComboBox selectFileRecipient;
    private JTextPane fileInfo;
    private ChatFrame chatFrame;

    public FileFrame(ChatFrame chatFrame){
        this.chatFrame = chatFrame;

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        selectFile = new JButton("Välj fil");
        sendFile = new JButton("Skicka");
        cancel = new JButton("Avbryt");
        //selectFileRecipient = new JComboBox();
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
        pan2.setLayout(new GridLayout(1, 2));
        add(fileInfo);
        pan2.add(cancel);
        pan2.add(sendFile);
        add(pan2);

        //add(sendFile);

        selectFile.addActionListener(this);
        sendFile.addActionListener(this);
        cancel.addActionListener(this);

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
                chatFrame.submitFile(selectedFile, fileInfo.getText());
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
    }
}
