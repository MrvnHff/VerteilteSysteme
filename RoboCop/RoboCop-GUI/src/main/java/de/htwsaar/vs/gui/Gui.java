package de.htwsaar.vs.gui;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import de.htwsaar.vs.gui.controller.RobotController;
import de.htwsaar.vs.gui.graph.CellType;
import de.htwsaar.vs.gui.graph.Graph;
import de.htwsaar.vs.gui.layout.grid.GridLayout;
import de.htwsaar.vs.gui.layout.robot.RobotLayout;
import de.htwsaar.vs.server.Server;
import de.htwsaar.vs.server.graph.nodes.RobotOrientation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Gui extends Application {

	private static final String CONFIG_FILENAME = "config/config";
	private static final String FXML_BUNDLE_FILENAME = "config/fxml_files";
	
	//Hier Sprache ändern
	private Locale locale = new Locale("de");
	
	private Graph graph;
	
	private Server server;
	
	private int controllerCount = 0;
	private RobotController robotControllers[] = new RobotController[4];
	
	private Stage primaryStage = new Stage();
	private BorderPane rootLayout;
	
	private ResourceBundle config;
	private ResourceBundle fxmlBundle;
	
	/**
	 * Lädt die configurationen aus den properties dateien
	 */
	private void loadConfiguration() {
		config = ResourceBundle.getBundle(CONFIG_FILENAME, locale);
		fxmlBundle = ResourceBundle.getBundle(FXML_BUNDLE_FILENAME);
	}
	
	@Override
	public void start(Stage primaryStage) {
		server = new Server();
		//wird benötigt um svg dateien zu laden
		SvgImageLoaderFactory.install();
		loadConfiguration();
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(config.getString("general.title"));
		
		initRootLayout();
	}

	private void initRootLayout() {
		try {
			FXMLLoader rootLayoutLoader = new FXMLLoader();
			rootLayoutLoader.setLocation(Gui.class.getClassLoader().getResource(fxmlBundle.getString("fxml.rootLayout")));
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
		SplitPane splitPane1 = new SplitPane();
		splitPane1.setOrientation(Orientation.HORIZONTAL);
		splitPane1.setDividerPositions(0.6f, 0.4f);
		
		FlowPane flow = new FlowPane();
		flow.setVgap(10);
		flow.setHgap(10);
		flow.setPrefWrapLength(800);
		flow.getChildren().add(buildRobotPane());
		flow.getChildren().add(buildRobotPane());
		flow.getChildren().add(buildRobotPane());
		flow.getChildren().add(buildRobotPane());		
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setContent(flow);
		
		graph = new Graph(server.getRoboGraph());
		graph.getModel().addCell("George", CellType.ROBOT);
		graph.getModel().addCell("Jane", CellType.ROBOT);
		graph.endUpdate();
		GridLayout gl = new GridLayout(graph);
		gl.setScale(200);
        gl.execute();
        
        RobotLayout rl = new RobotLayout(graph);
        rl.moveRobotTo("Jane", "2/2");
        rl.moveRobotTo("George", "1/1");
        rl.rotate("George" , 90);
        rl.execute();
        
        robotControllers[0].setServer(server);
        robotControllers[0].setRobotId("George");
        robotControllers[0].setRobotLayout(rl);
        
        robotControllers[1].setServer(server);
        robotControllers[1].setRobotId("Jane");
        robotControllers[1].setRobotLayout(rl);
		
		splitPane1.getItems().addAll(scrollPane, graph.getScrollPane());	
		
		//Server TextArea
		TextArea textarea = new TextArea();
		textarea.setEditable(false);
		
		SplitPane splitPane2 = new SplitPane();
		splitPane2.getItems().addAll(splitPane1, textarea);
		splitPane2.setOrientation(Orientation.VERTICAL);
		splitPane2.setDividerPositions(0.8f, 0.2f);
		
		rootLayout.setCenter(splitPane2);

	}
	
	private BorderPane buildRobotPane() {
		BorderPane robot = null;
		try {
			FXMLLoader robotLoader = new FXMLLoader(Gui.class.getClassLoader().getResource(fxmlBundle.getString("fxml.robot")), config);
			robot = (BorderPane) robotLoader.load();
			robotControllers[controllerCount] = robotLoader.getController();
			controllerCount++;
		} catch (IOException e) {
			System.out.println(e);
		}
		return robot;
	}
	
	public static void main(String[] args) {
        launch(args);
    }
}
