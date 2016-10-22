package fr.upmc.algav.patriciatries;

/**
 * Created by amadeus on 19.10.16.
 */
public class PatriciaTreeEdge {
    private PatriciaTreeNode parentNode;
    private PatriciaTreeNode childNode;
    private String edgeValue;

    public PatriciaTreeEdge(PatriciaTreeNode parentNode, PatriciaTreeNode childNode, String edgeValue) {
        this.parentNode = parentNode;
        this.childNode = childNode;
        this.edgeValue = edgeValue;
    }

}
