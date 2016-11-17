package fr.upmc.algav.patriciatries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.patriciatries.helper.PatriciaTrieHelper;
import fr.upmc.algav.tools.Printer;

public class PatriciaTrie implements IPatriciaTrie {

	private PatriciaTrieNode rootNode;
	private final Alphabet usedAlphabet;
	private int nodeCount;

	public PatriciaTrie(Alphabet usedAlphabet) {
		this.usedAlphabet = usedAlphabet;
		this.nodeCount = 0;
		this.rootNode = new PatriciaTrieNode(getNewNodeId(), usedAlphabet.getNodeArity(), true);
	}

	private int getNewNodeId() {
		final int newNodeId = nodeCount;
		nodeCount++;

		return newNodeId;
	}

	@Override
	public boolean isEmpty() {
		return rootNode.isLeaf();
	}

	@Override
	public void insert(String word) {
		insertCharacterSequenceInTree(rootNode, word);
	}

	private void insertCharacterSequenceInTree(PatriciaTrieNode currentNode, String word) {
		if (currentNode.isLeaf()) {
			// There are no edges yet to test. Just insert the whole word as new edge.
			currentNode.addNewValuedResultEdge(getNewNodeId(), word);
		} else {
			// There are already edge values for this node
			String commonPrefix = PatriciaTrieHelper.getCommonPrefix(currentNode, word);

			if (commonPrefix == null) {
				// We have no shared getPrefixCount. Just insert the whole word as new edge
				currentNode.addNewValuedResultEdge(getNewNodeId(), word);
			} else if (PatriciaTrieHelper.wordIsAlreadyStoredForNode(currentNode, word)) {
                // Word is already stored --> Do nothing!
            } else {
				String edgeValue = currentNode.getConcernedEdgeForValue(word);
				String wordWithoutCommonPrefix = word.substring(commonPrefix.length());
				String edgeValueWithoutCommonPrefix = edgeValue.substring(commonPrefix.length());

				if (edgeValueWithoutCommonPrefix.length() <= 0) {
					// The inserted word contains the whole edge getPrefixCount.

					if (wordWithoutCommonPrefix.length() <= 0) {
						// The word itself is also finished.
						// E.g. Inserted word = "why" and edge getPrefixCount = "why"
						// -> We add a result edge for signaling that a word ends here.
						currentNode.getChildNodeForEdgeValue(edgeValue).addNewResultOnlyEdge(getNewNodeId());
					} else {
						// The word itself is not yet finished.
						// E.g. Inserted word = "however" and edge getPrefixCount = "how"
						// -> Insert the remaining characters of the word for the child node of the current edge.
						insertCharacterSequenceInTree(currentNode.getChildNodeForEdgeValue(edgeValue),
														wordWithoutCommonPrefix);
					}
				} else {
					// The inserted word contains only a part of the edge getPrefixCount.
					// E.g. Inserted word = "talking", Edge Value = "talked"
					// -> We have to split the path!

                    // For example: We want to store "BLA1" and "BLA" ist already stored.
                    boolean isExtensionOfExistingWord =
                            Alphabet.getEndOfWordCharacter().equals(edgeValueWithoutCommonPrefix) &&
                                    !wordWithoutCommonPrefix.isEmpty();

                    if (isExtensionOfExistingWord) {
                        // Store the current's edge current child node because we need it later.
                        final PatriciaTrieNode oldCurrentEdgeChildNode = currentNode.getChildNodeForEdgeValue(edgeValue);

                        // Set the edge value to the common getPrefixCount
                        currentNode.setEdge(commonPrefix, oldCurrentEdgeChildNode);
                        // Add new result edge to child node
                        insertCharacterSequenceInTree(oldCurrentEdgeChildNode, Alphabet.getEndOfWordCharacter());
                        // Add the remaining word for the word to be inserted as new edge
                        insertCharacterSequenceInTree(oldCurrentEdgeChildNode, wordWithoutCommonPrefix);

                    } else {
                        // Store the current's edge current child node because we need it later.
                        final PatriciaTrieNode oldCurrentEdgeChildNode = currentNode.getChildNodeForEdgeValue(edgeValue);

                        // Create the new child node for the current edge.
                        PatriciaTrieNode newCurrentEdgeChildNode = new PatriciaTrieNode(getNewNodeId(),
                                usedAlphabet.getNodeArity(), false);
                        // Add the old child node as child node of the newly created child node
                        newCurrentEdgeChildNode.setEdge(edgeValueWithoutCommonPrefix, oldCurrentEdgeChildNode);
                        // Add the new child node to the current node
                        currentNode.setEdge(commonPrefix, newCurrentEdgeChildNode);
                        // Do the same insertion for the remaining characters of the word
                        insertCharacterSequenceInTree(newCurrentEdgeChildNode, wordWithoutCommonPrefix);
                    }
				}
			}
		}
	}

