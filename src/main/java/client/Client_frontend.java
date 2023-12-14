package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client_frontend extends Application{
    private boolean loaded = false;
    @Override
    public void start(Stage primaryStage) {
        try{
            /*URL resourceUrl = getClass().getClassLoader().getResource("client/esempio_io.fxml");

            if (resourceUrl == null) {
                System.out.println("FXML resource not found");
            }*/

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/login.fxml"));


            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("NULLPOINTER EXCEPTION: " + e.getMessage());
        }
        catch (IllegalStateException e){
            e.printStackTrace();
            System.out.println("ILLEGALSTATE EXCEPTION: " + e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("OTHER EXCEPTION: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
