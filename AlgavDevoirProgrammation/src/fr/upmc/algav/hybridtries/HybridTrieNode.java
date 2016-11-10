package fr.upmc.algav.hybridtries;

import java.util.UUID;

public class HybridTrieNode {

	private char character;
	private boolean isFinalNode;
	private String uniqueId; // to identify it when printing the tree
	
	private HybridTrieNode leftChild;
	private HybridTrieNode middleChild;
	private HybridTrieNode rightChild;
	
	public HybridTrieNode(char character) {
		this.character = character;
		this.isFinalNode = false;
		this.uniqueId = character + UUID.randomUUID().toString();
		this.leftChild = null;
		this.middleChild = null;
		this.rightChild = null;
	}
	
	public char getCharacter() {
		return character;
	}
	
	public boolean isFinalNode() {
		return isFinalNode;
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
	
	public void setIsFinalNode(boolean isFinalNode) {
		this.isFinalNode = isFinalNode;
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
