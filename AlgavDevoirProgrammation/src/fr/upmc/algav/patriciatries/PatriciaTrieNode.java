package fr.upmc.algav.patriciatries;

import fr.upmc.algav.patriciatries.helper.AlphabetHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by amadeus on 19.10.16.
 */
public class PatriciaTrieNode {
    private int arity;
    private boolean isLeaf;
    private ArrayList<String> edgeValues;
    private ArrayList<PatriciaTrieNode> childNodes;
    private int nodeId;

    public PatriciaTrieNode(int nodeId, int arity, boolean isLeaf) {
        this.nodeId = nodeId;
        this.arity = arity;
        this.isLeaf = isLeaf;
        this.edgeValues = new ArrayList<>();
        this.childNodes = new ArrayList<>();

        if (!isLeaf) {
            initNullEdges();
        }
    }

    private void initNullEdges() {
        for (int i = 0; i < arity; i++) {
            edgeValues.add(null);
            childNodes.add(null);
        }
    }

    public String getConcernedEdgeForValue(String value) {
        String edgeValue;

        try {
            int a = AlphabetHelper.getIndexForFirstCharOfWord(value);
            edgeValue = edgeValues.get(a);
        } catch (ArrayIndexOutOfBoundsException e) {
            edgeValue = null;
        }

        return edgeValue;
    }

    public void addNewValuedResultEdge(int nodeId, String edgeValueToInsert) {
        testForNodeStateChange();

        if (Alphabet.getEndOfWordCharacter().equals(edgeValueToInsert) || edgeValueToInsert.isEmpty()) {
            addNewResultOnlyEdge(nodeId);
        } else {
            addEdge(nodeId, AlphabetHelper.getIndexForFirstCharOfWord(edgeValueToInsert),
                    AlphabetHelper.makeResultWord(edgeValueToInsert));
        }
    }

    public void addNewResultOnlyEdge(int nodeId) {
        testForNodeStateChange();
        addEdge(nodeId, Alphabet.getEdgeIndexForEndOfWordCharacter(), Alphabet.getEndOfWordCharacter());
    }

    private void addEdge(int nodeId, int edgeIndex, String edgeValue) {
        edgeValues.set(edgeIndex, edgeValue);
        childNodes.set(edgeIndex, new PatriciaTrieNode(nodeId, arity, true));
    }

    private void testForNodeStateChange() {
        if (isLeaf) {
            isLeaf = false;
            initNullEdges();
        }
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public boolean hasEdgeValue(String edgeValue) {
        return edgeValues.contains(edgeValue);
    }

    public PatriciaTrieNode getChildNodeForEdgeValue(String edgeValue) {
        return isLeaf ? null : childNodes.get(edgeValues.indexOf(edgeValue));
    }

    public void setEdge(String edgeValue, PatriciaTrieNode childNode) {
        testForNodeStateChange();

        int edgeIndex = AlphabetHelper.getIndexForFirstCharOfWord(edgeValue);
        edgeValues.set(edgeIndex, edgeValue);
        childNodes.set(edgeIndex, childNode);
    }

    public LinkedHashMap<String, PatriciaTrieNode> getAllNonNullEdgesToChildNodes() {
        LinkedHashMap<String, PatriciaTrieNode> res = new LinkedHashMap<>();

        if (!edgeValues.isEmpty() && !childNodes.isEmpty()) {
            for (int i = 0; i < arity; i++) {
                if (edgeValues.get(i) != null && childNodes.get(i) != null) {
                    res.put(edgeValues.get(i), childNodes.get(i));
                }
            }
        }

        return res;
    }

    public ArrayList<String> getAllEdgeValues() {
        return edgeValues;
    }

    public ArrayList<PatriciaTrieNode> getAllChildNodes() {
        return childNodes;
    }

    public int getNodeId() {
        return nodeId;
    }

    public int getNodeArity() {
        return arity;
    }
}
