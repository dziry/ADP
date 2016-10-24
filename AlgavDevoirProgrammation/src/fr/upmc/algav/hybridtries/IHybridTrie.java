package fr.upmc.algav.hybridtries;

import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.IPatriciaTrie;

public interface IHybridTrie extends ITrie {

	// Complex functions
	IHybridTrie balance(IHybridTrie trie);
	IPatriciaTrie toPatriciaTrie();
}