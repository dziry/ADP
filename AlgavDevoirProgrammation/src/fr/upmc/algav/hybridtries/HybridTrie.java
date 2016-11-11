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
		if (word == null || word.isEmpty()) {
			throw new HybridTrieError("insert(word): word should not be null or empty!");
		} else if (search(word) == true) {
			/* word exist? -> ignore.. */ 
		}
		root = insertRecursively(root, word.toCharArray(), 0);
	}

	private static HybridTrieNode insertRecursively(HybridTrieNode node, char[] key, int position) {
		if (node == null) {
			node = new HybridTrieNode(key[position]);
		}
		if (key[position] < node.getCharacter()) {
			// go left, if less
			node.setLeftChild(insertRecursively(node.getLeftChild(), key, position));
		} else if (key[position] > node.getCharacter()) {
			// go right, if greater
			node.setRightChild(insertRecursively(node.getRightChild(), key, position));
		} else {
			if (position < key.length - 1) {
				// go middle, if equal (if we don't exceed the key)
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
			throw new HybridTrieError("insert(words): words should not be null or empty!");
		}
		for (String word : words) {
			insert(word);
		}
	}

	@Override
	public boolean search(String word) {
		if (word == null || word.isEmpty()) {
			throw new HybridTrieError("search(word): word should not be null or empty!");
		}
		return searchRecursively(root, word.toCharArray(), 0);
	}

	private static boolean searchRecursively(HybridTrieNode node, char[] key, int position) {
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

	private static int countWordsRecursively(HybridTrieNode node, int wordsCounter) {
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
		return listWordsRecursively(root, new String(), new ArrayList<String>());
	}

	private static ArrayList<String> listWordsRecursively(HybridTrieNode node, String word, ArrayList<String> listWords) {
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
	private static String removeLastCaracter(String word) {
		return word.substring(0, word.length() - 1);
	}

	@Override
	public int countNull() {
		if (isEmpty()) {
			return 0;
		}
		return countNullRecursively(root, 0);
	}

	private static int countNullRecursively(HybridTrieNode node, int nullCounter) {
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

	private static int heightRecursively(HybridTrieNode node, int heightCounter, int heightResult) {
		if (node != null) {
			heightCounter++;
			heightResult = heightRecursively(node.getLeftChild(), heightCounter, heightResult);
			if (heightCounter > heightResult && node.isFinalNode()) {				
				heightResult = heightCounter;
			}
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
	private static int countnodesRecursively(HybridTrieNode node, int nodesCounter) {
		if (node != null) {
			nodesCounter++;
			nodesCounter = countnodesRecursively(node.getLeftChild(), nodesCounter);
			nodesCounter = countnodesRecursively(node.getRightChild(), nodesCounter);
			nodesCounter = countnodesRecursively(node.getMiddleChild(), nodesCounter);
		}
		return nodesCounter;
	}

	// used for averageDepth
	private static int countTotalDepthRecursively(HybridTrieNode node, int depthCounter, int totalDepth) {
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
		if (word == null || word.isEmpty()) {
			throw new HybridTrieError("search(word): word should not be null or empty!");
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
					return countWords(node.getMiddleChild()) + (node.isFinalNode() ? 1 : 0);
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
		if (word == null || word.isEmpty()) {
			throw new HybridTrieError("remove(word): word should not be null or empty!");
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
    		// if we are on our last character and	
    		if (position == key.length - 1) {
    			if (node.isFinalNode()) {
    				// node is an end node, remove the end value
    				node.setIsFinalNode(false);
    			} else {
    				// if there is no end key as it does not exist within the tree
    				return false;
    			}
    		}
    		else if (position < key.length + 1) {
    			log = removeRecursively(node, node.getMiddleChild(), key, position + 1);
    		}
    	}
    	// only remove if node's middle is nil and if node is not an end key
    	// if log is false, means the value was never found. Thus the key does not exist within this tree
    	if (log && node.getMiddleChild() == null && !node.isFinalNode()) {
    		// case 1: no children, safe to delete
    		if (!node.hasChildren()) {
    			transplant(parent, node, null);
    		}
    		// case 2: right is null, transplant it to left
    		else if (node.getRightChild() == null) {
    			transplant(parent, node, node.getLeftChild());
    		}
    		// case 3: left is null, transplant it to right
    		else if (node.getLeftChild() == null) {
    			transplant(parent, node, node.getRightChild());
    		}
    		// case 4: both left and right children exists, find successor and transplant
    		else {    			
	            HybridTrieNode successor = findSuccessor(node.getRightChild());	            	        	            
	            HybridTrieNode successorParent = findSuccessorParent(successor);
            	// if successor node has left child(s)
	            if (successorParent != node) {
	                transplant(parent, node, node.getRightChild());
	            } else {
	            	transplant(parent, node, successor);
	            }
	            // make the node's left child as left child for successor node
            	successor.setLeftChild(node.getLeftChild());
    		}
    		node = null;
    	}
    	return log;
	}
	
	// make link between parent node and its child's successor node, used for deletion
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

	// find the successor (in in-order traversal) in right child, used for deletion
	private static HybridTrieNode findSuccessor(HybridTrieNode node) {
		if (node.getLeftChild() == null) {
			return node;
		} else {
			return findSuccessor(node.getLeftChild());
		}
	}
	
	// find the successor parent, used for deletion
	private HybridTrieNode findSuccessorParent(HybridTrieNode successor) {		
		HybridTrieNode parent = null;
		HybridTrieNode node = root;
		while (node != null) {			
			if (successor.getCharacter() < node.getCharacter()) {
				parent = node;
				node = node.getLeftChild();
			} else if (successor.getCharacter() > node.getCharacter()) {
				parent = node;
				node = node.getRightChild();
			} else {
				if (successor.getId() == node.getId()) {
					break;
				}				
				parent = node;
				node = node.getMiddleChild();
			}
		}		
		return parent;
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

	private static void printRecursively(HybridTrieNode previousNode, HybridTrieNode nextNode, Color color, Printer printer) {
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