
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/FuncionarioView.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Gestão de Funcionários");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
