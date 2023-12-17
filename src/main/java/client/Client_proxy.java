package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static client.Client_Thread.getChatList1;
import static client.Client_Thread.getState1;

//import static client.Client_LoginController.getsNickname;

public class Client_proxy {
    private static ArrayList<String> stringToServer;
    private static ArrayList<String> chatlist1 = new ArrayList<String>();

    private static Boolean state;

    public static void setLogin(String nickname, String password) { //arraylist = "login", nickname, password
        stringToServer = new ArrayList<>();
        stringToServer.add("login");
        stringToServer.add(nickname);
        stringToServer.add(password);
        sendToServerProxy();
    }

    public static void setRegister(String name, String surname, String nickname, String password) { //arraylist = "register", name, surname, nickname, password
        stringToServer = new ArrayList<>();
        stringToServer.add("register");
        stringToServer.add(name);
        stringToServer.add(surname);
        stringToServer.add(nickname);
        stringToServer.add(password);
        sendToServerProxy();
    }

    public static void receiveChatList(String nickname) { //arraylist = "chatlist", nickname
        stringToServer = new ArrayList<>();
        stringToServer.add("chatlist");
        stringToServer.add(nickname);
        sendToServerProxy();
    }

    public static void sendToServerProxy() {
        try {
            Socket sClient = new Socket("127.0.0.1", 8000);
            System.out.println("[Client]: socket creata.");


                Client_Thread clientThread = new Client_Thread(stringToServer, sClient);
                clientThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean getStateProxy() {return true;
    }
    public static void setChatList(ArrayList<String> chatlist) {
        System.out.println("sjdakjsfkajhdfalsfalsjfjafds"+chatlist);
    chatlist1=chatlist;
    }
    public static ArrayList<String> getChatList() {return chatlist1;}
}
