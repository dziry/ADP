package fr.umpc.algav.hybridtries;

import java.util.ArrayList;

import fr.umpc.algav.interfaces.ITree;
import fr.umpc.algav.patriciatries.IPatriciaTrie;

public class HybridTrie implements IHybridTrie {

	private HybridTrieNode parent;
	
	public HybridTrie() {
		this.parent = null;
	}
	
	public HybridTrieNode getParent() {
		return parent;
	}
	
	public void setParent(HybridTrieNode parent) {
		this.parent = parent;
	}

	@Override
	public boolean isEmpty() {
		return parent == null;
	}

	@Override
	public ITree insert(String word) {
		return insertRecursively(parent, word.toCharArray(), 0);
	}
	
	public ITree insertRecursively(HybridTrieNode node, char[] word, int position) {
		if (node == null) {
			node = new HybridTrieNode(word[position]);
		}
		if (word[position] < node.getCharacter()) {
			node.setLeftChild((HybridTrieNode)insertRecursively(node.getLeftChild(), word, position));
		} else if (word[position] > node.getCharacter()) {
			node.setRightChild((HybridTrieNode)insertRecursively(node.getRightChild(), word, position));			
		} else {
			if (position+1 < word.length) {
				node.setMiddleChild((HybridTrieNode)insertRecursively(node.getMiddleChild(), word, position+1));
			} else if (!node.isFinalNode()) {
				node.setIsFinalNode(true);
			}
		}
		return (ITree)node;
	}

	@Override
	public boolean search(String word) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int countWords() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<String> listWords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countNull() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int height() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float averageDepth() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private int countNods() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private int sumOfDepths(int depth) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int prefix(String word) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ITree remove(String word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void display(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPatriciaTrie toPatriciaTrie() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHybridTrie balance(IHybridTrie trie) {
		// TODO Auto-generated method stub
		return null;
	}

}
