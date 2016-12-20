package fr.upmc.algav.experiments;

import java.io.*;
import java.util.ArrayList;

import fr.upmc.algav.tools.TextFileReader;

/*
 * Statistics of the data set used in the experiments.
 */

public class DataStatistics {

	private final static String CONFIG = "%-25s%-20s%-20s%-20s%-20s\n";
	private final static String DIRECTORY_PATH = "triesalgav/files/Shakespeare";

    private static final String SEPARATOR_LINE = "------------------------------------------------------------------";

    private final static String RESULTS_PATH = "triesalgav/results/data_statistics.txt";
    private final static String RESULTS_ENCODING = "utf-8";

	private static Writer resultsWriter;
	
	public static void main(String[] args) {
        initWriter();
        printTestBegin();


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
        writeToResultsFileFormatted(arg1, arg2, arg3, arg4, arg5);
		System.out.println("------------------------------------------------------------------------------------------------------\n");
        writeToResultsFile("\n------------------------------------------------------------------------------------------------------\n");

		int fileIndex = 0;

        for (final File fileEntry : directoryFile.listFiles()) {
        	if (!fileEntry.isDirectory()) {
        		System.out.format("%-25s", fileEntry.getName());
                writeToResultsFileFormatted("%-25s", fileEntry.getName());

				SizeOfDocument(fileEntry.getPath());
                numberOfWordsInFile(fileEntry.getPath());
                minWordLength(shakespeareWordsList, fileIndex);
                maxWordLength(shakespeareWordsList, fileIndex);

				fileIndex++;

				System.out.println("------------------------------------------------------------------------------------------------------");
        	    writeToResultsFile("------------------------------------------------------------------------------------------------------\n");
        	}
        }

        printTestEnd();
	}

    private static void initWriter() {
        resultsWriter = null;

        try {
            resultsWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(RESULTS_PATH), RESULTS_ENCODING));
        } catch (IOException ex) {
            System.err.println("Error while initializing results writer!");
            ex.printStackTrace();
        }
    }

    private static void writeToResultsFile(String stringToWrite) {
        try {
            resultsWriter.write(stringToWrite);
        } catch (Exception e) {
            System.err.println("Error while writing to results file!");
            e.printStackTrace();
        }
    }

    private static void writeToResultsFileFormatted(String arg1, String arg2,
                                                    String arg3, String arg4, String arg5) {
        try {
            resultsWriter.write(String.format(CONFIG, arg1, arg2, arg3, arg4, arg5));
        } catch (Exception e) {
            System.err.println("Error while writing to results file!");
            e.printStackTrace();
        }
    }

    private static void writeToResultsFileFormatted(String config, String text) {
        try {
            resultsWriter.write(String.format(config, text));
        } catch (Exception e) {
            System.err.println("Error while writing to results file!");
            e.printStackTrace();
        }
    }

    private static void printTestBegin() {
        String begin =  "BEGIN OF DATA STATISTICS\n" +
                SEPARATOR_LINE + "\n" +
                SEPARATOR_LINE + "\n" +
                SEPARATOR_LINE + "\n\n";

        //System.out.println(begin);
        writeToResultsFile(begin);
    }

    private static void printTestEnd() {
        String end =    "\n" + SEPARATOR_LINE + "\n" +
                SEPARATOR_LINE + "\n" +
                SEPARATOR_LINE + "\n" +
                "END OF DATA STATISTICS";

        //System.out.println(end);
        writeToResultsFile(end);

        try {
            resultsWriter.close();
        } catch (Exception ex) {
            System.err.println("Error while closing results file writer!");
            ex.printStackTrace();
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
        writeToResultsFileFormatted("%-20s", Double.toString(kilobytes));
	}
	
	private static void numberOfWordsInFile(String filePath) {
		TextFileReader textFileReader = new TextFileReader(filePath);
		ArrayList<String> wordsList = textFileReader.read();

		System.out.format("%-20d", wordsList.size());
        writeToResultsFileFormatted("%-20s", Integer.toString(wordsList.size()));
	}

	private static void minWordLength(ArrayList<ArrayList<String>> wordsList, int fileIndex) {
		int minLength = 99;

		for (String word : wordsList.get(fileIndex)) {
			if (word.length() < minLength) {
				minLength = word.length();
			}
		}

		System.out.format("%-20d", minLength);
        writeToResultsFileFormatted("%-20s", Integer.toString(minLength));
	}
	
	private static void maxWordLength(ArrayList<ArrayList<String>> wordsList, int fileIndex) {
		int maxLength = 0;

		for (String word : wordsList.get(fileIndex)) {
			if (word.length() > maxLength) {
				maxLength = word.length();
			}
		}

		System.out.format("%-20d\n", maxLength);
        writeToResultsFileFormatted("%-20s\n", Integer.toString(maxLength));
	}
}
