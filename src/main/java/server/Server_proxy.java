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
            ServerSocket sServer = new ServerSocket(7777);
            System.out.println("[Server]: in attesa su porta 7777.");
            while (true) {
                Socket sClient = sServer.accept();
                System.out.println("[Server]: nuovo client.");
                // Handle the client within a single thread
                handleClient(sClient);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void handleClient(Socket sClient) {
        try {
            Server_thread thread = new Server_thread(sClient);
            thread.start();
            thread.join(); // Wait for the thread to finish handling this client
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
