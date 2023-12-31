package client;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static client.Client_proxy.receiveChat;
import static client.Client_proxy.receiveChatList;

public class Client_ChatListController implements Initializable {
    @FXML
    private VBox chatListLayout;

    @FXML
    private MFXListView<String> chatListView;
    @FXML
    private Label nickname;
    public Label getNicknameLabel() {
        return nickname;
    }

    public void initialize(URL location, ResourceBundle resources) {
        chatListView.setCellFactory((s) -> {
            MFXListCell<String> cell = new MFXListCell<>(chatListView, s);
            return cell;
        });
        List<String> chatData = null;
        try {
            chatData = receiveChatList();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        chatListView.getItems().addAll(chatData);

        chatListView.setOnMouseClicked(event -> {
            String selectedChat = String.valueOf(chatListView.getSelectionModel().getSelectedValues());
            if (selectedChat != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chat.fxml"));
                try {
                    BorderPane chat = fxmlLoader.load();
                    Client_ChatController controller = fxmlLoader.getController();
                    controller.setSender(nickname.getText());
                    controller.setReceiver(selectedChat.substring(1, selectedChat.length() - 1));

                    HashMap chatM= receiveChat();
                    if (chatM!= null){
                    for (int i = 0; i < chatM.size()/2; i++) {
                        System.out.println(chatM.get("Sender"+i) +nickname.getText());
                        if (chatM.get("Sender"+i).toString().equals(nickname.getText())){
                            controller.setSendMessage(chatM.get("Message"+i).toString());
                        }
                        else {
                            controller.setReceiveMessage(chatM.get("Message"+i).toString());
                        }
                    }}

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
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
