import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Dexter on 2017-02-22.
 */
public class ChatFrame extends JFrame implements ActionListener{
    private JPanel windowPanel;
    private JButton sendButton,discButton,fileButton;
    private JToggleButton aesButton, caesarButton;
    private JTextPane myText;
    private JTextPane chatText;

    private ChatThread chatThread;
    private Color myColor;
    private String myName;
    public int hej;

    public ChatFrame(ChatThread chatThread, String myName, Color myColor){
        this(myName, myColor);
        this.chatThread=chatThread;

        //fortsätt här!

    }

    public ChatFrame(String myName, Color myColor) {
        this.myName = myName;
        this.myColor = myColor;
        this.setTitle("ChatFrame");
        windowPanel = new JPanel();
        windowPanel.setLayout(new BoxLayout(windowPanel, BoxLayout.PAGE_AXIS));

        sendButton = new JButton("Skicka");
        discButton = new JButton("Koppla bort");
        fileButton = new JButton("Filöverföring");
        aesButton = new JToggleButton(new StyledEditorKit.BoldAction());
        aesButton.setText("AES");
        caesarButton = new JToggleButton(new StyledEditorKit.ItalicAction());
        caesarButton.setText("Caesar");

        chatText = new JTextPane();
        chatText.setPreferredSize(new Dimension(500, 300));
        chatText.setEditable(false);


        JPanel lowerPanel = new JPanel();

        myText = new JTextPane();
        myText.setPreferredSize(new Dimension(300,50));
        setUpEnterProperly(myText);
        myText.setCaret(new NoTextSelectionCaret());
        myText.setHighlighter(null);
        myText.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                updateEncryptButtons();
                //System.out.println(myText.getStyledDocument().getCharacterElement(myText.getCaretPosition()-1).getAttributes().containsAttribute(StyleConstants.Bold, true));
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,2));
        buttonPanel.add(sendButton);
        buttonPanel.add(discButton);
        buttonPanel.add(fileButton);
        buttonPanel.add(aesButton);
        buttonPanel.add(caesarButton);

        sendButton.addActionListener(this);
        discButton.addActionListener(this);
        fileButton.addActionListener(this);
        aesButton.addActionListener(this);
        caesarButton.addActionListener(this);

        lowerPanel.add(new JScrollPane(myText));
        lowerPanel.add(buttonPanel);

        windowPanel.add(new JScrollPane(chatText));
        windowPanel.add(lowerPanel);

        add(windowPanel);
        pack();

        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String[] options = {"Ja", "Nej"};
                int confirm = JOptionPane.showOptionDialog(
                        null, "Vill du stänga den här chatten?",
                        "OBS!", 0,
                        JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (confirm == 0) {
                    closeThread();
                    frameClose();


                }
            }
        };
        this.addWindowListener(exitListener);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        myText.grabFocus();
        setVisible(true);

    }

    protected void closeThread() {
        chatThread.closeThread();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            submitText();
        }
        if (e.getSource() == discButton) {
            disconnect();
        }
        if (e.getSource() == fileButton) {
            System.out.println(myText.getText());
        }
        if (e.getSource() == aesButton) {

            if (caesarButton.isSelected()){
                aesButton.setSelected(false);
                caesarButton.doClick();
                aesButton.setSelected(true);

            }
            myText.grabFocus();
        }
        if (e.getSource() == caesarButton) {
            if (aesButton.isSelected()) {
                caesarButton.setSelected(false);
                aesButton.doClick();
                caesarButton.setSelected(true);

            }
            myText.grabFocus();
        }

        if (e.getSource() == myText) {
            //updateEncryptButtons();
        }
    }

    private void updateEncryptButtons () {
        //AttributeSet attributeSet = myText.getCharacterAttributes();
        AttributeSet attributeSet = myText.getStyledDocument().getCharacterElement(myText.getCaretPosition()-1).getAttributes();
        //System.out.println(myText.getStyledDocument().getCharacterElement(myText.getCaretPosition()-1).getAttributes().containsAttribute(StyleConstants.Italic, true));
        if (attributeSet.containsAttribute(StyleConstants.Bold, true)) {
            //System.out.println(attributeSet.getAttribute(StyleConstants.Bold));
            if (!aesButton.isSelected()) {
                aesButton.setSelected(true);
            }
            if (caesarButton.isSelected()) {
                caesarButton.setSelected(false);
            }
        } else if (attributeSet.containsAttribute(StyleConstants.Italic, true)) {
            if (!caesarButton.isSelected()) {
                caesarButton.setSelected(true);
            }
            if (aesButton.isSelected()) {
                aesButton.setSelected(false);
            }
        } else {
            if (caesarButton.isSelected()) {
                caesarButton.setSelected(false);
            }
            if (aesButton.isSelected()) {
                aesButton.setSelected(false);
            }
        }
    }

    protected void disconnect() {
        closeThread();
        frameClose();
    }

    protected void frameClose() {
        setVisible(false);
        dispose();
    }

    // Gör så att man kan skicka med enter, och skapa ny rad med shift + enter
    private void setUpEnterProperly(JTextPane textPane) {
        InputMap iMap = textPane.getInputMap();
        ActionMap aMap = textPane.getActionMap();
        iMap.put(KeyStroke.getKeyStroke("shift ENTER"), "insert-break");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        aMap.put("enter", new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("enter pressed");
                submitText();
            }

        });
    }

    protected String getMyName() {
        return myName;
    }

    protected Color getMyColor() {
        return myColor;
    }

    protected JTextPane getMyText() {
        return myText;
    }

    protected void submitText() {
        String textMessage = submitHelper();
        if (textMessage.isEmpty()) return;
        chatThread.sendText(textMessage);


    }

    protected String submitHelper() {
        String textMessage = myText.getText();
        if (textMessage.isEmpty()) return "";   // inte skicka något om inget är inskrivet
        //StringBuilder stringBuilder = new StringBuilder(textMessage);

        // lägg till krypto-taggar

        //System.out.println(textMessage);

        writeToChat(textMessage, myName, myColor);
        textMessage = textMessage.replaceAll("<", "&lt;");
        textMessage = textMessage.replaceAll(">", "&gt;");

        String hexaColor = String.format("#%02X%02X%02X", myColor.getRed(),
                myColor.getGreen(), myColor.getBlue());
        textMessage = "<message sender=\"" + myName + "\"><text color=\""
                + hexaColor + "\">" + textMessage + "</text></message>";
        myText.setText("");
        return textMessage;
    }

    public void writeToChat(String text, String name, Color color) {
        StyledDocument chatDoc = chatText.getStyledDocument();

        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, color);

        try {
            chatDoc.insertString(chatDoc.getLength(), name + ": " + text + "\n", keyWord);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setChatThread(ChatThread chatThread) {
        this.chatThread = chatThread;
    }

    public static void main(String[] args) {
        new ChatFrame("david", Color.RED);
    }

    private class NoTextSelectionCaret extends DefaultCaret {
        @Override
        public int getMark() {
            return getDot();
        }
    }



}
