package miniGui;

import java.io.IOException;
import java.util.ResourceBundle;

import client.RemoteVehicleInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import test.VirtualRobot;

public class MiniGui extends Application {
	
	private static final String CONFIG_FILENAME = "resources/config/miniGui";
	
	private ResourceBundle config;
	
	private Stage primaryStage = new Stage();
	private BorderPane layout;
	private MiniGuiController controller;
	
	//private RemoteVehicleInterface vehicleInterface;
	
	private VirtualRobot vehicle;
	
	@Override
	public void start(Stage primaryStage) {
		config = ResourceBundle.getBundle(CONFIG_FILENAME);
		
		String destinationIP = config.getString("DestinationIP");
		int destinationPort = Integer.parseInt(config.getString("DestinationPort"));
		int ownPort = 55555;
		int waitTime = 2;
		String vehicleName = config.getString("VehicleName");
		vehicleName = vehicleName.replaceAll(" ", "_");
		
		
		vehicle = new VirtualRobot(
				destinationIP,
				destinationPort,
				ownPort, 
				waitTime,
				vehicleName);
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Fahrzeug");
		this.primaryStage.setMinHeight(400);
		this.primaryStage.setMinWidth(320);
		this.primaryStage.setResizable(false);
		

		
		try {
			FXMLLoader layoutLoader = new FXMLLoader(MiniGui.class.getClassLoader().getResource("resources/fxml/MiniGui.fxml"), config);
			layout = (BorderPane) layoutLoader.load();
			controller = layoutLoader.getController();
			controller.setGui(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		controller.setTargetIpTextField(destinationIP + ":" + destinationPort);
		controller.setVehicleIdTextField(vehicleName);
	}

	public static void main(String[] args) {
        launch(args);
    }
	
	public RemoteVehicleInterface getRobot() {
		return this.vehicle;
	}
}
