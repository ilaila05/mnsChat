package client;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static client.Client_proxy.getState;
import static client.Client_proxy.setLogin;

public class Client_LoginController {
    @FXML
    private MFXTextField nickname;
    @FXML
    private MFXTextField password;
    @FXML
    private MFXButton login;
    @FXML
    private MFXButton registrati;

    @FXML
    public void initialize() {
        login.setOnAction(event -> invio());
        registrati.setOnAction(event -> openRegistrati());
    }

    public void invio(){
        String sNickname = nickname.getText();
        String sPassword = password.getText();

        if(sNickname.equals("") || sPassword.equals("")){
            System.out.println("no");
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chat_list.fxml"));
            try {
                setLogin(sNickname, sPassword);

                if(getState()){
                    VBox chatListLayout = fxmlLoader.load();

                    Stage chatListStage = new Stage();
                    chatListStage.initModality(Modality.APPLICATION_MODAL);
                    chatListStage.setTitle("Registrati");

                    Scene chatListScene = new Scene(chatListLayout);
                    chatListStage.setScene(chatListScene);

                    Scene currentScene = login.getScene();
                    Stage currentStage = (Stage) currentScene.getWindow();
                    currentStage.close();

                    chatListStage.show();
                }
                else {
                    /*
                    - messaggio di errore
                    - ricaricamento del login
                    */
                    System.out.println("login sbagliato");
                }


            } catch ( Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void openRegistrati() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/register.fxml"));
        System.out.println("FXML File URL: " + fxmlLoader.getLocation().toString());
        try {
            VBox registratiLayout = fxmlLoader.load();

            Stage registratiStage = new Stage();
            registratiStage.initModality(Modality.APPLICATION_MODAL);
            registratiStage.setTitle("Registrati");

            Scene chatListScene = new Scene(registratiLayout);
            registratiStage.setScene(chatListScene);

            Scene currentScene = registrati.getScene();
            Stage currentStage = (Stage) currentScene.getWindow();
            currentStage.close();

            registratiStage.show();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
}
