package fr.upmc.algav.experiments;

import java.io.File;
import java.util.ArrayList;

import fr.upmc.algav.tools.TextFileReader;

/*
 * Statistics of the data set used in the experiments.
 */

public class DataStatistics {

	private final static String CONFIG = "%-25s%-20s%-20s%-20s%-20s\n";
	// Eclipse IDE
	private final static String DIRECTORY_PATH = "files/Shakespeare";
	// IntelliJ IDE
	//private final static String DIRECTORY_PATH = "AlgavDevoirProgrammation/files/Shakespeare";
	
	public static void main(String[] args) {
		File directoryFile = new File(DIRECTORY_PATH);
		File[] subFiles = directoryFile.listFiles();
		int numberOfFiles = subFiles == null ? 0 : subFiles.length;

		String arg1 = "File name";
		String arg2 = "Size of file(KB)";
		String arg3 = "Number of words";
		String arg4 = "Min words length";
		String arg5 = "Max words length";

		ArrayList<ArrayList<String>> shakespeareWordsList = getShakespeareWordsList(directoryFile, numberOfFiles);
			
		System.out.format(CONFIG, arg1, arg2, arg3, arg4, arg5);
		System.out.println("------------------------------------------------------------------------------------------------------\n");

		int fileIndex = 0;

        for (final File fileEntry : directoryFile.listFiles()) {
        	if (!fileEntry.isDirectory()) {
        		System.out.format("%-25s", fileEntry.getName());

				SizeOfDocument(fileEntry.getPath());
                numberOfWordsInFile(fileEntry.getPath());
                minWordLength(shakespeareWordsList, fileIndex);
                maxWordLength(shakespeareWordsList, fileIndex);

				fileIndex++;

				System.out.println("------------------------------------------------------------------------------------------------------");
        	}
        }
	}
	
	private static ArrayList<ArrayList<String>> getShakespeareWordsList(File file, int numberOfFiles) {
		ArrayList<ArrayList<String>> res = new ArrayList<>(numberOfFiles);

		for (final File fileEntry : file.listFiles()) {
			final String filePath = file + "/" + fileEntry.getName();
			TextFileReader textFileReader = new TextFileReader(filePath);
			res.add(textFileReader.read());
		}

		return res;
	}

	private static void SizeOfDocument(String filePath) {
		File file = new File(filePath);

		double bytes = file.length();
		double kilobytes = (bytes / 1024);

		System.out.format("%-20.2f", kilobytes);
	}
	
	private static void numberOfWordsInFile(String filePath) {
		TextFileReader textFileReader = new TextFileReader(filePath);
		ArrayList<String> wordsList = textFileReader.read();

		System.out.format("%-20d", wordsList.size());
	}

	private static void minWordLength(ArrayList<ArrayList<String>> wordsList, int fileIndex) {
		int minLength = 99;

		for (String word : wordsList.get(fileIndex)) {
			if (word.length() < minLength) {
				minLength = word.length();
			}
		}

		System.out.format("%-20d", minLength);
	}
	
	private static void maxWordLength(ArrayList<ArrayList<String>> wordsList, int fileIndex) {
		int maxLength = 0;

		for (String word : wordsList.get(fileIndex)) {
			if (word.length() > maxLength) {
				maxLength = word.length();
			}
		}

		System.out.format("%-20d\n", maxLength);
	}
}
