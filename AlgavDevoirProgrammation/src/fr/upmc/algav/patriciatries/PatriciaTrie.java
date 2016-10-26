package fr.upmc.algav.patriciatries;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.interfaces.ITrie;

public class PatriciaTrie implements IPatriciaTrie {

	private PatriciaTreeNode rootNode;
	private final Alphabet usedAlphabet;
	private int keyCount;

	public PatriciaTrie(Alphabet usedAlphabet) {
		this.usedAlphabet = usedAlphabet;
		this.keyCount = 0;
		this.rootNode = new PatriciaTreeNode(usedAlphabet.getNodeArity(), false);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void insert(@NonNull String word) {
		if (isEmpty()) {
			// Tree is empty. Add node to root and store whole word in the according edge.
			rootNode.updateChild(AlphabetHelper.getFirstCharOfWord(word), word, Alphabet.END_OF_WORD_CHAR_ID);
		} else {

		}
	}
	
	@SuppressWarnings("null")
	@Override
	public void insert(@NonNull ArrayList<String> words) {
		for (String word : words) {
			insert(word);
		}
	}
	
	@Override
	public boolean search(@NonNull String word) {
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
	public int prefix(@NonNull String word) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ITrie remove(@NonNull String word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void print(@NonNull String FileName) {
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
