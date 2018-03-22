package test.miniGui;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MiniGui extends Application {
	
	private static final String CONFIG_FILENAME = "miniGui/miniGui";
	
	private ResourceBundle config;
	
	private Stage primaryStage = new Stage();
	private BorderPane layout;
	private MiniGuiController controller;
	
	//Vehicle attributes
	private String destinationIP;
	private int destinationPort;
	private int ownPort = 55540;
	private int waitTime = 3;
	private String vehicleName;
	
		
	private RemoteTestVehicle vehicle;
	
	@Override
	public void start(Stage primaryStage) {
		config = ResourceBundle.getBundle(CONFIG_FILENAME);
		
		showDialog();
		
		destinationIP = config.getString("DestinationIP");
		destinationPort = Integer.parseInt(config.getString("DestinationPort"));
		
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Fahrzeug");
		this.primaryStage.setMinHeight(400);
		this.primaryStage.setMinWidth(320);
		this.primaryStage.setResizable(false);
		

		
		try {
			FXMLLoader layoutLoader = new FXMLLoader(MiniGui.class.getClassLoader().getResource("miniGui/MiniGui.fxml"), config);
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
		this.addMessageToUi("GUI: start fertig");
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
			if(result.get().isEmpty()) {
		    	showDialog();
		    } else {
		    	vehicleName = result.get().replaceAll(" ", "_");
		    }
		} 
		
	}
	
	public void connectVehicle() {
		vehicle = new RemoteTestVehicle(
				destinationIP,
				destinationPort,
				ownPort, 
				waitTime,
				vehicleName);
		vehicle.setGui(this);
		vehicle.start();
		this.addMessageToUi("GUI: connectVehicle ausgeloest");
	}
	
	public void disconnectVehicle() {
		vehicle.quitWorker();
		try {
			vehicle.closeConnection();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		vehicle = null;
		this.addMessageToUi("GUI: disconnectVehicle ausgeloest");
	}
	
	    
   public void addMessageToUi(String msg) { 
       controller.addTextMessage(msg);
       controller.addTextMessage("\n");
   }
}
