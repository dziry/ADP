package fr.upmc.algav.tools;

import java.util.HashMap;

import org.eclipse.jdt.annotation.NonNull;

import fr.upmc.algav.hybridtries.HybridTrieNode;

public class Printer {

	private static Writer fileObject;
	private static HashMap<Color, String> color;
	private static HashMap<Style, String> style;
	
	public Printer(@NonNull String fileName) {
		Printer.fileObject = new Writer(fileName);
		color = new HashMap<Color, String>();
		style = new HashMap<Style, String>();
		
		Printer.color.put(Color.BLUE, "blue");
		Printer.color.put(Color.RED, "red");
		Printer.color.put(Color.GREEN, "green");
		Printer.color.put(Color.BLACK, "black");
		Printer.color.put(Color.DEFAULT, "default");
		
		Printer.style.put(Style.SOLID, "solid");
		Printer.style.put(Style.DASHED, "dashed");
		Printer.style.put(Style.DOTTED, "dotted");
		Printer.style.put(Style.BOLD, "bold");
		Printer.style.put(Style.ROUNDED, "rounded");
		Printer.style.put(Style.DIAGONALS, "diagonals");
		Printer.style.put(Style.FILLED, "filled");
		Printer.style.put(Style.DEFAULT, "default");
	}

	public void begin() {
		fileObject.write("digraph G {\n");
	}
	
	public void end() {
		fileObject.write("}");
		fileObject.close();
	}
	
	public void printNode(HybridTrieNode node) {
		fileObject.write("	\"" + node.getId() + "\";\n");
	}
	
	public void printNode(HybridTrieNode node, Color color, Color fontColor, Style style) {
		fileObject.write("	\"" + node.getId() + 
				         "\" [color=" + Printer.color.get(color) + ", " + 
			             "fontcolor=" + Printer.color.get(fontColor) + ", " + 
				             "style=" + Printer.style.get(style) + "];\n");
	}
	
	public void printNodeLabel(HybridTrieNode node) {
		fileObject.write("	\"" + node.getId() + "\" [label=\""  + node.getId().charAt(0) + "\"];\n");
	}
	
	public void printEdge(HybridTrieNode startNode, HybridTrieNode arriveNode, Color color) {
		fileObject.write("	\"" + startNode.getId() + 
				     "\" -> \"" + arriveNode.getId() + 
				   "\" [color=" + Printer.color.get(color) + "];\n");
	}		
}
