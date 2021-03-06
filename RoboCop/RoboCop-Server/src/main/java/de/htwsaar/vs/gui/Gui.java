package de.htwsaar.vs.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import de.htwsaar.vs.gui.controller.RobotController;
import de.htwsaar.vs.gui.graph.CellType;
import de.htwsaar.vs.gui.graph.Graph;
import de.htwsaar.vs.gui.layout.grid.GridLayout;
import de.htwsaar.vs.gui.layout.robot.RobotLayout;
import de.htwsaar.vs.server.Server;
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
	
	private HashMap<String, RobotController> robotControllers = new HashMap<String, RobotController>();
	
	private Stage primaryStage = new Stage();
	private RobotLayout rl;
	private BorderPane rootLayout;
	private FlowPane flow;
	
	private TextArea textArea;
	
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
		server.setGui(this);
		
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
		
		flow = new FlowPane();
		flow.setVgap(10);
		flow.setHgap(10);
		flow.setPrefWrapLength(800);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setContent(flow);
		
		graph = new Graph(server.getRoboGraph());
		
		GridLayout gl = new GridLayout(graph);
		gl.setScale(200);
        gl.execute();
        
        rl = new RobotLayout(graph);
                
		splitPane1.getItems().addAll(scrollPane, graph.getScrollPane());	
		
		//Server TextArea
		textArea = new TextArea();
		textArea.setEditable(false);
		
		SplitPane splitPane2 = new SplitPane();
		splitPane2.getItems().addAll(splitPane1, textArea);
		splitPane2.setOrientation(Orientation.VERTICAL);
		splitPane2.setDividerPositions(0.8f, 0.2f);
		
		rootLayout.setCenter(splitPane2);
	}
	
	public void addRobot(String robotId, String position) {
		flow.getChildren().add(buildRobotPane(robotId, position));
		rl.moveRobotTo(robotId, position);
	}
	
	private BorderPane buildRobotPane(String robotId, String position) {
		BorderPane robot = null;
		try {
			FXMLLoader robotLoader = new FXMLLoader(Gui.class.getClassLoader().getResource(fxmlBundle.getString("fxml.robot")), config);
			robot = (BorderPane) robotLoader.load();
			RobotController controller = robotLoader.getController();
			controller.setServer(server);
			controller.setRobotId(robotId);
			controller.setRobotLayout(rl);
			controller.setPosition(position);
			robotControllers.put(robotId, controller);
			
			graph.getModel().addCell(robotId, CellType.ROBOT);
			graph.endUpdate();
		} catch (IOException e) {
			System.out.println(e);
		}
		return robot;
	}
	
	public void addServerTextMessage(String msg) {
		textArea.appendText(msg);
		textArea.appendText("\n");
	}
	
	public void addRobotTextMessage(String robotId, String msg) {
		RobotController controller = robotControllers.get(robotId);
		controller.addTextMessage(msg);
		controller.addTextMessage("\n");
	}
	
	public static void main(String[] args) {
        launch(args);
    }
}
