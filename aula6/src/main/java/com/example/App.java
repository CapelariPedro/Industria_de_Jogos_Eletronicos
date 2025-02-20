package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;


public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        //Caminho para o arquivo FXML
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/example/Tela01.fxml"));
        Parent root = loader.load();
        //Gera o titulo da Página e o Icone
        stage.setTitle("Flip Niquel");
        Image image = new Image(this.getClass().getResource("/com/example/logoFabrica.png").toExternalForm());
        stage.getIcons().add(image);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}