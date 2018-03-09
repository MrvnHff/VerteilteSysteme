package server.gui.graph;

import java.util.ArrayList;
import java.util.List;

import server.server.graph.nodes.StreetNode;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Jede Zelle wird alsPane repräsentiert in der jeder art von Knoten als view eingesetzt werden kann
 * @author Mathias Wittling
 *
 */
public class Cell extends Pane {

    String cellId;
    StreetNode cellValue;

    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();

    Node view;

    public Cell(String cellId) {
        this.cellId = cellId;
    }
    
    public Cell(String cellId, StreetNode cellValue) {
    	this(cellId);
    	this.cellValue = cellValue;
    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);
    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }
    
    public void setCellValue(StreetNode cellValue) {
    	this.cellValue = cellValue;
    }
    
    public StreetNode getCellvalue() {
    	return this.cellValue;
    }
}
