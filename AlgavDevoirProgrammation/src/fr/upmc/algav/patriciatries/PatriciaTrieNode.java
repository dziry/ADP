package fr.upmc.algav.patriciatries;

import java.util.ArrayList;

/**
 * Created by amadeus on 19.10.16.
 */
public class PatriciaTrieNode {
    private int arity;
    private boolean isLeaf;
    private ArrayList<PatriciaTrieEdge> edgesToChildren;

    public PatriciaTrieNode(int arity, boolean isLeaf) {
        this.arity = arity;
        this.isLeaf = isLeaf;

        if (isLeaf) {
            initNullEdges();
        }
    }

    public PatriciaTrieNode(int arity, ArrayList<PatriciaTrieEdge> edges) {
        this(arity, false);

        this.edgesToChildren = edges;
    }

    private void initNullEdges() {
        this.edgesToChildren = new ArrayList<>();

        for (int i = 0; i < arity; i++) {
            this.edgesToChildren.add(null);
        }
    }

    public ArrayList<PatriciaTrieEdge> getEdges() {
        return edgesToChildren;
    }

    public PatriciaTrieEdge getEdgeByIndex(int edgeIndex) {
        return isLeaf ? null : edgesToChildren.get(edgeIndex);
    }

    public void addNewEdgeWithWordAndResult(String edgeValue, int edgeIndex) {
        checkIfTransformToInnerNode();

        PatriciaTrieEdge edge = new PatriciaTrieEdge(this, new PatriciaTrieNode(arity, true),
                                                     edgeValue, true, edgeIndex);
        edgesToChildren.set(edgeIndex, edge);
    }

    public void addNewResultEdge() {
        checkIfTransformToInnerNode();

        PatriciaTrieEdge edge = new PatriciaTrieEdge(this, new PatriciaTrieNode(arity, true),
                Alphabet.getEndOfWordCharacter(), true, Alphabet.getEdgeIndexForEndOfWordCharacter());

        edgesToChildren.set(Alphabet.getEdgeIndexForEndOfWordCharacter(), edge);
    }

    public void addNewNonKeyEdge(PatriciaTrieEdge edge, int edgeIndex) {
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
            for (PatriciaTrieEdge childEdge : edgesToChildren) {
                if (childEdge != null) {
                    res = false;
                    break;
                }
            }
        }

        return res;
    }
}
