package miniGui;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MiniGui extends Application {
	
	private static final String CONFIG_FILENAME = "resources/config/miniGui";
	
	private ResourceBundle config;
	
	private Stage primaryStage = new Stage();
	private BorderPane layout;
	private MiniGuiController controller;
	
	@Override
	public void start(Stage primaryStage) {
		config = ResourceBundle.getBundle(CONFIG_FILENAME);
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(config.getString("general.title"));
		this.primaryStage.setMinHeight(400);
		this.primaryStage.setMinWidth(320);
		
		try {
			FXMLLoader layoutLoader = new FXMLLoader(MiniGui.class.getClassLoader().getResource("resources/fxml/MiniGui.fxml"), config);
			layout = (BorderPane) layoutLoader.load();
			controller = layoutLoader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
        launch(args);
    }
}
