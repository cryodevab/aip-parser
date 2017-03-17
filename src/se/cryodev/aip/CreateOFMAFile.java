package se.cryodev.aip;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import se.cryodev.aip.parsers.LGObstacleParser;
import se.cryodev.aip.parsers.LOObstacleParser;
import se.cryodev.aip.model.Obstacle;
import se.cryodev.aip.model.ObstacleParser;
import se.cryodev.aip.model.ParseException;
import se.cryodev.aip.parsers.ESObstacleParser;

/**
 * This class creates an obstacle CSV file as specified by open flightmaps.
 * 
 * @author Dimitrios Vlastaras
 *
 */
public class CreateOFMAFile {

	public static void main(String[] args) throws InvalidPasswordException, ParseException, IOException {
		String country = "ES";

		ObstacleParser parser;
		switch (country) {
		case "ES":
			parser = new ESObstacleParser();
			break;

		case "LO":
			parser = new LOObstacleParser();
			break;

		case "LG":
			parser = new LGObstacleParser();
			break;

		default:
			// Because there has to be a default...
			parser = new ESObstacleParser();
		}

		parser.loadFile("aip-files/" + country + "_ENR_5_4.pdf");
		parser.parse();
		ArrayList<Obstacle> obstacles = parser.getObstacles();

		System.out.println("Creating OFMA obstacle file...");
		PrintWriter writer = new PrintWriter("ofma-files/" + country + "_obstacles.csv", "UTF-8");
		writer.println(
				"codeGroup,groupInternalId,name,type,lighted,markingDescription,heightUnit,heightValue,elevationValue,latutide,longitude,defaultHeightFlag,verticalPrecision,lateralPrecision,obstacleRadius,linkedToGroupInternalId,linkType");
		for (Obstacle obstacle : obstacles) {
			writer.println(obstacle.getOFMAline());
		}
		writer.close();
		System.out.println("Saved OFMA obstacle file to: ofma-files/" + country + "_obstacles.csv");

	}
}
