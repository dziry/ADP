package fr.umpc.algav.hybridtries;

public class HybridTrieNode {

	private char character;
	private boolean isFinalNode;
	
	private HybridTrieNode leftChild;
	private HybridTrieNode middleChild;
	private HybridTrieNode rightChild;
	
	public HybridTrieNode(char character) {
		this.character = character;
		this.isFinalNode = false;
		this.leftChild = null;
		this.middleChild = null;
		this.rightChild = null;
	}
	
	public char getCharacter() {
		return this.character;
	}
	
	public boolean isFinalNode() {
		return isFinalNode;
	}
	
	public void setIsFinalNode(boolean isFinalNode) {
		this.isFinalNode = isFinalNode;
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
	
	public void setLeftChild(HybridTrieNode leftChild) {
		this.leftChild = leftChild;
	}
	
	public void setMiddleChild(HybridTrieNode middleChild) {
		this.middleChild = middleChild;
	}

	public void setRightChild(HybridTrieNode rightChild) {
		this.rightChild = rightChild;
	}
}
