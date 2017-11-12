package com.blackuml.volet.product.constraints;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.classes.edge.AggregationEdge;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Patrick Bednarski
 */
public class ConstraintVerifierSingleton {
    
    private static final Color PROBLEM_COLOR = Color.yellow;
    
    private final List<IEdge> CONSTRAINED_EDGES_PROTOTYPES;
    private final List<ConstrainedEdge> CONSTRAINED_EDGES;
    
    private ConstraintVerifierSingleton() {
        CONSTRAINED_EDGES_PROTOTYPES = new ArrayList<>(Arrays.asList(
        new AggregationEdge()));
        CONSTRAINED_EDGES = new ArrayList<>();
    }
    
    private boolean isConstrainedEdge(IEdge e) {
        return CONSTRAINED_EDGES_PROTOTYPES.stream().anyMatch((prototype) -> (e.getClass().equals(prototype.getClass())));
    }
    
    /**
     * Adds a new constrained edge to the list of constrained edges
     * Checks if there is a problem with this edge
     * @param e the edge to be added
     */
    public void addEdge(IEdge e) {
        ConstrainedEdge newConstrainedEdge;
        if (isConstrainedEdge(e)) {
            newConstrainedEdge = new ConstrainedEdge(e);
            CONSTRAINED_EDGES.add(newConstrainedEdge);
            checkForProblems(newConstrainedEdge);
        }
    }
    
    private void checkForProblems(ConstrainedEdge newConstrainedEdge) {
        for (ConstrainedEdge edge : CONSTRAINED_EDGES) {
            if (edge.getClass().equals(newConstrainedEdge.getClass())) {
                if (!edge.getEdge().getId().getValue().equals(newConstrainedEdge.getEdge().getId().getValue())) {
                    if (newConstrainedEdge.getStartNode().getId().getValue().equals(edge.getEndNode().getId().getValue())
                            && newConstrainedEdge.getEndNode().getId().getValue().equals(edge.getStartNode().getId().getValue())) {
                        newConstrainedEdge.getStartNode().setBackgroundColor(PROBLEM_COLOR);
                        newConstrainedEdge.getEndNode().setBackgroundColor(PROBLEM_COLOR);
                    }                            
                }
            }
        }
    }
    
    /**
     * returns the instance of the constraint verifier
     * @return ConstraintVerifierSignleton 
     */
    public static ConstraintVerifierSingleton getInstance() {
        return ConstraintVerifierSingletonHolder.INSTANCE;
    }
    
    private static class ConstraintVerifierSingletonHolder {
        private static final ConstraintVerifierSingleton INSTANCE = new ConstraintVerifierSingleton();
    }
}
