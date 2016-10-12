package fr.umpc.algav.hybridtries;

import fr.umpc.algav.interfaces.ITree;
import fr.umpc.algav.patriciatries.IPatriciaTrie;

public interface IHybridTrie extends ITree {

	// Complex functions
	IHybridTrie balance(IHybridTrie trie);
	IPatriciaTrie toPatriciaTrie();
}
