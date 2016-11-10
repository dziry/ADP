package fr.upmc.algav.patriciatries;

/**
 * Created by amadeus on 19.10.16.
 */
public class PatriciaTrieEdge {
    private PatriciaTrieNode parentNode;
    private PatriciaTrieNode childNode;
    private String edgeValue;
    private boolean isKeyEdge;
    private int edgeToParentIndex;

    public PatriciaTrieEdge(PatriciaTrieNode parentNode, PatriciaTrieNode childNode, String edgeValue,
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

    public PatriciaTrieNode getChildNode() {
        return childNode;
    }

    public PatriciaTrieNode getParentNode() {
        return parentNode;
    }

    public void updateChildNode(PatriciaTrieNode node) {
        this.childNode = node;
    }

    public void updateEdgeValue(String value) {
        this.edgeValue = value;
    }
}
