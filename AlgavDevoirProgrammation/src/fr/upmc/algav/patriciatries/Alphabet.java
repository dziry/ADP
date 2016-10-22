package fr.upmc.algav.patriciatries;

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

    public int getEndOfWordCharId() {
        return END_OF_WORD_CHAR_ID;
    }

    public boolean isInAlphabet(char c) {
        return isInAlphabet(getcharIdForChar(c));
    }

    public boolean isInAlphabet(int chardId) {
        return chardId >= ALPHABET_MIN_CHAR_ID && chardId <= ALPHABET_MAX_CHAR_ID;
    }

    public char getCharForId(int chardId) {
        return (char) chardId;
    }

    public int getcharIdForChar(char c) {
        return Character.getNumericValue(c);
    }

    public int getNodeArity() {
        return ALPHABET_MAX_CHAR_ID + 1;
    }
}
