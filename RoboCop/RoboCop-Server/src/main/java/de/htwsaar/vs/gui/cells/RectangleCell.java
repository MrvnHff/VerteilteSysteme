package de.htwsaar.vs.gui.cells;

import de.htwsaar.vs.gui.graph.Cell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class RectangleCell extends Cell {

    public RectangleCell( String id) {
        super( id);
        
        StackPane view = new StackPane();
        Rectangle rectangle = new Rectangle( 50,50);
        rectangle.setStroke(Color.DODGERBLUE);
        rectangle.setFill(Color.DODGERBLUE);
        Text text = new Text(id);
        view.getChildren().addAll(rectangle, text);

        setView( view);

    }

}
