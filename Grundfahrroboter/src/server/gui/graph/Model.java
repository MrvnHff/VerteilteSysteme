package server.gui.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.gui.cells.RectangleCell;
import server.gui.cells.RobotCell;
import server.gui.cells.TriangleCell;

public class Model {

    Cell graphParent;

    List<Cell> allCells;
    List<Cell> addedCells;
    List<Cell> removedCells;
    
    List<RobotCell> allRobotCells;
    List<RobotCell> addedRobotCells;
    List<RobotCell> removedRobotCells;

    List<Edge> allEdges;
    List<Edge> addedEdges;
    List<Edge> removedEdges;

    Map<String,Cell> cellMap; // <id,cell>
    Map<String, Cell> robotMap;

    public Model() {

         graphParent = new Cell( "_ROOT_");

         // clear model, create lists
         clear();
    }

    public void clear() {

        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();

        allRobotCells = new ArrayList<>();
        addedRobotCells = new ArrayList<>();
        removedRobotCells = new ArrayList<>();
        
        addedRobotCells = new ArrayList<>();
        cellMap = new HashMap<>(); // <id,cell>
        robotMap = new HashMap<>();

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
        addedRobotCells.clear();
    }

    public List<Cell> getAddedCells() {
        return addedCells;
    }
    
    public List<RobotCell> getAddedRobotCells() {
    	return addedRobotCells;
    }

    public List<Cell> getRemovedCells() {
        return removedCells;
    }

    public List<RobotCell> getRemovedRobotCells() {
        return removedRobotCells;
    }
    
    public List<Cell> getAllCells() {
        return allCells;
    }
    
    public List<RobotCell> getAllRobotCells() {
    	return allRobotCells;
    }
    
    public Cell getCell(String id) {
    	return cellMap.get(id);
    }
    
    public Cell getRobotCell(String id) {
    	return robotMap.get(id);
    }

    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public void addCell(String id, CellType type) {

        switch (type) {

        case RECTANGLE:
            RectangleCell rectangleCell = new RectangleCell(id);
            addCell(rectangleCell);
            break;

        case TRIANGLE:
            TriangleCell circleCell = new TriangleCell(id);
            addCell(circleCell);
            break;
        case ROBOT:
        	RobotCell robotCell = new RobotCell(id);
        	addRobotCell(robotCell);
        	break;

        default:
            throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    private void addCell( Cell cell) {

        addedCells.add(cell);
        cellMap.put( cell.getCellId(), cell);

    }
    
    private void addRobotCell(RobotCell cell) {
    	addedRobotCells.add(cell);
    	robotMap.put(cell.getCellId(), cell);
    }

    public void addEdge( String sourceId, String targetId) {

        Cell sourceCell = cellMap.get( sourceId);
        Cell targetCell = cellMap.get( targetId);

        Edge edge = new Edge( sourceCell, targetCell);

        addedEdges.add( edge);

    }

    /**
     * Attach all cells which don't have a parent to graphParent 
     * @param cellList
     */
    public void attachOrphansToGraphParent( List<Cell> cellList) {

        for( Cell cell: cellList) {
            if( cell.getCellParents().size() == 0) {
                graphParent.addCellChild( cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     * @param cellList
     */
    public void disconnectFromGraphParent( List<Cell> cellList) {

        for( Cell cell: cellList) {
            graphParent.removeCellChild( cell);
        }
    }

    public void merge() {

        // cells
        allCells.addAll( addedCells);
        allCells.removeAll( removedCells);
        
        allRobotCells.addAll(addedRobotCells);
        allRobotCells.addAll(removedRobotCells);

        addedCells.clear();
        removedCells.clear();
        
        addedRobotCells.clear();
        removedRobotCells.clear();

        // edges
        allEdges.addAll( addedEdges);
        allEdges.removeAll( removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }
}
