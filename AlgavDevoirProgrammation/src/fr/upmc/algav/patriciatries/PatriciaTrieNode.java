package fr.upmc.algav.patriciatries;

import fr.upmc.algav.patriciatries.helper.AlphabetHelper;

import java.util.ArrayList;

/**
 * Created by amadeus on 19.10.16.
 */
public class PatriciaTrieNode {
    private int arity;
    private boolean isLeaf;
    private ArrayList<String> edgeValues;
    private ArrayList<PatriciaTrieNode> childNodes;

    public PatriciaTrieNode(int arity, boolean isLeaf) {
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
        return edgeValues.get(AlphabetHelper.getIndexForFirstCharOfWord(value));
    }

    public void addNewValuedResultEdge(String edgeValueToInsert) {
        testForNodeStateChange();
        addEdge(AlphabetHelper.getIndexForFirstCharOfWord(edgeValueToInsert),
                AlphabetHelper.makeResultWord(edgeValueToInsert));
    }

    public void addNewResultOnlyEdge() {
        testForNodeStateChange();
        addEdge(Alphabet.getEdgeIndexForEndOfWordCharacter(), Alphabet.getEndOfWordCharacter());
    }

    private void addEdge(int edgeIndex, String edgeValue) {
        edgeValues.set(edgeIndex, edgeValue);
        childNodes.set(edgeIndex, new PatriciaTrieNode(arity, true));
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

    }
}
