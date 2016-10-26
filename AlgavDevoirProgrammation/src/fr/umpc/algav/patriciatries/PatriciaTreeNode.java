package fr.umpc.algav.patriciatries;

import java.util.ArrayList;

/**
 * Created by amadeus on 19.10.16.
 */
public class PatriciaTreeNode {
    private int key;
    private int arity;
    private boolean isLeaf;
    private ArrayList<PatriciaTreeEdge> edgesToChildren;

    public PatriciaTreeNode(int arity, boolean isLeaf) {
        this(arity, isLeaf, -1);
    }

    public PatriciaTreeNode(int arity, boolean isLeaf, int key) {
        this.arity = arity;
        this.isLeaf = isLeaf;
        this.key = key;

        if (!isLeaf) {
            initNullEdges();
        }
    }

    private void initNullEdges() {
        this.edgesToChildren = new ArrayList<>();

        for (int i = 0; i < arity; i++) {
            this.edgesToChildren.add(null);
        }
    }

    public boolean representsKeyForWord() {
        return key != -1;
    }

    public void updateChild(int edgeIndex, String edgeValue, int childNodeKey) {
        // If node was a leaf so far, make it an inner node and initialize all edges
        if (isLeaf) {
            isLeaf = false;
            initNullEdges();
        }

        try {
            PatriciaTreeEdge edge = edgesToChildren.get(edgeIndex);

            if (edge == null) {
                // The edge was so far a null pointer. Create a new one.
                edge = new PatriciaTreeEdge(this, new PatriciaTreeNode(arity, true, childNodeKey), edgeValue);
            } else {
                // The edge already existed. Update edge.
                edge.updatePointer(edgeValue);
            }

            // Put the updated edge in the children list.
            edgesToChildren.set(edgeIndex, edge);
        } catch (IndexOutOfBoundsException e) {
            // TODO: Handle this
        }

    }
}
