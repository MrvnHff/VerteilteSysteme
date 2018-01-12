package de.htwsaar.vs.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Gui extends Application {

	private static final String CONFIG_FILENAME = "config/config.properties";
	
	private Locale locale = new Locale("de");
	
	private Stage primaryStage = new Stage();
	private BorderPane rootLayout;
	
	private Configuration config;
	
	private void loadConfiguration() {
		try {
			org.apache.commons.configuration2.builder.fluent.Parameters params = new org.apache.commons.configuration2.builder.fluent.Parameters();			

			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
			    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
			    .configure(params.fileBased()
			        .setFileName(CONFIG_FILENAME));
			
			config = builder.getConfiguration();
		} catch (ConfigurationException cex) {
			System.out.println(cex);
		}		
	}
	
	@Override
	public void start(Stage primaryStage) {
		loadConfiguration();
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(config.getString("general.title"));
		
		initRootLayout();
	}

	

	private void initRootLayout() {
		try {
			System.out.println(config.getString("fxml.rootLayout"));
			FXMLLoader rootLayoutLoader = new FXMLLoader();
			rootLayoutLoader.setLocation(Gui.class.getClassLoader().getResource(config.getString("fxml.rootLayout")));
			rootLayout = (BorderPane) rootLayoutLoader.load();
			
			buildGui();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println(e);
		}	
	}
	
	private void buildGui() {
		GridPane gridPane = new GridPane();
		HBox hbox = new HBox();
		hbox.getChildren().add(buildRobotPane());
		hbox.getChildren().add(buildRobotPane());
		hbox.getChildren().add(buildRobotPane());
		gridPane.add(hbox, 0, 0);
		
		Pane pane = new Pane();
		Canvas canvas = new Canvas(400, 400);
		pane.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.strokeText("Hello Canvas", 150, 100);
		
		pane.getChildren().add(canvas);
		gridPane.add(pane, 1, 0);
		
		rootLayout.setCenter(gridPane);
		
		
		//Server TextArea
		TextArea textarea = new TextArea();
		textarea.setEditable(false);
		rootLayout.setBottom(textarea);
	}
	
	private BorderPane buildRobotPane() {
		BorderPane robot = null;
		
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("config/robot", locale);
			FXMLLoader robotLoader = new FXMLLoader(Gui.class.getClassLoader().getResource(config.getString("fxml.robot")), bundle);
		
			robot = (BorderPane) robotLoader.load();
		} catch (IOException e) {
			System.out.println(e);
		}
		return robot;
	}
	
	public static void main(String[] args) {
        launch(args);
    }
}
