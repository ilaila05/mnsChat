package client;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Client_ChatList implements Initializable {
    @FXML
    private VBox chatListLayout;

    @FXML
    private MFXListView<String> chatListView;


    public void initialize(URL location, ResourceBundle resources) {
        chatListView.setCellFactory((s) -> {
            MFXListCell<String> cell = new MFXListCell<>(chatListView, s);
            //cell.setStyle("-fx-background-color: red;");
            return cell;
        });
        List<String> chatData = Arrays.asList("Chat 1", "Chat 2", "Chat 3", "Chat 4");
        chatListView.getItems().addAll(chatData);

        chatListView.setOnMouseClicked(event -> {
            String selectedChat = String.valueOf(chatListView.getSelectionModel().getSelectedValues());
            if (selectedChat != null) {
                System.out.println("Hai selezionato: " + selectedChat);
            }
        });
    }

}
