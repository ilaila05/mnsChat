package client;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static io.github.palexdev.materialfx.controls.MFXListView.*;

public class Client_ChatController {
    @FXML
    private VBox chatBox;

    @FXML
    private MFXTextField messageField;

    @FXML
    private MFXButton sendButton;

    @FXML
    private MFXButton chatListButton;

    private Stage chatListStage;



    @FXML
    public void initialize() {
        // Puoi inizializzare qui il controller, ad esempio aggiungere un'azione al pulsante "Send"
        sendButton.setOnAction(event -> sendMessage());
        chatListButton.setOnAction(event -> openChatList());


    }
private void receiveMessage(){
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

    private void sendMessage() {
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chat_list.fxml"));
        try {
            VBox chatListLayout = fxmlLoader.load();

            // Ottieni il controller se necessario
            // ChatListController controller = fxmlLoader.getController();

            Stage chatListStage = new Stage();
            chatListStage.initModality(Modality.APPLICATION_MODAL);
            chatListStage.setTitle("Elenco Chat");

            Scene chatListScene = new Scene(chatListLayout, 200, 400);
            chatListStage.setScene(chatListScene);
            chatListStage.setMaxHeight(400);
            chatListStage.setMaxWidth(200);
            Scene currentScene =  chatBox.getScene();
            Stage currentStage = (Stage) currentScene.getWindow();
            currentStage.close();
            chatListStage.show();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

}