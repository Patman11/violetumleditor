package com.blackuml.volet.product.constraints;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;

/**
 * A class encapsulating constrained edges
 * @author Patrick Bednarski
 */
public class ConstrainedEdge {
    
    /**
     * Constructs a new instance of ConstrainedEdge
     * @param edge the constrained edge to be created
     */
    public ConstrainedEdge(IEdge edge) {
        this.edge = edge;
        startNode = null;
        endNode = null;
        if (edge.getStartNode() instanceof ColorableNode) {
            startNode = (ColorableNode) edge.getStartNode();
        }
        if (edge.getEndNode() instanceof ColorableNode) {
            endNode = (ColorableNode) edge.getEndNode();
        }
    }
    
    /**
     * Gets the constrained edge
     * @return edge
     */
    public IEdge getEdge() {
        return edge;
    }
    
    /**
     * Gets the start node as casted into a ColorableNode
     * @return startNode
     */
    public ColorableNode getStartNode() {
        return startNode;
    }
    
    /**
     * Gets the end node as casted into a ColorableNode
     * @return endNode
     */
    public ColorableNode getEndNode() {
        return endNode;
    }
    
    private final IEdge edge;
    private ColorableNode startNode, endNode;
}
