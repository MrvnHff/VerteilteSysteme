package miniGui;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import client.RemoteVehicleInterface;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import test.VirtualRobot;

public class MiniGui extends Application {
	
	private static final String CONFIG_FILENAME = "resources/config/miniGui";
	
	private ResourceBundle config;
	
	private Stage primaryStage = new Stage();
	private BorderPane layout;
	private MiniGuiController controller;
	private String vehicleName;
	
	//private RemoteVehicleInterface vehicleInterface;
	
	private VirtualRobot vehicle;
	
	@Override
	public void start(Stage primaryStage) {
		config = ResourceBundle.getBundle(CONFIG_FILENAME);
		
		showDialog();
		
		String destinationIP = config.getString("DestinationIP");
		int destinationPort = Integer.parseInt(config.getString("DestinationPort"));
		int ownPort = 55565;
		int waitTime = 2;
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
		controller.addTextMessage("GUI: start fertig");
	}

	public static void main(String[] args) {
        launch(args);
    }
	
	private void showDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText("Choose your Name");
		dialog.setContentText("Vehicle Name: ");
		dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    vehicleName = result.get();
		    if(result.get().isEmpty()) {
		    	showDialog();
		    }
		} 
		
	}
	
	public void connectVehicle() {
		vehicle.start();
		controller.addTextMessage("GUI: connectVehicle ausgel�st");
	}
	
	public void disconnectVehicle() {
		vehicle.quitWorker();
		controller.addTextMessage("GUI: disconnectVehicle ausgel�st");
	}
}
