package fr.upmc.algav.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileWriter {

	private final String DRAWABLES_DIRECTORY = System.getProperty("user.dir") + "/triesalgav/drawables/";

	private String fileName;
	private String filePath;
	BufferedWriter output;
	
	public TextFileWriter(String fileName) {
		this.fileName = fileName;
		this.filePath = DRAWABLES_DIRECTORY + fileName;		

		try {
			FileWriter fileWriter = new FileWriter(filePath, false);
			this.output = new BufferedWriter(fileWriter);

            System.out.println("Creating new file \"" + fileName + "\"...");
		} catch (IOException ioe) {
			System.out.print("Couldn't create file \"" + fileName + "\" because of:");
			ioe.printStackTrace();
		}
	}
	
	public void write(String text) {
		try {						
			output.write(text);
			output.flush();
			System.out.print(".");			
		} catch (IOException ioe) {
			System.out.print("Couldn't write text \"" + text + "\"!");
			ioe.printStackTrace();
		}
	}
	
	public void close() {
		try {									
			output.close();
			System.out.println("\nClosing the file...");
			System.out.println("The file can be found in the directory: " + filePath);
			System.out.println("Use the following command to open it: xdot " + fileName);
			System.out.println("If xdot is is not installed, you can install it with this command: sudo apt install xdot");
		}
		catch(IOException ioe) {
			System.out.print("Could not close file because of:");
			ioe.printStackTrace();
		}
	}
}
