package fr.upmc.algav.patriciatries;

import java.util.*;

import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.patriciatries.helper.AlphabetHelper;
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
		insertCharacterSequenceForNode(rootNode, word);
	}

	private void insertCharacterSequenceForNode(PatriciaTrieNode currentNode, String word) {
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
						insertCharacterSequenceForNode(currentNode.getChildNodeForEdgeValue(edgeValue),
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
                        insertCharacterSequenceForNode(oldCurrentEdgeChildNode, Alphabet.getEndOfWordCharacter());
                        // Add the remaining word for the word to be inserted as new edge
                        insertCharacterSequenceForNode(oldCurrentEdgeChildNode, wordWithoutCommonPrefix);

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
                        insertCharacterSequenceForNode(newCurrentEdgeChildNode, wordWithoutCommonPrefix);
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
		return word != null && searchWordInNode(word, rootNode);
	}

	private boolean searchWordInNode(String word, PatriciaTrieNode currentNode) {
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
                        res = searchWordInNode("", childNode);
                    } else {
                        // The word itself is not yet finished.
                        // E.g. Searched word = "however" and edge getPrefixCount = "how"
                        // Search for the remaining characters of the word at the child node of the current edge.
                        res = searchWordInNode(wordWithoutCommonPrefix, childNode);
                    }
                }
            }
        }

		return res;
	}

	@Override
	public int getWordCount() {
		return getStoredWordsForNode(rootNode, "").size();
	}
	
	@Override
	public ArrayList<String> getStoredWords() {
		ArrayList<String> storedWords = new ArrayList<>(getStoredWordsForNode(rootNode, ""));
		storedWords.sort((o1, o2) -> o1.compareTo(o2));

		return storedWords;
	}

	private HashSet<String> getStoredWordsForNode(PatriciaTrieNode currentNode, String tempPathValue) {
		HashSet<String> res = new HashSet<>();

		// Only if we're not visiting a leaf, we have do actually do stuff.
		if (!currentNode.isLeaf()) {
			// If we're not visiting a leaf, we have to visit all possible children.
			ArrayList<String> edgeValues = currentNode.getAllEdgeValues();
			ArrayList<PatriciaTrieNode> childNodes = currentNode.getAllChildNodes();

			for (int i = 0; i < currentNode.getNodeArity(); i++) {
				String edgeValue = edgeValues.get(i);
				PatriciaTrieNode childNode = childNodes.get(i);

				// Only visit the non-null edges
				if (edgeValue != null && childNode != null) {
					if (AlphabetHelper.containsResultCharacter(edgeValue)) {
						// We have found the end of a word.
						// Store the current path concatenated with the current edge value.
						// But also remove the result character from the edge before storing the word.
						res.add(tempPathValue + AlphabetHelper.removeResultCharacterFromWord(edgeValue));
					} else {
						// No word found. We have to extend the path value and go further.
						String newTempPathValue = tempPathValue + edgeValue;
						res.addAll(getStoredWordsForNode(childNode, newTempPathValue));
					}
				}
			}
		}

		return res;
	}

	@Override
	public int getNullPointerCount() {
		return countNullPointersForNode(rootNode);
	}

	private int countNullPointersForNode(PatriciaTrieNode currentNode) {
		int count = 0;

		if (!currentNode.isLeaf()) {
			// If we're not visiting a leaf, we have to visit also all possible children.
			ArrayList<String> edgeValues = currentNode.getAllEdgeValues();
			ArrayList<PatriciaTrieNode> childNodes = currentNode.getAllChildNodes();

			for (int i = 0; i < currentNode.getNodeArity(); i++) {
				if (edgeValues.get(i) == null && childNodes.get(i) == null) {
					// We have a null pointer
					count++;
				} else {
					// We have not a null pointer. Search for null pointers in child.
					count += countNullPointersForNode(childNodes.get(i));
				}
			}
		} else {
			// We have a leaf. Therefore add all edges to the result count.
			count += currentNode.getNodeArity();
		}

		return count;
	}

	@Override
	public int getHeight() {
		return getHeightForNode(rootNode, -1);
	}

	private static int getHeightForNode(PatriciaTrieNode currentNode, int currentHeight) {
        int newCurrentHeight = currentHeight + 1;

        // If we have a leaf, we can simply return the height incremented by 1
        if (!currentNode.isLeaf()) {
            // Otherwise, we have to visit also all possible children as the height must have a greater value.
            ArrayList<String> edgeValues = currentNode.getAllEdgeValues();
            ArrayList<PatriciaTrieNode> childNodes = currentNode.getAllChildNodes();
            // Save the current height for this level to pass to possible children.
            final int currentHeightAtThisLevel = newCurrentHeight;

            for (int i = 0; i < currentNode.getNodeArity(); i++) {
                // Visit each child path
                if (edgeValues.get(i) != null && childNodes.get(i) != null) {
                    int childHeight = getHeightForNode(childNodes.get(i), currentHeightAtThisLevel);

                    // If we have a child path with a height that is greater as the current height, we have
                    // to update the height value
                    if (childHeight > newCurrentHeight) {
                        newCurrentHeight = childHeight;
                    }
                }
            }
        }

        return newCurrentHeight;
    }

	@Override
	public double getAverageDepthOfLeaves() {
		double totalDepthForLeaves = (double) getTotalDepthOfLeavesForNode(rootNode, -1);
        double leavesCount = (double) getTotalLeavesCountForNode(rootNode);
        return totalDepthForLeaves / leavesCount;
	}

    private int getTotalDepthOfLeavesForNode(PatriciaTrieNode currentNode, int currentDepth) {
        // We have a new level, so increase the current depth value.
        int newDepth = currentDepth + 1;
        int totalDepthForLeaves = 0;

        if (!currentNode.isLeaf()) {
            // No leaf --> we have to visit also all possible children for further leaves.
            ArrayList<String> edgeValues = currentNode.getAllEdgeValues();
            ArrayList<PatriciaTrieNode> childNodes = currentNode.getAllChildNodes();

            for (int i = 0; i < currentNode.getNodeArity(); i++) {
                // Visit each child
                if (edgeValues.get(i) != null && childNodes.get(i) != null) {
                    // We have no null pointer --> Search for leaves in descendants.
                    totalDepthForLeaves += getTotalDepthOfLeavesForNode(childNodes.get(i), newDepth);
                }
            }
        } else {
            // We have found a leaf. Increase count
            totalDepthForLeaves += newDepth;
        }

        return totalDepthForLeaves;
    }

    private int getTotalLeavesCountForNode(PatriciaTrieNode currentNode) {
        int newLeafCount = 0;

        if (!currentNode.isLeaf()) {
            // No leaf --> we have to visit also all possible children for further leaves.
            ArrayList<String> edgeValues = currentNode.getAllEdgeValues();
            ArrayList<PatriciaTrieNode> childNodes = currentNode.getAllChildNodes();

            for (int i = 0; i < currentNode.getNodeArity(); i++) {
                // Visit each child
                if (edgeValues.get(i) != null && childNodes.get(i) != null) {
                    // We have no null pointer --> Search for leaves in descendants.
                    newLeafCount += getTotalLeavesCountForNode(childNodes.get(i));
                }
            }
        } else {
            // We have found a leaf. Increase count
            newLeafCount++;
        }

        return newLeafCount;
    }

    @Override
	public int getPrefixCount(String prefix) {
		return (prefix == null || prefix.isEmpty()) ? 0 : getPrefixCountForNode(prefix, prefix, rootNode);
	}

	private int getPrefixCountForNode(String searchedPrefix, String tempPrefix, PatriciaTrieNode currentNode) {
		int res = 0;

		// Only if we're not at a leaf
		if (!currentNode.isLeaf()) {
			// There are already edge values for this node
			String commonPrefix = PatriciaTrieHelper.getCommonPrefix(currentNode, tempPrefix);

			if (commonPrefix != null) {
				String edgeValue = currentNode.getConcernedEdgeForValue(tempPrefix);
				String prefixWithoutCommonPrefix = tempPrefix.substring(commonPrefix.length());

				PatriciaTrieNode childNode = currentNode.getChildNodeForEdgeValue(edgeValue);

				if (prefixWithoutCommonPrefix.length() <= 0) {
					// The searched word itself is also finished.
					// E.g. Searched prefix = "why" and edge value = "why"
					// Look how many stored words there are now for the child node
					HashSet<String> storedWordsForChildNode = getStoredWordsForNode(childNode, searchedPrefix);
					res += storedWordsForChildNode.size();
				} else {
					// The search prefix itself is not yet finished.
					// E.g. Searched prefix = "however" and edge value = "how"
					// Search for the remaining characters of the prefix at the child node of the current edge.
					res += getPrefixCountForNode(searchedPrefix, prefixWithoutCommonPrefix, childNode);
				}
			}
		}

		return res;
	}

	@Override
	public boolean remove(String word) {
		return word != null && removeWordFromNode(rootNode, word, null, null);
	}

	private boolean removeWordFromNode(PatriciaTrieNode currentNode, String word,
                                       String previouslyVisitedEdgeValue,
                                       PatriciaTrieNode previouslyVisitedNode) {
		boolean res = false;

		if (!word.isEmpty()) {
			if (currentNode.isLeaf()) {
				// We're at a leaf. No more edges, a dead end.
				res = false;
			} else if (PatriciaTrieHelper.wordIsAlreadyStoredForNode(currentNode, word)) {
				// We have found the stored word !
				res = true;

                // Find the concerning edge and remove the word
                String currentNodeEdgeValue = currentNode.getConcernedEdgeForValue(word);
                currentNode.removeEdge(currentNodeEdgeValue);

                // Test if the node has now only one child as we could then also remove that child node
                // and merge its edge value with the current node's edge value
                Integer onlyChildIndex = PatriciaTrieHelper.getOnlyChildIndexOfNodeIfPresent(currentNode);

                // Update the previous edge with the merged value
                if (onlyChildIndex != null && previouslyVisitedNode != null &&
                        previouslyVisitedEdgeValue != null) {

                    // We have an only child, so get the only child's edge value
                    String onlyChildEdgeValue = currentNode.getEdgeValueForIndex(onlyChildIndex);
                    // Define the new merged edge value for the previous edge
                    String newEdgeValue = previouslyVisitedEdgeValue + onlyChildEdgeValue;
                    previouslyVisitedNode.updateEdgeValue(previouslyVisitedEdgeValue, newEdgeValue);

                    // Remove also the only child and its edge as a consequence and mark the current
                    // node as leaf as it has now only null edges.
                    currentNode.removeEdge(onlyChildEdgeValue);
                    currentNode.setAsLeaf();
                }
			} else {
				// We have to test for a common prefix
				String commonPrefix = PatriciaTrieHelper.getCommonPrefix(currentNode, word);

				if (commonPrefix == null) {
					// We have no shared getPrefixCount. The word can't exist in the tree.
					res = false;
				} else {
					String edgeValue = currentNode.getConcernedEdgeForValue(word);
					String wordWithoutCommonPrefix = word.substring(commonPrefix.length());

					PatriciaTrieNode childNode = currentNode.getChildNodeForEdgeValue(edgeValue);

					if (wordWithoutCommonPrefix.length() <= 0 &&
                            PatriciaTrieHelper.nodeContainsResultOnlyEdge(childNode)) {
						// The to be removed word itself is also finished.
						// E.g. To be removed word = "why" and edge value = "why"
						// The concerned child node needs to have a result only edge
                        res = true;

                        // Remove the result only edge
                        childNode.removeEdge(Alphabet.getEndOfWordCharacter());

                        // Test if the child node has now only one child as we could then also remove it
                        // and merge its edge value with the current edge value
                        Integer onlyChildIndex = PatriciaTrieHelper.getOnlyChildIndexOfNodeIfPresent(childNode);

                        // Update the current edge with the merged value
                        if (onlyChildIndex != null) {
                            // We have an only child, so get the only child's edge value
                            String onlyChildEdgeValue = childNode.getEdgeValueForIndex(onlyChildIndex);
                            // Define the new merged edge value for the current edge
                            String newEdgeValue = edgeValue + onlyChildEdgeValue;
                            currentNode.updateEdgeValue(edgeValue, newEdgeValue);

                            // Remove also the only child and its edge as a consequence and mark the child
                            // node as leaf as it has now only null edges.
                            childNode.removeEdge(onlyChildEdgeValue);
                            childNode.setAsLeaf();
                        }
					} else {
						// The word itself is not yet finished.
						// E.g. To be removed word = "however" and edge value = "how"
						// Search for the remaining characters of the word at the child node of the current edge.
						res = removeWordFromNode(childNode, wordWithoutCommonPrefix, edgeValue, currentNode);
					}
				}
			}
		}

		return res;
	}

	@Override
	public void print(String fileName) {
		Printer printer = new Printer(fileName);
		printer.beginUndirected();

        printPatriciaNode(rootNode, printer);

        printer.end();
    }

	private static void printPatriciaNode(PatriciaTrieNode currentNode, Printer printer) {
		if (currentNode.isLeaf()) {
            printer.printNode(currentNode);
        } else {
            HashMap<String, PatriciaTrieNode> existantEdgesToChildren = currentNode.getAllNonNullEdgesToChildNodes();

            for (Map.Entry<String, PatriciaTrieNode> entry : existantEdgesToChildren.entrySet()) {
                printer.printEdge(entry.getKey(), currentNode, entry.getValue());
                printPatriciaNode(entry.getValue(), printer);
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
