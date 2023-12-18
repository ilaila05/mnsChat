package client;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

import static client.Client_proxy.*;

public class Client_ChatController {
    @FXML
    private VBox chatBox;

    @FXML
    private MFXTextField messageField;

    @FXML
    private MFXButton sendButton;

    @FXML
    private MFXButton chatListButton;

    private static String Sender;

    public static String getSender() {
        return Sender;
    }

    public static String getReceiver() {
        return Receiver;
    }

    public void setMessage() throws IOException, InterruptedException {
        receiveChat();
    }

    private static String Receiver;

    public void setSender(String Sender) {
        Client_ChatController.Sender = Sender;
    }

    public void setReceiver(String Receiver) {
        Client_ChatController.Receiver = Receiver;
    }

    @FXML
    public void initialize() {
        // Puoi inizializzare qui il controller, ad esempio aggiungere un'azione al pulsante "Send"
        sendButton.setOnAction(event -> {
            try {
                sendMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        chatListButton.setOnAction(event -> openChatList());

    }

    public void setReceiveMessage(String message) {
        if (!message.isEmpty()) {
            Label newMessage = new Label(message);
            HBox messageContainer = new HBox(newMessage);

            newMessage.setWrapText(true);
            newMessage.setMaxWidth(Double.MAX_VALUE);
            newMessage.getStyleClass().add("received-message");

            messageContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            messageContainer.setFillHeight(true);// Assicura che l'HBox occupi lo spazio disponibile in altezza

            newMessage.widthProperty().addListener((observable, oldValue, newValue) -> {
                double textHeight = computeTextHeight(new Text(message), newMessage.getWidth());
                messageContainer.setMinHeight(textHeight + 10); // Aggiungi spazio per migliorare la visualizzazione
                messageContainer.setPrefHeight(textHeight + 10); // Aggiungi spazio per migliorare la visualizzazione
            });

            chatBox.getChildren().add(messageContainer);

        }

    }

    public void setSendMessage(String message) {
        if (!message.isEmpty()) {
            Label newMessage = new Label(message);
            HBox messageContainer = new HBox(newMessage);

            newMessage.setWrapText(true);
            newMessage.setMaxWidth(Double.MAX_VALUE);
            newMessage.getStyleClass().add("sent-message");

            messageContainer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            messageContainer.setFillHeight(true);// Assicura che l'HBox occupi lo spazio disponibile in altezza

            newMessage.widthProperty().addListener((observable, oldValue, newValue) -> {
                double textHeight = computeTextHeight(new Text(message), newMessage.getWidth());
                messageContainer.setMinHeight(textHeight + 10); // Aggiungi spazio per migliorare la visualizzazione
                messageContainer.setPrefHeight(textHeight + 10); // Aggiungi spazio per migliorare la visualizzazione
            });

            chatBox.getChildren().add(messageContainer);

        }

    }

    private void receiveMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            Label newMessage = new Label(message);
            HBox messageContainer = new HBox(newMessage);

            newMessage.setWrapText(true);
            newMessage.setMaxWidth(Double.MAX_VALUE);
            newMessage.getStyleClass().add("received-message");

            messageContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            messageContainer.setFillHeight(true);// Assicura che l'HBox occupi lo spazio disponibile in altezza

            newMessage.widthProperty().addListener((observable, oldValue, newValue) -> {
                double textHeight = computeTextHeight(new Text(message), newMessage.getWidth());
                messageContainer.setMinHeight(textHeight + 10); // Aggiungi spazio per migliorare la visualizzazione
                messageContainer.setPrefHeight(textHeight + 10); // Aggiungi spazio per migliorare la visualizzazione
            });

            chatBox.getChildren().add(messageContainer);
            messageField.clear();
        }
    }

    private void sendMessage() throws InterruptedException {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            Label newMessage = new Label(message);
            HBox messageContainer = new HBox(newMessage);

            newMessage.setWrapText(true);
            newMessage.setMaxWidth(Double.MAX_VALUE);
            newMessage.getStyleClass().add("sent-message");

            messageContainer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            messageContainer.setFillHeight(true); // Assicura che l'HBox occupi lo spazio disponibile in altezza

            newMessage.widthProperty().addListener((observable, oldValue, newValue) -> {
                double textHeight = computeTextHeight(new Text(message), newMessage.getWidth());
                messageContainer.setMinHeight(textHeight + 10); // Aggiungi spazio per migliorare la visualizzazione
                messageContainer.setPrefHeight(textHeight + 10); // Aggiungi spazio per migliorare la visualizzazione
            });

            double maxWidth = chatBox.getWidth() * 0.8; // Set max width to 70% of the chatBox width
            newMessage.setMaxWidth(maxWidth);
            saveMessage(getSender(), getReceiver(), message);
            chatBox.getChildren().add(messageContainer);
            messageField.clear();
        }
    }

    // Modifica computeTextHeight per accettare larghezza specifica
    private double computeTextHeight(Text text, double width) {
        text.setWrappingWidth(width);
        double textHeight = text.getLayoutBounds().getHeight();
        double lineHeight = text.getFont().getSize();
        return Math.ceil(textHeight / lineHeight) * lineHeight; // Calcola l'altezza basandoti sul numero di righe
    }

    private void openChatList() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chat_list.fxml"));

                    VBox chatListLayout = fxmlLoader.load();

                    Client_ChatListController controller = fxmlLoader.getController();
                    controller.getNicknameLabel().setText(getSender());

                    Stage chatListStage = new Stage();
                    chatListStage.initModality(Modality.APPLICATION_MODAL);
                    chatListStage.setTitle("chatlist");

                    Stage currentStage = (Stage) chatListLayout.getScene().getWindow();
                    currentStage.close();

                    Scene chatListScene = new Scene(chatListLayout);
                    chatListStage.setScene(chatListScene);



                    chatListStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}