	@Override
	public void insert(ArrayList<String> words) {
		for (String word : words) {
			insert(word);
		}
	}
	
	@Override
	public boolean search(String word) {
		return word != null && searchWord(word, rootNode);
	}

	private boolean searchWord(String word, PatriciaTrieNode currentNode) {
		boolean res = false;

        if (word.isEmpty()) {
            res = PatriciaTrieHelper.nodeContainsResultOnlyEdge(currentNode);
        } else {
            if (currentNode.isLeaf()) {
                // We're at a leaf. No more edges, a dead end.
                res = false;
            } else if (PatriciaTrieHelper.wordIsAlreadyStoredForNode(currentNode, word)) {
                // Word is already stored!
                res = true;
            } else {
                // There are already edge values for this node
                String commonPrefix = PatriciaTrieHelper.getCommonPrefix(currentNode, word);

                if (commonPrefix == null) {
                    // We have no shared getPrefixCount. The word can't exist in the tree.
                    res = false;
                } else {
                    String edgeValue = currentNode.getConcernedEdgeForValue(word);
                    String wordWithoutCommonPrefix = word.substring(commonPrefix.length());

                    PatriciaTrieNode childNode = currentNode.getChildNodeForEdgeValue(edgeValue);

                    if (wordWithoutCommonPrefix.length() <= 0) {
                        // The searched word itself is also finished.
                        // E.g. Searched word = "why" and edge getPrefixCount = "why"
                        // Look if the concerned child node contains a result only edge
                        res = searchWord("", childNode);
                    } else {
                        // The word itself is not yet finished.
                        // E.g. Searched word = "however" and edge getPrefixCount = "how"
                        // Search for the remaining characters of the word at the child node of the current edge.
                        res = searchWord(wordWithoutCommonPrefix, childNode);
                    }
                }
            }
        }

		return res;
	}

	@Override
	public int getWordCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public ArrayList<String> getStoredWords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNullPointerCount() {
		return countNullPointersInTrie(rootNode);
	}

	private int countNullPointersInTrie(PatriciaTrieNode node) {
		int count = 0;

		if (!node.isLeaf()) {
			// If we're not visiting a leaf, we have to visit also all possible children.
			ArrayList<String> edgeValues = node.getAllEdgeValues();
			ArrayList<PatriciaTrieNode> childNodes = node.getAllChildNodes();

			for (int i = 0; i < node.getNodeArity(); i++) {
				if (edgeValues.get(i) == null && childNodes.get(i) == null) {
					// We have a null pointer
					count++;
				} else {
					// We have not a null pointer. Search for null pointers in child.
					count += countNullPointersInTrie(childNodes.get(i));
				}
			}
		} else {
			// We have a leaf. Therefore add all edges to the result count.
			count += node.getNodeArity();
		}

		return count;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAverageDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPrefixCount(String word) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean remove(String word) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void print(String fileName) {
		Printer printer = new Printer(fileName);
		printer.beginUndirected();

        printPatriciaTrie(rootNode, printer);

        printer.end();
    }

	private static void printPatriciaTrie(PatriciaTrieNode currentNode, Printer printer) {
		if (currentNode.isLeaf()) {
            printer.printNode(currentNode);
        } else {
            HashMap<String, PatriciaTrieNode> existantEdgesToChildren = currentNode.getAllNonNullEdgesToChildNodes();

            for (Map.Entry<String, PatriciaTrieNode> entry : existantEdgesToChildren.entrySet()) {
                printer.printEdge(entry.getKey(), currentNode, entry.getValue());
                printPatriciaTrie(entry.getValue(), printer);
            }
        }
	}

	@Override
	public IPatriciaTrie merge(IPatriciaTrie trie1, IPatriciaTrie trie2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHybridTrie toHybridTrie() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public void clearTrie() {
        this.nodeCount = 0;
        this.rootNode = new PatriciaTrieNode(getNewNodeId(), usedAlphabet.getNodeArity(), true);
    }
}
