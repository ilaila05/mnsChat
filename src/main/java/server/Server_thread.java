package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Server_thread extends Thread {
    public static Document document;
    protected static final String XML_FILE_NAME = "src/main/java/server/db/users.xml";
    private Socket sClient;
    private InputStream inputStream;
    private DataInputStream dataInputStream;
    private ObjectOutputStream objstream;
    private static ArrayList<String> stringFromClient = new ArrayList<>();
    public static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    public static TransformerFactory tFactory = TransformerFactory.newInstance();
    public static DataOutputStream toClient;
    public static File inputFile = new File("src/main/java/server/db/chats.xml");
    public static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    public Server_thread(Socket sClient) {
        this.sClient = sClient;
    }

    public void run() {
        try {
            System.out.println("[Server-Thread]: in attesa del client");

            inputStream = sClient.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
            toClient = new DataOutputStream(sClient.getOutputStream());

            int size = dataInputStream.readInt();
            System.out.println(size);
            for (int i = 0; i < size; i++) {
                String element = dataInputStream.readUTF();
                stringFromClient.add(element);
            }

            String caseClass = stringFromClient.get(0);
            System.out.println(caseClass);

            switch (caseClass) {
                case "login":
                    toClient.writeBoolean(login());
                    toClient.flush();
                    break;
                case "register":
                    register();
                    break;
                case "chatlist":
                    chatList(stringFromClient.get(1));
                    toClient.flush();
                    break;
                case "chat":
                    System.out.println(1);
                    HashMap chat = chat(stringFromClient.get(1), stringFromClient.get(2));
                    System.out.println(2);
                    if (chat.isEmpty()){
                        buildChat(chat, stringFromClient.get(1), stringFromClient.get(2));
                        toClient.writeInt(0);
                    }else{
                        toClient.writeInt(chat.size());
                        System.out.println(chat+""+chat.size());
                        for (int i = 0; i < chat.size()/2; i++) {
                            toClient.writeUTF( chat.get("Sender"+i).toString());
                            toClient.writeUTF( chat.get("Message"+i).toString());
                        }

                    }
                    System.out.println(5);
                    toClient.flush();
                    break;
                case "save":
                    saveMessagge(stringFromClient.get(1),stringFromClient.get(2),stringFromClient.get(3));

                    toClient.flush();
                    break;
                default:
                    break;
            }

            dataInputStream.close();
            inputStream.close();
            sClient.close();
            stringFromClient.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean login() {
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

                    if (storedUsername.equals(stringFromClient.get(1)) && storedPassword.equals(stringFromClient.get(2))) {
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
            name.appendChild(document.createTextNode(stringFromClient.get(1)));
            newUser.appendChild(name);

            Element surname = document.createElement("surname");
            surname.appendChild(document.createTextNode(stringFromClient.get(2)));
            newUser.appendChild(surname);

            Element nickname = document.createElement("nickname");
            nickname.appendChild(document.createTextNode(stringFromClient.get(3)));
            newUser.appendChild(nickname);

            Element password = document.createElement("password");
            password.appendChild(document.createTextNode(stringFromClient.get(4)));
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

    public void chatList(String nickname) {
        ArrayList<String> chatList = new ArrayList<>();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(XML_FILE_NAME);
            NodeList userList = document.getElementsByTagName("user");

            for (int i = 0; i < userList.getLength(); i++) {
                Node user = userList.item(i);
                if (user.getNodeType() == Node.ELEMENT_NODE) {
                    Element userElement = (Element) user;
                    chatList.add(userElement.getElementsByTagName("nickname").item(0).getTextContent());
                }
            }
            for (int i = 0; i < chatList.size(); i++) {
                if (chatList.get(i).equals(nickname)) {
                    chatList.remove(chatList.get(i));
                    i = chatList.size() + 1;
                }
            }

            toClient.writeInt(chatList.size());
            for (String s : chatList) {
                toClient.writeUTF(s);
            }

            System.out.println("elenco nickname" + chatList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap chat(String sender, String receiver) {
        try {

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList chatList = doc.getElementsByTagName("chat");
            HashMap<String, String> messageMap = new HashMap<>();

            for (int i = 0; i < chatList.getLength(); i++) {
                Node chatNode = chatList.item(i);
                if (chatNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element chatElement = (Element) chatNode;
                    String id = chatElement.getAttribute("id");

                    String[] parts = id.split("_");
                    if (parts.length == 2) {
                        String first = parts[0];
                        String second = parts[1];
                        System.out.println(sender + " " + Arrays.toString(parts) + " " + receiver);
                        if ((first.equals(sender) && second.equals(receiver)) || (first.equals(receiver) && second.equals(sender))) {
                            NodeList messageList = chatElement.getElementsByTagName("message");
                            System.out.println("Chat ID: " + id);
                            for (int j = 0; j < messageList.getLength(); j++) {
                                Node messageNode = messageList.item(j);
                                if (messageNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element messageElement = (Element) messageNode;
                                    String sender2 = messageElement.getAttribute("sender");
                                    String messageText = messageElement.getTextContent();

                                    System.out.println("Sender: " + sender2 + ", Message: " + messageText);

                                    messageMap.put("Sender"+j, sender2);
                                    messageMap.put("Message"+j, messageText);

                                }
                            }
                        }
                    }
                }
            }
            return messageMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    public void buildChat(Map chat, String sender, String receiver) {
        String idnew=sender+"_"+receiver;
        boolean exist = false;
        if (chat.isEmpty()) {
            System.out.println("chat vuota");
            try {
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

                Document doc = dBuilder.parse(inputFile);
                doc.getDocumentElement().normalize();

                NodeList chatList = doc.getElementsByTagName("chat");
                for (int i = 0; i < chatList.getLength(); i++) {
                    Node chatNode = chatList.item(i);
                    if (chatNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element chatElement = (Element) chatNode;
                        String id = chatElement.getAttribute("id");
                        if (id.equals(idnew)) {
                            exist = true;
                        }
                    }

                }

                if (!exist) {

                Element newChat = doc.createElement("chat");
                newChat.setAttribute("id", sender + "_" + receiver);

                NodeList chats = doc.getElementsByTagName("chats");
                chats.item(0).appendChild(newChat);

                Transformer transformer = tFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(inputFile);
                transformer.transform(source, result);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    public void saveMessagge(String sender,String receiver,String message) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList chatList = doc.getElementsByTagName("chat");
        HashMap<String, String> messageMap = new HashMap<>();
        for (int i = 0; i < chatList.getLength(); i++) {
            Node chatNode = chatList.item(i);
            if (chatNode.getNodeType() == Node.ELEMENT_NODE) {
                Element chatElement = (Element) chatNode;
                String id = chatElement.getAttribute("id");

                String[] parts = id.split("_");
                if (parts.length == 2) {
                    String first = parts[0];
                    String second = parts[1];
                    System.out.println(sender + " " + Arrays.toString(parts) + " " + receiver);
                    if ((first.equals(sender) && second.equals(receiver)) || (first.equals(receiver) && second.equals(sender))) {

                        NodeList messageList = chatElement.getElementsByTagName("message");
                        System.out.println("Chat ID: " + id);
                        for (int j = 0; j < messageList.getLength(); j++) {
                            Node messageNode = messageList.item(j);
                            if (messageNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element messageElement = (Element) messageNode;
                                String sender2 = messageElement.getAttribute("sender");
                                String messageText = messageElement.getTextContent();

                                System.out.println("Sender: " + sender2 + ", Message: " + messageText);

                                messageMap.put("Sender"+j, sender2);
                                messageMap.put("Message"+j, messageText);

                            }
                        }

                        Element messageElement = doc.createElement("message");
                        messageElement.setAttribute("sender"+messageMap.size()/2, sender); // Set sender attribute
                        messageElement.setTextContent(message); // Set message content

                        chatElement.appendChild(messageElement); // Append the message to the chat

                        // Save the changes back to the XML file
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult(inputFile);
                        transformer.transform(source, result);

                        System.out.println("Message saved successfully!");
                        return; // Exit the method after saving the message
                    }
                }
            }
        }
    }
}
