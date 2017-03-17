package se.cryodev.aip.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

public abstract class ObstacleParser {
	private String fileName;
	protected String textAIP;
	protected ArrayList<Obstacle> obstacles;

	public ObstacleParser() {
		this.obstacles = new ArrayList<Obstacle>();
	}

	public void loadFile(String fileName) throws InvalidPasswordException, IOException {
		// Load the PDF file into text
		this.fileName = fileName;
		System.out.println("Loading " + fileName + "...");
		PDDocument pdf = PDDocument.load(new File(fileName));
		textAIP = new PDFTextStripper().getText(pdf);
		pdf.close();
	}

	public abstract void parse() throws ParseException;

	public ArrayList<Obstacle> getObstacles() {
		return this.obstacles;
	}

	protected void fail() throws ParseException {
		throw new ParseException(fileName);
	}
}
