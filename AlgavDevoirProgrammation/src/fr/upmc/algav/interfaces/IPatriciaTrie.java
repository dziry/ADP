package fr.upmc.algav.interfaces;

import fr.upmc.algav.patriciatries.PatriciaTrieNode;

public interface IPatriciaTrie extends ITrie {

	// Complex functions
	IPatriciaTrie merge(IPatriciaTrie otherTrie);
	IHybridTrie toHybridTrie();
	PatriciaTrieNode getRootNode();
}
