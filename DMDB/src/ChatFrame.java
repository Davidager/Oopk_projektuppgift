import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
    private SimpleAttributeSet attributeSet;

    private StyledEditorKit.BoldAction aesBoldActionListener;
    private StyledEditorKit.ItalicAction caesarItalicActionListener;

    //private JFrame chatFrame;

    public ChatFrame(ChatThread chatThread){
        this();
        this.chatThread=chatThread;

        //fortsätt här!

    }

    public ChatFrame() {
        windowPanel = new JPanel();
        windowPanel.setLayout(new BoxLayout(windowPanel, BoxLayout.PAGE_AXIS));

        sendButton = new JButton("Skicka");
        discButton = new JButton("Koppla bort");
        fileButton = new JButton("Filöverföring");
        aesButton = new JToggleButton("AES");
        caesarButton = new JToggleButton("Caesar");

        chatText = new JTextPane();
        chatText.setPreferredSize(new Dimension(500, 300));
        chatText.setEditable(false);

        JPanel lowerPanel = new JPanel();

        myText = new JTextPane();
        myText.setPreferredSize(new Dimension(300,50));
        setUpEnterProperly(myText);
        attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet, false);
        myText.getStyledDocument().setCharacterAttributes(0, myText.getText().length(), attributeSet, false);






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
        myText.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                updateEncryptButtons();
            }
        });

        aesBoldActionListener = new StyledEditorKit.BoldAction();
        //aesButton.addActionListener(ae);
        caesarButton.addActionListener(this);
        caesarItalicActionListener = new StyledEditorKit.ItalicAction();

        //caesarButton.addActionListener(new StyledEditorKit.ItalicAction());


        lowerPanel.add(new JScrollPane(myText));
        lowerPanel.add(buttonPanel);

        windowPanel.add(new JScrollPane(chatText));
        windowPanel.add(lowerPanel);

        add(windowPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //måste nog tas bort
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            submitText();
        }
        if (e.getSource() == discButton) {

        }
        if (e.getSource() == fileButton) {

        }
        if (e.getSource() == aesButton) { //|| e.getSource() == caesarButton) {
            if (caesarButton.isSelected()){
                caesarButton.setSelected(false);
                caesarItalicActionListener.actionPerformed(new ActionEvent
                        (this, ActionEvent.ACTION_PERFORMED, null){});

            }
            aesBoldActionListener.actionPerformed(new ActionEvent
                    (this, ActionEvent.ACTION_PERFORMED, null){});
        }
        if (e.getSource() == caesarButton) {
            if (aesButton.isSelected()) {
                aesButton.setSelected(false);
                aesBoldActionListener.actionPerformed(new ActionEvent
                        (this, ActionEvent.ACTION_PERFORMED, null){});

            }
            System.out.println("1" + myText.getCharacterAttributes().getAttribute(StyleConstants.Italic));
            caesarItalicActionListener.actionPerformed(new ActionEvent
                    (this, ActionEvent.ACTION_PERFORMED, null){});
            myText.grabFocus();

            System.out.println(myText.getCharacterAttributes().getAttribute(StyleConstants.Italic));
        }

        if (e.getSource() == myText) {
            updateEncryptButtons();
        }
    }

    private void updateEncryptButtons () {
        AttributeSet attributeSet = myText.getCharacterAttributes();
        System.out.println(myText.getStyledDocument().getCharacterElement(myText.getCaretPosition()-1).getAttributes().containsAttribute(StyleConstants.Italic, true));
        if ((boolean)attributeSet.getAttribute(StyleConstants.Bold)) {
            System.out.println(attributeSet.getAttribute(StyleConstants.Bold));
            if (!aesButton.isSelected()) {
                aesButton.setSelected(true);
            }
            if (caesarButton.isSelected()) {
                caesarButton.setSelected(false);
            }
        } else if ((boolean)attributeSet.getAttribute(StyleConstants.Italic)) {
            if (!caesarButton.isSelected()) {
                caesarButton.setSelected(true);
            }
            if (aesButton.isSelected()) {
                aesButton.setSelected(false);
            }
        } else {
            if (caesarButton.isSelected()) {
                caesarButton.setSelected(false);
                System.out.println("caaaa");
                System.out.println(attributeSet.getAttribute(StyleConstants.Italic));
            }
            if (aesButton.isSelected()) {
                aesButton.setSelected(false);
            }
        }
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

    private void submitText() {
        String textMessage = myText.getText();
        if (textMessage.isEmpty()) return;   // inte skicka något om inget är inskrivet
        System.out.println("asdasdasd");

    }

    public static void main(String[] args) {
        new ChatFrame();
    }



}
