package dev.cryo.aip.model;

public class ParseException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParseException(String fileName) {
		super("Failed miserably while parsing " + fileName + ". Please contact my master and let him fix me!");
	}

}
