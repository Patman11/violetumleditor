package com.horstmann.violet.product.diagram.classes;

import java.util.*;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

import com.horstmann.violet.product.diagram.classes.edge.*;
import com.horstmann.violet.product.diagram.classes.node.*;

import com.horstmann.violet.product.diagram.common.edge.NoteEdge;
import com.horstmann.violet.product.diagram.common.node.NoteNode;
import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * A UML class diagram.
 */
public class ClassDiagramGraph extends AbstractGraph
{
    public List<INode> getNodePrototypes()
    {
        return NODE_PROTOTYPES;
    }

    public List<IEdge> getEdgePrototypes()
    {
        return EDGE_PROTOTYPES;
    }

    private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>(Arrays.asList(
            new ClassNode(),
            new InterfaceNode(),
            new EnumNode(),
            new PackageNode(),
            new BallAndSocketNode(),
            new NoteNode()
    ));

    private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>(Arrays.asList(
            new DependencyEdge(),
            new InheritanceEdge(),
            new InterfaceInheritanceEdge(),
            new AssociationEdge(),
            new AggregationEdge(),
            new CompositionEdge(),
            new NoteEdge()
    ));
    
    @Override
    public boolean connect(IEdge e, INode start, Point2D startLocation, INode end, Point2D endLocation, Point2D[] transitionPoints)
    {
        // Step 1 : find if node exist
        Collection<INode> allNodes = getAllNodes();
        if (start != null && !allNodes.contains(start))
        {
            addNode(start, start.getLocation());
        }
        if (end != null && !allNodes.contains(end))
        {
            addNode(end, end.getLocation());
        }

        e.setStartNode(start);
        e.setStartLocation(startLocation);
        e.setEndNode(end);
        e.setEndLocation(endLocation);
        e.setTransitionPoints(transitionPoints);
        if (start instanceof ColorableNode) {
            ((ColorableNode) start).setBackgroundColor(Color.red);
        }
        if (null != start && start.addConnection(e))
        {
            e.setId(new Id());
            super.getEdges().add(e);

            start.onConnectedEdge(e);
            if(end != null)
            {
                end.onConnectedEdge(e);
            }

            return true;
        }

        return false;
    }
}
