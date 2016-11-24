package fr.upmc.algav.patriciatries.helper;

import fr.upmc.algav.patriciatries.Alphabet;

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

    public static int getIndexForFirstCharOfWord(String word) {
        return getCharIdForChar(word.charAt(0));
    }

    public static char getCharForId(int chardId) {
        return (char) chardId;
    }

    public static int getCharIdForChar(char c) {
        return (int) c;
    }

    public static String makeResultWord(String word) {
        return word + Alphabet.getEndOfWordCharacterAsString();
    }

    public static String removeResultCharacterFromWord(String word) {
        return word.replace(Alphabet.getEndOfWordCharacterAsString(), "");
    }

    public static boolean containsResultCharacter(String word) {
        return word.contains(Alphabet.getEndOfWordCharacterAsString());
    }

    public static boolean isResultCharacter(char c) {
        return c == Alphabet.getEndOfWordCharacterAsChar();
    }
}
