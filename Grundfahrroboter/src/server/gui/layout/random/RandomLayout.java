package server.gui.layout.random;

import java.util.List;
import java.util.Random;

import server.gui.graph.Cell;
import server.gui.graph.Graph;
import server.gui.layout.base.Layout;

/**
 * Zur Positionierung und anordnung von Knoten nach dem Zufallsprinzip
 * @author Mathias
 */
public class RandomLayout extends Layout {

    private Graph graph;

    private Random rnd = new Random();

    public RandomLayout(Graph graph) {

        this.graph = graph;

    }

    /**
     * Wei�t  jedem Knoten eine zuf�llige Position zu
     */
    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {

            double x = rnd.nextDouble() * 500;
            double y = rnd.nextDouble() * 500;

            cell.relocate(x, y);
        }
    }
}
