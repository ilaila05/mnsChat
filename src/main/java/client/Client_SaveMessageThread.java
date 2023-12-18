package client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client_SaveMessageThread extends Thread {

// Other methods and connection handling remain the same...
private Socket sClient;
    private InputStream inputStream;
    private DataInputStream fromServer;
    private ArrayList<String> stringToServer;
    public DataOutputStream toServer;

    public Client_SaveMessageThread(Socket sClient, ArrayList<String> stringToServer) {
        this.sClient = sClient;
        this.stringToServer = stringToServer;
    }
    public void run() {
        try {
            inputStream = sClient.getInputStream();
            toServer= new DataOutputStream(sClient.getOutputStream());
            fromServer = new DataInputStream(sClient.getInputStream());
            toServer.writeInt(stringToServer.size());

            for (String s : stringToServer) {
                toServer.writeUTF(s);
            }
            toServer.flush();


        }catch (Exception e){e.printStackTrace();}
    }
}