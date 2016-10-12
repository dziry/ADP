package fr.umpc.algav.tests;

import fr.umpc.algav.hybridtries.HybridTrie;
import fr.umpc.algav.interfaces.ITrie;

public class Main {

	public static void main(String[] args) {
		
		ITrie hybridTrie = new HybridTrie();
		
		hybridTrie.insert("lou");
		hybridTrie.insert("leve");
		hybridTrie.insert("les");
		hybridTrie.insert("loups");
		hybridTrie.insert("dans");
		hybridTrie.insert("le");
		hybridTrie.insert("lourds");
		hybridTrie.insert("tapis");
		hybridTrie.insert("de");
		hybridTrie.insert("luxe");
		hybridTrie.insert("vert");
		hybridTrie.insert("olive");
		
		hybridTrie.print("hybridTrie");
	}
}
