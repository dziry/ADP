package fr.upmc.algav.hybridtries;

import java.util.ArrayList;

import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.IPatriciaTrie;

public interface IHybridTrie extends ITrie {

	// Complex functions	
	void insertBalanced(String word);
	void insertBalanced(ArrayList<String> words);
	IPatriciaTrie toPatriciaTrie();
}
