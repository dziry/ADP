package fr.umpc.algav.patriciatries;

/**
 * Created by amadeus on 20.10.16.
 */
public class AlphabetHelper {

    private AlphabetHelper() {
    }

    public static Character getFirstCharOfWord(String word) {
        return getIthCharOfWord(0, word);
    }

    public static Character getIthCharOfWord(int index, String word) {
        Character res = null;

        // Make sure that we're in the bounds of the word.
        if (index >= 0 && index < word.length()) {
            res = word.charAt(index);
        }

        return res;
    }
}
