package dev.cryo.aip.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import dev.cryo.aip.model.Obstacle;
import dev.cryo.aip.model.ParseException;

public abstract class ObstacleParser {
	protected File file;
	protected String textFromPDF;
	protected ArrayList<Obstacle> obstacles;

	public ObstacleParser() {
		this.obstacles = new ArrayList<Obstacle>();
	}
	
	public void loadTextFile(File file) {
		// Load the PDF file into text
		this.file = file;
	}
	
	public void loadPDFFile(File file) throws InvalidPasswordException, IOException {
		// Do the basic text file loading
		loadTextFile(file);
		
		// If the file is a PDF the do the PDF stuff
		String[] filenameParts = file.getName().split(".");
		String extension = filenameParts[filenameParts.length - 1];
		if (extension.equalsIgnoreCase("pdf")) {
			PDDocument pdf = PDDocument.load(file);
			textFromPDF = new PDFTextStripper().getText(pdf);
			pdf.close();			
		}
	}

	public abstract void parse() throws ParseException, IOException;

	public ArrayList<Obstacle> getObstacles() {
		return this.obstacles;
	}

	protected void fail() throws ParseException {
		throw new ParseException(file.getName());
	}
}
