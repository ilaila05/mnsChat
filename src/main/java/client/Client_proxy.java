package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client_proxy {
    private static ArrayList<String> stringToServer;
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
            Socket s = new Socket ("127.0.0.1", 8000 );
            System.out.println ("[Client]: socket creata." );

            DataOutputStream toServer = new DataOutputStream ( s.getOutputStream() );
            DataInputStream fromServer = new DataInputStream ( s.getInputStream() );

            toServer.writeInt(stringToServer.size());
            for (int i = 0; i < stringToServer.size(); i++) {
                toServer.writeUTF(stringToServer.get(i));
            }

            System.out.println(stringToServer.size());

            toServer.close();
            s.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
