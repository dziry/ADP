package fr.upmc.algav.hybridtries;

import java.util.ArrayList;
import java.util.Collection;

import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.IPatriciaTrie;

public interface IHybridTrie extends ITrie {

	// Complex functions	
	void insertBalanced(String word);
	void insertBalanced(Collection<String> words);
	IPatriciaTrie toPatriciaTrie();
}
