package fr.upmc.algav.patriciatries;

import java.util.ArrayList;

import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.patriciatries.helper.AlphabetHelper;
import fr.upmc.algav.patriciatries.helper.PatriciaTrieHelper;

public class PatriciaTrie implements IPatriciaTrie {

	private PatriciaTrieNode rootNode;
	private final Alphabet usedAlphabet;
	private int wordCount;

	public PatriciaTrie(Alphabet usedAlphabet) {
		this.usedAlphabet = usedAlphabet;
		this.wordCount = 0;
		this.rootNode = new PatriciaTrieNode(usedAlphabet.getNodeArity(), true);
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
			// There are now edges yet to test. Just insert the whole word as new edge.
			currentNode.addNewValuedResultEdge(word);
		} else {
			// There are already edge values for this node
			String commonPrefix = PatriciaTrieHelper.getCommonPrefix(currentNode, word);

			if (commonPrefix == null) {
				// We have no shared prefix. Just insert the whole word as new edge
				currentNode.addNewValuedResultEdge(word);
			} else {
				String edgeValue = currentNode.getConcernedEdgeForValue(word);
				String wordWithoutCommonPrefix = word.substring(commonPrefix.length());
				String edgeValueWithoutCommonPrefix = edgeValue.substring(commonPrefix.length());

				if (edgeValueWithoutCommonPrefix.length() <= 0) {
					// The inserted word contains the whole edge prefix.

					if (wordWithoutCommonPrefix.length() <= 0) {
						// The word itself is also finished.
						// E.g. Inserted word = "why" and edge prefix = "why"
						// -> We add a result edge for signaling that a word ends here.
						currentNode.addNewResultOnlyEdge();
					} else {
						// The word itself is not yet finished.
						// E.g. Inserted word = "however" and edge prefix = "how"
						// -> Insert the remaining characters of the word for the child node of the current edge.
						insertCharacterSequenceInTree(currentNode.getChildNodeForEdgeValue(edgeValue),
														wordWithoutCommonPrefix);
					}
				} else {
					// The inserted word contains only a part of the edge prefix.
					// E.g. Inserted word = "talking", Edge Value = "talked"
					// -> We have to split the path!

					// Store the current's edge current child node because we need it later.
					final PatriciaTrieNode oldCurrentEdgeChildNode = currentNode.getChildNodeForEdgeValue(edgeValue);

					// Create the new child node for the current edge.
					PatriciaTrieNode newCurrentEdgeChildNode = new PatriciaTrieNode(usedAlphabet.getNodeArity(), false);
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

	@Override
	public void insert(ArrayList<String> words) {
		for (String word : words) {
			insert(word);
		}
	}
	
	@Override
	public boolean search(String word) {
		return searchWord(word, rootNode);
	}

	private boolean searchWord(String word, PatriciaTrieNode currentNode) {
		boolean res = false;

		if (word != null) {
			if (word.isEmpty()) {
				res = PatriciaTrieHelper.nodeContainsResultOnlyEdge(currentNode);
			} else {
				// TODO
			}
		}

		return res;
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
	public double averageDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int prefix(String word) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean remove(String word) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void print(String FileName) {
		// TODO Auto-generated method stub
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
}
