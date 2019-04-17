package dev.cryo.aip.countries;

import java.time.LocalDate;

import dev.cryo.aip.model.Obstacle;
import dev.cryo.aip.model.WGS84Coordinates;

public class ESObstacle extends Obstacle {

	public ESObstacle(
			String no,
			String name_of_obstacle, 
			String latitude, 
			String longitude, 
			String quality_not_certified,
			String height_ft, 
			String elevation_ft, 
			String light_character, 
			String type_of_obstacle,
			String wef
			) throws NumberFormatException {
		
		codeId = Integer.valueOf(no);
		txtName = name_of_obstacle.trim().toUpperCase();
		codeLgt = light_character.trim().isEmpty() ? "N" : "Y";
		codeMarking = "";
		txtDescrLgt = light_character.trim();
		txtDescrMarking = "";
		coordinates = new WGS84Coordinates(latitude.trim(), longitude.trim());
		valGeoAccuracy = 0;
		uomGeoAccuracy = "M";
		valElev = Integer.valueOf(elevation_ft);
		valElevAccuracy = 0;
		valHgt = Integer.valueOf(height_ft);
		codeHgtAccuracy = quality_not_certified.trim().isEmpty() ? "Y" : "N";
		uomDistVer = "FT";
		valRadius = type_of_obstacle.toLowerCase().contains("radius") ? Integer.valueOf(type_of_obstacle.replaceAll("[\\D.]", "")) : 0;
		uomRadius = "M";
		codeGroupId = "";
		txtGroupName = "";
		codeLinkedToId = "";
		codeLinkType = "";
		txtRmk = type_of_obstacle;
		
		// Convert time
		LocalDate fromDate = parseWef(wef);
		LocalDate toDate = fromDate.plusYears(2);
		datetimeValidWef = fromDate.toString() + "T00:00:00Z";
		datetimeValidTil = toDate.toString() + "T00:00:00Z";
		
		source = "ES|ENR|5.4|" + fromDate.toString() + "|CSV";
		
		// Search for the type of obstacle
		type_of_obstacle = type_of_obstacle.trim().toLowerCase();
		if (type_of_obstacle.contains("antenna")) {
			codeType = "ANTENNA";
		} else if (type_of_obstacle.contains("bridge")) { 
			codeType = "BRIDGE";
		} else if (type_of_obstacle.contains("building")) { 
			codeType = "BUILDING";
		} else if (type_of_obstacle.contains("chimney")) { 
			codeType = "CHIMNEY";
		} else if (type_of_obstacle.contains("crane")) { 
			codeType = "CRANE";
		} else if (type_of_obstacle.contains("mast")) { 
			codeType = "MAST";
		} else if (type_of_obstacle.contains("tower")) { 
			codeType = "TOWER";
		} else if (type_of_obstacle.contains("tree")) { 
			codeType = "TREE";
		} else if (type_of_obstacle.contains("turbine")) { 
			codeType = "WINDTURBINE";
		} else {
			codeType = "OTHER";
		}
		
	}

}
