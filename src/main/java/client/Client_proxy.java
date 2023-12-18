package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static client.Client_ChatController.getReceiver;
import static client.Client_ChatController.getSender;
import static client.Client_LoginController.getsNickname;

public class Client_proxy {
    private static Socket sClient;
    private static DataOutputStream toServer;
    private static DataInputStream fromServer;
    private static ArrayList<String> stringToServer;
    private static ArrayList<String> stringFromServer;
    private static Boolean state;
    private static List<String> chatlist;
    private static HashMap chat;
    public static boolean setLogin(String nickname, String password) throws IOException, InterruptedException {
        initializeConnection(); // Create the socket here or at the start of your application
        stringToServer = new ArrayList<>();
        stringToServer.add("login");
        stringToServer.add(nickname);
        stringToServer.add(password);

        // Create a new thread for login and start it
        Client_LoginThread loginThread = new Client_LoginThread(sClient, stringToServer);
        loginThread.start();
        loginThread.join();
        stringToServer.clear();
        return getState();
    }

    public static void setRegister(String name, String surname, String nickname, String password) throws IOException, InterruptedException { //arraylist = "register", name, surname, nickname, password
        initializeConnection();
        stringToServer = new ArrayList<>();
        stringToServer.add("register");
        stringToServer.add(name);
        stringToServer.add(surname);
        stringToServer.add(nickname);
        stringToServer.add(password);

        Client_RegisterThread registerThread = new Client_RegisterThread(sClient, stringToServer);
        registerThread.start();
        registerThread.join();
        stringToServer.clear();

    }

    public static List<String> receiveChatList() throws IOException, InterruptedException { //arraylist = "chatlist", nickname
        initializeConnection();
        stringToServer = new ArrayList<>();
        stringToServer.add("chatlist");
        stringToServer.add(getsNickname());
        Client_ChatListThread chatListThread = new Client_ChatListThread(sClient, stringToServer);
        chatListThread.start();
        chatListThread.join();
        return chatlist;
    }
    public static HashMap  receiveChat() throws IOException, InterruptedException { //arraylist = "chatlist", nickname
        initializeConnection();
        stringToServer = new ArrayList<>();
        stringToServer.add("chat");
        stringToServer.add(getSender());
        stringToServer.add(getReceiver());
        Client_ChatThread chatThread = new Client_ChatThread(sClient, stringToServer);
        chatThread.start();
        chatThread.join();
        return chat;
    }
    public synchronized static void saveMessage(String sender,String receiver,String message) throws InterruptedException {
        initializeConnection();
        stringToServer = new ArrayList<>();
        stringToServer.add("save");
        stringToServer.add(sender);
        stringToServer.add(receiver);
        stringToServer.add(message);
        Client_SaveMessageThread chatThread = new Client_SaveMessageThread(sClient, stringToServer);
        chatThread.start();
        chatThread.join();

    }

    public static void initializeConnection() {
        try {
            sClient = new Socket("127.0.0.1", 7777);
            System.out.println("[Client]: socket created.");


            toServer = new DataOutputStream(sClient.getOutputStream());
            fromServer = new DataInputStream(sClient.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (sClient != null) {
                sClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean getState(){
        return state;
    }
    public static void setState(boolean st){
        state=st;
    }

    public static void setChatList(List<String> st){
        chatlist=st;
    }
    public static void setChat(HashMap chat2){
        chat=chat2;
        System.out.println(chat2);
    }

}
