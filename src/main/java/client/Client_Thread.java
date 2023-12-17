package client;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class Client_Thread extends Thread {
    private ArrayList<String> stringToServer;
    private Object stringToClient;
    private static Boolean state;
    private Socket sClient;
    private static DataOutputStream toServer;
    private static ObjectInputStream fromServer;

    public Client_Thread(ArrayList<String> data, Socket sClient) {
        this.stringToServer = data;
        this.sClient = sClient;
    }

    @Override
    public void run() {
        try {
            toServer = new DataOutputStream(sClient.getOutputStream());
            fromServer = new ObjectInputStream(sClient.getInputStream());
            toServer.writeInt(stringToServer.size());

            for (int i = 0; i < stringToServer.size(); i++) {
                toServer.writeUTF(stringToServer.get(i));
            }

            String caseClass = stringToServer.get(0);
            stringToServer.remove(0);

            switch (caseClass){
                case "login":
                    state = fromServer.readBoolean();
                    break;
                case "register":
                    break;
                case "chatlist":

                    break;
                default:
                    break;
            }

            toServer.close();
            sClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getStringToClient() {
        return stringToClient;
    }
    public static Boolean getState1(){
        return state;
    }

    public static ArrayList<String> getChatList(){
        ArrayList<String> chatlistFromServer= new ArrayList<>();

        try{
            chatlistFromServer = fromServer.readObject();
        }catch (Exception e){
            e.printStackTrace(;);
        }


        return chatlistFromServer;
    }
}
