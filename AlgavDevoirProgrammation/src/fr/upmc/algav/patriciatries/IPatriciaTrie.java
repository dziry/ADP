package fr.upmc.algav.patriciatries;

import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.interfaces.ITrie;

public interface IPatriciaTrie extends ITrie {

	// complex functions
	IPatriciaTrie merge(IPatriciaTrie trie1, IPatriciaTrie trie2);
	IHybridTrie toHybridTrie();
	void clearTrie();
}
