package fr.upmc.algav.patriciatries;

import java.util.ArrayList;

/**
 * Created by amadeus on 19.10.16.
 */
public class PatriciaTreeNode {
    private String key;
    private int arity;
    private boolean isLeaf;
    private ArrayList<PatriciaTreeEdge> edgesToChildren;

    public PatriciaTreeNode(int arity, boolean isLeaf) {
        this(arity, isLeaf, null);
    }

    public PatriciaTreeNode(int arity, boolean isLeaf, String key) {
        this.arity = arity;
        this.isLeaf = isLeaf;
        this.key = key;

        if (!isLeaf) {
            this.edgesToChildren = new ArrayList<>();
            initNullEdges();
        }
    }

    private void initNullEdges() {
        for (int i = 0; i < arity; i++) {
            //this.edgesToChildren.add(new PatriciaTreeEdge(this, childNode, edgeValue));
        }
    }

    public boolean representsKeyForWord() {
        return key != null;
    }
}
