package com.blackuml.volet.product.constraints;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.classes.edge.AggregationEdge;
import com.horstmann.violet.product.diagram.classes.edge.CompositionEdge;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Patrick Bednarski
 */
public class ConstraintVerifier {

    private static final Color PROBLEM_COLOR = new Color(254, 132, 132);
    private static final Color PROBLEM_COLOR_BORDER = new Color(254, 0, 0);
    private final ArrayList<IEdge> CONSTRAINED_EDGES_PROTOTYPES;
    private final Collection<IEdge> GRAPH_EDGES;

    public ConstraintVerifier(Collection<IEdge> GRAPH_EDGES) {
        CONSTRAINED_EDGES_PROTOTYPES = new ArrayList<>(Arrays.asList(
                new AggregationEdge(),
                new CompositionEdge()));
        this.GRAPH_EDGES = GRAPH_EDGES;
    }

    private boolean isConstrainedEdge(IEdge e) {
        return CONSTRAINED_EDGES_PROTOTYPES.stream().anyMatch((edge) -> (edge.getClass().equals(e.getClass())));
    }

    public void verifyEdge(IEdge e) {
        if (isConstrainedEdge(e)) {
            if (e instanceof CompositionEdge || e instanceof AggregationEdge) {
                if (bidirectionalEdge(e)) {
                    ((ColorableNode) e.getStartNode()).setBackgroundColor(PROBLEM_COLOR);
                    ((ColorableNode) e.getStartNode()).setBorderColor(PROBLEM_COLOR_BORDER);
                    ((ColorableNode) e.getEndNode()).setBackgroundColor(PROBLEM_COLOR);
                    ((ColorableNode) e.getEndNode()).setBorderColor(PROBLEM_COLOR_BORDER);
                }
            }
        }
    }

    private boolean bidirectionalEdge(IEdge e) {
        INode start = e.getStartNode(), end = e.getEndNode();
        Class<?> eClass = e.getClass();
        return GRAPH_EDGES.stream().anyMatch((edge) ->
                (!edge.getId().equals(e.getId()) &&
                edge.getClass().equals(eClass) &&
                edge.getStartNode().getId().equals(end.getId()) &&
                edge.getEndNode().getId().equals(start.getId())));
    }
}
