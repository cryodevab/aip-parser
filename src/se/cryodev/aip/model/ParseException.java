package se.cryodev.aip.model;

public class ParseException extends Exception {

	public ParseException(String fileName) {
		super("Failed miserably while parsing " + fileName + ". Please contact my master and let him fix me!");
	}

}
