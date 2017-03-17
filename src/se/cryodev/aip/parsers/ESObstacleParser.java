package se.cryodev.aip.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import se.cryodev.aip.model.Obstacle;
import se.cryodev.aip.model.ObstacleParser;
import se.cryodev.aip.model.ParseException;
import se.cryodev.aip.model.WGS84Coordinates;

public class ESObstacleParser extends ObstacleParser {

	public void parse() throws ParseException {
		// Parse the text of the PDF file using regular expressions
		String[] lines = textAIP.split(System.lineSeparator());
		Pattern areaPattern = Pattern.compile("\\d{2}[NS]\\s\\d{2,3}[EW]");
		Pattern nrPattern = Pattern.compile("\\d{1,5}");
		Pattern designationPattern = Pattern.compile(".*\\d{6}\\.?\\d?[NS]");
		Pattern coordinatesPattern = Pattern.compile("\\d{6}\\.?\\d?[NS]\\s*\\d{6,7}\\.?\\d?[EW]");
		Pattern heightPattern = Pattern.compile("\\d{1,5}");
		Pattern elevPattern = Pattern.compile("\\d{1,5}");
		Pattern lightPattern = Pattern.compile("[FLRGW/\\s-]*\\s");

		int number = 0;
		String designation = "";
		WGS84Coordinates coordinates = null;
		int height = 0;
		int elev = 0;
		String light = "";
		String type = "";

		System.out.println("Parsing obstacles...");
		for (String line : lines) {

			Matcher coordinatesMatcher = coordinatesPattern.matcher(line);
			if (coordinatesMatcher.find()) {
				// Remove multiple spaces and other crap from the line
				line = line.replaceAll("\\s+", " ");
				line = line.replaceAll("N\\.", "N");
				line = line.replaceAll("S\\.", "S");
				line = line.replaceAll("Ö\\.", "Ö");
				line = line.replaceAll("V\\.", "V");
				line = line.replaceAll("E\\.", "E");
				line = line.replaceAll("W\\.", "W");
				line = line.replaceFirst("\\s\\(\\*\\)\\s", " ");

				// Remove area information in the beginning (not all lines have
				// it)
				Matcher areaMatcher = areaPattern.matcher(line);
				if (areaMatcher.find())
					line = line.substring(areaMatcher.end());

				// Remove leading space (all lines have it)
				line = line.trim();

				// Pop the number from the line
				Matcher nrMatcher = nrPattern.matcher(line);
				if (nrMatcher.find()) {
					String tmp = line.substring(nrMatcher.start(), nrMatcher.end());
					line = line.substring(tmp.length() + 1);
					number = Integer.valueOf(tmp);
				} else
					fail();

				// Pop the designation from the line
				Matcher designationMatcher = designationPattern.matcher(line);
				if (designationMatcher.find()) {
					designation = line.substring(designationMatcher.start(), designationMatcher.end());

					if (designation.contains("."))
						designation = designation.substring(0, designation.length() - 10);
					else
						designation = designation.substring(0, designation.length() - 8);
					line = line.substring(designation.length() + 1);
				} else
					fail();

				// Pop the coordinates from the line
				coordinatesMatcher = coordinatesPattern.matcher(line);
				if (coordinatesMatcher.find()) {
					String tmp = line.substring(coordinatesMatcher.start(), coordinatesMatcher.end());
					line = line.substring(tmp.length() + 1);

					float latDeg = 0, latMin = 0, latSec = 0, lonDeg = 0, lonMin = 0, lonSec = 0, lat = 0, lon = 0;
					if (tmp.length() == 16) {
						// 551212N 0131212E
						latDeg = Float.valueOf(tmp.substring(0, 2));
						latMin = Float.valueOf(tmp.substring(2, 4));
						latSec = Float.valueOf(tmp.substring(4, 6));

						lonDeg = Float.valueOf(tmp.substring(8, 11));
						lonMin = Float.valueOf(tmp.substring(11, 13));
						lonSec = Float.valueOf(tmp.substring(13, 15));

						lat = latDeg + latMin / 60 + latSec / 3600;
						lon = lonDeg + lonMin / 60 + lonSec / 3600;

						if (tmp.substring(6, 7).equals("S"))
							lat = -lat;
						if (tmp.substring(15, 16).equals("W"))
							lon = -lon;
					} else {
						// 551212.0N 0131212.0E
						latDeg = Float.valueOf(tmp.substring(0, 2));
						latMin = Float.valueOf(tmp.substring(2, 4));
						latSec = Float.valueOf(tmp.substring(4, 8));

						lonDeg = Float.valueOf(tmp.substring(10, 13));
						lonMin = Float.valueOf(tmp.substring(13, 15));
						lonSec = Float.valueOf(tmp.substring(15, 19));

						lat = latDeg + latMin / 60 + latSec / 3600;
						lon = lonDeg + lonMin / 60 + lonSec / 3600;

						if (tmp.substring(8, 9).equals("S"))
							lat = -lat;
						if (tmp.substring(19, 20).equals("W"))
							lon = -lon;
					}

					coordinates = new WGS84Coordinates(lat, lon);
				} else
					fail();

				// Pop the height from the line
				Matcher heightMatcher = heightPattern.matcher(line);
				if (heightMatcher.find()) {
					String tmp = line.substring(heightMatcher.start(), heightMatcher.end());
					line = line.substring(tmp.length() + 1);
					height = Integer.valueOf(tmp);
				} else
					fail();

				// Pop the elevation from the line
				Matcher elevMatcher = elevPattern.matcher(line);
				if (elevMatcher.find()) {
					String tmp = line.substring(elevMatcher.start(), elevMatcher.end());
					line = line.substring(tmp.length() + 1);
					elev = Integer.valueOf(tmp);
				} else
					fail();

				// Pop the light character from the line
				Matcher lightMatcher = lightPattern.matcher(line);
				if (lightMatcher.find()) {
					light = line.substring(lightMatcher.start(), lightMatcher.end() - 1);
					line = line.substring(light.length() + 1);
				} else
					fail();

				// And finally the only thing remaining in the line is the type
				type = line;

				obstacles.add(new Obstacle(number, designation, coordinates, height, elev, light, type));
			}
		}
	}

}