package server.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import server.gui.layout.vehicle.VehicleLayout;
import server.server.Server;
import server.server.ServerInterface;
import server.server.exceptions.NoValidTargetNodeException;
import server.server.exceptions.TargetIsOccupiedException;
import server.utils.IdUtils;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
		        	 if(!IdUtils.isValidId(destination.getText())) {
		        		 showAlert("Keine Gültige Node Id\n"
			        		 		+ "Id muss dem Muster [0-9]*/[0-9]* entsprechen");
		        	 } else if(!server.nodeExists(destination.getText())) { 
		        		 showAlert("Die Knoten Id " + destination.getText() + " ist nicht im Graph vorhanden");
		        	 } else {
		        		 driveVehicleTo(destination.getText());
		        	 }
		        	 
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
	
	public void addTextMessage(final String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textArea.appendText(msg);
			}
		});
		
	}
	
	/**
	 * Rotiert das Fahrzeug in der Gui und im Server nach Links
	 */
	public void rotateVehicleLeft() {
		
		Service<Void> ser = new Service<Void>() {
	        @Override protected Task createTask() {
	            return new Task<Void>() {
	            @Override protected Void call() throws InterruptedException {
	            	server.turnVehicleLeft(vehicleId);
	                return null;
	                }
	            };
	         }
		};
		ser.start();
	}
	
	public void rotateVehicleLeftServer() {
		rotateVehicle(-90);
	}
	
	/**
	 * Rotiert das Fahrzeug in der Gui und im Server nach Rechts
	 */
	public void rotateVehicleRight() {
		
		Service<Void> ser = new Service<Void>() {
	        @Override protected Task createTask() {
	            return new Task<Void>() {
	            @Override protected Void call() throws InterruptedException {
	            	server.turnVehicleRight(vehicleId);
	                return null;
	                }
	            };
	         }
		};
		ser.start();
		
	}
	
	public void rotateVehicleRightServer() {
		rotateVehicle(90);
	}
	
	/**
	 * Rotiert den Robotor nur in der Gui
	 * @param grad der rotation
	 */
	public void rotateVehicle(final int angle) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vehicleLayout.rotate(vehicleId, angle);
			}
		});
		
	}
	
	
	private void showAlert(final String message) {
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Aktion ist fehlgeschlagen");
				alert.setContentText(message);
				alert.showAndWait();
			}
		});
	}
	
	public void moveVehicleForward() {
		
		Service<Void> ser = new Service<Void>() {
	        @Override protected Task createTask() {
	            return new Task<Void>() {
	            @Override protected Void call() throws InterruptedException {
	            	try {
						String destination = server.moveVehicleForward(vehicleId);
						vehicleLayout.moveVehicleTo(vehicleId, destination);
						setPositionTextField(destination);
					} catch (NoValidTargetNodeException e) {
						showAlert("Kein Gueltiges Ziel");
					} catch (TargetIsOccupiedException e) {
						showAlert("Ziel ist bereits belegt");
					} catch (NullPointerException e) {
						System.out.println("NullPointer");
					}
	                return null;
	                }
	            };
	         }
		};
		ser.start();
		
	}
	
	public void setVehiclePosition(final String position) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setPosition(position);
			}
		});
		
	}
	
	private void setPosition(final String pos) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				position .setText(pos);
				vehicleLayout.moveVehicleTo(vehicleId, pos);
			}
		});
		
	}
	
	public void setDestinationTextField(final String position) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				destination.setText(position);
			}
		});
	}

	public void setPositionTextField(final String pos) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				position.setText(pos);
			}
		});
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
