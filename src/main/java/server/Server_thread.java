package server;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Server_thread extends Thread{
    private Socket sClient;
    private InputStream inputStream;
    private DataInputStream dataInputStream;
    private static ArrayList<String> stringFromClient = new ArrayList<>();


    public Server_thread(Socket sClient) {
        this.sClient = sClient;
    }

    public void run() {
        try {
            inputStream = sClient.getInputStream();
            dataInputStream = new DataInputStream(inputStream);

            System.out.println("[Server-Thread]: in attesa del client");

            int size = dataInputStream.readInt();

            System.out.println(size);

            for (int i = 0; i < size; i++) {
                String element = dataInputStream.readUTF();
                stringFromClient.add(element);
            }

            dataInputStream.close();
            inputStream.close();
            sClient.close();

            String caseClass = stringFromClient.get(0);
            stringFromClient.remove(0);

            switch (caseClass){
                case "login":
                    login();
                    break;
                case "register":
                    register();
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void login(){
        System.out.println(stringFromClient);
    }

    public static void register(){
        System.out.println(stringFromClient);
    }
}
