package client;




import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static client.Client_proxy.setChat;


public class Client_ChatThread extends Thread {

// Other methods and connection handling remain the same...
private Socket sClient;
    private InputStream inputStream;
    private DataInputStream fromServer;
    private ArrayList<String> stringToServer;
    public DataOutputStream toServer;

    public Client_ChatThread(Socket sClient, ArrayList<String> stringToServer) {
        this.sClient = sClient;
        this.stringToServer = stringToServer;
    }
    public void run() {
        try {
            inputStream = sClient.getInputStream();
            toServer= new DataOutputStream(sClient.getOutputStream());
            fromServer = new DataInputStream(sClient.getInputStream());

            toServer.writeInt(stringToServer.size());
            for (int i = 0; i < stringToServer.size(); i++) {
                toServer.writeUTF(stringToServer.get(i));
            }

            int size = fromServer.readInt()/2;
            HashMap<String, String> receivedChat = new HashMap<>();

            for (int i = 0; i < size; i++) {
                String sender = fromServer.readUTF();
                String message = fromServer.readUTF();

                // Assuming Sender and Message are the keys in the HashMap
                receivedChat.put("Sender" + i, sender);
                receivedChat.put("Message" + i, message);
            }
            setChat(receivedChat);


        }catch (Exception e){e.printStackTrace();}
    }

}