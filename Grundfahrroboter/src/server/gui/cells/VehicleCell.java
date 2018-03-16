package server.gui.cells;

import server.gui.graph.Cell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

/**
 * Dreieckige Zelle mit Namen. Als repräsentation der Fahrzeuge
 */
public class VehicleCell extends Cell {
	
	double width = 50;
    double height = 50;

	public VehicleCell(String id) {
		super(id);
		
		StackPane view = new StackPane();
		Polygon polygon = new Polygon( width / 2, 0, width, height, 0, height);
        polygon.setStroke(Color.RED);
        polygon.setFill(Color.RED);
        Text text = new Text(id);
        view.getChildren().addAll(polygon, text);

        setView( view);
	}
}
