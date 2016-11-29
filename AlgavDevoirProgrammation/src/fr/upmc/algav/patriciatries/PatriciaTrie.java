package fr.upmc.algav.patriciatries;

import java.util.*;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.hybridtries.HybridTrieNode;
import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.patriciatries.helper.AlphabetHelper;
import fr.upmc.algav.patriciatries.helper.PatriciaTrieHelper;
import fr.upmc.algav.tools.GraphPrinter;

public class PatriciaTrie implements IPatriciaTrie {

	private PatriciaTrieNode rootNode;
	private final Alphabet usedAlphabet;
    private PatriciaTrieNodeManager patriciaTrieNodeManager;

	public PatriciaTrie(Alphabet usedAlphabet) {
		this.usedAlphabet = usedAlphabet;
		this.patriciaTrieNodeManager = new PatriciaTrieNodeManager();
		this.rootNode = new PatriciaTrieNode(patriciaTrieNodeManager.generateNewNodeId(), usedAlphabet.getNodeArity(), true);
	}

    public PatriciaTrie(PatriciaTrieNode rootNode, Alphabet usedAlphabet) {
        this.usedAlphabet = usedAlphabet;
        this.patriciaTrieNodeManager = new PatriciaTrieNodeManager();
        this.rootNode = rootNode;
    }

	@Override
	public boolean isEmpty() {
		return rootNode.isLeaf();
	}

	@Override
	public void insert(String word) {
        if (usedAlphabet.isValidWord(word)) {
            insertCharacterSequenceForNode(rootNode, word);
        } else {
            System.err.println("The word \"" + word + "\" could not be inserted as it is not supported by the " +
            "trie's underlying alphabet!");
        }
	}

