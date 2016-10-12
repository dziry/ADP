package fr.umpc.algav.hybridtries;

import java.util.ArrayList;

import fr.umpc.algav.interfaces.ITrie;
import fr.umpc.algav.patriciatries.IPatriciaTrie;
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
			if (position+1 < word.length) {
				node.setMiddleChild(insertRecursively(node.getMiddleChild(), word, position+1));
			} else if (!node.isFinalNode()) {
				node.setIsFinalNode(true);
			}
		}
		return node;
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
