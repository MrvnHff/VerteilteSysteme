package de.htwsaar.vs.gui.graph;

import java.util.ArrayList;
import java.util.List;

import de.htwsaar.vs.server.graph.nodes.RoboNode;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Cell extends Pane {

    String cellId;
    RoboNode cellValue;

    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();

    Node view;

    public Cell(String cellId) {
        this.cellId = cellId;
    }
    
    public Cell(String cellId, RoboNode cellValue) {
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
    
    public void setCellValue(RoboNode cellValue) {
    	this.cellValue = cellValue;
    }
    
    public RoboNode getCellvalue() {
    	return this.cellValue;
    }
}
