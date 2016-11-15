package fr.upmc.algav.patriciatries.helper;

import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrieNode;

/**
 * Created by amadeus on 11.11.16.
 */
public class PatriciaTrieHelper {

    private PatriciaTrieHelper() {
    }

    public static boolean nodeContainsResultOnlyEdge(PatriciaTrieNode node) {
        return node.hasEdgeValue(Alphabet.getEndOfWordCharacter());
    }

    public static String getCommonPrefix(PatriciaTrieNode node, String word) {
        return getCommonPrefixForNodeAndValue(node, word);
    }

    private static String getCommonPrefixForNodeAndValue(PatriciaTrieNode node, String word) {
        if (word.isEmpty()) {
            // We haven't found a shared prefix for the node and the word
            return null;
        } else if (node.hasEdgeValue(word)) {
            // The node contains the whole word as edge value
            return word;
        } else {
            return getCommonPrefixForNodeAndValue(node, word.substring(0, word.length() - 1));
        }
    }
}
