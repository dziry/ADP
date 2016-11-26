package fr.upmc.algav.hybridtries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.ThreadLocalRandom;

import fr.upmc.algav.errors.HybridTrieError;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.IPatriciaTrie;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.Color;
import fr.upmc.algav.tools.GraphPrinter;
import fr.upmc.algav.tools.Style;

public class HybridTrie implements IHybridTrie {

	private HybridTrieNode root;
	private final static int MAX_WORDS = Integer.MAX_VALUE;

	public HybridTrie() {
		this.root = null;
	}

	public HybridTrie(HybridTrieNode root) {
		this.root = root;
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public void insert(String word) {
		if (word == null || word.isEmpty()) {
			throw new HybridTrieError("Inserting word: Word should not be null or empty!");
		} else if (search(word)) {
			// Word exists, do nothing
		}

		root = insertRecursively(root, word.toCharArray(), 0);
	}

	private static HybridTrieNode insertRecursively(HybridTrieNode node, char[] key, int position) {
		if (node == null) {
			node = new HybridTrieNode(key[position]);
		}

		if (key[position] < node.getCharacter()) {
			// Go left, if current character smaller than character of current node
			node.setLeftChild(insertRecursively(node.getLeftChild(), key, position));
		} else if (key[position] > node.getCharacter()) {
            // Go right, if current character greater than character of current node
			node.setRightChild(insertRecursively(node.getRightChild(), key, position));
		} else {
			if (position < key.length - 1) {
				// Go middle, if current character equal to node character (If we don't exceed the key)
				node.setMiddleChild(insertRecursively(node.getMiddleChild(), key, position + 1));
			} else if (!node.isFinalNode()) {
				node.setIsFinalNode(1);
			}
		}

		return node;
	}

	@Override
	public void insert(Collection<String> words) {
		if (words == null || words.size() < 1) {
			throw new HybridTrieError("Inserting words: Words should not be null or empty!");
		}

		for (String word : words) {
			insert(word);
		}	
	}
	
	@Override
	public void removeAll() {
		this.root = null;
	}

	@Override
	public boolean search(String word) {
		if (word == null || word.isEmpty()) {
			throw new HybridTrieError("Searching word: word should not be null or empty!");
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
				return node.isFinalNode();
			}
		}
	}
	
