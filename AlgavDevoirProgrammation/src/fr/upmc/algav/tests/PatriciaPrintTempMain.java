package fr.upmc.algav.tests;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.IPatriciaTrie;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.Reader;

import java.io.File;


/**
 * Created by amadeus on 15.11.16.
 */
public class PatriciaPrintTempMain {

    /*public static void main(String[] args) {
        /*PatriciaTrie trie1 = new PatriciaTrie(new Alphabet());
        trie1.insert("ROMAN");
        trie1.insert("ROMUS");
        trie1.print("test_trie_1.dot");

        PatriciaTrie trie2 = new PatriciaTrie(new Alphabet());
        trie2.insert("ROMA");
        trie2.insert("ROMER");
        trie2.print("test_trie_2.dot");

        IPatriciaTrie trie12 = trie1.merge(trie2);
        trie12.print("test_trie_12.dot");

        IPatriciaTrie trie21 = trie2.merge(trie1);
        trie21.print("test_trie_21.dot");

        IPatriciaTrie trie11 = trie1.merge(trie1);
        trie11.print("test_trie_11.dot");

        IPatriciaTrie trie22 = trie2.merge(trie2);
        trie22.print("test_trie_22.dot");

        trie1.print("test_trie_1copy.dot");
        trie2.print("test_trie_2copy.dot");*/
/*
        HybridTrie hybridTrie = new HybridTrie();
        hybridTrie.insert("AMS");
        hybridTrie.insert("AMT");
        hybridTrie.insert("AMXY");
        hybridTrie.insert("BALL");
        hybridTrie.insert("BELL");
        hybridTrie.insert("TAS");
        hybridTrie.insert("TC");
        hybridTrie.insert("TF");
        hybridTrie.insert("TG");
        hybridTrie.insert("ZASS");
        hybridTrie.insert("ZEUS");

        PatriciaTrie patriciaTrie = new PatriciaTrie(new Alphabet());
        patriciaTrie.insert("AMS");
        patriciaTrie.insert("AMT");
        patriciaTrie.insert("AMXY");
        patriciaTrie.insert("BALL");
        patriciaTrie.insert("BELL");
        patriciaTrie.insert("TAS");
        patriciaTrie.insert("TC");
        patriciaTrie.insert("TF");
        patriciaTrie.insert("TG");
        patriciaTrie.insert("ZASS");
        patriciaTrie.insert("ZEUS");

        IHybridTrie convert = patriciaTrie.toHybridTrie();

        patriciaTrie.print("patricia_convert.dot");
        hybridTrie.print("hybrid_convert.dot");
        convert.print("convert.dot");

    }*/

    public static void main(String[] args) {
        PatriciaTrie patriciaTrie = new PatriciaTrie(new Alphabet());

        System.out.println(new File(".").getAbsolutePath());
        Reader reader = new Reader("AlgavDevoirProgrammation/files/basicExample.txt");
        patriciaTrie.insert(reader.read());
        patriciaTrie.print("patricia_trie_basic_example.dot");
    }


}
