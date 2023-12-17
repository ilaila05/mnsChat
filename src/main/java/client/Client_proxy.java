package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import static client.Client_Thread.getChatList1;
import static client.Client_Thread.getState1;

//import static client.Client_LoginController.getsNickname;

public class Client_proxy {
    private static ArrayList<String> stringToServer;
    private static Object stringToClient;

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

            while (true) {
                Client_Thread clientThread = new Client_Thread(stringToServer, sClient);
                clientThread.start(); // Starts the thread
                clientThread.join();
            }



            /*
             *   questo appunto e per te ilaria
             *    il problema vero e' come gestire le 2 richieste \(?)
             *   puoi mandare un oggetto e fin qua pero i problemi sono altri
             * sei vai nel thread ti ho fatto leggere i nick name e gli ho fatti mettere dentro un array list
             * il nickname lo prende dalla label che c'e dentro la chat list (che e' la prima cosa che vado a settare)
             * non posso prendere una cosa da una finestra che e' chiusa
             * il punto e' come gestire le 2 rischieste essendo che la richiesta e' su qeusto ordine
             * login-setlogin-thread-salvastate-chiudelogin-aprechatlist
             * nella rischiesta di chat list pero ci sono dei problemi nel read che non capisco
             * spiace
             * forse centrano i objectinputstream
             * almeno penso
             * se prorio modifica e togli che manda solo l'oggetto
             *
             *
             *
             * */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean getStateProxy() {return getState1();
    }

    public static ArrayList<String> getChatList() {return getChatList1();}
}
