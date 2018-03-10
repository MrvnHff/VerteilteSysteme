package server.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import server.gui.layout.vehicle.VehicleLayout;
import server.server.Server;
import server.server.ServerInterface;
import server.server.exceptions.NoValidTargetNodeException;
import server.server.exceptions.TargetIsOccupiedException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class VehicleController implements Initializable{

	@FXML
	private TextArea textArea;
	@FXML
	private Text title;
	@FXML
	private TextField position;
	@FXML
	private TextField destination;
	@FXML
	private Button modeButton;
	
	private ServerInterface server;
	private String vehicleId;
	private VehicleLayout vehicleLayout;
	
	private ResourceBundle bundle;
	private boolean automatic;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.bundle = resources;
		this.automatic = false;
		this.modeButton.setText(bundle.getString("robot.manual"));
		this.modeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				toggleMode();
			}
		});
		
		destination.setOnKeyPressed(new EventHandler<KeyEvent>() {
		    @Override
		    public void handle(KeyEvent event) {
		        if(event.getCode().equals(KeyCode.ENTER)) {
		             driveVehicleTo(destination.getText());
		        }
		    }
		});
	}
	
	public void setServer(ServerInterface server) {
		this.server = server;
	}
	
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
		this.title.setText(bundle.getString("robot.title") + " " + vehicleId);
	}
	
	private void toggleMode() {
		
		
		Thread t1 = null;
		if(automatic == false) {
			automatic = true;
			modeButton.setText(bundle.getString("robot.automatic"));
			
			t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					server.activateAutoDst(vehicleId);
				}
			});
			
			t1.start();
			
			
		} else {
			automatic = false;
			modeButton.setText(bundle.getString("robot.manual"));
			server.deactivateAutoDst(vehicleId);
		}
		
	}
	
	public void setVehicleLayout(VehicleLayout rl) {
		this.vehicleLayout = rl;
	}
	
	public void addTextMessage(String msg) {
		textArea.appendText(msg);
	}
	
	/**
	 * Rotiert das Fahrzeug in der Gui und im Server nach Links
	 */
	public void rotateVehicleLeft() {
		//rotateVehicle(-90); //FIXME dreht doppelt, wenn einkommentiert und Fahrzeug manuell gesteuert
		server.turnVehicleLeft(vehicleId);
	}
	
	public void rotateVehicleLeftServer() {
		rotateVehicle(-90);
	}
	
	/**
	 * Rotiert das Fahrzeug in der Gui und im Server nach Rechts
	 */
	public void rotateVehicleRight() {
		//rotateVehicle(90); //FIXME dreht doppelt, wenn einkommentiert und Fahrzeug manuell gesteuert
		server.turnVehicleRight(vehicleId);
	}
	
	public void rotateVehicleRightServer() {
		rotateVehicle(90);
	}
	
	/**
	 * Rotiert den Robotor nur in der Gui
	 * @param grad der rotation
	 */
	public void rotateVehicle(int angle) {
		vehicleLayout.rotate(this.vehicleId, angle);
	}
	
	
	private void showAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Aktion ist fehlgeschlagen");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public void moveVehicleForward() {
		try {
			String destination = server.moveVehicleForward(vehicleId);
			vehicleLayout.moveVehicleTo(vehicleId, destination);
			position.setText(destination);
		} catch (NoValidTargetNodeException e) {
			showAlert("Kein Gültiges Ziel");
		} catch (TargetIsOccupiedException e) {
			showAlert("Ziel ist bereits belegt");
		}
	}
	
	public void setVehiclePosition(final String position) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				setPosition(position);
			}
		});
		
	}
	
	private void setPosition(String position) {
		this.position .setText(position);
		vehicleLayout.moveVehicleTo(vehicleId, position);
	}
	
	public void setDestinationTextField(String position) {
		this.destination.setText(position);
	}

	public void setPositionTextField(String position) {
		this.position.setText(position);		
	}
	
	private void driveVehicleTo(final String destination) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				server.driveVehicletTo(vehicleId, destination);
			}
		}).start();
		
	}
	
	public void removeVehicle() {
		server.removeWorker(vehicleId);
	}
} 
