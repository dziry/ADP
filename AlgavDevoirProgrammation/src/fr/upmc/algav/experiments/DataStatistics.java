package fr.upmc.algav.experiments;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import fr.upmc.algav.tools.Reader;

/*
 * Statistics of the data set used in the experiments.
 */

public abstract class DataStatistics {

	private final static String config = "%-25s%-20s%-20s%-20s%-20s\n";
	private final static String directoryPath = "files/Shakespeare";
	private final static int numberOfFiles = new File(directoryPath).listFiles().length;	
	private final static HashMap<Integer, ArrayList<String>> shakespeareWordsList = new HashMap<Integer, ArrayList<String>>(numberOfFiles);
	
	public static void main(String[] args) {
		String arg1 = "File name";
		String arg2 = "Size of file(KB)";
		String arg3 = "number of words";
		String arg4 = "min words length";
		String arg5 = "max words length";
		
		putWordsInHashMapOfLists();
			
		System.out.format(config, arg1, arg2, arg3, arg4, arg5);
		System.out.println("------------------------------------------------------------------------------------------------------\n");
	    int fileIndex = 0;		   
	    final File folder = new File(directoryPath);
        for (final File fileEntry : folder.listFiles()) {
        	if (!fileEntry.isDirectory()) {
        		System.out.format("%-25s", fileEntry.getName());
                SizeOfDocument(fileEntry.getPath());
                numberOfWordsInFile(fileEntry.getPath());
                minWordLength(fileIndex);
                maxWordLength(fileIndex);
                fileIndex++;
                System.out.println("------------------------------------------------------------------------------------------------------");
        	}
        }
	}
	
	public static void putWordsInHashMapOfLists() {
		final File folder = new File(directoryPath);
		int index = 0;
		for (final File fileEntry : folder.listFiles()) {
			final String filePath = folder + "/" + fileEntry.getName();			
			Reader reader = new Reader(filePath);
			shakespeareWordsList.put(index++, reader.read());
		}
	}

	private static void SizeOfDocument(String filePath) {
		File file = new File(filePath.toString());
		double bytes = file.length();
		double kilobytes = (bytes / 1024);
		System.out.format("%-20.2f", kilobytes);
		
	}
	
	private static void numberOfWordsInFile(String filePath) {
		Reader reader = new Reader(filePath.toString());
		ArrayList<String> wordsList = new ArrayList<String>();		
		wordsList = reader.read();
		System.out.format("%-20d", wordsList.size());
	}

	private static void minWordLength(int fileIndex) {
		int minLength = 99;
		for (String word : shakespeareWordsList.get(fileIndex)) {
			if (word.length() < minLength) {
				minLength = word.length();
			}
		}
		System.out.format("%-20d", minLength);
	}
	
	private static void maxWordLength(int fileIndex) {
		int maxLength = 0;
		for (String word : shakespeareWordsList.get(fileIndex)) {
			if (word.length() > maxLength) {
				maxLength = word.length();
			}
		}
		System.out.format("%-20d\n", maxLength);
	}
}
