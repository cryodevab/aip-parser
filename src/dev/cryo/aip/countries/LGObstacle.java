package dev.cryo.aip.countries;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
		
		this.codeId = obstacleId;
		this.codeLgt = lighted.toLowerCase().contains("yes") ? "Y" : "N";
		this.codeMarking = "";
		this.txtDescrLgt = "";
		this.txtDescrMarking = "";
		this.coordinates = new WGS84Coordinates(lat, lon);
		this.valGeoAccuracy = 0;
		this.uomGeoAccuracy = "M";
		this.valElev = Integer.valueOf(elev_m);
		this.valElevAccuracy = 0;
		this.valHgt = Integer.valueOf(height_m);
		this.codeHgtAccuracy = "Y";
		this.uomDistVer = "M";
		this.valRadius = 0;
		this.uomRadius = "M";
		this.txtRmk = remarks;
		this.codeGroupId = "";
		this.txtGroupName = "";
		this.codeLinkedToId = "";
		this.codeLinkType = "";
		
		// Convert time (horrible APIs require horrible solutions)
		String[] months = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
		for (int i = 0; i <= 11; i++) {
			if (wef.contains(months[i])) {
				wef = wef.replaceFirst(months[i], i + 1 + "");
				break;
			}
		}
		LocalDate fromDate = LocalDate.parse(wef, DateTimeFormatter.ofPattern("d M yyyy"));
		LocalDate toDate = fromDate.plusYears(2);
		this.datetimeValidWef = fromDate.toString() + "T00:00:00Z";
		this.datetimeValidTil = toDate.toString() + "T00:00:00Z";
		
		// The date does not match AIRAC date since it is not provided in the CSN file
		this.source = "LG|ENR|5.4|" + fromDate.toString() + "|PDF";
		
		// Search for the type of obstacle
		type = type.toLowerCase();
		if (type.matches(".*antenna.*")) { // OK
			this.codeType = "ANTENNA";
		} else if (type.matches(".*bridge.*")) { // OK
			this.codeType = "BRIDGE";
		} else if (type.matches(".*house.*")) { // OK
			this.codeType = "BUILDING";
		} else if (type.matches("(.*chimney.*|.*smoke.*)")) { // OK
			this.codeType = "CHIMNEY";
		} else if (type.matches(".*crane.*")) { // No samples
			this.codeType = "CRANE";
		} else if (type.matches(".*mast.*")) { // OK 
			this.codeType = "MAST";
		} else if (type.matches(".*tower.*")) { // OK
			this.codeType = "TOWER";
		} else if (type.matches(".*tree.*")) { // No samples
			this.codeType = "TREE";
		} else if (type.matches(".*wind.*")) { // OK 
			this.codeType = "WINDTURBINE";
		} else {
			this.codeType = "OTHER";
		}
		
		this.txtName = this.codeType;
		
	}

}
