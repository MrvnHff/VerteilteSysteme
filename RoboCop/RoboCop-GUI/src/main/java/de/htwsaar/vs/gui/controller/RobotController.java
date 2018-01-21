package de.htwsaar.vs.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import de.htwsaar.vs.gui.layout.robot.RobotLayout;
import de.htwsaar.vs.server.Server;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	}
	
	public void setRobotLayout(RobotLayout rl) {
		this.robotLayout = rl;
	}
	
	public void rotateRobotLeft() {
		rotateRobot(-90);
	}
	
	public void rotateRobotRight() {
		rotateRobot(90);
	}
	
	public void rotateRobot(int angle) {
		robotLayout.rotate(this.robotId, angle);
	}
} 
