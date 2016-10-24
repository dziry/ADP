package fr.upmc.algav.hybridtries;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

import fr.upmc.algav.errors.HybridTrieError;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.IPatriciaTrie;
import fr.upmc.algav.tools.Color;
import fr.upmc.algav.tools.Printer;
import fr.upmc.algav.tools.Style;

public class HybridTrie implements IHybridTrie {

	private HybridTrieNode parent;
	
	public HybridTrie() {
		this.parent = null;
	}

	@Override
	public boolean isEmpty() {
		return parent == null;
	}

	@Override
	public void insert(@NonNull String word) {
		if (word == "") throw new HybridTrieError("word should not be empty!");
		else if (search(word) == true) { /* do nothing..*/ }
		parent = insertRecursively(parent, word.toCharArray(), 0);
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

	@SuppressWarnings("null")
	@Override
	public void insert(@NonNull ArrayList<String> words) {
		for (String word : words) {
			insert(word);
		}
	}
	
	@Override
	public boolean search(@NonNull String word) {
		return searchRecursively(parent, word.toCharArray(), 0);
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
		return countWordsRecursively(parent, 0);
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
		return listWordsRecursively(parent, "", new ArrayList<String>());
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
		return countNullRecursively(parent, 0);
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float averageDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int prefix(@NonNull String word) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ITrie remove(@NonNull String word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void print(@NonNull String fileName) {
		Printer printer = new Printer(fileName);
		printer.begin();
		if (!parent.isFinalNode()) {			
			printer.printNode(parent);
		} else {			
			printer.printNode(parent, Color.BLACK, Color.BLACK, Style.DASHED);
    	}
		printRecursively(null, parent, Color.BLUE, printer);
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
        		printer.printNodeLabel(previousNode);        		
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
