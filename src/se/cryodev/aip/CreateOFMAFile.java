package se.cryodev.aipsweden;

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
public class OFMAObstacleFile {

	public static void main(String[] args) throws InvalidPasswordException, ParseException, IOException {

		ArrayList<Obstacle> obstacles = new ObstacleFactory("ES_ENR_5_4_en.pdf").getObstacles();
		
		System.out.println("Creating OFMA obstacle file...");
		PrintWriter writer = new PrintWriter("obstacles-sweden.csv", "UTF-8");
		writer.println("codeGroup,groupInternalId,name,type,lighted,markingDescription,heightUnit,heightValue,elevationValue,latutide,longitude,defaultHeightFlag,verticalPrecision,lateralPrecision,obstacleRadius,linkedToGroupInternalId,linkType");
		for (Obstacle obstacle : obstacles) {
			writer.println(obstacle.getOFMAline());
		}
		writer.close();

		System.out.println("All done! =)");

	}
}
