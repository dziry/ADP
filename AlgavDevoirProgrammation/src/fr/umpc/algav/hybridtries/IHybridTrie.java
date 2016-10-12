package fr.umpc.algav.hybridtries;

import fr.umpc.algav.interfaces.ITrie;
import fr.umpc.algav.patriciatries.IPatriciaTrie;

public interface IHybridTrie extends ITrie {

	// Complex functions
	IHybridTrie balance(IHybridTrie trie);
	IPatriciaTrie toPatriciaTrie();
}
