package de.htwsaar.vs.gui.cells;

import de.htwsaar.vs.gui.graph.Cell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class RobotCell extends Cell {
	
	double width = 50;
    double height = 50;

	public RobotCell(String id) {
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
