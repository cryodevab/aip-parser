package se.cryodev.aipsweden;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.*;
import org.apache.pdfbox.util.*;

public class PDFTextParser {

	// Extract text from PDF Document
	static String pdftoText(String fileName) {
		PDFParser parser;
		String parsedText = null;
		
		
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File file = new File(fileName);
		if (!file.isFile()) {
			System.err.println("File " + fileName + " does not exist.");
			return null;
		}
		try {
			parser = new PDFParser(null, parsedText);
		} catch (IOException e) {
			System.err.println("Unable to open PDF Parser. " + e.getMessage());
			return null;
		}
		try {
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(5);
			parsedText = pdfStripper.getText(pdDoc);
		} catch (Exception e) {
			System.err.println("An exception occured in parsing the PDF Document." + e.getMessage());
		} finally {
			try {
				if (cosDoc != null)
					cosDoc.close();
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return parsedText;
	}

	public static void main(String args[]) {
		System.out.println(pdftoText("/home/santhosh/pdfbox/test.pdf"));
	}

}