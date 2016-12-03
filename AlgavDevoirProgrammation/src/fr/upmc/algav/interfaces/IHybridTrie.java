package fr.upmc.algav.interfaces;

import java.util.Collection;

public interface IHybridTrie extends ITrie {

	// Complex functions	
	void insertBalanced(String word);
	void insertBalanced(Collection<String> words);
	IPatriciaTrie toPatriciaTrie();
}
