import javafx.concurrent.Task;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * Created by Dexter on 2017-03-05.
 */
public class FileThread extends Thread implements Runnable {
    private String infoResponse;
    private ChatThread chatThread;
    //BufferedReader myInText;
    Socket socket;
    File file;

    public FileThread(Socket socket){
        this.socket = socket;

    }

    public void run(){

    }

    protected void setFile(File file) {
        this.file = file;
    }

    protected void startReceivingFile(int portNumber, String fileSize, String fileName) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println("Could not listen to port: " + portNumber);
            System.out.println(e);
            return;
        }

        Thread helpThread = new Thread(new Runnable(){
            public void run(){
                while (true){
                    Socket clientSocket = null;
                    try {
                        clientSocket = serverSocket.accept();
                    } catch (IOException e) {
                        System.out.println("Accept failed: " + portNumber);
                        System.exit(-1);
                    }
                    //startReceivingFile2(clientSocket);
                    System.out.println("hello from receiving");
                    int bytesRead;
                    int current = 0;
                    FileOutputStream fileOutputStream = null;
                    BufferedOutputStream bufferedOutputStream = null;
                    String filePath = (new File("").getAbsolutePath()) + "\\" + fileName;
                    try {
                        // receive file
                        byte [] myByteArray  = new byte [Integer.parseInt(fileSize)+100];
                        InputStream is = clientSocket.getInputStream();
                        fileOutputStream = new FileOutputStream(filePath);
                        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                        bytesRead = is.read(myByteArray,0,myByteArray.length);
                        current = bytesRead;

                        do {
                            bytesRead =
                                    is.read(myByteArray, current, (myByteArray.length-current));
                            if(bytesRead >= 0) current += bytesRead;
                        } while(bytesRead > -1);

                        bufferedOutputStream.write(myByteArray, 0 , current);
                        bufferedOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        helpThread.start();
    }

    protected void startSendingFile(String[] parsedArray) {
        try {
            Socket newSocket = new Socket(socket.getInetAddress(), Integer.parseInt(parsedArray[3]));
            FileInputStream fileInputStream = null;
            BufferedInputStream bufferedInputStream = null;
            OutputStream outputStream = null;
            System.out.println("hello from sending");

            byte[] fileBytes = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.read(fileBytes, 0, fileBytes.length);
            outputStream = newSocket.getOutputStream();
            outputStream.write(fileBytes, 0, fileBytes.length);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void startListeningForResponse() {
        Thread helpThread = new Thread(new Runnable(){
            public void run(){
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<Void> future = executor.submit(new ListenForFileresponse());
                try {

                    try {
                        future.get(1, TimeUnit.MINUTES);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                } catch (TimeoutException e) {
                    future.cancel(true);
                }

                executor.shutdownNow();
            }
        });
        helpThread.start();

    }

    protected class ListenForFileresponse implements Callable<Void>{
        //boolean done;
        //BufferedReader myInText;

        @Override
        public Void call() {
            BufferedReader myInText;
            try {
                myInText = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Boolean done = false;
                while(!done) {
                    try {
                        System.out.println("y2o");
                        String s = myInText.readLine();
                        if (s == null) {
                            System.out.println("fafa");
                            done = true;
                        } else {
                            System.out.println(s);
                            String[] parsedArray = XmlParser.parse(s);
                            System.out.println(parsedArray[0] + "good job");
                            if (parsedArray[0].equals("fileresponse")) {
                                if (parsedArray[2].equals("yes")) {
                                    FileThread.this.startSendingFile(parsedArray);
                                }
                                done = true;
                            }
                        }
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
