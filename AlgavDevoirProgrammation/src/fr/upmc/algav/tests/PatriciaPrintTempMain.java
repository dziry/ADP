package fr.upmc.algav.tests;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.Reader;

import java.io.File;

/**
 * Created by amadeus on 15.11.16.
 */
public class PatriciaPrintTempMain {

    /*public static void main(String[] args) {
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
        patriciaTrie.insert("RUB");

        patriciaTrie.print("patricia_trie_test_graph_1.dot");
    }*/

    public static void main(String[] args) {
        PatriciaTrie patriciaTrie = new PatriciaTrie(new Alphabet());

        System.out.println(new File(".").getAbsolutePath());
        Reader reader = new Reader("AlgavDevoirProgrammation/files/basicExample.txt");
        patriciaTrie.insert(reader.read());
        patriciaTrie.print("patricia_trie_basic_example.dot");
    }


}
