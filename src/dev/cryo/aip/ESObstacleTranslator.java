package dev.cryo.aip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import dev.cryo.aip.control.ObstacleParser;
import dev.cryo.aip.model.Obstacle;
import dev.cryo.aip.model.ParseException;
import dev.cryo.aip.parsers.ESObstacleParser;

/**
 * This class creates an obstacle CSV file as specified by open flightmaps.
 * 
 * @author Dimitrios Vlastaras
 *
 */
public class ESObstacleTranslator {
	
	public static void main(String[] args) {
		
		// Exit if no filename has been provided
		if (args.length != 2) {
			System.err.println("Usage: java -jar es_translator.jar <input_filename> <output_filename>");
			System.exit(1);
		}
		
		// Check if file exists
		File file = new File(args[0]);
		if (!file.exists()) {
			System.err.println("File " + file.getName() + " does not exist!");
			System.exit(1);
		}
		
		// Create an obstacle parser for Sweden
		ObstacleParser parser = new ESObstacleParser();

		// Try to parse the file
		parser.loadTextFile(file);
		try {
			parser.parse();
		} catch (ParseException | IOException e) {
			System.err.println("Could not parse " + file.getName() + ": " + e.getLocalizedMessage());
			System.exit(1);
		}
		
		// Get all obstacles after successful parsing
		ArrayList<Obstacle> obstacles = parser.getObstacles();

		// Create the output .csv file
		System.out.println("Creating OFMA obstacle file...");
		PrintWriter writer;
		try {
			writer = new PrintWriter(args[1], "UTF-8");
			writer.print("codeId,codeType,txtName,codeLgt,codeMarking,txtDescrLgt,txtDescrMarking,geoLat,geoLong,valGeoAccuracy,uomGeoAccuracy,valElev,valElevAccuracy,valHgt,codeHgtAccuracy,uomDistVer,valRadius,uomRadius,codeGroupId,txtGroupName,codeLinkedToId,codeLinkType,datetimeValidWef,datetimeValidTil,txtRmk,source\r\n");
			
			for (Obstacle obstacle : obstacles) {
				writer.print(obstacle.getOFMAline() + "\r\n");
			}
			writer.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("File could not be written: " + e.getLocalizedMessage());
			System.exit(1);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported encoding: " + e.getLocalizedMessage());
			System.exit(1);
		}
		
		System.out.println("Saved OFMA obstacle file to: " + args[1]);
	}

}
