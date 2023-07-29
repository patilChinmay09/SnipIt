package main.java.userInterface;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
//			Image taskbarIcon = new Image(getClass().getResourceAsStream("C://Users//cjp20//Downloads//icons8-screenshot-100.png"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
	        UIController controller = new UIController();
	        loader.setController(controller);
	        Parent root = loader.load();
			Scene scene = new Scene(root);
            Image taskbarIcon =  new Image(getClass().getResourceAsStream("Logo.png"));
			
			primaryStage.setTitle("SnipIt");
			primaryStage.getIcons().add(taskbarIcon); 
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
