package client;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Client_RegisterController {
    @FXML
    private MFXTextField nome;
    @FXML
    private MFXTextField cognome;
    @FXML
    private MFXTextField nickname;
    @FXML
    private MFXTextField password;
    @FXML
    private MFXButton registrati;
    @FXML
    private MFXButton login;

    public void initialize() {
        registrati.setOnAction(event -> invio());
        login.setOnAction(event -> openLogin());
    }

    public void invio(){
        String sNome = nome.getText();
        String sCognome= cognome.getText();
        String sNickname = nickname.getText();
        String sPassword = password.getText();

        if(sNome.equals("") || sCognome.equals("") || sNickname.equals("") || sPassword.equals("")){
            System.out.println("no");
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chat_list.fxml"));
            try {
                VBox registratiLayout = fxmlLoader.load();

                Stage registratiStage = new Stage();
                registratiStage.initModality(Modality.APPLICATION_MODAL);
                registratiStage.setTitle("Registrati");

                Scene chatListScene = new Scene(registratiLayout);
                registratiStage.setScene(chatListScene);

                Scene currentScene = login.getScene();
                Stage currentStage = (Stage) currentScene.getWindow();
                currentStage.close();

                registratiStage.show();
            } catch ( IOException e) {
                e.printStackTrace();
            }

        }

        System.out.println(sNome);
        System.out.println(sCognome);
        System.out.println(sNickname);
        System.out.println(sPassword);
    }

    private void openLogin() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        try {
            VBox loginLayout = fxmlLoader.load();

            Stage loginStage = new Stage();
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setTitle("Registrati");

            Scene chatListScene = new Scene(loginLayout);
            loginStage.setScene(chatListScene);

            Scene currentScene = login.getScene();
            Stage currentStage = (Stage) currentScene.getWindow();
            currentStage.close();

            loginStage.show();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
}
