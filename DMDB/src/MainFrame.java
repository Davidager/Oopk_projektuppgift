import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

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
    private String hostIP;
    public static AtomicInteger atomicInteger = new AtomicInteger();


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

        String[] items = {"Blå", "Röd", "Svart", "Grön"};
        //Color[] items = {Color.BLUE, Color.RED, Color.BLACK, Color.GREEN};
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
        okClientButton.addActionListener(this);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void okServerButtonPressed(String name, Color textColor){
        listeningThread = new ListeningThread(serverPortNumber, name, textColor);
    }

    public void okClientButtonPressed(String name, Color textColor){
        try{
            System.out.println(hostIP + " " + hostPortNumber);
            Socket socket = new Socket(hostIP, hostPortNumber);

            requestConnection(socket, name);
            chatThread = new ChatThread(socket, name, textColor);
            chatThread.start();
        } catch (IOException f){
            System.out.println("Något är fel med input :)");   //lägga till någon popup
        }


    }

    private void requestConnection(Socket socket, String name) {
        String requestString = "<request>" + name + " vill skapa ett chattsamtal!</request>";
        try{
            new PrintWriter(socket.getOutputStream(), true).println(requestString);
        } catch (IOException e) {
            System.out.println("ojdå");
        }
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == okClientButton) {
            String name = nameInputField.getText();
            Color textColor = getColor();
            if (name.length() != 0 && textClientPort.getText().length() != 0 && textClientIP.getText().length() !=0){
                hostPortNumber = Integer.parseInt(textClientPort.getText());

                hostIP = textClientIP.getText();

                okClientButtonPressed(name, textColor);
            }
        }
        if (e.getSource() == okServerButton) {
            String name = nameInputField.getText();
            Color textColor = getColor();
            if (name.length() != 0 && textInpServer.getText().length() != 0){
                serverPortNumber = Integer.parseInt(textInpServer.getText());
                okServerButtonPressed(name, textColor);
            }
            //lägga till ett felmeddelande som else här
        }
    }

    private Color getColor() {
        String colorString = (String)colorSelector.getSelectedItem();
        if (colorString.equals("Blå")) return Color.BLUE;
        if (colorString.equals("Röd")) return Color.RED;
        if (colorString.equals("Grön")) return Color.GREEN;
        if (colorString.equals("Svart")) return Color.BLACK;
        return Color.BLACK;
    }

    public static void main(String[] args) {
        new MainFrame();

    }

}
