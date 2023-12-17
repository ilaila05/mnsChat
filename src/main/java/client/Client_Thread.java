package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import static client.Client_proxy.setChatList;

public class Client_Thread extends Thread {
    private ArrayList<String> stringToServer;
    private static Boolean state;
    private Socket sClient;
    private static DataOutputStream toServer;
    private static DataInputStream fromServer;
    private static ArrayList<String> chatlistFromServer;

    public Client_Thread(ArrayList<String> data, Socket sClient) {
        this.stringToServer = data;
        this.sClient = sClient;
    }

    @Override
    public void run() {
        try {
            toServer = new DataOutputStream(sClient.getOutputStream());
            fromServer = new DataInputStream(sClient.getInputStream());
            toServer.writeInt(stringToServer.size());

            for (int i = 0; i < stringToServer.size(); i++) {
                toServer.writeUTF(stringToServer.get(i));
            }

            String caseClass = stringToServer.get(0);
            stringToServer.remove(0);

            switch (caseClass){
                case "login":
                    state = true;
                    chatlistFromServer = new ArrayList<>();
                    try{
                        int size = fromServer.readInt();
                        for (int i = 0; i < size; i++) {
                            chatlistFromServer.add(fromServer.readUTF());
                        }
                        setChatList(chatlistFromServer);
                        System.out.println(chatlistFromServer);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "register":
                    break;
                case "chatlist":
                    chatlistFromServer = new ArrayList<>();
                    try{
                        int size = fromServer.readInt();
                        for (int i = 0; i < size; i++) {
                            chatlistFromServer.add(fromServer.readUTF());
                        }
                        System.out.println(chatlistFromServer);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

            toServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Boolean getState1(){
        return state;
    }


    public static ArrayList<String> getChatList1(){
        return chatlistFromServer;
    }
}
