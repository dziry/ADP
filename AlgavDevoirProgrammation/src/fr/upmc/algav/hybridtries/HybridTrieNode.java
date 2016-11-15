package fr.upmc.algav.hybridtries;

import java.util.UUID;

public class HybridTrieNode {

	private char character;	
	
	private int priority; // used when balancing the tree
	private int stringPriority; // used when balancing the tree
	
	private String uniqueId; // used to identify the node when printing the tree
	
	private HybridTrieNode leftChild;
	private HybridTrieNode middleChild;
	private HybridTrieNode rightChild;
	
	public HybridTrieNode(char character) {
		this.character = character;
		this.priority = 0;
		this.stringPriority = 0; // nonterminal node, stringPriority value is 0
		this.uniqueId = character + UUID.randomUUID().toString();
		this.leftChild = null;
		this.middleChild = null;
		this.rightChild = null;
	}
	
	public char getCharacter() {
		return character;
	}
	
	public boolean isFinalNode() {
		return stringPriority != 0;
	}

	public int getPriority() {
		return priority;
	}
	
	public int getStringPriority() {
		return stringPriority;
	}
	
	public String getId() {
		return uniqueId;
	}
	
	public HybridTrieNode getLeftChild() {
		return leftChild;
	}
	
	public HybridTrieNode getMiddleChild() {
		return middleChild;
	}
	
	public HybridTrieNode getRightChild() {
		return rightChild;
	}
	
	public void setCharacter(char character) {
		this.character = character;
	}
	
	public void setIsFinalNode(int stringPriority) {
		this.stringPriority = stringPriority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void setLeftChild(HybridTrieNode leftChild) {
		this.leftChild = leftChild;
	}
	
	public void setMiddleChild(HybridTrieNode middleChild) {
		this.middleChild = middleChild;
	}

	public void setRightChild(HybridTrieNode rightChild) {
		this.rightChild = rightChild;
	}
	
	public boolean hasChildren () {
		return leftChild != null || middleChild != null || rightChild != null;		
	}
}
