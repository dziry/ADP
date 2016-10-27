package fr.upmc.algav.patriciatries;

import fr.upmc.algav.patriciatries.helper.AlphabetHelper;

/**
 * Created by amadeus on 19.10.16.
 */
public class Alphabet {
    // Use the "DEL" character to signal the end of a word in the tree
    private static final int END_OF_WORD_CHAR_ID = 127;
    private static final int ALPHABET_MIN_CHAR_ID = 0;
    private static final int ALPHABET_MAX_CHAR_ID = 126;

    public Alphabet() {

    }

    public boolean isInAlphabet(char c) {
        return isInAlphabet(AlphabetHelper.getcharIdForChar(c));
    }

    public static boolean isInAlphabet(int chardId) {
        return chardId >= ALPHABET_MIN_CHAR_ID && chardId <= ALPHABET_MAX_CHAR_ID;
    }

    public int getNodeArity() {
        return ALPHABET_MAX_CHAR_ID + 2;
    }

    public static int getEdgeIndexForEndOfWordCharacter() {
        return END_OF_WORD_CHAR_ID;
    }

    public static String getEndOfWordCharacter() {
        return Character.toString(AlphabetHelper.getCharForId(END_OF_WORD_CHAR_ID));
    }
}
