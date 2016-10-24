package fr.upmc.algav.interfaces;

import java.util.ArrayList;
import org.eclipse.jdt.annotation.NonNull;

public interface ITrie {
	
	// Basic primitives
	boolean isEmpty();
	void insert(@NonNull String word);
	void insert(@NonNull ArrayList<String> words);
	
	// Advanced functions
	boolean search(@NonNull String word);
	int countWords();
	ArrayList<String> listWords();
	int countNull();
	int height();
	float averageDepth();
	int prefix(@NonNull String word);
	ITrie remove(@NonNull String word);
	
	// Show visual preview
	void print(@NonNull String fileName);
}
