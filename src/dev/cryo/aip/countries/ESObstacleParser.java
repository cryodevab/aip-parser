package dev.cryo.aip.countries;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import dev.cryo.aip.model.ObstacleParser;
import dev.cryo.aip.model.ParseException;

public class ESObstacleParser extends ObstacleParser {

	public void parse() throws ParseException, IOException {
		
		// Parse the text of the CSV file
		boolean titleFound = false;
		String wef = "";

		List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.ISO_8859_1);
		try {
			for (String line : lines) {

				if (wef.isEmpty() && line.contains("WEF:")) {
					String[] wefArr = line.split(":");
					
					if (wefArr.length != 2) fail();
					
					wef = wefArr[1].trim();
				}
				
				// Look for the starting line of the CSV data			
				if (!titleFound) {
					if (line.equals("NO;NAME_OF_OBSTACLE;LATITUDE;LONGITUDE;QUALITY_NOT_CERTIFIED;HEIGHT_FT;ELEVATION_FT;LIGHT_CHARACTER;TYPE_OF_OBSTACLE")) { 
						titleFound = true;
					}
					continue;
				}
	
				// Avoid empty lines like the last one
				if (line.trim().isEmpty()) {
					continue;
				}
				
				// By the time we get here we must have WEF
				if (wef.isEmpty()) fail();
				
				// Check if the line has the right amount of elements
				String[] lineData = line.split(";", -1);
				if (lineData.length != 9) fail();
				
				obstacles.add(new ESObstacle(
						lineData[0],
						lineData[1],
						lineData[2],
						lineData[3],
						lineData[4],
						lineData[5],
						lineData[6],
						lineData[7],
						lineData[8],
						wef
						));
				
			}
		} catch (NumberFormatException e) {
			fail();
		}
		
		if (!titleFound) {
			fail();
		}
	}

}