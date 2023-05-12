package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass());
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        stage.setTitle("SushiJei - Login");
        stage.setScene(new Scene(root, 620, 364));
//        stage.setScene(new Scene(root, 900, 570));
        stage.show(); 
    }

    public static void main(String[] args) {
        launch(args);
    }
}
