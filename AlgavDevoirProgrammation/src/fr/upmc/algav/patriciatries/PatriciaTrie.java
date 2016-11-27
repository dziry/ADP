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
	private int nodeCount;

	public PatriciaTrie(Alphabet usedAlphabet) {
		this.usedAlphabet = usedAlphabet;
		this.nodeCount = 0;
		this.rootNode = new PatriciaTrieNode(getNewNodeId(), usedAlphabet.getNodeArity(), true);
	}

    public PatriciaTrie(PatriciaTrieNode rootNode, Alphabet usedAlphabet) {
        this.usedAlphabet = usedAlphabet;
        this.nodeCount = 0;
        this.rootNode = rootNode;
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
			// There are no edges to test. Just insert the whole word as new edge.
			currentNode.addNewValuedResultEdge(getNewNodeId(), word);
		} else {
			// There are already edge values for this node.
			String commonPrefix = PatriciaTrieHelper.getCommonPrefix(currentNode, word);

			if (commonPrefix == null) {
				// We have no shared prefix. Just insert the whole word as new edge.
				currentNode.addNewValuedResultEdge(getNewNodeId(), word);
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
						concernedChildNode.addNewResultOnlyEdge(getNewNodeId());
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

                        // Set the edge value to the common getPrefixCount
                        currentNode.setEdge(commonPrefix, oldCurrentEdgeChildNode);
                        // Add new result edge to child node
                        insertCharacterSequenceForNode(oldCurrentEdgeChildNode, Alphabet.getEndOfWordCharacterAsString());
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
	public void insert(Collection<String> words) {
		for (String word : words) {
			insert(word);
		}
	}
	
	@Override
	public void removeAll() {
        this.nodeCount = 0;
        this.rootNode = new PatriciaTrieNode(getNewNodeId(), usedAlphabet.getNodeArity(), true);
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
		boolean res = word != null && removeWordFromNode(rootNode, word, null, null);

        if (PatriciaTrieHelper.nodeHasOnlyNullEdges(rootNode)) {
            rootNode.setAsLeaf();
        }

        return res;
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

                    // TODO
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

                            // TODO
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
            HashMap<String, PatriciaTrieNode> existantEdgesToChildren = currentNode.getAllNonNullEdgesToChildNodes();

            for (Map.Entry<String, PatriciaTrieNode> entry : existantEdgesToChildren.entrySet()) {
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
            res = createMergedTrie(rootNode, otherTrie.getRootNode());

            // TODO
            // Better complexity method. Not yet working because of bugs.
            /*PatriciaTrie mergedTree =
                    new PatriciaTrie(mergeCurrentNodeLevel(
                            PatriciaTrieHelper.copyNode(rootNode),
                            PatriciaTrieHelper.copyNode(otherTrie.getRootNode())), new Alphabet());

            // All nodes should have an unique ID after the merge!
            res = PatriciaTrieHelper.makeAllNodeIdsUnique(mergedTree);*/
        }

        return res;
	}

    private PatriciaTrie createMergedTrie(PatriciaTrieNode thisRootNode, PatriciaTrieNode otherTrieRootNode) {
        PatriciaTrie mergedTrie = new PatriciaTrie(new Alphabet());

        HashSet<String> mergedWords = new HashSet<>();
        mergedWords.addAll(getStoredWordsForNode(thisRootNode, ""));
        mergedWords.addAll(getStoredWordsForNode(otherTrieRootNode, ""));

        mergedTrie.insert(mergedWords);

        return mergedTrie;
    }

    // THIS IS THE MERGE METHOD WITH BETTER TIME COMPLEXITY. BUT THERE'S STILL A BUG, SO IT'S NOT FULLY WORKING

	/*private PatriciaTrieNode mergeCurrentNodeLevel(PatriciaTrieNode thisCurrentNode,
                                                   PatriciaTrieNode otherCurrentNode) {

        if (thisCurrentNode != null && otherCurrentNode != null) {
            // All edges for the current node of this trie if not a leaf
            LinkedHashMap<String, PatriciaTrieNode> thisEdges = thisCurrentNode.getAllNonNullEdgesToChildNodes();
            Set<String> thisEdgeValues = thisEdges.keySet();

            // All edges for the current node of the other trie if not a leaf
            LinkedHashMap<String, PatriciaTrieNode> otherEdges = otherCurrentNode.getAllNonNullEdgesToChildNodes();

            // Try to add all the other edges to the edges of this current node
            for (Map.Entry<String, PatriciaTrieNode> otherEdge : otherEdges.entrySet()) {
                String otherEdgeValue = otherEdge.getKey();

                if (thisEdgeValues.contains(otherEdgeValue)) {
                    // Both tries contain the same prefix, so we can keep this prefix edge value.
                    // Go the next node level and look what we have to add/change there.
                    PatriciaTrieNode thisChildNode = thisCurrentNode.getChildNodeForEdgeValue(otherEdgeValue);
                    PatriciaTrieNode otherChildNode = otherCurrentNode.getChildNodeForEdgeValue(otherEdgeValue);

                    if (thisChildNode != null && otherChildNode != null) {
                        thisCurrentNode.setEdge(otherEdgeValue, mergeCurrentNodeLevel(thisChildNode, otherChildNode));
                    }
                } else {
                    // Test if there's a common prefix between the edge of the other trie with all edges of this trie
                    String commonPrefixThisAndOtherEdge =
                            PatriciaTrieHelper.getCommonPrefixForListOfEdges(otherEdgeValue, thisEdgeValues);

                    if (commonPrefixThisAndOtherEdge != null) {
                        String thisEdgeValue = thisCurrentNode.getConcernedEdgeForValue(commonPrefixThisAndOtherEdge);

                        // Edge value of the other node reduced by the common prefix
                        String otherEdgeValueWithoutCommonPrefix =
                                otherEdgeValue.substring(commonPrefixThisAndOtherEdge.length());
                        // The child node of the current other node
                        PatriciaTrieNode otherNodeChildNode = otherCurrentNode.getChildNodeForEdgeValue(otherEdgeValue);

                        // If the prefix for the edge value of this current node changed
                        if (commonPrefixThisAndOtherEdge.length() < thisEdgeValue.length()) {
                            // New edge value for the edge of this current node
                            String thisEdgeValueWithoutCommonPrefix =
                                    thisEdgeValue.substring(commonPrefixThisAndOtherEdge.length());

                            // Store the old child node of this current node for this edge value
                            final PatriciaTrieNode oldThisNodeChildNode = thisCurrentNode.getChildNodeForEdgeValue(thisEdgeValue);

                            // Create the new child node for this current node.
                            PatriciaTrieNode newThisNodeChildNode = new PatriciaTrieNode(0,
                                    usedAlphabet.getNodeArity(), false);
                            // Add the old child node as child node of the newly created child node
                            newThisNodeChildNode.setEdge(thisEdgeValueWithoutCommonPrefix, oldThisNodeChildNode);
                            // Add the new child node to the current node
                            thisCurrentNode.setEdge(commonPrefixThisAndOtherEdge, newThisNodeChildNode);

                            // If the prefix for the edge value of the other node is the same as the common prefix
                            if (commonPrefixThisAndOtherEdge.length() == otherEdgeValue.length()) {
                                // Add all the edges of the child of the other current node to this current node
                                newThisNodeChildNode = mergeCurrentNodeLevel(newThisNodeChildNode, otherNodeChildNode);
                                // The prefix for the edge value of the other node did also change
                            } else if (commonPrefixThisAndOtherEdge.length() < otherEdgeValue.length()) {
                                // Add a new edge with the other edge value reduced by the common prefix as edge value
                                // and the other current node's child node to the newly created child node of
                                // this current node.
                                newThisNodeChildNode.setEdge(otherEdgeValueWithoutCommonPrefix, otherNodeChildNode);
                            }
                            // The prefix for the edge value of this current node did not change
                        } else if (commonPrefixThisAndOtherEdge.length() == thisEdgeValue.length()) {
                            PatriciaTrieNode thisCurrentNodeChildNode =
                                    thisCurrentNode.getChildNodeForEdgeValue(thisEdgeValue);

                            otherCurrentNode.updateEdgeValue(otherEdgeValue, otherEdgeValueWithoutCommonPrefix);
                            thisCurrentNode.setEdge(thisEdgeValue,
                                    mergeCurrentNodeLevel(thisCurrentNodeChildNode, otherCurrentNode));
                        }
                    } else {
                        // There's no shared prefix. Simply add the other edge to the set of "this edges"
                        thisCurrentNode.setEdge(otherEdgeValue, otherEdge.getValue());
                    }
                }
            }
        }

        return thisCurrentNode;
    }*/

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

		if (currentEdges != null) {
			LinkedHashMap<String, PatriciaTrieNode> edgesToChildren = new LinkedHashMap<>(restEdgesFromHigherLevel);
            edgesToChildren.putAll(currentEdges);
            LinkedHashMap<String, PatriciaTrieNode> remainingEdges = new LinkedHashMap<>(edgesToChildren);
			boolean hasNoRootNodeYet = true;

            for (Map.Entry<String, PatriciaTrieNode> currentEdge : currentEdges.entrySet()) {
                if (Alphabet.getEndOfWordCharacterAsString().equals(currentEdge.getKey()) && formerRoot != null) {
                    wordCount++;
                    formerRoot.setIsFinalNode(wordCount);
                    edgesToChildren.remove(currentEdge.getKey());
                    remainingEdges.remove(currentEdge.getKey());
                    break;
                }
            }

			for (Map.Entry<String, PatriciaTrieNode> edgeToChild : edgesToChildren.entrySet()) {
				String edgeValue = edgeToChild.getKey();
				char[] edgeValueChars = edgeValue.toCharArray();

				PatriciaTrieNode childNode = edgeToChild.getValue();

				if (hasNoRootNodeYet) {
                    // We define the root node
					hasNoRootNodeYet = false;

					if (edgeValueChars.length > 0) {
						resultRootNode = new HybridTrieNode(edgeValueChars[0]);

						HybridTrieNode tempHybridTrieNode = null;
						for (int charIndex = 1; charIndex < edgeValueChars.length; charIndex++) {
							if (charIndex == 1) {
                                // First created hybrid trie node is special as we have to add it to the new root.
								if (AlphabetHelper.isResultCharacter(edgeValueChars[charIndex])) {
									wordCount++;
									resultRootNode.setIsFinalNode(wordCount);
								} else {
                                    tempHybridTrieNode = new HybridTrieNode(edgeValueChars[charIndex]);
                                    resultRootNode.setMiddleChild(tempHybridTrieNode);
                                }
							} else {
								if (AlphabetHelper.isResultCharacter(edgeValueChars[charIndex])) {
									wordCount++;
									tempHybridTrieNode.setIsFinalNode(wordCount);
								} else {
									HybridTrieNode newMiddleChild = new HybridTrieNode(edgeValueChars[charIndex]);
									tempHybridTrieNode.setMiddleChild(newMiddleChild);

									tempHybridTrieNode = newMiddleChild;
								}
							}
						}

                        remainingEdges.remove(edgeValue);

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
				} else {
                    // There's still empty space to append a new edge as right child to the result root node.
                    if (resultRootNode.getRightChild() == null && edgeValueChars.length > 0) {

                        HybridTrieNode tempHybridTrieNode = null;

                        for (int charIndex = 0; charIndex < edgeValueChars.length; charIndex++) {
                            if (charIndex == 0) {
                                // First created hybrid trie node is special as we have to add it to the new root.
                                if (AlphabetHelper.isResultCharacter(edgeValueChars[charIndex])) {
                                    wordCount++;
                                    resultRootNode.setIsFinalNode(wordCount);
                                } else {
                                    tempHybridTrieNode = new HybridTrieNode(edgeValueChars[charIndex]);
                                    resultRootNode.setRightChild(tempHybridTrieNode);
                                }
                            } else {
                                if (AlphabetHelper.isResultCharacter(edgeValueChars[charIndex])) {
                                    wordCount++;
                                    tempHybridTrieNode.setIsFinalNode(wordCount);
                                } else {
                                    HybridTrieNode newMiddleChild = new HybridTrieNode(edgeValueChars[charIndex]);
                                    tempHybridTrieNode.setMiddleChild(newMiddleChild);

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
                        HybridTrieNode rightChild = resultRootNode.getRightChild();
                        rightChild.setRightChild(convertToHybridTrie(
                                new LinkedHashMap<>(), wordCount, remainingEdges, rightChild
                        ));
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

    @Override
    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }
}
