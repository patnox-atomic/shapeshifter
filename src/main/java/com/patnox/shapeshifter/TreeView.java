package com.patnox.shapeshifter;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.shape.*;

import javax.swing.*;
import java.util.ArrayList;

/**
 * A foldable directed acyclic graph (DAG) where each child has only one parent. AKA a Tree.
 *
 * @author patnox
 *
 */
public class TreeView extends JFrame
{
    private static final long serialVersionUID = -2707712944901661771L;

    public TreeView(Shape unit)
    {
        super("Shape Shift Drifter");

        FoldableTree graph = new FoldableTree();

        mxCompactTreeLayout layout = new mxCompactTreeLayout(graph, false);
        layout.setUseBoundingBox(false);
        layout.setEdgeRouting(false);
        layout.setLevelDistance(30);
        layout.setNodeDistance(10);

        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            visit(unit, graph, parent, null);

            layout.execute(parent);
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        graph.addListener(mxEvent.FOLD_CELLS,  new mxIEventListener() {

            @Override
            public void invoke(Object sender, mxEventObject evt) {
                layout.execute(graph.getDefaultParent());
            }
        });

        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        getContentPane().add(graphComponent);
    }

    //Walk the tree
    public void visit(Shape input, FoldableTree graph, Object parent, Object current)
    {
        ////Object v1 = graph.insertVertex(parent, input.getLabel(), input.getLabel(), 0, 0, 60, 40, "ellipse;shape=ellipse;rounded=1;strokeColor=red;fillColor=green;whiteSpace=wrap;html=1;sketch=1;hachureGap=4;");
        String style = "";
        if(input instanceof Circle) {
            style = "shape=ellipse;fillColor=orange";
        }
        else if(input instanceof Square) {
            style = "shape=rectangle;";
        }

        Object v1 = graph.insertVertex(parent, input.getLabel(), input.getLabel(), 0, 0, 60, 60, style);
        if(current != null)
        {
            graph.insertEdge(parent, null, "", current, v1);
        }
        for (Shape node: input.getChildren())
        {
            visit(node, graph, parent, v1);
        }
    }

}