	@Override
	public int getWordCount() {
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
	public ArrayList<String> getStoredWords() {
		if (isEmpty()) {
			return null;
		}

		return listWordsRecursively(root, "", new ArrayList<>());
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

    /**
     * Helper method for {@link #getStoredWords()}.
     * Returning the word without its last character.
     *
     * @param word The manipulated word
     * @return The word reduced by its last character
     */
	private static String removeLastCaracter(String word) {
		return word.substring(0, word.length() - 1);
	}

	@Override
	public int getNullPointerCount() {
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
	public int getHeight() {
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
	public double getAverageDepthOfLeaves() {
		if (isEmpty()) {
			return 0.0;
		}

		// TODO: This should be totalLeafDepth / totalLeafCount
		return (double) countTotalDepthRecursively(root, -1, 0) / (double) countNodesRecursively(root, 0);
	}

    /**
     * Helper method for {@link #getAverageDepthOfLeaves()}.
     * Returns the node count for a given node by regarding the node itself and all of
     * its successors.
     *
     * @param node The current node
     * @param nodesCounter The current node count
     * @return The count of all nodes for this node including itself
     */
	private static int countNodesRecursively(HybridTrieNode node, int nodesCounter) {
		if (node != null) {
			nodesCounter++;
			nodesCounter = countNodesRecursively(node.getLeftChild(), nodesCounter);
			nodesCounter = countNodesRecursively(node.getRightChild(), nodesCounter);
			nodesCounter = countNodesRecursively(node.getMiddleChild(), nodesCounter);
		}

		return nodesCounter;
	}

    /**
     * Helper method for {@link #getAverageDepthOfLeaves()}.
     * TODO: Are we're really calculating the total depth of all leaves here? Don't we rather need to to add
     * TODO: the depth of each found leaf to a total value?
     *
     * @param node
     * @param depthCounter
     * @param totalDepth
     * @return
     */
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
	public int getPrefixCount(String word) {
		if (word == null || word.isEmpty()) {
			throw new HybridTrieError("Getting prefix count for word: Word should not be null or empty!");
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

    /**
     * Helper method for {@link #getPrefixCount(String)}.
     * Counting all the stored words for a given node.
     *
     * @param node The given node
     * @return The word count
     */
	private int countWords(HybridTrieNode node) {
		if (isEmpty()) {
			return 0;
		}

		return countWordsRecursively(node, 0);
	}

	@Override
	public boolean remove(String word) {		
		if (word == null || word.isEmpty()) {
			throw new HybridTrieError("Removing word: Word should not be null or empty!");
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
    		// If we are on our last character
    		if (position == key.length - 1) {
    			if (node.isFinalNode()) {
    				// The node is an end node, remove the end value
    				node.setIsFinalNode(0);
    			} else {
    				// If there is no end key as it does not exist within the tree
    				return false;
    			}
    		} else if (position < key.length + 1) {
    			log = removeRecursively(node, node.getMiddleChild(), key, position + 1);
    		}
    	}

    	// Only remove if the node's middle child is null and if the node is not an end key!
    	// If log is false, the value has never been found. Thus the key does not exist within this tree.
    	if (log && node.getMiddleChild() == null && !node.isFinalNode()) {
    		// Case 1: No children, safe to delete
    		if (!node.hasChildren()) {
    			transplant(parent, node, null);
    		}
    		// Case 2: Right is null, transplant it to left
    		else if (node.getRightChild() == null) {
    			transplant(parent, node, node.getLeftChild());
    		}
    		// Case 3: Left is null, transplant it to right
    		else if (node.getLeftChild() == null) {
    			transplant(parent, node, node.getRightChild());
    		}
    		// Case 4: Both left and right children exists, find successor and transplant
    		else {    			
	            HybridTrieNode successor = findSuccessor(node.getRightChild());	            	        	            
	            HybridTrieNode successorParent = findSuccessorParent(successor);
            	// If successor node has left child(ren)
	            if (successorParent != node) {
	                transplant(parent, node, node.getRightChild());
	            } else {
	            	transplant(parent, node, successor);
	            }
	            // Make the node's left child as left child for successor node
            	successor.setLeftChild(node.getLeftChild());
    		}

    		node = null;
    	}

    	return log;
	}

    /**
     * Helper method for {@link #removeRecursively(HybridTrieNode, HybridTrieNode, char[], int)}.
     * Make a link between a parent node and its child's successor node.
     *
     * @param parent The parent node of the current node
     * @param node The current node
     * @param successor The successor node of the current node
     */
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

    /**
     * Helper method for {@link #removeRecursively(HybridTrieNode, HybridTrieNode, char[], int)}.
     * Find the successor (in in-order traversal) in the right child.
     *
     * @param node The current node
     * @return The successor node of the current node
     */
	private static HybridTrieNode findSuccessor(HybridTrieNode node) {
		if (node.getLeftChild() == null) {
			return node;
		} else {
			return findSuccessor(node.getLeftChild());
		}
	}

    /**
     * Helper method for {@link #removeRecursively(HybridTrieNode, HybridTrieNode, char[], int)}.
     * Find the successor's parent
     *
     * @param successor The current node
     * @return The parent node of the current node
     */
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
				if (node.getId().equals(successor.getId())) {
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
		GraphPrinter graphPrinter = new GraphPrinter(fileName);
		graphPrinter.beginDirected();

		if (!root.isFinalNode()) {
			graphPrinter.printNode(root);
		} else {
			graphPrinter.printNode(root, Color.BLACK, Color.BLACK, Style.DASHED);
		}

		graphPrinter.printNodeLabel(root);
		printRecursively(null, root, Color.BLUE, graphPrinter);

		graphPrinter.end();
	}

	private static void printRecursively(HybridTrieNode previousNode, HybridTrieNode nextNode, Color color, GraphPrinter graphPrinter) {
		if (nextNode != null) {
			if (previousNode != null) {
				if (color.equals(Color.BLUE)) {
					graphPrinter.printEdge(previousNode, nextNode, Color.BLUE);
				} else if (color.equals(Color.RED)) {
					graphPrinter.printEdge(previousNode, nextNode, Color.RED);
				} else if (color.equals(Color.GREEN)) {
					graphPrinter.printEdge(previousNode, nextNode, Color.GREEN);
				}

				if (nextNode.isFinalNode()) {
					if (color.equals(Color.BLUE)) {
						graphPrinter.printNode(nextNode, Color.BLUE, Color.BLUE, Style.DASHED);
					} else if (color.equals(Color.RED)) {
						graphPrinter.printNode(nextNode, Color.RED, Color.RED, Style.DASHED);
					} else if (color.equals(Color.GREEN)) {
						graphPrinter.printNode(nextNode, Color.GREEN, Color.GREEN, Style.DASHED);
					}
				}

				graphPrinter.printNodeLabel(nextNode);
			}

			printRecursively(nextNode, nextNode.getLeftChild(), Color.BLUE, graphPrinter);
			printRecursively(nextNode, nextNode.getMiddleChild(), Color.RED, graphPrinter);
			printRecursively(nextNode, nextNode.getRightChild(), Color.GREEN, graphPrinter);
		}
	}

	@Override
	public void insertBalanced(String word) {
		if (word == null || word.isEmpty()) {
			throw new HybridTrieError("Inserting word balanced: Word should not be null or empty!");
		} else if (search(word)) {
			// Word does already exist, do nothing!
		}

		root = insertBalancedRecursively(root, word.toCharArray(), 0);
	}

	private static HybridTrieNode insertBalancedRecursively(HybridTrieNode node, char[] key, int position) {
		if (node == null) {
			node = new HybridTrieNode(key[position]);
		}

		if (key[position] < node.getCharacter()) {
			node.setLeftChild(insertBalancedRecursively(node.getLeftChild(), key, position));
			if (node.getLeftChild().getPriority() > node.getPriority()) {
				node = rotateWithLeft(node);
			}
		} else if (key[position] > node.getCharacter()) {
			node.setRightChild(insertBalancedRecursively(node.getRightChild(), key, position));
			if (node.getRightChild().getPriority() > node.getPriority()) {
				node = rotateWithRight(node);
			}
		} else {
			if (position < key.length - 1) {
				node.setMiddleChild(insertBalancedRecursively(node.getMiddleChild(), key, position + 1));
			} else if (!node.isFinalNode()) {
				node.setIsFinalNode(generateRandomInteger(1, MAX_WORDS));
			}
			if (node.getMiddleChild() == null) {
				node.setPriority(node.getStringPriority());
			} else {
				node.setPriority(max(node.getStringPriority(), node.getMiddleChild().getPriority()));
			}
		}

		return node;
	}

	private static HybridTrieNode rotateWithLeft(HybridTrieNode nodeX) {
		HybridTrieNode nodeY = nodeX.getLeftChild();
		nodeX.setLeftChild(nodeY.getRightChild());
		nodeY.setRightChild(nodeX);

		return nodeY;
	}
	
	private static HybridTrieNode rotateWithRight(HybridTrieNode nodeX) {
		HybridTrieNode nodeY = nodeX.getRightChild();
		nodeX.setRightChild(nodeY.getLeftChild());
		nodeY.setLeftChild(nodeX);

		return nodeY;
	}

	private static int generateRandomInteger(int min, int max) {
		OfInt iterator = ThreadLocalRandom.current().ints(min, max).distinct().iterator();
		return iterator.next();
	}
	
	private static int max(int n1, int n2) {
		return (n1 > n2) ? n1 : n2;
	}
 	
	@Override
	public void insertBalanced(ArrayList<String> words) {
		if (words == null || words.size() < 1) {
			throw new HybridTrieError("Inserting words balanced: Words should not be null or empty!");
		}
		for (String word : words) {
			insertBalanced(word);
		}
	}
	
	@Override
	public IPatriciaTrie toPatriciaTrie() {
		if (isEmpty()) {
			return null;
		}

		return toPatriciaTrieRecursively(root, "", new PatriciaTrie(new Alphabet()));
	}

	private static IPatriciaTrie toPatriciaTrieRecursively(HybridTrieNode node, String word, PatriciaTrie patriciaTrie) {
		if (node != null) {
			toPatriciaTrieRecursively(node.getLeftChild(), word, patriciaTrie);
			word += node.getCharacter();
			if (node.isFinalNode()) {
				patriciaTrie.insert(word);
			}
			toPatriciaTrieRecursively(node.getMiddleChild(), word, patriciaTrie);
			word = removeLastCaracter(word);
			toPatriciaTrieRecursively(node.getRightChild(), word, patriciaTrie);
		}
		return patriciaTrie;
	}
}