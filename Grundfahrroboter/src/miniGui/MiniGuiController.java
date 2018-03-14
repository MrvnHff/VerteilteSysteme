package miniGui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MiniGuiController implements Initializable {
	
	@FXML
	private TextField vehicleId;
	@FXML
	private TextField targetIp;
	@FXML
	private TextArea textArea;
	
	private MiniGui gui;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}
	
	public void connect() {
		gui.connectVehicle();
	}
	
	public void disconnect() {
		gui.disconnectVehicle();
	}
	
	public void addTextMessage(final String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textArea.appendText(msg);
			}
		});
	}
	
	public void setVehicleIdTextField(final String id) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vehicleId.setText(id);
			}
		});
	}
	
	public void setTargetIpTextField(final String ip) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				targetIp.setText(ip);
			}
		});
	}
	
	public void setGui(MiniGui gui) {
		this.gui = gui;
	}
}
