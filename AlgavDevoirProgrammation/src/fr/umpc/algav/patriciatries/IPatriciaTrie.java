package fr.umpc.algav.patriciatries;

import fr.umpc.algav.hybridtries.IHybridTrie;
import fr.umpc.algav.interfaces.ITree;

public interface IPatriciaTrie extends ITree {

	// complex functions
	IPatriciaTrie merge(IPatriciaTrie trie1, IPatriciaTrie trie2);
	IHybridTrie toHybridTrie();
}
