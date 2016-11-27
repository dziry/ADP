package fr.upmc.algav.patriciatries.helper;

import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.patriciatries.PatriciaTrieNode;

import java.util.ArrayList;
import java.util.Set;

public class PatriciaTrieHelper {

    private PatriciaTrieHelper() {
    }

    public static boolean nodeContainsResultOnlyEdge(PatriciaTrieNode node) {
        return node.hasEdgeValue(Alphabet.getEndOfWordCharacterAsString());
    }

    public static String getCommonPrefix(PatriciaTrieNode node, String word) {
        return word.isEmpty() ? null : getCommonPrefixForNodeAndValue(node, word);
    }

    private static String getCommonPrefixForNodeAndValue(PatriciaTrieNode node, String word) {
        String commonPrefix = "";

        String edgeValue = node.getConcernedEdgeForValue(word);
        int index = 0;

        if (edgeValue != null) {
            try {
                while (edgeValue.charAt(index) == word.charAt(index)) {
                    commonPrefix += edgeValue.charAt(index);
                    index++;
                }
            } catch (IndexOutOfBoundsException e) {
                // No longer the same getPrefixCount
            }
        }

        return commonPrefix.isEmpty() ? null : commonPrefix;
    }

    public static boolean wordIsAlreadyStoredForNode(PatriciaTrieNode node, String wordToInsert) {
        boolean res = false;

        String edgeValue = node.getConcernedEdgeForValue(wordToInsert);

        if (edgeValue != null && wordToInsert != null) {
            res = edgeValue.equals(AlphabetHelper.makeResultWord(wordToInsert));
        }

        return res;
    }

    public static Integer getOnlyChildIndexOfNodeIfPresent(PatriciaTrieNode node) {
        Integer onlyChildIndex = null;

        boolean alreadyFoundChild = false;

        for (int i = 0; i < node.getNodeArity(); i++) {
            if (!node.isNullEdge(i)) {
                if (alreadyFoundChild) {
                    // We have found more than one child, so we have no only-child node!
                    onlyChildIndex = null;
                    break;
                } else {
                    onlyChildIndex = i;
                    alreadyFoundChild = true;
                }
            }
        }

        return onlyChildIndex;
    }

    public static String getCommonPrefixForListOfEdges(String edgeValue, Set<String> edgesList) {
        String res = null;

        // Test for special cases
        if (edgeValue != null && !edgeValue.isEmpty() && edgesList != null && !edgesList.isEmpty()) {
            // Get the other edge corresponding to the edge value
            for (String otherEdge : edgesList) {
                if (otherEdge != null && !otherEdge.isEmpty() &&
                    edgeValue.charAt(0) == otherEdge.charAt(0)) {
                    // We have found an edge that has at least the same char as our edge as first char.
                    // Test now, what the real common prefix is.
                    String commonPrefix = "";
                    int index = 0;

                    try {
                        while (edgeValue.charAt(index) == otherEdge.charAt(index)) {
                            commonPrefix += edgeValue.charAt(index);
                            index++;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        // No longer the same prefix value.
                    }

                    // We have found the common prefix. We don't have to test for the other edges.
                    res = commonPrefix;
                    break;
                }
            }
        }

        return res;
    }

    public static PatriciaTrie makeAllNodeIdsUnique(PatriciaTrie trie) {
        PatriciaTrie res = trie;

        int nodeCount = updateNodeIfOfNode(trie.getRootNode(), 0);
        res.setNodeCount(nodeCount);

        return res;
    }

    private static int updateNodeIfOfNode(PatriciaTrieNode currentNode, int currentNodeCount) {
        // Update the node id of the current node
        currentNode.updateNodeId(currentNodeCount);
        currentNodeCount++;

        for (PatriciaTrieNode childNode : currentNode.getAllChildNodes()) {
            if (childNode != null) {
                currentNodeCount = updateNodeIfOfNode(childNode, currentNodeCount);
            }
        }

        return currentNodeCount;
    }

    public static PatriciaTrieNode copyNode(PatriciaTrieNode currentNode) {
        PatriciaTrieNode res = null;

        if (currentNode != null) {
            ArrayList<PatriciaTrieNode> newChildNodes = new ArrayList<>();
            int nodeIdCounter = 0;

            for (PatriciaTrieNode oldChildNode : currentNode.getAllChildNodes()) {
                PatriciaTrieNode newChildNode = copyNode(oldChildNode);
                newChildNodes.add(newChildNode);
                nodeIdCounter++;
            }

            ArrayList<String> newEdgeValues = new ArrayList<>(currentNode.getAllEdgeValues());

            res = new PatriciaTrieNode(nodeIdCounter, currentNode.getNodeArity(),
                    newEdgeValues, newChildNodes, currentNode.isLeaf());
        }

        return res;
    }

    public static boolean nodeHasOnlyNullEdges(PatriciaTrieNode node) {
        boolean res = true;

        if (node != null) {
            ArrayList<String> nodeEdgeValues = node.getAllEdgeValues();
            ArrayList<PatriciaTrieNode> nodeChildNodes = node.getAllChildNodes();

            if (nodeEdgeValues != null && nodeChildNodes != null) {
                for (int i = 0; i < node.getNodeArity(); i++) {
                    res = nodeEdgeValues.get(i) == null && nodeChildNodes.get(i) == null;

                    if (!res) {
                        break;
                    }
                }
            }
        }

        return res;
    }
}