	private void insertCharacterSequenceForNode(PatriciaTrieNode currentNode, String word) {
		if (currentNode.isLeaf()) {
			// There are no edges to test. Just insert the whole word as new edge.
			currentNode.addNewValuedResultEdge(patriciaTrieNodeManager.generateNewNodeId(), word);
		} else {
			// There are already edge values for this node.
			String commonPrefix = PatriciaTrieHelper.getCommonPrefix(currentNode, word);

			if (commonPrefix == null) {
				// We have no shared prefix. Just insert the whole word as new edge.
				currentNode.addNewValuedResultEdge(patriciaTrieNodeManager.generateNewNodeId(), word);
			} else if (PatriciaTrieHelper.wordIsAlreadyStoredForNode(currentNode, word)) {
                // Word is already stored --> Do nothing!
            } else {
				String edgeValue = currentNode.getConcernedEdgeForValue(word);
				String wordWithoutCommonPrefix = word.substring(commonPrefix.length());
				String edgeValueWithoutCommonPrefix = edgeValue.substring(commonPrefix.length());

				if (edgeValueWithoutCommonPrefix.length() <= 0) {
					// The inserted word contains the whole edge prefix.
                    PatriciaTrieNode concernedChildNode = currentNode.getChildNodeForEdgeValue(edgeValue);

					if (wordWithoutCommonPrefix.length() <= 0) {
						// The word itself is also finished.
						// E.g. Inserted word = "why" and edge prefix = "why"
						// -> We add a result edge for signaling that a word ends here.
						concernedChildNode.addNewResultOnlyEdge(patriciaTrieNodeManager.generateNewNodeId());
					} else {
						// The word itself is not yet finished.
						// E.g. Inserted word = "however" and edge prefix = "how"
						// -> Insert the remaining characters of the word for the child node of the current edge.
						insertCharacterSequenceForNode(concernedChildNode, wordWithoutCommonPrefix);
					}
				} else {
					// The inserted word contains only a part of the edge getPrefixCount.
					// E.g. Inserted word = "talking", Edge Value = "talked"
					// -> We have to split the path!

                    // For example: We want to store "BLA1" and "BLA" ist already stored.
                    boolean isExtensionOfExistingWord =
                            Alphabet.getEndOfWordCharacterAsString().equals(edgeValueWithoutCommonPrefix) &&
                                    !wordWithoutCommonPrefix.isEmpty();

                    if (isExtensionOfExistingWord) {
                        // Store the current's edge current child node because we need it later.
                        final PatriciaTrieNode oldCurrentEdgeChildNode = currentNode.getChildNodeForEdgeValue(edgeValue);

                        // Set the edge value to the common prefix
                        currentNode.setEdge(commonPrefix, oldCurrentEdgeChildNode);
                        // Add new result edge to child node
                        insertCharacterSequenceForNode(oldCurrentEdgeChildNode, Alphabet.getEndOfWordCharacterAsString());
                        // Add the remaining word for the word to be inserted as new edge
                        insertCharacterSequenceForNode(oldCurrentEdgeChildNode, wordWithoutCommonPrefix);

                    } else {
                        // Store the current's edge current child node because we need it later.
                        final PatriciaTrieNode oldCurrentEdgeChildNode = currentNode.getChildNodeForEdgeValue(edgeValue);

                        // Create the new child node for the current edge.
                        PatriciaTrieNode newCurrentEdgeChildNode = new PatriciaTrieNode(patriciaTrieNodeManager.generateNewNodeId(),
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
	public void insert(Collection<String> words) {
		for (String word : words) {
			insert(word);
		}
	}
	
	@Override
	public void removeAll() {
        this.patriciaTrieNodeManager = new PatriciaTrieNodeManager();
        this.rootNode = new PatriciaTrieNode(patriciaTrieNodeManager.generateNewNodeId(), usedAlphabet.getNodeArity(), true);
	}
	
	@Override
	public boolean search(String word) {
        boolean hasBeenFound = false;

        if (usedAlphabet.isValidWord(word)) {
            hasBeenFound = word != null && searchWordInNode(word, rootNode);
        } else {
            System.err.println("The word \"" + word + "\" could not be searched as it is not supported by the " +
                    "trie's underlying alphabet!");
        }

        return hasBeenFound;
	}

	private boolean searchWordInNode(String word, PatriciaTrieNode currentNode) {
		boolean res;

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
                        // E.g. Searched word = "why" and edge prefix = "why"
                        // Look if the concerned child node contains a result only edge.
                        res = searchWordInNode("", childNode);
                    } else {
                        // The word itself is not yet finished.
                        // E.g. Searched word = "however" and edge prefix = "how"
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

		// Only if we're not visiting a leaf, we have to actually do stuff.
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
        int resultCount = 0;

        if (usedAlphabet.isValidWord(prefix)) {
            resultCount = getPrefixCountForNode(prefix, prefix, rootNode);
        } else {
            System.err.println("The prefix \"" + prefix + "\" could not be searched as it is not supported by the " +
                    "trie's underlying alphabet!");
        }

		return resultCount;
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
        boolean hasBeenRemoved = false;

        if (usedAlphabet.isValidWord(word)) {
            hasBeenRemoved = removeWordFromNode(rootNode, word, null, null);

            if (PatriciaTrieHelper.nodeHasOnlyNullEdges(rootNode)) {
                rootNode.setAsLeaf();
            }
        } else {
            System.err.println("The word \"" + word + "\" could not be removed as it is not supported by the " +
                    "trie's underlying alphabet!");
        }

        return hasBeenRemoved;
	}

	private boolean removeWordFromNode(PatriciaTrieNode currentNode, String word,
                                       String previouslyVisitedEdgeValue,
                                       PatriciaTrieNode previouslyVisitedNode) {
		boolean res;

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

                // Append also all children of the only child node
                for (Map.Entry<String, PatriciaTrieNode> childNodeEdges :
                        currentNode.getChildNodeForEdgeValue(onlyChildEdgeValue).getAllNonNullEdgesToChildNodes().entrySet()) {

                    currentNode.setEdge(childNodeEdges.getKey(), childNodeEdges.getValue());
                }

                // Remove also the only child and its edge as a consequence and mark the current
                // node as leaf as it has now only null edges.
                currentNode.removeEdge(onlyChildEdgeValue);

                if (PatriciaTrieHelper.nodeHasOnlyNullEdges(currentNode)) {
                    currentNode.setAsLeaf();
                }
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
                    childNode.removeEdge(Alphabet.getEndOfWordCharacterAsString());

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

                        // Append also all the child node's children
                        for (Map.Entry<String, PatriciaTrieNode> childNodeEdges :
                                childNode.getChildNodeForEdgeValue(onlyChildEdgeValue).getAllNonNullEdgesToChildNodes().entrySet()) {

                            childNode.setEdge(childNodeEdges.getKey(), childNodeEdges.getValue());
                        }

                        // Remove also the only child and its edge as a consequence and mark the child
                        // node as leaf as it has now only null edges.
                        childNode.removeEdge(onlyChildEdgeValue);

                        if (PatriciaTrieHelper.nodeHasOnlyNullEdges(childNode)) {
                            childNode.setAsLeaf();
                        }
                    }
                } else {
                    // The word itself is not yet finished.
                    // E.g. To be removed word = "however" and edge value = "how"
                    // Search for the remaining characters of the word at the child node of the current edge.
                    res = removeWordFromNode(childNode, wordWithoutCommonPrefix, edgeValue, currentNode);
                }
            }
        }

        return res;
	}

	@Override
	public void print(String fileName) {
		GraphPrinter graphPrinter = new GraphPrinter(fileName);

        graphPrinter.beginUndirected();
        printPatriciaNode(rootNode, graphPrinter);
        graphPrinter.end();
    }

	private static void printPatriciaNode(PatriciaTrieNode currentNode, GraphPrinter graphPrinter) {
		if (currentNode.isLeaf()) {
            graphPrinter.printNode(currentNode);
        } else {
            HashMap<String, PatriciaTrieNode> existingEdgesToChildren = currentNode.getAllNonNullEdgesToChildNodes();

            for (Map.Entry<String, PatriciaTrieNode> entry : existingEdgesToChildren.entrySet()) {
                graphPrinter.printEdge(entry.getKey(), currentNode, entry.getValue());
                printPatriciaNode(entry.getValue(), graphPrinter);
            }
        }
	}

	@Override
	public IPatriciaTrie merge(IPatriciaTrie otherTrie) {
		IPatriciaTrie res;

        if (this.isEmpty()) {
            res = (otherTrie == null) ? this : otherTrie;
        } else if (otherTrie == null || otherTrie.isEmpty()) {
                res = this;
        } else {
            //res = createMergedTrie(rootNode, otherTrie.getRootNode());
            LinkedHashMap<String, PatriciaTrieNode> thisEdges = rootNode.getAllNonNullEdgesToChildNodes();
            LinkedHashMap<String, PatriciaTrieNode> otherEdges = otherTrie.getRootNode().getAllNonNullEdgesToChildNodes();

            res = new PatriciaTrie(
                    mergeCurrentNodeLevel(thisEdges, otherEdges, new PatriciaTrieNodeManager()),
                    new Alphabet()
            );
        }

        return res;
	}

    /*private PatriciaTrie createMergedTrie(PatriciaTrieNode thisRootNode, PatriciaTrieNode otherTrieRootNode) {
        PatriciaTrie mergedTrie = new PatriciaTrie(new Alphabet());

        HashSet<String> mergedWords = new HashSet<>();
        mergedWords.addAll(getStoredWordsForNode(thisRootNode, ""));
        mergedWords.addAll(getStoredWordsForNode(otherTrieRootNode, ""));

        mergedTrie.insert(mergedWords);

        return mergedTrie;
    }*/

	private PatriciaTrieNode mergeCurrentNodeLevel(LinkedHashMap<String, PatriciaTrieNode> trie1Edges,
                                                   LinkedHashMap<String, PatriciaTrieNode> trie2Edges,
                                                   PatriciaTrieNodeManager nodeManager) {

        PatriciaTrieNode newRoot = new PatriciaTrieNode(
                nodeManager.generateNewNodeId(), usedAlphabet.getNodeArity(), false
        );

        if (!trie1Edges.isEmpty() && !trie2Edges.isEmpty()) {
            // All edges for the current node of this trie if not a leaf
            Set<String> trie1EdgeValues = new HashSet<>(trie1Edges.keySet());
            Set<String> trie2EdgeValues = new HashSet<>(trie2Edges.keySet());

            for (String trie1EdgeValue : trie1EdgeValues) {
                EdgesWithSharedPrefix commonEdgePrefixWithTrie2Edges = PatriciaTrieHelper.getEdgeWithCommonPrefixForEdge(
                        trie1EdgeValue, trie2EdgeValues
                );

                if (commonEdgePrefixWithTrie2Edges != null) {
                    String commonPrefix = commonEdgePrefixWithTrie2Edges.getSharedPrefix();

                    if (commonEdgePrefixWithTrie2Edges.edgesAreIdentical()) {
                        // We have two edges with the same edge value.
                        // E.g.: "abc" and "abc" with common prefix "abc"
                        // Append all children of trie1 and all children of trie2 for this edge value to the new edge
                        LinkedHashMap<String, PatriciaTrieNode> trie1EdgeChildNodeEdges = getAllEdgesForChildOfEdge(trie1Edges, commonPrefix);
                        LinkedHashMap<String, PatriciaTrieNode> trie2EdgeChildNodeEdges = getAllEdgesForChildOfEdge(trie2Edges, commonPrefix);

                        newRoot.setEdge(commonPrefix, mergeCurrentNodeLevel(
                                trie1EdgeChildNodeEdges, trie2EdgeChildNodeEdges, nodeManager
                                )
                        );
                    } else if (commonEdgePrefixWithTrie2Edges.firstEdgeAndPrefixAreIdentical()) {
                        // One edge has the full prefix, the other one just a part of if
                        // E.g. "abc" and "abcd" with common prefix "abc"
                        LinkedHashMap<String, PatriciaTrieNode> trie1EdgeChildNodeEdges = getAllEdgesForChildOfEdge(trie1Edges, commonPrefix);

                        LinkedHashMap<String, PatriciaTrieNode> otherEdges = new LinkedHashMap<>();

                        PatriciaTrieNode trie2EdgeChildNode = trie2Edges.get(commonEdgePrefixWithTrie2Edges.getSecondEdgeValue());
                        String trie2EdgeValueWithoutPrefix = commonEdgePrefixWithTrie2Edges.getSecondEdgeValueWithoutCommonPrefix();
                        PatriciaTrieNode childNodeForRemainingChars = new PatriciaTrieNode(
                                nodeManager.generateNewNodeId(), usedAlphabet.getNodeArity(), false
                        );
                        // Set the old children as children of the new remaining chars node
                        for (Map.Entry<String, PatriciaTrieNode> child : trie2EdgeChildNode.getAllNonNullEdgesToChildNodes().entrySet()) {
                            childNodeForRemainingChars.setEdge(child.getKey(), PatriciaTrieHelper.copyNode(child.getValue(), nodeManager));
                        }

                        // Add as new edge to others set and then do recursively the same for the new added root node edge
                        otherEdges.put(trie2EdgeValueWithoutPrefix, childNodeForRemainingChars);
                        newRoot.setEdge(commonPrefix, mergeCurrentNodeLevel(
                                trie1EdgeChildNodeEdges, otherEdges, nodeManager
                                )
                        );

                    } else if (commonEdgePrefixWithTrie2Edges.secondEdgeAndPrefixAreIdentical()) {
                        // One edge has the full prefix, the other one just a part of if
                        // E.g. "abcd" and "abc" with common prefix "abc"

                        LinkedHashMap<String, PatriciaTrieNode> otherEdges = new LinkedHashMap<>();

                        PatriciaTrieNode trie1EdgeChildNode = trie1Edges.get(commonEdgePrefixWithTrie2Edges.getFirstEdgeValue());
                        String trie1EdgeValueWithoutPrefix = commonEdgePrefixWithTrie2Edges.getFirstEdgeValueWithoutCommonPrefix();
                        PatriciaTrieNode childNodeForRemainingChars = new PatriciaTrieNode(
                                nodeManager.generateNewNodeId(), usedAlphabet.getNodeArity(), false
                        );
                        // Set the old children as children of the new remaining chars node
                        for (Map.Entry<String, PatriciaTrieNode> child : trie1EdgeChildNode.getAllNonNullEdgesToChildNodes().entrySet()) {
                            childNodeForRemainingChars.setEdge(child.getKey(), PatriciaTrieHelper.copyNode(child.getValue(), nodeManager));
                        }

                        LinkedHashMap<String, PatriciaTrieNode> trie2EdgeChildNodeEdges = getAllEdgesForChildOfEdge(trie2Edges, commonPrefix);

                        // Add as new edge to others set and then do recursively the same for the new added root node edge
                        otherEdges.put(trie1EdgeValueWithoutPrefix, childNodeForRemainingChars);
                        newRoot.setEdge(commonPrefix, mergeCurrentNodeLevel(
                                otherEdges, trie2EdgeChildNodeEdges, nodeManager
                                )
                        );
                    } else {
                        // Both edge values have a common prefix but none of them contains the whole prefix
                        // E.g. "abcd" and "abce" with common prefix "abc"

                        PatriciaTrieNode commonPrefixChildNode = new PatriciaTrieNode(
                                nodeManager.generateNewNodeId(), usedAlphabet.getNodeArity(), false
                        );
                        newRoot.setEdge(commonPrefix, commonPrefixChildNode);

                        String trie1EdgeValueWithoutPrefix = commonEdgePrefixWithTrie2Edges.getFirstEdgeValueWithoutCommonPrefix();
                        String trie2EdgeValueWithoutPrefix = commonEdgePrefixWithTrie2Edges.getSecondEdgeValueWithoutCommonPrefix();

                        PatriciaTrieNode trie1EdgeChildNode = trie1Edges.get(commonEdgePrefixWithTrie2Edges.getFirstEdgeValue());
                        PatriciaTrieNode trie2EdgeChildNode = trie2Edges.get(commonEdgePrefixWithTrie2Edges.getSecondEdgeValue());

                        // New intermediate node for resting value "d" of trie1 edge value
                        PatriciaTrieNode newNode1 = new PatriciaTrieNode(
                                nodeManager.generateNewNodeId(), usedAlphabet.getNodeArity(), false
                        );
                        // Set the old children as children of the intermediate node
                        for (Map.Entry<String, PatriciaTrieNode> child : trie1EdgeChildNode.getAllNonNullEdgesToChildNodes().entrySet()) {
                            newNode1.setEdge(child.getKey(), PatriciaTrieHelper.copyNode(child.getValue(), nodeManager));
                        }

                        // New intermediate node for resting value "e" of trie2 edge value
                        PatriciaTrieNode newNode2 = new PatriciaTrieNode(
                                nodeManager.generateNewNodeId(), usedAlphabet.getNodeArity(), false
                        );
                        // Set the old children as children of the intermediate node
                        for (Map.Entry<String, PatriciaTrieNode> child : trie2EdgeChildNode.getAllNonNullEdgesToChildNodes().entrySet()) {
                            newNode2.setEdge(child.getKey(), PatriciaTrieHelper.copyNode(child.getValue(), nodeManager));
                        }

                        // Append to child node for common prefix edge
                        commonPrefixChildNode.setEdge(trie1EdgeValueWithoutPrefix, newNode1);
                        commonPrefixChildNode.setEdge(trie2EdgeValueWithoutPrefix, newNode2);
                    }

                    // We have treated the other edge, so remove it from the set
                    trie2EdgeValues.remove(commonEdgePrefixWithTrie2Edges.getSecondEdgeValue());

                } else {
                    // Simply add the edge and all its descendants to the new root
                    PatriciaTrieNode childNode = trie1Edges.get(trie1EdgeValue);
                    newRoot.setEdge(trie1EdgeValue, PatriciaTrieHelper.copyNode(childNode, nodeManager));
                }
            }

            // Add all remaining trie 2 edges to the new root as they don't match with the trie 1 edges
            for (String trie2EdgeValue : trie2EdgeValues) {
                PatriciaTrieNode childNode = trie2Edges.get(trie2EdgeValue);
                newRoot.setEdge(trie2EdgeValue, PatriciaTrieHelper.copyNode(childNode, nodeManager));
            }
        }

        return newRoot;
    }

    private static LinkedHashMap<String, PatriciaTrieNode> getAllEdgesForChildOfEdge(
                                                LinkedHashMap<String, PatriciaTrieNode> edges, String edgeValue) {

        LinkedHashMap<String, PatriciaTrieNode> res = new LinkedHashMap<>();

        if (edges != null && edgeValue != null) {
            PatriciaTrieNode childForEdge = edges.get(edgeValue);

            if (childForEdge != null) {
                res = childForEdge.getAllNonNullEdgesToChildNodes();
            }
        }

        return res;
    }

	@Override
	public IHybridTrie toHybridTrie() {
        HybridTrie hybridTrie;

        if (rootNode == null) {
            hybridTrie = new HybridTrie();
        } else {
            hybridTrie = new HybridTrie(
                convertToHybridTrie(rootNode.getAllNonNullEdgesToChildNodes(), 0, new LinkedHashMap<>(), null)
            );
        }

		return hybridTrie;
	}

	private HybridTrieNode convertToHybridTrie(LinkedHashMap<String, PatriciaTrieNode> currentEdges, int wordCount,
							   LinkedHashMap<String, PatriciaTrieNode> restEdgesFromHigherLevel, HybridTrieNode formerRoot) {

		HybridTrieNode resultRootNode = null;

        // Only if we have edges to convert
		if (currentEdges != null) {
            // All edges to consider for this step, are the current edges + the remaining edges from a previous step
			LinkedHashMap<String, PatriciaTrieNode> edgesToChildren = new LinkedHashMap<>(restEdgesFromHigherLevel);
            edgesToChildren.putAll(currentEdges);
            // Used to determine the remaining edges for this step
            LinkedHashMap<String, PatriciaTrieNode> remainingEdges = new LinkedHashMap<>(edgesToChildren);
			// We have not yet selected a root node for our sub-trie.
            boolean hasNoRootNodeYet = true;

            // Find and remove a result only edge in the set of current edges for this step
            // if existing. Mark then consequently the former root as final node.
            for (Map.Entry<String, PatriciaTrieNode> currentEdge : currentEdges.entrySet()) {
                if (Alphabet.getEndOfWordCharacterAsString().equals(currentEdge.getKey()) && formerRoot != null) {
                    wordCount++;
                    formerRoot.setIsFinalNode(wordCount);
                    edgesToChildren.remove(currentEdge.getKey());
                    remainingEdges.remove(currentEdge.getKey());
                    break;
                }
            }

            // Iterate now the full set of edges for this step
			for (Map.Entry<String, PatriciaTrieNode> edgeToChild : edgesToChildren.entrySet()) {
				String edgeValue = edgeToChild.getKey();
				char[] edgeValueChars = edgeValue.toCharArray();

				PatriciaTrieNode childNode = edgeToChild.getValue();

				if (hasNoRootNodeYet) {
                    // We define a new root node
					hasNoRootNodeYet = false;

					if (edgeValueChars.length > 0) {
                        // Init the sub-trie root node with the first character of the edge value
						resultRootNode = new HybridTrieNode(edgeValueChars[0]);

						HybridTrieNode tempHybridTrieNode = null;
                        // Iterate through the remaining characters and append them as child nodes
						for (int charIndex = 1; charIndex < edgeValueChars.length; charIndex++) {
							if (charIndex == 1) {
                                // First created hybrid trie node is special as we have to add it to the new root.
								if (AlphabetHelper.isResultCharacter(edgeValueChars[charIndex])) {
                                    // End of word, mark as final node
									wordCount++;
									resultRootNode.setIsFinalNode(wordCount);
								} else {
                                    tempHybridTrieNode = new HybridTrieNode(edgeValueChars[charIndex]);
                                    resultRootNode.setMiddleChild(tempHybridTrieNode);
                                }
							} else {
                                // For all the other nodes after charIndex == 1
								if (AlphabetHelper.isResultCharacter(edgeValueChars[charIndex])) {
                                    // End of word, mark as final node
									wordCount++;

                                    if (tempHybridTrieNode != null) {
                                        tempHybridTrieNode.setIsFinalNode(wordCount);
                                    }
								} else {
                                    // Otherwise append middle children until the end of a prefix or word has been reached.
									HybridTrieNode newMiddleChild = new HybridTrieNode(edgeValueChars[charIndex]);

                                    if (tempHybridTrieNode != null) {
                                        tempHybridTrieNode.setMiddleChild(newMiddleChild);
                                    }

                                    // Update the current hybrid node for chaining
									tempHybridTrieNode = newMiddleChild;
								}
							}
						}

						// The edge has been converted. Therefore remove it as we won't pass it to the next level.
                        remainingEdges.remove(edgeValue);

                        // If we created a hybrid trie node, append also all successor edges of the current edge
                        // Otherwise append them to the result root node.
                        if (tempHybridTrieNode != null) {
                            tempHybridTrieNode.setMiddleChild(convertToHybridTrie(
                                    childNode.getAllNonNullEdgesToChildNodes(), wordCount,
                                    new LinkedHashMap<>(), tempHybridTrieNode
                            ));
						} else {
                            LinkedHashMap<String, PatriciaTrieNode> childNodeEdges = childNode.getAllNonNullEdgesToChildNodes();

                            if (!childNodeEdges.isEmpty()) {
                                resultRootNode.setMiddleChild(convertToHybridTrie(
                                        childNode.getAllNonNullEdgesToChildNodes(), wordCount,
                                        new LinkedHashMap<>(), resultRootNode
                                ));
                            }
                        }
					}
				// We already have a root node for the current sub-trie.
				} else {
                    // There's still empty space to append a new edge as right child to the result root node.
                    if (resultRootNode != null && resultRootNode.getRightChild() == null && edgeValueChars.length > 0) {

                        HybridTrieNode tempHybridTrieNode = null;

                        for (int charIndex = 0; charIndex < edgeValueChars.length; charIndex++) {
                            if (charIndex == 0) {
                                // First created hybrid trie node is special as we have to add it to the new root.
                                if (AlphabetHelper.isResultCharacter(edgeValueChars[charIndex])) {
                                    // We have a result word, set the according mark.
                                    wordCount++;
                                    resultRootNode.setIsFinalNode(wordCount);
                                } else {
                                    tempHybridTrieNode = new HybridTrieNode(edgeValueChars[charIndex]);
                                    resultRootNode.setRightChild(tempHybridTrieNode);
                                }
                            } else {
                                if (AlphabetHelper.isResultCharacter(edgeValueChars[charIndex])) {
                                    // We have a result word, set the according mark.
                                    wordCount++;

                                    if (tempHybridTrieNode != null) {
                                        tempHybridTrieNode.setIsFinalNode(wordCount);
                                    }
                                } else {
                                    HybridTrieNode newMiddleChild = new HybridTrieNode(edgeValueChars[charIndex]);

                                    if (tempHybridTrieNode != null) {
                                        tempHybridTrieNode.setMiddleChild(newMiddleChild);
                                    }

                                    tempHybridTrieNode = newMiddleChild;
                                }
                            }
                        }

                        if (tempHybridTrieNode != null) {
                            tempHybridTrieNode.setMiddleChild(convertToHybridTrie(
                                    childNode.getAllNonNullEdgesToChildNodes(), wordCount,
                                    new LinkedHashMap<>(), tempHybridTrieNode
                            ));
                        }

                        remainingEdges.remove(edgeValue);

                    } else {
                        // There's no more space left to append a new edge as right child to the result root node.
                        // Therefore try to append the edges to the right child of the current right child.
                        if (resultRootNode != null) {
                            HybridTrieNode rightChild = resultRootNode.getRightChild();
                            rightChild.setRightChild(convertToHybridTrie(
                                    new LinkedHashMap<>(), wordCount, remainingEdges, rightChild
                            ));
                        }
                    }
                }
		    }
		}

		return resultRootNode;
	}

    @Override
    public PatriciaTrieNode getRootNode() {
        return rootNode;
    }
}
