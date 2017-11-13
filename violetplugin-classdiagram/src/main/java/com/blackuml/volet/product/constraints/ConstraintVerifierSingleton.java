package com.blackuml.volet.product.constraints;

import com.horstmann.violet.product.diagram.abstracts.Id;
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
    private static final Color GOOD_COLOR = Color.white;

    
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
    
    public void removeEdge(Id id) {
        outerloop:
        for (ConstrainedEdge edgeToRemove : CONSTRAINED_EDGES) {
            if (id.equals(edgeToRemove.getEdge().getId())) {
                if (edgeToRemove.isProblematic()) {
                    for (ConstrainedEdge edgeToClear : CONSTRAINED_EDGES) {
                        if (edgeToRemove.getEdge().getId().equals(edgeToClear.getEdge().getId())) {
                            edgeToClear.getStartNode().setBackgroundColor(GOOD_COLOR);
                            edgeToClear.getEndNode().setBackgroundColor(GOOD_COLOR);
                            edgeToClear.clearProblematic();
                            break outerloop; //this was added after commenting the buggy line, it's not the cause of the bug
                        }
                    }
                }
                //CONSTRAINED_EDGES.remove(edgeToRemove); //this line causes a bug, List may be deleting the object as well (how to prevent that)
            }
        }
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
        CONSTRAINED_EDGES.stream().filter((edge) ->
        (edge.getClass().equals(newConstrainedEdge.getClass()))).filter((edge) ->
        (!edge.getEdge().getId().equals(newConstrainedEdge.getEdge().getId()))).filter((edge) ->
        (newConstrainedEdge.getStartNode().getId().equals(edge.getEndNode().getId())
        && newConstrainedEdge.getEndNode().getId().equals(edge.getStartNode().getId()))).map((_item) -> {
            return _item;
        }).forEachOrdered((_item) -> {
            newConstrainedEdge.getEndNode().setBackgroundColor(PROBLEM_COLOR);
            newConstrainedEdge.getStartNode().setBackgroundColor(PROBLEM_COLOR);
            newConstrainedEdge.setProblematic(_item.getEdge().getId());
            _item.setProblematic(newConstrainedEdge.getEdge().getId());
        });
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
