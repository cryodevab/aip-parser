package se.cryodev.aip;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

/**
 * This class creates an obstacle CSV file as specified by open flightmaps.
 * 
 * @author Dimitrios Vlastaras
 *
 */
public class CreateOFMAFile {

	public static void main(String[] args) throws InvalidPasswordException, ParseException, IOException {
		String country = "Sweden";
		String fileName = "ES_ENR_5_4_en.pdf";

		ObstacleParser parser;
		switch (country) {
		case "Sweden":
			parser = new SwedenObstacleParser();
			break;

		case "Austria":
			parser = new AustriaObstacleParser();
			break;

		case "Greece":
			parser = new GreeceObstacleParser();
			break;

		default:
			// Because there has to be a default...
			parser = new SwedenObstacleParser();
		}

		parser.loadFile(fileName);
		parser.parse();
		ArrayList<Obstacle> obstacles = parser.getObstacles();

		System.out.println("Creating OFMA obstacle file...");
		PrintWriter writer = new PrintWriter("obstacles-" + country.toLowerCase() + ".csv", "UTF-8");
		writer.println(
				"codeGroup,groupInternalId,name,type,lighted,markingDescription,heightUnit,heightValue,elevationValue,latutide,longitude,defaultHeightFlag,verticalPrecision,lateralPrecision,obstacleRadius,linkedToGroupInternalId,linkType");
		for (Obstacle obstacle : obstacles) {
			writer.println(obstacle.getOFMAline());
		}
		writer.close();

		System.out.println("All done! =)");

	}
}
