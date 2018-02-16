package de.htwsaar.vs.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import de.htwsaar.vs.gui.layout.robot.RobotLayout;
import de.htwsaar.vs.server.Server;
import de.htwsaar.vs.server.exceptions.NoValidTargetNodeException;
import de.htwsaar.vs.server.exceptions.TargetIsOccupiedException;
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

public class RobotController implements Initializable{

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
	
	private Server server;
	private String robotId;
	private RobotLayout robotLayout;
	
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
		             driveRobotTo(destination.getText());
		        }
		    }
		});
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public void setRobotId(String robotId) {
		this.robotId = robotId;
		this.title.setText(bundle.getString("robot.title") + " " + robotId);
	}
	
	private void toggleMode() {
		if(automatic == false) {
			automatic = true;
			modeButton.setText(bundle.getString("robot.automatic"));
		} else {
			automatic = false;
			this.modeButton.setText(bundle.getString("robot.manual"));
		}
	}
	
	public void setRobotLayout(RobotLayout rl) {
		this.robotLayout = rl;
	}
	
	public void addTextMessage(String msg) {
		textArea.appendText(msg);
	}
	
	/**
	 * Rotiert den Roboter in der Gui und im Server nach Links
	 */
	public void rotateRobotLeft() {
		rotateRobot(-90);
		server.turnRobotLeft(robotId);
	}
	
	public void rotateRobotLeftServer() {
		rotateRobot(-90);
	}
	
	/**
	 * Rotiert den Roboter in der Gui und im Server nach Rechts
	 */
	public void rotateRobotRight() {
		rotateRobot(90);
		server.turnRobotRight(robotId);
	}
	
	public void rotateRobotRightServer() {
		rotateRobot(90);
	}
	
	/**
	 * Rotiert den Robotor nur in der Gui
	 * @param grad der rotation
	 */
	public void rotateRobot(int angle) {
		robotLayout.rotate(this.robotId, angle);
	}
	
	
	private void showAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Aktion ist fehlgeschlagen");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public void moveRobotForward() {
		try {
			String destination = server.moveRobotForward(robotId);
			robotLayout.moveRobotTo(robotId, destination);
			position.setText(destination);
		} catch (NoValidTargetNodeException e) {
			showAlert("Kein Gültiges Ziel");
		} catch (TargetIsOccupiedException e) {
			showAlert("Ziel ist bereits belegt");
		}
	}
	
	public void setRobotPosition(String position) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				setPosition(position);
			}
		});
		
	}
	
	private void setPosition(String position) {
		this.position .setText(position);
		robotLayout.moveRobotTo(robotId, position);
	}

	public void setPositionTextField(String position) {
		this.position.setText(position);		
	}
	
	private void driveRobotTo(String destination) {
		new Thread(() -> {
			server.driveRobotTo(robotId, destination);
		}).start();
		
	}
} 
