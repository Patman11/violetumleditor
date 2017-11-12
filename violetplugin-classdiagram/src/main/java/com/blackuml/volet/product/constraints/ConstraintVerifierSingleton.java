package com.blackuml.volet.product.constraints;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.classes.edge.AggregationEdge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Patrick Bednarski
 */
public class ConstraintVerifierSingleton {
    
    private final List<IEdge> CONSTRAINED_EDGES_PROTOTYPES;
    
    private ConstraintVerifierSingleton() {
        CONSTRAINED_EDGES_PROTOTYPES = new ArrayList<>(Arrays.asList(
        new AggregationEdge()));
    }
    
    /**
     * Loops through the constrained edges to check if the 
     * passed edge has to be verified against the constraints
     * @param e the edge to be checked
     * @return true/false if this edge is part of the constraints
     */
    public boolean isConstrainedEdge(IEdge e) {
        return CONSTRAINED_EDGES_PROTOTYPES.stream().anyMatch((prototype) -> (e.getClass().equals(prototype.getClass())));
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
