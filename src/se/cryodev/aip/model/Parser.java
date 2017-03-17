package se.cryodev.aip.model;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

public interface Parser {
	public void loadFile(String fileName) throws InvalidPasswordException, IOException;

	public void parse() throws ParseException;
}
