package server.gui.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.gui.cells.RectangleCell;
import server.gui.cells.VehicleCell;
import server.gui.cells.TriangleCell;

public class Model {

    Cell graphParent;

    List<Cell> allCells;
    List<Cell> addedCells;
    List<Cell> removedCells;
    
    List<VehicleCell> allVehicleCells;
    List<VehicleCell> addedVehicleCells;
    List<VehicleCell> removedVehicleCells;

    List<Edge> allEdges;
    List<Edge> addedEdges;
    List<Edge> removedEdges;

    Map<String,Cell> cellMap; // <id,cell>
    Map<String, Cell> vehicleMap;

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

        allVehicleCells = new ArrayList<>();
        addedVehicleCells = new ArrayList<>();
        removedVehicleCells = new ArrayList<>();
        
        addedVehicleCells = new ArrayList<>();
        cellMap = new HashMap<>(); // <id,cell>
        vehicleMap = new HashMap<>();

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
        addedVehicleCells.clear();
    }

    public List<Cell> getAddedCells() {
        return addedCells;
    }
    
    public List<VehicleCell> getAddedVehicleCells() {
    	return addedVehicleCells;
    }

    public List<Cell> getRemovedCells() {
        return removedCells;
    }

    public List<VehicleCell> getRemovedVehicleCells() {
        return removedVehicleCells;
    }
    
    public List<Cell> getAllCells() {
        return allCells;
    }
    
    public List<VehicleCell> getAllVehicleCells() {
    	return allVehicleCells;
    }
    
    public Cell getCell(String id) {
    	return cellMap.get(id);
    }
    
    public Cell getVehicleCell(String id) {
    	return vehicleMap.get(id);
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
        case VEHICLE:
        	VehicleCell vehicleCell = new VehicleCell(id);
        	addVehicleCell(vehicleCell);
        	break;

        default:
            throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    private void addCell( Cell cell) {
        addedCells.add(cell);
        cellMap.put( cell.getCellId(), cell);
    }
    
    public void removeCell(String cellId) {
		this.removedCells.add(cellMap.get(cellId));
		cellMap.remove(cellId);
		//FIXME (Obsolet) verknüpfte Kanten ebenfalls Löschen
		
	}
    
    private void addVehicleCell(VehicleCell cell) {
    	addedVehicleCells.add(cell);
    	vehicleMap.put(cell.getCellId(), cell);
    }
    
    public void removeVehicleCell(String vehicleId) {
		removedVehicleCells.add((VehicleCell) vehicleMap.get(vehicleId));
		vehicleMap.remove(vehicleId);
	}

    public void addEdge( String sourceId, String targetId) {

        Cell sourceCell = cellMap.get( sourceId);
        Cell targetCell = cellMap.get( targetId);

        Edge edge = new Edge( sourceCell, targetCell);

        addedEdges.add( edge);

    }

    /**
     * Attach all cells which don't have a parent to graphParent 
     * @param cellList List of cells
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
     * @param cellList List of Cells
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
        
        allVehicleCells.addAll(addedVehicleCells);
        allVehicleCells.addAll(removedVehicleCells);

        addedCells.clear();
        removedCells.clear();
        
        addedVehicleCells.clear();
        removedVehicleCells.clear();

        // edges
        allEdges.addAll( addedEdges);
        allEdges.removeAll( removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }

	

	
}
