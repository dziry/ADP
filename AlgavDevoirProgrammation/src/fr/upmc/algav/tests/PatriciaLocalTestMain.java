package fr.upmc.algav.tests;

import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;

/**
 * Created by amadeus on 15.11.16.
 */
public class PatriciaLocalTestMain {

    public static void main(String[] args) {
        PatriciaTrie trie = new PatriciaTrie(new Alphabet());
        trie.insert("ROMANE");
        trie.insert("ROMANUS");
        trie.insert("ROMULUS");
        trie.insert("RUBENS");
        trie.insert("RUBER");
        trie.insert("RUBICON");
        trie.insert("RUBICUNDUS");
        trie.insert("RUB");
        trie.insert("ROMULUSBBB");
        trie.insert("ROMANE");
        trie.insert("ROMANE");
        trie.insert("RUBER");
        trie.insert("ROMANE");

        trie.print("test1.dot");
    }
}
