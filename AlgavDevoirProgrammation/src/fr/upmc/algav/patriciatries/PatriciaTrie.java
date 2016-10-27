package fr.upmc.algav.patriciatries;

import java.util.ArrayList;

import fr.upmc.algav.patriciatries.helper.AlphabetHelper;

import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.interfaces.ITrie;

public class PatriciaTrie implements IPatriciaTrie {

	private PatriciaTreeNode rootNode;
	private final Alphabet usedAlphabet;
	private int wordCount;

	public PatriciaTrie(Alphabet usedAlphabet) {
		this.usedAlphabet = usedAlphabet;
		this.wordCount = 0;
		this.rootNode = new PatriciaTreeNode(usedAlphabet.getNodeArity(), false);
	}

	@Override
	public boolean isEmpty() {
		return rootNode.hasOnlyNullEdges();
	}

	@Override
	public void insert(String word) {
		insertCharacterSequenceInTree(rootNode, word);
	}

	private void insertCharacterSequenceInTree(PatriciaTreeNode currentNode, String word) {
		// Use the index of the first character of the word to get the edge that is the only
		// possible option for this word.
		int indexOfFirstCharacterOfWord = AlphabetHelper.getIndexForFirstCharOfWord(word);
		PatriciaTreeEdge currentSequence = currentNode.getEdgeByIndex(indexOfFirstCharacterOfWord);

		if (currentSequence == null) {
			// There's no edge yet labeled with at least the first character of the word.
			// E.g. Inserted word = "dog"
			// -> We can insert the full word as new edge and mark this edge as result edge.
			currentNode.addNewEdgeWithWordAndResult(word, indexOfFirstCharacterOfWord);
		} else {
			// There's a edge that contains at least the first character of the word as prefix.
			// E.g. Inserted word = "dogma" and edge prefix = "dog"
			// or Inserted word = "cats" and edge prefix = "c"
			// -> Test how much common characters the edge prefix and the word have.
			char[] edgeValueChars = currentSequence.getEdgeValue().toCharArray();
			char[] wordChars = word.toCharArray();
			int equalCharactersIndex = 0;

			// As long as we're finding common characters, test the next character.
			while (true) {
				try {
					if (edgeValueChars[equalCharactersIndex] == wordChars[equalCharactersIndex]) {
						// We have found a new common character!
						equalCharactersIndex++;
					} else {
						// First difference in edge prefix and inserted word.
						// -> End the comparison.
						break;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// Reached the end of the word, the edge prefix or both.
					// -> End the comparison.
					break;
				}
			}

			int remainingCharsForEdgePrefix = edgeValueChars.length - equalCharactersIndex;
			int remainingCharsForWord = wordChars.length - equalCharactersIndex;

			if (remainingCharsForEdgePrefix <= 0) {
				// The inserted word contains the whole edge prefix.

				if (remainingCharsForWord <= 0) {
					// The word itself is also finished.
					// E.g. Inserted word = "why" and edge prefix = "why"
					// -> We add a result edge for signaling that a word ends here.
					currentNode.addNewResultEdge();
				} else {
					// The word itself is not yet finished.
					// E.g. Inserted word = "however" and edge prefix = "how"
					// -> Insert the remaining characters of the word for the child node of the current edge.
					String remainingWord = word.substring(equalCharactersIndex);
					insertCharacterSequenceInTree(currentSequence.getChildNode(), remainingWord);
				}
			} else {
				// The inserted word contains only a part of the edge prefix.
				// E.g. Inserted word = "talking", Edge Value = "talked"
				// -> We have to split the path!

				String commonPrefix = currentSequence.getEdgeValue().substring(0, equalCharactersIndex);
				String remainingEdgePrefixValue = currentSequence.getEdgeValue().substring(equalCharactersIndex);
				String remainingWord = word.substring(equalCharactersIndex);
				// Store the current's edge current child node because we need it later.
				final PatriciaTreeNode oldCurrentEdgeChildNode = currentSequence.getChildNode();

				// Create the new child node for the current edge.
				PatriciaTreeNode newCurrentEdgeChildNode = new PatriciaTreeNode(usedAlphabet.getNodeArity(), false);

				// Create new edge with the remaining characters of the current edge's prefix as edge value.
				int indexForNewEdge = AlphabetHelper.getIndexForFirstCharOfWord(remainingEdgePrefixValue);
				PatriciaTreeEdge remainingPrefixEdge = new PatriciaTreeEdge(
						newCurrentEdgeChildNode,
						oldCurrentEdgeChildNode,
						remainingEdgePrefixValue, false, indexForNewEdge
				);

				// Add the new edge to the new child node of the current edge.
				newCurrentEdgeChildNode.addNewNonKeyEdge(remainingPrefixEdge, indexForNewEdge);
				// Add the new child node to the current edge.
				currentSequence.updateChildNode(newCurrentEdgeChildNode);
				// Set the common prefix of the inserted word and the previous prefix as value for the current edge.
				currentSequence.updateEdgeValue(commonPrefix);
				// Do the same insertion for the remaining characters of the word
				insertCharacterSequenceInTree(newCurrentEdgeChildNode, remainingWord);
			}
		}
	}

	@SuppressWarnings("null")
	@Override
	public void insert(ArrayList<String> words) {
		for (String word : words) {
			insert(word);
		}
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
