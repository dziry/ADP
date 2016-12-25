package fr.upmc.algav.patriciatries.helper;

import fr.upmc.algav.patriciatries.Alphabet;

public class AlphabetHelper {

    private AlphabetHelper() {
    }

    public static int getIndexForFirstCharOfWord(String word) {
        int res = -1;

        if (word != null && !word.isEmpty()) {
            res = getCharIdForChar(word.charAt(0));
        }

        return res;
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
