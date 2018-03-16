package server.gui.cells;

import server.gui.graph.Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *	Dreieckige Zelle zum einfügen in den Graphen
 */
public class TriangleCell extends Cell {

    public TriangleCell( String id) {
        super( id);

        double width = 50;
        double height = 50;

        Polygon view = new Polygon( width / 2, 0, width, height, 0, height);

        view.setStroke(Color.RED);
        view.setFill(Color.RED);

        setView( view);

    }

}
