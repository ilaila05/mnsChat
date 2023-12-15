package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.spec.ECField;
import java.util.ArrayList;

public class Server_proxy {
    private static ArrayList<String> stringFromClient;

    public static void main (String[] args) {
        try {
            while (true) {
                System.out.println("dasdh");
                fromClientProxy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void fromClientProxy() {
        try {
            stringFromClient = new ArrayList<>();

            System.out.println("eee");
            ServerSocket sServer = new ServerSocket(8000);
            System.out.println("aaa");
            Socket sClient = sServer.accept();
            System.out.println("bbb");

            InputStream inputStream = sClient.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            int size = dataInputStream.readInt();

            for (int i = 0; i < size; i++) {
                String element = dataInputStream.readUTF();
                stringFromClient.add(element);
            }

            dataInputStream.close();
            inputStream.close();
            sClient.close();
            sServer.close();

            System.out.println("Received ArrayList: " + stringFromClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
