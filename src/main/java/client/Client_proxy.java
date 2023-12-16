package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Client_proxy {
    private static ArrayList<String> stringToServer;
    private static Boolean state;
    public static void setLogin(String nickname, String password){
        stringToServer = new ArrayList<>();
        stringToServer.add("login");
        stringToServer.add(nickname);
        stringToServer.add(password);
        sendToServerProxy();
    }

    public static void setRegister(String name, String surname, String nickname, String password){
        stringToServer = new ArrayList<>();
        stringToServer.add("register");
        stringToServer.add(name);
        stringToServer.add(surname);
        stringToServer.add(nickname);
        stringToServer.add(password);
        sendToServerProxy();
    }

    public static void sendToServerProxy(){
        try{
            Socket sClient = new Socket ("127.0.0.1", 8000 );
            System.out.println ("[Client]: socket creata." );

            DataOutputStream toServer = new DataOutputStream ( sClient.getOutputStream() );
            DataInputStream fromServer = new DataInputStream ( sClient.getInputStream() );

            toServer.writeInt(stringToServer.size());
            for (int i = 0; i < stringToServer.size(); i++) {
                toServer.writeUTF(stringToServer.get(i));
            }

            state = fromServer.readBoolean();

            toServer.close();
            sClient.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean getState(){
        return state;
    }


}
