package client;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

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
    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    private static Stage thisStage;
    @FXML
    private VBox vBox;


    private static String sNickname;
    private static String sPassword;

    /******************** getter per il proxy
    public static String getsNickname() {
        return sNickname;
    }
    ****************************************/

    @FXML
    public void initialize() {
        login.setOnAction(event -> invio());
        registrati.setOnAction(event -> openRegistrati());
    }

    public void invio() {
        sNickname = nickname.getText();
        sPassword = password.getText();

        if (sNickname.equals("") || sPassword.equals("")) {
            System.out.println("no");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chat_list.fxml"));
            try {
                setLogin(sNickname, sPassword);

                if (getState()) { //utente presente = apertura chat

                    VBox chatListLayout = fxmlLoader.load();

                    Client_ChatList controller = fxmlLoader.getController();
                    controller.getNicknameLabel().setText(sNickname);

                    Stage chatListStage = new Stage();
                    chatListStage.initModality(Modality.APPLICATION_MODAL);
                    chatListStage.setTitle("Registrati");

                    Scene chatListScene = new Scene(chatListLayout);
                    chatListStage.setScene(chatListScene);

                    Scene currentScene = login.getScene();
                    Stage currentStage = (Stage) currentScene.getWindow();
                    currentStage.close();

                    chatListStage.show();
                } else { //utente inesistente = messaggio di errore e reset del login
                    Scene currentScene = login.getScene();
                    thisStage = (Stage) currentScene.getWindow();

                    this.dialogContent = MFXGenericDialogBuilder.build()
                            .setContentText("Attenzione\nil nickname o la password sono sbagliati\nriprova a fare il login oppure, se non sei iscritto, registrati")
                            .makeScrollable(true)
                            .get();
                    this.dialog = MFXGenericDialogBuilder.build(dialogContent)
                            .toStageDialogBuilder()
                            .initOwner(thisStage)
                            .initModality(Modality.APPLICATION_MODAL)
                            .setDraggable(true)
                            .setTitle("Errore")
                            .setOwnerNode(vBox)
                            .setScrimPriority(ScrimPriority.WINDOW)
                            .setScrimOwner(true)
                            .get();
                    dialogContent.addActions(
                            Map.entry(new MFXButton("Confirm"), event -> {
                                dialog.close();
                                nickname.setText("");
                                password.setText("");
                            }),
                            Map.entry(new MFXButton("Register"), event -> {
                                dialog.close();
                                openRegistrati();
                            })
                    );

                    dialogContent.setMaxSize(400, 200);

                    openError();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void openRegistrati() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/register.fxml"));
        System.out.println("FXML File URL: " + fxmlLoader.getLocation().toString());
        try { //utente vuole registrarsi = apertura del registrati
            VBox registratiLayout = fxmlLoader.load();

            Stage registratiStage = new Stage();
            registratiStage.initModality(Modality.APPLICATION_MODAL);
            registratiStage.setTitle("Registrati");

            Scene registratiScene = new Scene(registratiLayout);
            registratiStage.setScene(registratiScene);

            Scene currentScene = registrati.getScene();
            Stage currentStage = (Stage) currentScene.getWindow();
            currentStage.close();

            registratiStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openError() {
        dialogContent.setHeaderText("Attenzione");
        convertDialogTo("mfx-error-dialog");
        dialog.showDialog();
    }

    private void convertDialogTo(String styleClass) {
        dialogContent.getStyleClass().removeIf(
                s -> s.equals("mfx-error-dialog")
        );

        if (styleClass != null)
            dialogContent.getStyleClass().add(styleClass);
    }
}
