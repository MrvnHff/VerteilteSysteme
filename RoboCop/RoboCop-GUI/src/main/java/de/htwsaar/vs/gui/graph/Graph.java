package de.htwsaar.vs.gui.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import de.htwsaar.vs.server.graph.RoboGraph;
import de.htwsaar.vs.server.graph.edges.RoboEdge;
import de.htwsaar.vs.server.graph.nodes.RoboNode;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class Graph {

    private Model model;

    private Group canvas;

    private ZoomableScrollPane scrollPane;

    MouseGestures mouseGestures;

    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */
    CellLayer cellLayer;

    public Graph() {

        this.model = new Model();

        canvas = new Group();
        cellLayer = new CellLayer();

        canvas.getChildren().add(cellLayer);

        mouseGestures = new MouseGestures(this);

        scrollPane = new ZoomableScrollPane(canvas);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

    }
    
    public Graph(RoboGraph roboGraph) {
    	this();
    	RoboNode node;
    	Collection<RoboNode> nodes = roboGraph.getAllNodes();
    	RoboNode[] nodesArray = new RoboNode[nodes.size()];
    	nodes.toArray(nodesArray);
    	RoboEdge edge;
    	Set<RoboEdge> edges;
    	int col = roboGraph.getColumnCount();
    	int row = roboGraph.getRowCount();
    	
    	
    	for(int i = 0; i < nodes.size(); i++) {
    		node = nodesArray[i];
    		model.addCell(node.getNodeId(), CellType.RECTANGLE);
    	}
    	
    	for(int i = 0; i < nodes.size(); i++) {
    		node = nodesArray[i];
    		edges = roboGraph.getEdgesOf(node.getNodeId());
    		for(Iterator<RoboEdge> it = edges.iterator(); it.hasNext();) {
    			edge = it.next();
    			model.addEdge(roboGraph.getEdgeSource(edge), roboGraph.getEdgeTarget(edge));
    			
    		}
    	}
    	
    	/*for(int i = 0; i < row - 1 ; i++) {
    		for(int j = 0; j < col - 1 ; j++) {
    			model.addEdge(i + "/" + j, i + "/" + (j+1));
    			model.addEdge(i + "/" + j, (i+1) + "/" + j);
    		}
    	}
    	
    	
    	for(int n = 0; n < col-1; n++) {
			model.addEdge((row-1) + "/" + n, (row-1) + "/" + (n+1));    			
		}
		for(int m = 0; m < row-1; m++) {
			model.addEdge(m + "/" + (col-1), (m+1) + "/" + (col-1));
		}*/
    	endUpdate();
    	
    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public Pane getCellLayer() {
        return this.cellLayer;
    }

    public Model getModel() {
        return model;
    }

    public void beginUpdate() {
    }

    public void endUpdate() {

        // add components to graph pane
        getCellLayer().getChildren().addAll(model.getAddedEdges());
        getCellLayer().getChildren().addAll(model.getAddedCells());
        getCellLayer().getChildren().addAll(model.getAddedRobotCells());

        // remove components from graph pane
        getCellLayer().getChildren().removeAll(model.getRemovedCells());
        getCellLayer().getChildren().removeAll(model.getRemovedRobotCells());
        getCellLayer().getChildren().removeAll(model.getRemovedEdges());

        // enable dragging of cells
        for (Cell cell : model.getAddedCells()) {
            mouseGestures.makeDraggable(cell);
        }

        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getModel().attachOrphansToGraphParent(model.getAddedCells());

        // remove reference to graphParent
        getModel().disconnectFromGraphParent(model.getRemovedCells());

        // merge added & removed cells with all cells
        getModel().merge();

    }

    public double getScale() {
        return this.scrollPane.getScaleValue();
    }
}
