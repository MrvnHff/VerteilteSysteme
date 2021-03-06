package de.htwsaar.vs.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import de.htwsaar.vs.gui.layout.robot.RobotLayout;
import de.htwsaar.vs.server.Server;
import de.htwsaar.vs.server.exceptions.NoValidTargetNodeException;
import de.htwsaar.vs.server.exceptions.TargetIsOccupiedException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	
	private Server server;
	private String robotId;
	private RobotLayout robotLayout;
	
	private ResourceBundle bundle;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.bundle = resources;
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public void setRobotId(String robotId) {
		this.robotId = robotId;
		this.title.setText(bundle.getString("robot.title") + " " + robotId);
	}
	
	public void setRobotLayout(RobotLayout rl) {
		this.robotLayout = rl;
	}
	
	public void addTextMessage(String msg) {
		textArea.appendText(msg);
	}
	
	public void rotateRobotLeft() {
		rotateRobot(-90);
		server.turnRobotLeft(robotId);
	}
	
	public void rotateRobotRight() {
		rotateRobot(90);
		server.turnRobotRight(robotId);
	}
	
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

	public void setPosition(String position) {
		this.position.setText(position);		
	}
} 
