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
}
