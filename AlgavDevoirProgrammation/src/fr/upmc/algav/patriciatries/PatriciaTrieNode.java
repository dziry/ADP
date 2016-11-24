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

    public PatriciaTrieNode(int nodeId, int arity, ArrayList<String> edgeValues,
                            ArrayList<PatriciaTrieNode> childNodes, boolean isLeaf) {
        this.nodeId = nodeId;
        this.arity = arity;
        this.edgeValues = edgeValues;
        this.childNodes = childNodes;
        this.isLeaf = isLeaf;
    }

    private void initNullEdges() {
        for (int i = 0; i < arity; i++) {
            edgeValues.add(null);
            childNodes.add(null);
        }
    }

    public String getEdgeValueForIndex(int index) {
        String res = null;

        if (index >= 0 && index < arity) {
            res = edgeValues.get(index);
        }

        return res;
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
        testForStateChangeToInnerNode();

        if (Alphabet.getEndOfWordCharacterAsString().equals(edgeValueToInsert) || edgeValueToInsert.isEmpty()) {
            addNewResultOnlyEdge(nodeId);
        } else {
            addEdge(nodeId, AlphabetHelper.getIndexForFirstCharOfWord(edgeValueToInsert),
                    AlphabetHelper.makeResultWord(edgeValueToInsert));
        }
    }

    public void addNewResultOnlyEdge(int nodeId) {
        testForStateChangeToInnerNode();
        addEdge(nodeId, Alphabet.getEdgeIndexForEndOfWordCharacter(), Alphabet.getEndOfWordCharacterAsString());
    }

    private void addEdge(int nodeId, int edgeIndex, String edgeValue) {
        edgeValues.set(edgeIndex, edgeValue);
        childNodes.set(edgeIndex, new PatriciaTrieNode(nodeId, arity, true));
    }

    private void testForStateChangeToInnerNode() {
        if (isLeaf) {
            isLeaf = false;
            initNullEdges();
        }
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setAsLeaf() {
        this.isLeaf = true;
        initNullEdges();
    }

    public boolean hasEdgeValue(String edgeValue) {
        return edgeValues.contains(edgeValue);
    }

    public PatriciaTrieNode getChildNodeForEdgeValue(String edgeValue) {
        return isLeaf ? null : childNodes.get(edgeValues.indexOf(edgeValue));
    }

    public void setEdge(String edgeValue, PatriciaTrieNode childNode) {
        testForStateChangeToInnerNode();

        int edgeIndex = AlphabetHelper.getIndexForFirstCharOfWord(edgeValue);

        if (edgeIndex >= 0 && edgeIndex < arity) {
            edgeValues.set(edgeIndex, edgeValue);
            childNodes.set(edgeIndex, childNode);
        }
    }

    public void updateEdgeValue(String oldEdgeValue, String newEdgeValue) {
        testForStateChangeToInnerNode();

        int edgeIndex = AlphabetHelper.getIndexForFirstCharOfWord(oldEdgeValue);

        if (edgeIndex >= 0 && edgeIndex < arity) {
            edgeValues.set(edgeIndex, newEdgeValue);
        }
    }

    public void removeEdge(String edgeValue) {
        int edgeIndex = AlphabetHelper.getIndexForFirstCharOfWord(edgeValue);
        edgeValues.set(edgeIndex, null);
        childNodes.set(edgeIndex, null);
    }

    private void testForStateChangeToLeaf() {
        boolean isChangeToLeaf = true;

        for (int i = 0; i < arity; i++) {
            isChangeToLeaf = edgeValues.get(i) == null && childNodes.get(i) == null;

            if (!isChangeToLeaf) {
               break;
            }
        }

        this.isLeaf = isChangeToLeaf;
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

    public void updateNodeId(int newNodeId) {
        this.nodeId = newNodeId;
    }

    public int getNodeArity() {
        return arity;
    }

    public boolean isNullEdge(int index) {
        boolean isNullEdge;

        try {
            isNullEdge = edgeValues.get(index) == null && childNodes.get(index) == null;
        } catch (ArrayIndexOutOfBoundsException e) {
            isNullEdge = true;
        }

        return isNullEdge;
    }

}
