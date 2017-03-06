import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

/**
 * Created by David on 23-Feb-17.
 */
public class ServerChatFrame extends ChatFrame{
    private ServerChatThread serverChatThread;
    private int serverNumber;

    public ServerChatFrame( String myName, Color myColor) {
        super(myName, myColor);
        serverNumber = MainFrame.atomicInteger.get();
        this.setTitle("Server chat " + serverNumber);



        //this.serverChatThread = serverChatThread;

    }

    @Override
    protected void fileButtonPressed() {
        new FileFrame(this, serverChatThread);
    }

    @Override
    protected void disconnect() {
        if (serverChatThread.getNumberConnected() == 1) {
            super.disconnect();
        } else {
            JFrame frame = new JFrame();
            Hashtable<Socket, PrintWriter> socketPrintTable = serverChatThread.getSocketPrintWriterHashtable();
            Hashtable<Socket, String> socketNameTable = serverChatThread.getSocketandNameHashtable();

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
                    return "Kicka " + connectedName;
                }
            }
            Object[] possibilities = new Object[1 + socketPrintTable.size()];
            possibilities[0] = "Stäng ner hela samtalet";
            int temp = 1;
            for (Socket key : socketPrintTable.keySet()) {
                if (socketNameTable.containsKey(key)) {
                    possibilities[temp] = new PossibilityHelper(key, socketNameTable.get(key));
                } else {
                    possibilities[temp] = new PossibilityHelper(key, "person med okänt namn");
                }
                temp++;
            }


            Thread helpThread = new Thread(new Runnable(){
                public void run(){
                    Object obj = JOptionPane.showInputDialog(
                            frame, "Vad vill du göra?", "Bortkoppling",
                            JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[0]);
                    if (obj == null) return;
                    if (obj.getClass() == "".getClass()) {
                        serverChatThread.closeThread();
                        frameClose();
                    } else {
                        PossibilityHelper posHelper = (PossibilityHelper)obj;
                        serverChatThread.removeSocket(posHelper.getSocket());//
                        try {
                            posHelper.getSocket().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            helpThread.start();
        }
    }

    public int getServerNumber() {
        return serverNumber;
    }

    protected void submitFile(File file, String fileInfo, Socket socket) {
        String fileMessage = submitFileHelper(file, fileInfo);
        System.out.println(fileMessage);
        FileThread fileThread = new FileThread(socket);
        fileThread.setFile(file);
        fileThread.startListeningForResponse();
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(fileMessage);
            out.println("<message sender=\"" + "david" + "\"><text color=\""
                    + "#000000" + "\">" + "yoyoyo" + "</text></message>");
            System.out.println(fileMessage + "from serverchatframe submitfile");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void submitText() {
        String textMessage = submitTextHelper();
        if (textMessage.isEmpty()) return;
        serverChatThread.sendText(textMessage);
    }

    @Override
    protected void closeThread() {
        serverChatThread.closeThread();
    }

    public void setServerChatThread(ServerChatThread sct) {
        this.serverChatThread = sct;
    }
}
