package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Server_proxy {
    private static ArrayList<String> stringFromClient;

    public static void main(String[] args) {
        try {
            ServerSocket sServer = new ServerSocket(8000);
            System.out.println("[Server]: in attesa su porta 8000.");
            while (true) {
                Socket sClient = sServer.accept();
                System.out.println("[Server]: nuovo client.");

                Server_thread thread = new Server_thread(sClient);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
