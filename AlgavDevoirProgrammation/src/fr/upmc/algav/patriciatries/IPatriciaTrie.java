package fr.upmc.algav.patriciatries;

import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.interfaces.ITrie;

public interface IPatriciaTrie extends ITrie {

	// Complex functions
	IPatriciaTrie merge(IPatriciaTrie otherTrie);
	IHybridTrie toHybridTrie();
	PatriciaTrieNode getRootNode();
}
