package fr.upmc.algav.hybridtries;

import java.util.ArrayList;
import fr.upmc.algav.errors.HybridTrieError;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.IPatriciaTrie;
import fr.upmc.algav.tools.Color;
import fr.upmc.algav.tools.Printer;
import fr.upmc.algav.tools.Style;

public class HybridTrie implements IHybridTrie {

	private HybridTrieNode root;
	
	public HybridTrie() {
		this.root = null;
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public void insert(String word) {
		if (word == "") throw new HybridTrieError("word should not be empty!");
		else if (search(word) == true) { /* do nothing..*/ }
		root = insertRecursively(root, word.toCharArray(), 0);
	}
	
	private HybridTrieNode insertRecursively(HybridTrieNode node, char[] word, int position) {
		if (node == null) {
			node = new HybridTrieNode(word[position]);
		}
		if (word[position] < node.getCharacter()) {
			node.setLeftChild(insertRecursively(node.getLeftChild(), word, position));
		} else if (word[position] > node.getCharacter()) {
			node.setRightChild(insertRecursively(node.getRightChild(), word, position));			
		} else {
			if (position < word.length-1) {
				node.setMiddleChild(insertRecursively(node.getMiddleChild(), word, position+1));
			} else if (!node.isFinalNode()) {
				node.setIsFinalNode(true);
			}
		}
		return node;
	}

	@Override
	public void insert(ArrayList<String> words) {
		for (String word : words) {
			insert(word);
		}
	}
	
	@Override
	public boolean search(String word) {
		return searchRecursively(root, word.toCharArray(), 0);
	}
	
	private boolean searchRecursively(HybridTrieNode node, char[] word, int position) {
		if (node == null) {
			return false;
		}
		if (word[position] < node.getCharacter()) {
			return searchRecursively(node.getLeftChild(), word, position);
		} else if (word[position] > node.getCharacter()) {
			return searchRecursively(node.getRightChild(), word, position);
		} else {
			if (position < word.length-1) {
				return searchRecursively(node.getMiddleChild(), word, position+1);
			} else {
				return node.isFinalNode()? true: false;
			}
		}
	}

	@Override
	public int countWords() {
		if (isEmpty()) return 0;
		return countWordsRecursively(root, 0);
	}
	
	private int countWordsRecursively(HybridTrieNode node, int wordsCounter) {		       
		if (node != null) {
			if (node.isFinalNode()) {
				wordsCounter++;				
			}				
			wordsCounter = countWordsRecursively(node.getLeftChild(), wordsCounter);
			wordsCounter = countWordsRecursively(node.getRightChild(), wordsCounter);
			wordsCounter = countWordsRecursively(node.getMiddleChild(), wordsCounter);
		}
		return wordsCounter;
	}

	@Override
	public ArrayList<String> listWords() {
		if (isEmpty()) return null;
		return listWordsRecursively(root, "", new ArrayList<String>());
	}

	private ArrayList<String> listWordsRecursively(HybridTrieNode node, String word, ArrayList<String> listWords) {
		if (node != null) {
			listWordsRecursively(node.getLeftChild(), word, listWords);
			word += node.getCharacter();
			if (node.isFinalNode()) {
				listWords.add(word);
			}
			listWordsRecursively(node.getMiddleChild(), word, listWords);
			word = removeLastCaracter(word);
			listWordsRecursively(node.getRightChild(), word, listWords);
		}		
		return listWords;
	}
	
	private String removeLastCaracter(String word) {
		return word.substring(0, word.length() - 1);
	}
		
	@Override
	public int countNull() {
		if (isEmpty()) return 0;
		return countNullRecursively(root, 0);
	}

	private int countNullRecursively(HybridTrieNode node, int nullCounter) {
		if (node != null) {
			nullCounter = countNullRecursively(node.getLeftChild(), nullCounter);
			nullCounter = countNullRecursively(node.getRightChild(), nullCounter);
			nullCounter = countNullRecursively(node.getMiddleChild(), nullCounter);
		} else {
			nullCounter++;
		}
		return nullCounter;		
	}

	@Override
	public int height() {
		if (isEmpty()) return 0;
		return heightRecursively(root, -1, 0);
	}

	private int heightRecursively(HybridTrieNode node, int heightCounter, int heightResult) {
		if (node != null) {			
            if (heightCounter++ > heightResult && node.isFinalNode()) {
            	heightResult = heightCounter;            	
            }
            heightResult = heightRecursively(node.getLeftChild(), heightCounter, heightResult);                       
            heightResult = heightRecursively(node.getRightChild(), heightCounter, heightResult);                           
            heightResult = heightRecursively(node.getMiddleChild(), heightCounter, heightResult);						
		}
		return heightResult;
	}
	
	@Override
	public double averageDepth() {
		if (isEmpty()) return 0.0;
		return (double)countTotalDepthRecursively(root, -1, 0) / (double)countnodesRecursively(root, 0);
	}

	public int countnodesRecursively(HybridTrieNode node, int nodesCounter) {
		if (node != null) {
			nodesCounter++;
			nodesCounter = countnodesRecursively(node.getLeftChild(), nodesCounter);
			nodesCounter = countnodesRecursively(node.getRightChild(), nodesCounter);
			nodesCounter = countnodesRecursively(node.getMiddleChild(), nodesCounter);
		}
		return nodesCounter;
	}

	private int countTotalDepthRecursively(HybridTrieNode node, int depthCounter, int totalDepth) {
		if (node != null) {			
			depthCounter++;
            if (node.isFinalNode()) {
            	totalDepth += depthCounter;            	
            }
            totalDepth = countTotalDepthRecursively(node.getLeftChild(), depthCounter, totalDepth);                       
            totalDepth = countTotalDepthRecursively(node.getRightChild(), depthCounter, totalDepth);                           
            totalDepth = countTotalDepthRecursively(node.getMiddleChild(), depthCounter, totalDepth);						
		}
		return totalDepth;
	}

	@Override
	public int prefix(String word) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ITrie remove(String word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void print(String fileName) {
		Printer printer = new Printer(fileName);
		printer.begin();
		if (!root.isFinalNode()) {			
			printer.printNode(root);
		} else {			
			printer.printNode(root, Color.BLACK, Color.BLACK, Style.DASHED);
    	}
		printer.printNodeLabel(root);
		printRecursively(null, root, Color.BLUE, printer);
		printer.end();
	}
	
	private void printRecursively(HybridTrieNode previousNode, HybridTrieNode nextNode, Color color, Printer printer) {    	
		if (nextNode != null) {
        	if (previousNode != null) { 		        		
        		if (color.equals(Color.BLUE)) {        			
        			printer.printEdge(previousNode, nextNode, Color.BLUE);
        		} else if (color.equals(Color.RED)) {
        			printer.printEdge(previousNode, nextNode, Color.RED);
        		} else if(color.equals(Color.GREEN)) {
        			printer.printEdge(previousNode, nextNode, Color.GREEN);
        		} 
        		if (nextNode.isFinalNode()) {        			
        			if (color.equals(Color.BLUE)) {        				        				
        				printer.printNode(nextNode, Color.BLUE, Color.BLUE, Style.DASHED);
            		} else if (color.equals(Color.RED)) {
            			printer.printNode(nextNode, Color.RED, Color.RED, Style.DASHED);
            		} else if(color.equals(Color.GREEN)) {
            			printer.printNode(nextNode, Color.GREEN, Color.GREEN, Style.DASHED);
            		}
        		}        		
        		printer.printNodeLabel(nextNode);
        	}        	
        	printRecursively(nextNode, nextNode.getLeftChild(), Color.BLUE, printer);        
        	printRecursively(nextNode, nextNode.getMiddleChild(), Color.RED, printer);        	
        	printRecursively(nextNode, nextNode.getRightChild(), Color.GREEN, printer);           
        }
    }

	@Override
	public boolean isBalanced() {
		// TODO Auto-generated method stub
		return false;
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
