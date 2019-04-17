package dev.cryo.aip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import dev.cryo.aip.model.Obstacle;
import dev.cryo.aip.model.ObstacleParser;
import dev.cryo.aip.model.ParseException;

/**
 * This class creates an obstacle CSV file as specified by open flightmaps.
 * 
 * @author Dimitrios Vlastaras
 *
 */
public class OFMXParser {
	
	// If you add a country here, make sure to create the corresponding [CountryCode]ObstacleParser
	private static String[] supportedCountries = { "ES", "LG", "LK" };
	
	public static void main(String[] args) {
		
		// Due to pdfbox
		disableStderr();
		
		// Exit if no filename has been provided
		if (args.length != 3) {
			System.out.println("ERR: Usage: java -jar ofmx_parser.jar " + Arrays.toString(supportedCountries).replaceAll(",\\s", "|") + " <input_filename> <output_filename>");
			System.exit(1);
		}
		
		// Get the arguments
		String country = args[0];
		String inputFilename = args[1];
		String outputFilename = args[2];
		
		// Check if the country is supported
		if (!Arrays.asList(supportedCountries).contains(country)) {
			System.out.println("ERR: Country " + country + " is not supported! Select one from: " + Arrays.toString(supportedCountries));
			System.exit(1);
		}
		
		// Check if file exists
		File file = new File(inputFilename);
		if (!file.exists()) {
			System.out.println("ERR: File " + file.getName() + " does not exist!");
			System.exit(1);
		}
		
		// Create an obstacle parser for country
		ObstacleParser parser = null;
		try {
			Class<?> parserClass = java.lang.Class.forName("dev.cryo.aip.countries." + country + "ObstacleParser");
			parser = (ObstacleParser) parserClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("ERR: " + e.getLocalizedMessage());
			System.exit(1);
		}

		// Try to parse the file
		try {
			System.out.println("MSG: Loading " + file.getName() + "...");
			parser.loadFile(file);
			System.out.println("MSG: Parsing obstacles...");
			parser.parse();
		} catch (ParseException e) {
			System.out.println("ERR: " + e.getLocalizedMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println("ERR: Could not parse " + file.getName() + ": " + e.getLocalizedMessage());
			System.exit(1);
		}
		
		// Get all obstacles after successful parsing
		ArrayList<Obstacle> obstacles = parser.getObstacles();

		// Create the output .csv file
		System.out.println("MSG: Creating OFM obstacle file...");
		PrintWriter writer;
		try {
			writer = new PrintWriter(outputFilename, "UTF-8");
			writer.print("codeId,codeType,txtName,codeLgt,codeMarking,txtDescrLgt,txtDescrMarking,geoLat,geoLong,valGeoAccuracy,uomGeoAccuracy,valElev,valElevAccuracy,valHgt,codeHgtAccuracy,uomDistVer,valRadius,uomRadius,codeGroupId,txtGroupName,codeLinkedToId,codeLinkType,datetimeValidWef,datetimeValidTil,txtRmk,source\r\n");
			
			for (Obstacle obstacle : obstacles) {
				writer.print(obstacle.getOFMline() + "\r\n");
			}
			writer.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("ERR: File could not be written: " + e.getLocalizedMessage());
			System.exit(1);
		} catch (UnsupportedEncodingException e) {
			System.out.println("ERR: Unsupported encoding: " + e.getLocalizedMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println("ERR: " + e.getLocalizedMessage());
			System.exit(1);
		} 
		
		System.out.println("MSG: Saved OFM obstacle file to: " + outputFilename);
	}

	// Just a function to disable pdfbox prints to stderr as we need the program to be silent there
	private static void disableStderr() {
		System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
			@Override
			public void write(int b) { }
		}));
	}

}
