package client;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static client.Client_proxy.receiveChatList;

public class Client_ChatList implements Initializable {
    @FXML
    private VBox chatListLayout;

    @FXML
    private MFXListView<String> chatListView;
    @FXML
    private Label nickname;

    // Getter for the Label
    public Label getNicknameLabel() {
        return nickname;
    }


    public void initialize(URL location, ResourceBundle resources) {
        chatListView.setCellFactory((s) -> {
            MFXListCell<String> cell = new MFXListCell<>(chatListView, s);
            //cell.setStyle("-fx-background-color: red;");
            return cell;
        });

        List<String> chatData = (List<String>) receiveChatList(nickname.getText());
        chatListView.getItems().addAll(chatData);

        chatListView.setOnMouseClicked(event -> {
            String selectedChat = String.valueOf(chatListView.getSelectionModel().getSelectedValues());
            if (selectedChat != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chat.fxml"));
                try {

                    BorderPane chat = fxmlLoader.load();

                    Stage chatListStage = new Stage();
                    chatListStage.initModality(Modality.APPLICATION_MODAL);
                    chatListStage.setTitle("Elenco Chat");

                    Scene chatListScene = new Scene(chat, 649.0, 430.0);
                    chatListStage.setScene(chatListScene);

                    Scene currentScene =  chatListLayout.getScene();
                    Stage currentStage = (Stage) currentScene.getWindow();
                    currentStage.close();

                    chatListStage.show();
                } catch ( IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Hai selezionato: " + selectedChat);
            }
        });
    }

}
