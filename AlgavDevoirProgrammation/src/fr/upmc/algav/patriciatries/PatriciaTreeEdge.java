package fr.upmc.algav.patriciatries;

/**
 * Created by amadeus on 19.10.16.
 */
public class PatriciaTreeEdge {
    private PatriciaTreeNode parentNode;
    private PatriciaTreeNode childNode;
    private String edgeValue;
    private boolean isKeyEdge;
    private int edgeToParentIndex;

    public PatriciaTreeEdge(PatriciaTreeNode parentNode, PatriciaTreeNode childNode, String edgeValue,
                            boolean isKeyEdge, int edgeToParentIndex) {

        this.parentNode = parentNode;
        this.childNode = childNode;
        this.edgeValue = edgeValue;
        this.isKeyEdge = isKeyEdge;
        this.edgeToParentIndex = edgeToParentIndex;
    }

    public String getEdgeValue() {
        return edgeValue;
    }

    public PatriciaTreeNode getChildNode() {
        return childNode;
    }

    public PatriciaTreeNode getParentNode() {
        return parentNode;
    }

    public void updateChildNode(PatriciaTreeNode node) {
        this.childNode = node;
    }

    public void updateEdgeValue(String value) {
        this.edgeValue = value;
    }
}
