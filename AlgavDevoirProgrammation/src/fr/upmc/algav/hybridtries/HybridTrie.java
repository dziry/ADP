package fr.upmc.algav.hybridtries;

import java.util.ArrayList;

import fr.upmc.algav.errors.HybridTrieError;
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
		if (word == "" || word == null) {
			throw new HybridTrieError("insert(word): word should not be empty or null!");
		} else if (search(word) == true) {
			/* word exist? -> do nothing.. */ 
		}
		root = insertRecursively(root, word.toCharArray(), 0);
	}

	private HybridTrieNode insertRecursively(HybridTrieNode node, char[] key, int position) {
		if (node == null) {
			node = new HybridTrieNode(key[position]);
		}
		if (key[position] < node.getCharacter()) {
			node.setLeftChild(insertRecursively(node.getLeftChild(), key, position));
		} else if (key[position] > node.getCharacter()) {
			node.setRightChild(insertRecursively(node.getRightChild(), key, position));
		} else {
			if (position < key.length - 1) {
				node.setMiddleChild(insertRecursively(node.getMiddleChild(), key, position + 1));
			} else if (!node.isFinalNode()) {
				node.setIsFinalNode(true);
			}
		}
		return node;
	}

	@Override
	public void insert(ArrayList<String> words) {
		if (words == null || words.size() < 1) {
			throw new HybridTrieError("insert(words): words should not be empty or null!");
		}
		for (String word : words) {
			insert(word);
		}
	}

	@Override
	public boolean search(String word) {
		if (word == null || word == "") {
			throw new HybridTrieError("search(word): word should not be empty or null!");
		}
		return searchRecursively(root, word.toCharArray(), 0);
	}

	private boolean searchRecursively(HybridTrieNode node, char[] key, int position) {
		if (node == null) {
			return false;
		}
		if (key[position] < node.getCharacter()) {
			return searchRecursively(node.getLeftChild(), key, position);
		} else if (key[position] > node.getCharacter()) {
			return searchRecursively(node.getRightChild(), key, position);
		} else {
			if (position < key.length - 1) {
				return searchRecursively(node.getMiddleChild(), key, position + 1);
			} else {
				return node.isFinalNode() ? true : false;
			}
		}
	}
	
	@Override
	public int countWords() {
		if (isEmpty()) {
			return 0;
		}
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
		if (isEmpty()) {
			return null;
		}
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

	// used for listWords
	private String removeLastCaracter(String word) {
		return word.substring(0, word.length() - 1);
	}

	@Override
	public int countNull() {
		if (isEmpty()) {
			return 0;
		}
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
		if (isEmpty()) {
			return 0;
		}
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
		if (isEmpty()) {
			return 0.0;
		}
		return (double) countTotalDepthRecursively(root, -1, 0) / (double) countnodesRecursively(root, 0);
	}

	// used for averageDepth
	private int countnodesRecursively(HybridTrieNode node, int nodesCounter) {
		if (node != null) {
			nodesCounter++;
			nodesCounter = countnodesRecursively(node.getLeftChild(), nodesCounter);
			nodesCounter = countnodesRecursively(node.getRightChild(), nodesCounter);
			nodesCounter = countnodesRecursively(node.getMiddleChild(), nodesCounter);
		}
		return nodesCounter;
	}

	// used for averageDepth
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
		if (word == "" || word == null) {
			throw new HybridTrieError("search(word): word should not be empty or null!");
		}
		return prefixRecursively(root, word.toCharArray(), 0);
	}

	private int prefixRecursively(HybridTrieNode node, char[] key, int position) {
		if (node == null) {
			return 0;
		}
		if (key[position] < node.getCharacter()) {
			return prefixRecursively(node.getLeftChild(), key, position);
		} else if (key[position] > node.getCharacter()) {
			return prefixRecursively(node.getRightChild(), key, position);
		} else {
			if (position < key.length - 1) {
				return prefixRecursively(node.getMiddleChild(), key, position + 1);
			} else {
				if (node.getMiddleChild() == null) {
					return 1;
				} else {
					return node.isFinalNode() ? 1 + countWords(node.getMiddleChild()) : countWords(node.getMiddleChild());
				}
			}
		}
	}

	// used for prefix
	private int countWords(HybridTrieNode node) {
		if (isEmpty()) {
			return 0;
		}
		return countWordsRecursively(node, 0);
	}

	@Override
	public boolean remove(String word) {		
	    if (word.isEmpty()) {
	        return false;
	    }
	    return removeRecursively(null, root, word.toCharArray(), 0);
	}
	
	private boolean removeRecursively(HybridTrieNode parent, HybridTrieNode node, char[] key, int position) {
	    if (node == null) {
	        return false;
	    }
    	boolean log = true;
    	if (key[position] < node.getCharacter()) {
    		log = removeRecursively(node, node.getLeftChild(), key, position);
    	} else if (key[position] > node.getCharacter()) {
    		log = removeRecursively(node, node.getRightChild(), key, position);
    	} else {
    		if (position == key.length - 1) {
    			if (node.isFinalNode()) {
    				node.setIsFinalNode(false);
    			} else {
    				return false;
    			}
    		}
    		else if (position < key.length + 1) {
    			log = removeRecursively(node, node.getMiddleChild(), key, position + 1);
    		}
    	}
    	if (log && node.getMiddleChild() == null && !node.isFinalNode()) {
    		if (!node.hasChildren()) {
    			transplant(parent, node, null);
    		} else if (node.getRightChild() == null) {
    			transplant(parent, node, node.getLeftChild());
    		} else if (node.getLeftChild() == null) {
    			transplant(parent, node, node.getRightChild());
    		} else {
	            HybridTrieNode successor = findSuccessor(node.getRightChild());
	            // TODO
    		}
    		node = null;
    	}
    	return log;
	}
	
	// used for deletion
	private void transplant(HybridTrieNode parent, HybridTrieNode node, HybridTrieNode successor) {		
	    if (parent == null) {
	        root = successor;
	    } else if (node == parent.getLeftChild()) {
	        parent.setLeftChild(successor);
	    } else if (node == parent.getRightChild()) {
	        parent.setRightChild(successor);
	    } else {
	        parent.setMiddleChild(successor);
	    }
	}

	// find the successor (in inorder traversal) in right child, used for deletion
	private static HybridTrieNode findSuccessor(HybridTrieNode node) {
		if (node.getLeftChild() == null) {
			return node;
		} else {
			return findSuccessor(node.getLeftChild());
		}
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
				} else if (color.equals(Color.GREEN)) {
					printer.printEdge(previousNode, nextNode, Color.GREEN);
				}
				if (nextNode.isFinalNode()) {
					if (color.equals(Color.BLUE)) {
						printer.printNode(nextNode, Color.BLUE, Color.BLUE, Style.DASHED);
					} else if (color.equals(Color.RED)) {
						printer.printNode(nextNode, Color.RED, Color.RED, Style.DASHED);
					} else if (color.equals(Color.GREEN)) {
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

//private HybridTrieNode find(char[] word) {
//if (word == null) {
//	throw new HybridTrieError("word == null");
//}
//if (word.length == 0) {
//	return null;
//}
//HybridTrieNode rootNode = root;	
//int index = 0;
//char character;
//while (index < word.length && rootNode != null) {
//	character = word[index];
//	if (character < rootNode.getCharacter()) {
//		rootNode = rootNode.getLeftChild();
//	} else if (character > rootNode.getCharacter()) {
//		rootNode = rootNode.getRightChild();
//	} else {
//		if (index == word.length - 1) {
//			return rootNode;
//		} else {
//			++index;
//			rootNode = rootNode.getMiddleChild();
//		}
//	}		
//}
//return rootNode;
//}