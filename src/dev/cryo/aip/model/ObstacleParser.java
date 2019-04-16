package dev.cryo.aip.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

public abstract class ObstacleParser {
	protected File file;
	protected String textFromPDF;
	protected ArrayList<Obstacle> obstacles;

	protected ObstacleParser() {
		this.obstacles = new ArrayList<Obstacle>();
	}
	
	public void loadFile(File file) throws InvalidPasswordException, IOException {
		// Load the PDF file into text
		this.file = file;
		
		// If the file is a PDF the do the PDF stuff
		boolean isPDF = false;
		FileReader reader = new FileReader(file);
		Scanner scanner = new Scanner(reader);
		while (scanner.hasNextLine()) {
			if (scanner.nextLine().contains("%PDF-")) {
				isPDF = true;
				break;
			}
		}
		scanner.close();
		reader.close();
		
		if (isPDF) {
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
