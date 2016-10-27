package fr.upmc.algav.patriciatries;

import java.util.ArrayList;

/**
 * Created by amadeus on 19.10.16.
 */
public class PatriciaTreeNode {
    private int arity;
    private boolean isLeaf;
    private ArrayList<PatriciaTreeEdge> edgesToChildren;

    public PatriciaTreeNode(int arity, boolean isLeaf) {
        this.arity = arity;
        this.isLeaf = isLeaf;

        if (isLeaf) {
            initNullEdges();
        }
    }

    public PatriciaTreeNode(int arity, ArrayList<PatriciaTreeEdge> edges) {
        this(arity, false);

        this.edgesToChildren = edges;
    }

    private void initNullEdges() {
        this.edgesToChildren = new ArrayList<>();

        for (int i = 0; i < arity; i++) {
            this.edgesToChildren.add(null);
        }
    }

    public ArrayList<PatriciaTreeEdge> getEdges() {
        return edgesToChildren;
    }

    public PatriciaTreeEdge getEdgeByIndex(int edgeIndex) {
        return isLeaf ? null : edgesToChildren.get(edgeIndex);
    }

    public void addNewEdgeWithWordAndResult(String edgeValue, int edgeIndex) {
        checkIfTransformToInnerNode();

        PatriciaTreeEdge edge = new PatriciaTreeEdge(this, new PatriciaTreeNode(arity, true),
                                                     edgeValue, true, edgeIndex);
        edgesToChildren.set(edgeIndex, edge);
    }

    public void addNewResultEdge() {
        checkIfTransformToInnerNode();

        PatriciaTreeEdge edge = new PatriciaTreeEdge(this, new PatriciaTreeNode(arity, true),
                Alphabet.getEndOfWordCharacter(), true, Alphabet.getEdgeIndexForEndOfWordCharacter());

        edgesToChildren.set(Alphabet.getEdgeIndexForEndOfWordCharacter(), edge);
    }

    public void addNewNonKeyEdge(PatriciaTreeEdge edge, int edgeIndex) {
        checkIfTransformToInnerNode();

        edgesToChildren.set(edgeIndex, edge);
    }

    private void checkIfTransformToInnerNode() {
        // If node was a leaf so far, make it an inner node and initialize all edges
        if (isLeaf) {
            isLeaf = false;
            initNullEdges();
        }
    }

    public boolean hasOnlyNullEdges() {
        boolean res = true;

        if (!isLeaf) {
            for (PatriciaTreeEdge childEdge : edgesToChildren) {
                if (childEdge != null) {
                    res = false;
                    break;
                }
            }
        }

        return res;
    }
}
