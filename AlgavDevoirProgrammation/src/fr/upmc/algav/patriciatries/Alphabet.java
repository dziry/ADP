package fr.upmc.algav.patriciatries;

import fr.upmc.algav.patriciatries.helper.AlphabetHelper;

public class Alphabet {
    /**
       Use the "DEL" ASCII character with decimal value 127 for signaling the end of a word in the trie.
     */
    private static final int END_OF_WORD_CHAR_ID = 127;
    private static final int ALPHABET_MIN_CHAR_ID = 0;
    private static final int ALPHABET_MAX_CHAR_ID = 126;

    public Alphabet() {
    }

    public boolean isInAlphabet(char c) {
        return isInAlphabet(AlphabetHelper.getCharIdForChar(c));
    }

    public static boolean isInAlphabet(int chardId) {
        return chardId >= ALPHABET_MIN_CHAR_ID && chardId <= ALPHABET_MAX_CHAR_ID;
    }

    public int getNodeArity() {
        // We have 128 edges for each node
        return ALPHABET_MAX_CHAR_ID + 2;
    }

    public static int getEdgeIndexForEndOfWordCharacter() {
        return END_OF_WORD_CHAR_ID;
    }

    public static String getEndOfWordCharacterAsString() {
        return Character.toString(AlphabetHelper.getCharForId(END_OF_WORD_CHAR_ID));
    }

    public static char getEndOfWordCharacterAsChar() {
        return AlphabetHelper.getCharForId(END_OF_WORD_CHAR_ID);
    }
}
