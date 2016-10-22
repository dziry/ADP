package fr.upmc.algav.hybridtries;

import java.util.ArrayList;

import fr.upmc.algav.errors.HybridTrieError;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.IPatriciaTrie;
import fr.upmc.algav.tools.Writer;

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
	public void insert(String word) {
		if (word == "" || word == null) throw new HybridTrieError("word should not be empty!");
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

	@Override
	public void insert(ArrayList<String> words) {
		for (String word : words) {
			insert(word);
		}
	}
	
	@Override
	public boolean search(String word) {
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
			countWordsRecursively(node.getLeftChild(), wordsCounter);
			countWordsRecursively(node.getRightChild(), wordsCounter);
			countWordsRecursively(node.getMiddleChild(), wordsCounter);
		}
		return wordsCounter;
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
	public ITrie remove(String word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void print(String fileName) {
		Writer fileObject = new Writer(fileName);
		fileObject.write("digraph G {\n");
		if (!parent.isFinalNode()) {
			fileObject.write("	\"" + parent.getId() + "\";\n");
		} else {
			fileObject.write("	\"" + parent.getId() + "\" [color=red, " + "fontcolor=red];\n");
    	}
		printRecursively(null, parent, Color.BLUE, fileObject);
		fileObject.write("}");
		fileObject.close();
	}
	
	private enum Color {
	    BLUE, RED, GREEN
	}
	
	private void printRecursively(HybridTrieNode previousNode, HybridTrieNode nextNode, Color color, Writer fileObject) {    	
        if (nextNode != null) {
        	if (previousNode != null) {
        		fileObject.write("	\"" + previousNode.getId() + "\" -> \"" + nextNode.getId() + "\"");
        		if (color.equals(Color.BLUE)) {
        			fileObject.write(" [color=blue];\n");
        		} else if (color.equals(Color.RED)) {
        			fileObject.write(" [color=red];\n");
        		} else if(color.equals(Color.GREEN)) {
        			fileObject.write(" [color=green];\n");
        		} 
        		if (nextNode.isFinalNode()) {        			
        			if (color.equals(Color.BLUE)) {
        				fileObject.write("	\"" + nextNode.getId() + "\" [color=blue," + " fontcolor=blue];\n");
            		} else if (color.equals(Color.RED)) {
            			fileObject.write("	\"" + nextNode.getId() + "\" [color=red," + " fontcolor=red];\n");
            		} else if(color.equals(Color.GREEN)) {
            			fileObject.write("	\"" + nextNode.getId() + "\" [color=green," + " fontcolor=green];\n");
            		}
        		}
        		fileObject.write("	\"" + previousNode.getId() + "\" [label=\""  + previousNode.getId().charAt(0) + "\"];\n");
        		fileObject.write("	\"" + nextNode.getId() + "\" [label=\"" + nextNode.getId().charAt(0) + "\"];\n");
        	}        	
        	printRecursively(nextNode, nextNode.getLeftChild(), Color.BLUE, fileObject);        
        	printRecursively(nextNode, nextNode.getMiddleChild(), Color.RED, fileObject);        	
        	printRecursively(nextNode, nextNode.getRightChild(), Color.GREEN, fileObject);           
        }
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
