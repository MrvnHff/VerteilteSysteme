package de.htwsaar.vs.gui.layout.random;

import java.util.List;
import java.util.Random;

import de.htwsaar.vs.gui.graph.Cell;
import de.htwsaar.vs.gui.graph.Graph;
import de.htwsaar.vs.gui.layout.base.Layout;

public class RandomLayout extends Layout {

    private Graph graph;

    private Random rnd = new Random();

    public RandomLayout(Graph graph) {

        this.graph = graph;

    }

    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {

            double x = rnd.nextDouble() * 500;
            double y = rnd.nextDouble() * 500;

            cell.relocate(x, y);

        }

    }

}
