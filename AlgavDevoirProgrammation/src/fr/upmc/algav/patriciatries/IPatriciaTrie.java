package fr.upmc.algav.patriciatries;

import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.interfaces.ITrie;

public interface IPatriciaTrie extends ITrie {

	// complex functions
	IPatriciaTrie merge(IPatriciaTrie otherTrie);
	IHybridTrie toHybridTrie();
	void clearTrie();
	PatriciaTrieNode getRootNode();
    int getNodeCount();
    void setNodeCount(int nodeCount);
}
