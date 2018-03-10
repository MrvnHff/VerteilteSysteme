package server.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import server.gui.controller.VehicleController;
import server.gui.graph.CellType;
import server.gui.graph.Graph;
import server.gui.layout.grid.GridLayout;
import server.gui.layout.vehicle.VehicleLayout;
import server.server.Server;
import server.server.ServerInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Gui extends Application implements GuiInterface{
	
	private static final String CONFIG_FILENAME = "resources/config/config";
	private static final String FXML_BUNDLE_FILENAME = "resources/config/fxml_files";
	
	//Hier Sprache Ã¤ndern
	private Locale locale = new Locale("de");
	
	private Graph graph;
	
	private ServerInterface server;
	
	private HashMap<String, VehicleController> vehicleControllers = new HashMap<String, VehicleController>();
	
	private Stage primaryStage = new Stage();
	private VehicleLayout rl;
	private BorderPane rootLayout;
	private FlowPane flow;
	
	private TextArea textArea;
	
	private ResourceBundle config;
	private ResourceBundle fxmlBundle;
	
	/**
	 * LÃ¤dt die configurationen aus den properties dateien
	 */
	private void loadConfiguration() {
		config = ResourceBundle.getBundle(CONFIG_FILENAME, locale);
		fxmlBundle = ResourceBundle.getBundle(FXML_BUNDLE_FILENAME);
	}
	
	@Override
	public void start(Stage primaryStage) {
		server = new Server();
		
		//wird benÃ¶tigt um svg dateien zu laden
		SvgImageLoaderFactory.install();
		loadConfiguration();
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(config.getString("general.title"));
		
		initRootLayout();
		
		server.setGui(this);
		server.startServer();
		
	}
	
	@Override
	public void stop() {
		server.stopServer();
		this.server = null;
		
		System.gc();
		System.runFinalization();
		
		//System.exit(0);
		//TODO Ist das an dieser stelle möglich oder gibt das Probleme mit den JavaFx Threads, 
		// da die vermutlich gewaltsam abgebrochen werden??
		//Sollte keine Probleme geben. Das hier ist die Methode die aufgerufen wird wenn du die javaFx anwendung über
		//Platform.exit(), oder auf das x symbol im fenster klicks. (Das ruft auch meines wissens Platform.exit() auf und somit diese methode)
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
		
		graph = new Graph(server.getStreetGraph());
		
		GridLayout gl = new GridLayout(graph);
		gl.setScale(200);
        gl.execute();
        
        rl = new VehicleLayout(graph);
                
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
	
	public void addVehicle(final String vehicleId, final String position) {
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	flow.getChildren().add(buildVehiclePane(vehicleId, position));
        		rl.moveVehicleTo(vehicleId, position);
            }
        });
		
	}
	
	public void removeVehicle(final String vehicleId) {
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	graph.getModel().removeVehicleCell(vehicleId);
        		graph.endUpdate();
        		vehicleControllers.remove(vehicleId);
        		Object[] vehiclePanes = flow.getChildren().toArray();
        		
        		for(Object o: vehiclePanes) {
        			BorderPane vehiclePane = (BorderPane)o;
        			if(vehiclePane.getId().equals(vehicleId)) {
        				flow.getChildren().remove(vehiclePane);
        			}
        		}
            }
        });
	}
	
	private BorderPane buildVehiclePane(String vehicleId, String position) {
		BorderPane vehicle = null;
		try {
			FXMLLoader vehicleLoader = new FXMLLoader(Gui.class.getClassLoader().getResource(fxmlBundle.getString("fxml.robot")), config);
			vehicle = (BorderPane) vehicleLoader.load();
			vehicle.setId(vehicleId);
			VehicleController controller = vehicleLoader.getController();
			controller.setServer(server);
			controller.setVehicleId(vehicleId);
			controller.setVehicleLayout(rl);
			controller.setPositionTextField(position);
			vehicleControllers.put(vehicleId, controller);
			
			graph.getModel().addCell(vehicleId, CellType.VEHICLE);
			graph.endUpdate();
		} catch (IOException e) {
			System.out.println(e);
		}
		return vehicle;
	}
	
	public void addServerTextMessage(String msg) {
		textArea.appendText(msg);
		textArea.appendText("\n");
	}
	
	public void addVehicleTextMessage(String vehicleId, String msg) {
		VehicleController controller = vehicleControllers.get(vehicleId);
		controller.addTextMessage(msg);
		controller.addTextMessage("\n");
	}
	
	public void setVehiclePosition(String vehicleId, String position) {
		VehicleController controller = vehicleControllers.get(vehicleId);
		controller.setVehiclePosition(position);
	}
	
	public void turnVehicleLeft(String vehicleId) {
		VehicleController controller = vehicleControllers.get(vehicleId);
		controller.rotateVehicleLeftServer();
	}
	
	public void turnVehicleRight(String vehicleId) {
		VehicleController controller = vehicleControllers.get(vehicleId);
		controller.rotateVehicleRightServer();
	}
	
	public void setVehicleDestinationTextField(String vehicleId, String position) {
		VehicleController controller = vehicleControllers.get(vehicleId);
		controller.setDestinationTextField(position);
	}
	
	public static void main(String[] args) {
        launch(args);
    }
}
