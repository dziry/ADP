package fr.upmc.algav.tests;

import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;

/**
 * Created by amadeus on 15.11.16.
 */
public class PatriciaPrintTempMain {

    public static void main(String[] args) {
        PatriciaTrie patriciaTrie = new PatriciaTrie(new Alphabet());
        patriciaTrie.insert("ROMANE");
        patriciaTrie.insert("ROMANUS");
        patriciaTrie.insert("ROMULUS");
        patriciaTrie.insert("RUBENS");
        patriciaTrie.insert("RUBER");
        patriciaTrie.insert("RUBICON");
        patriciaTrie.insert("RUBICUNDUS");
        patriciaTrie.insert("RUB");
        patriciaTrie.insert("hello");
        patriciaTrie.insert("ROMULUSBBB");
        patriciaTrie.insert("ROMANE");
        patriciaTrie.insert("ROMANE");
        patriciaTrie.insert("RUBER");
        patriciaTrie.insert("ROMANE");

        patriciaTrie.print("patricia_trie_test_graph_1.dot");
    }
}
