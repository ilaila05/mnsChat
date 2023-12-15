package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.util.concurrent.Semaphore;

public class Server_thread extends Thread{
    public static Document document;
    protected static final String XML_FILE_NAME = "src/main/java/server/db/users.xml";
    private Socket sClient;
    private InputStream inputStream;
    private DataInputStream dataInputStream;
    private static ArrayList<String> stringFromClient = new ArrayList<>();
    public static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    public static TransformerFactory tFactory = TransformerFactory.newInstance();
    public static Transformer transformer;

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
                    boolean state=login();
                    if (!state){
                        sendLoginStateToClient(state);
                    }
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
    public void sendLoginStateToClient(boolean loginState) {
        // Establish connection with Client_proxy and send login state
        try {
            Socket s = new Socket("127.0.0.1", 8001); // Assuming port 8001 is used by the Client_proxy
            DataOutputStream toClient = new DataOutputStream(s.getOutputStream());

            // Sending the login state to the Client_proxy
            toClient.writeBoolean(loginState);

            toClient.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean login(){
        System.out.println(stringFromClient);

        try {
            // Parsing the XML file
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(XML_FILE_NAME);
            NodeList userList = document.getElementsByTagName("user");

            for (int i = 0; i < userList.getLength(); i++) {
                Node user = userList.item(i);
                if (user.getNodeType() == Node.ELEMENT_NODE) {
                    Element userElement = (Element) user;
                    String storedUsername = userElement.getElementsByTagName("nickname").item(0).getTextContent();
                    String storedPassword = userElement.getElementsByTagName("password").item(0).getTextContent();

                    if (storedUsername.equals(stringFromClient.get(0)) && storedPassword.equals(stringFromClient.get(1))) {
                        System.out.println("Login successful!");
                        // Perform further actions for a successful login
                        return true;
                    }
                }
            }

            System.out.println("Invalid username or password!");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void register() {
        System.out.println(stringFromClient);

        try {
            // Parsing the XML file
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(XML_FILE_NAME));

            Element newUser = document.createElement("user");

            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(stringFromClient.get(0)));
            newUser.appendChild(name);

            Element surname = document.createElement("surname");
            surname.appendChild(document.createTextNode(stringFromClient.get(1)));
            newUser.appendChild(surname);

            Element nickname = document.createElement("nickname");
            nickname.appendChild(document.createTextNode(stringFromClient.get(2)));
            newUser.appendChild(nickname);

            Element password = document.createElement("password");
            password.appendChild(document.createTextNode(stringFromClient.get(3)));
            newUser.appendChild(password);

            NodeList users = document.getElementsByTagName("users");
            users.item(0).appendChild(newUser);

            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(XML_FILE_NAME));
            transformer.transform(source, result);

            System.out.println("User registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
