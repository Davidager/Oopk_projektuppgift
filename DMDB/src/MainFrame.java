import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.io.*;

/**
 * Created by Dexter on 2017-02-08.
 */
public class MainFrame extends JFrame implements ActionListener {
    JEditorPane textInpServer, textClientPort, textClientIP, nameInputField;
    JButton okServerButton, okClientButton, closeMain;
    JComboBox colorSelector;
    ListeningThread listeningThread;
    int serverPortNumber, hostPortNumber;
    InetAddress hostIP;
    String name, textColor;

    public MainFrame(){
        //kasta in alla visuella grejer i mainframe här
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        textInpServer = new JEditorPane();
        textClientPort = new JEditorPane();
        textClientIP = new JEditorPane();
        nameInputField = new JEditorPane();

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
        //chatThread = new ChatThread(socket, name, textColor);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == okClientButton);{
            okClientButtonPressed();
        }
        if (e.getSource() == okServerButton);{
            serverPortNumber = Integer.parseInt(textInpServer.getText());
            name = nameInputField.getText();
            textColor = (String)colorSelector.getSelectedItem();
            okServerButtonPressed();
            System.out.println("hi");
        }
    }

    public static void main(String[] args) {
        new MainFrame();

    }
}
