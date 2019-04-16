package dev.cryo.aip.countries;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.cryo.aip.countries.LGObstacle;
import dev.cryo.aip.model.ObstacleParser;
import dev.cryo.aip.model.ParseException;

public class LGObstacleParser extends ObstacleParser {

	public void parse() throws ParseException, IOException {
		
		// Check whether there was any text in the PDF or not
		if (textFromPDF.isEmpty()) {
			throw new IOException("Use the AIP ENR 5.4 .pdf file for Greece!");
		}
		
		// Remove painful things to match https://regex101.com/r/4qN23U/11
		textFromPDF = textFromPDF.replaceAll("\\([IV\\-D35]+\\)", " "); // Trash
		textFromPDF = textFromPDF.replaceAll("\\-", " "); // Remove all dashes
		textFromPDF = textFromPDF.replaceAll("[\\(\\)]", " "); // Remove all parentheses
		textFromPDF = textFromPDF.replaceAll("[,;]", " "); // Remove commas and semicolons
		textFromPDF = textFromPDF.replaceAll("\\h+/", "/").replaceAll("/\\h+", "/"); // Clean up dashes
		textFromPDF = textFromPDF.replaceAll("\\u039D", "N"); // Replace Greek and Cyrillic chars 
		textFromPDF = textFromPDF.replaceAll("[\\u0395\\u0415\\uA5CB]", "E"); // Replace Greek and Cyrillic chars 
		textFromPDF = textFromPDF.replaceAll("NIL", "0"); // Replace NIL with 0 
		textFromPDF = textFromPDF.replaceAll("\\v+", "\n"); // Make sure all newlines are the same 
		textFromPDF = textFromPDF.replaceAll("\\h+", " "); // Make sure all white spaces are the same
		
		// Find the WEF (With Effect From) date
		Pattern wefP = Pattern.compile("(\\d{1,2}\\s(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\\s20\\d{2})");
		Matcher wefM = wefP.matcher(textFromPDF);
		String wef;
		if (wefM.find()) {
			wef = wefM.group(1);
		} else {
			throw new IOException("Could not find WEF date!");
		}

		// Regex on steroids to find a valid line using the appropriate groups
		String regex = "(.*) (\\d{6})[\\. ]*(\\d+)(\\.\\d+)? ?[NESW]? ?(0\\d{6}|[1-9]\\d{5})[\\. ]*(\\d+)(\\.\\d+)? ?[NESW]?.? (\\d+)(\\.\\d+)?[ \\./]+(\\d+)(\\.\\d+)? (No|Yes)";
		
		Pattern obstacleLineP = Pattern.compile(regex);
		Matcher obstacleLineM = obstacleLineP.matcher(textFromPDF);
		
		int obstacleId = 0;
		
		try {
			while (obstacleLineM.find()) {
				
				obstacleId++;
				
				String designation = "OBSTACLE " + obstacleId;
				
				String type = obstacleLineM.group(1).trim();
				
				// Get the latitude and fix Greek AIP formating errors
				String lat = obstacleLineM.group(2) + "." + obstacleLineM.group(3) + "N";
					
				// Get the longitude and fix Greek AIP formating errors
				String lon = obstacleLineM.group(5) + "." + obstacleLineM.group(6) + "E";
				if (lon.length() == 10 && lon.charAt(0) != '0') lon = "0" + lon;
					
				// Get the elevation in meters
				String elev_m = obstacleLineM.group(8);
				
				// Get the height in meters
				String height_m = obstacleLineM.group(10);
				
				// Get light status
				String lighted = obstacleLineM.group(12);
				
				// Remarks
				String remarks = "";
				
				obstacles.add(new LGObstacle(
						obstacleId,
						designation,
						type,
						lat,
						lon,
						elev_m,
						height_m,
						lighted,
						remarks,
						wef
						));
			}
		} catch (NumberFormatException e) {
			fail();
		}
		
	}

}