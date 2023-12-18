package client;




import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import static client.Client_proxy.setChatList;


public class Client_ChatListThread extends Thread {

// Other methods and connection handling remain the same...
private Socket sClient;
    private InputStream inputStream;
    private DataInputStream fromServer;
    private ArrayList<String> stringToServer;
    public DataOutputStream toServer;

    public Client_ChatListThread(Socket sClient, ArrayList<String> stringToServer) {
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

            ArrayList<String> chatList = new ArrayList<>();
            int count=fromServer.readInt();
            for (int i = 0; i < count; i++) {
                chatList.add(fromServer.readUTF());
            }
            setChatList(chatList);


        }catch (Exception e){e.printStackTrace();}
    }

}