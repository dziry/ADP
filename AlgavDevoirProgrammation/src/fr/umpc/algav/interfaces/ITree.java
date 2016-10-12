package fr.umpc.algav.interfaces;

import java.util.ArrayList;

public interface ITree {
	
	// Basic primitives
	boolean isEmpty();
	ITree insert(String word);
	
	// Advanced functions
	boolean search(String word);
	int countWords();
	ArrayList<String> listWords();
	int countNull();
	int height();
	float averageDepth();
	int prefix(String word);
	ITree remove(String word);
	
	// Show visual preview
	void display(String value);
}
