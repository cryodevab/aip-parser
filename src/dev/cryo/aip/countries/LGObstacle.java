package dev.cryo.aip.countries;

import java.time.LocalDate;

import dev.cryo.aip.model.Obstacle;
import dev.cryo.aip.model.WGS84Coordinates;

public class LGObstacle extends Obstacle {

	public LGObstacle(
			int obstacleId,
			String designation,
			String type,
			String lat,
			String lon,
			String elev_m,
			String height_m,
			String lighted,
			String remarks,
			String wef
			) throws NumberFormatException {
		
		codeId = obstacleId;
		codeLgt = lighted.toLowerCase().contains("yes") ? "Y" : "N";
		codeMarking = "";
		txtDescrLgt = "";
		txtDescrMarking = "";
		coordinates = new WGS84Coordinates(lat, lon);
		valGeoAccuracy = 0;
		uomGeoAccuracy = "M";
		valElev = Integer.valueOf(elev_m);
		valElevAccuracy = 0;
		valHgt = Integer.valueOf(height_m);
		codeHgtAccuracy = "Y";
		uomDistVer = "M";
		valRadius = 0;
		uomRadius = "M";
		txtRmk = remarks;
		codeGroupId = "";
		txtGroupName = "";
		codeLinkedToId = "";
		codeLinkType = "";
		
		// Fix the elevation/height switcheroo (water bridges and such)
		if (valElev > 0 && valHgt == 0) {
			valHgt = valElev;
			valElev = 0;
		}
		
		// Convert time
		LocalDate fromDate = parseWef(wef);
		LocalDate toDate = fromDate.plusYears(2);
		datetimeValidWef = fromDate.toString() + "T00:00:00Z";
		datetimeValidTil = toDate.toString() + "T00:00:00Z";
		
		// The date does not match AIRAC date since it is not provided in the CSN file
		source = "LG|ENR|5.4|" + fromDate.toString() + "|PDF";
		
		// Search for the type of obstacle
		type = type.toLowerCase();
		if (type.matches(".*antenna.*")) { // OK
			codeType = "ANTENNA";
		} else if (type.matches(".*bridge.*")) { // OK
			codeType = "BRIDGE";
		} else if (type.matches(".*house.*")) { // OK
			codeType = "BUILDING";
		} else if (type.matches("(.*chimney.*|.*smoke.*)")) { // OK
			codeType = "CHIMNEY";
		} else if (type.matches(".*crane.*")) { // No samples
			codeType = "CRANE";
		} else if (type.matches(".*mast.*")) { // OK 
			codeType = "MAST";
		} else if (type.matches(".*tower.*")) { // OK
			codeType = "TOWER";
		} else if (type.matches(".*tree.*")) { // No samples
			codeType = "TREE";
		} else if (type.matches(".*wind.*")) { // OK 
			codeType = "WINDTURBINE";
		} else {
			codeType = "OTHER";
		}
		
		txtName = codeType;
		
	}

}
