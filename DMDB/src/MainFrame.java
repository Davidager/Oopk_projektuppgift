import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Dexter on 2017-02-08.
 */
public class MainFrame extends JFrame implements ActionListener {
    private JTextField textInpServer, textClientPort, textClientIP, nameInputField;
    private JButton okServerButton, okClientButton, closeMain;
    private JComboBox colorSelector;
    private ListeningThread listeningThread;
    private ChatThread chatThread;
    private int serverPortNumber, hostPortNumber;
    private InetAddress hostIP;
    private String name, textColor;
    private Socket socket;

    public MainFrame(){
        //kasta in alla visuella grejer i mainframe här
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        textInpServer = new JTextField();
        textClientPort = new JTextField();
        textClientIP = new JTextField();
        nameInputField = new JTextField();

        JLabel label1 = new JLabel("Ange namn och färg nedan",JLabel.LEADING);
        JLabel label2 = new JLabel("Ange portnummer", JLabel.LEADING);
        JLabel label3 = new JLabel("Ange portnummer samt IP-address",JLabel.LEADING);

        okClientButton = new JButton("Bekräfta klient");
        okServerButton = new JButton("Bekräfta server");

        String[] items = {"Blå", "Röd", "Gul"};
        colorSelector = new JComboBox(items);

        add(label1);
        add(nameInputField);
        add(colorSelector);
        add(label2);
        add(textInpServer);
        add(okServerButton);
        add(label3);
        add(textClientPort);
        add(textClientIP);
        add(okClientButton);

        okServerButton.addActionListener(this);
        //okClientButton.addActionListener(this);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void okServerButtonPressed(){
        listeningThread = new ListeningThread(serverPortNumber, name, textColor);
    }

    public void okClientButtonPressed(){
        try{
            socket = new Socket(hostIP, hostPortNumber);
        } catch (IOException f){
            System.out.println("boom");
        }

        chatThread = new ChatThread(socket, name, textColor);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == okClientButton);{
            name = nameInputField.getText();
            textColor = (String)colorSelector.getSelectedItem();
            if (name.length() != 0 && textClientPort.getText().length() != 0 && textClientIP.getText().length() !=0){
                hostPortNumber = Integer.parseInt(textClientPort.getText());
                try{
                    hostIP = InetAddress.getByName(textClientIP.getText());
                }catch(UnknownHostException e1){
                    System.out.println("IP failed: " + hostIP);
                    System.exit(-1);
                }
                okClientButtonPressed();
            }
        }
        if (e.getSource() == okServerButton);{
            name = nameInputField.getText();
            textColor = (String)colorSelector.getSelectedItem();
            if (name.length() != 0 && textInpServer.getText().length() != 0){
                serverPortNumber = Integer.parseInt(textInpServer.getText());
                okServerButtonPressed();
            }
            //lägga till ett felmeddelande som else här
        }
    }

    public static void main(String[] args) {
        new MainFrame();

    }
}
