package fr.upmc.algav.patriciatries;

/**
 * Created by amadeus on 27.11.16.
 */
public class PatriciaTrieNodeManager {
    private int nodeCount;

    public PatriciaTrieNodeManager() {
        this.nodeCount = 0;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int generateNewNodeId() {
        final int newNodeId = nodeCount;
        nodeCount++;

        return newNodeId;
    }
}
