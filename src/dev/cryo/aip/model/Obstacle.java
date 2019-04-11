package dev.cryo.aip.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Obstacle {

	private int codeId;
	private String codeType;
	private String txtName;
	private String codeLgt;
	private String codeMarking;
	private String txtDescrLgt;
	private String txtDescrMarking;
	private WGS84Coordinates coordinates;
	private int valGeoAccuracy;
	private String uomGeoAccuracy;
	private int valElev;
	private int valElevAccuracy;
	private int valHgt;
	private String codeHgtAccuracy;
	private String uomDistVer;
	private int valRadius;
	private String uomRadius;
	private String codeGroupId;
	private String txtGroupName;
	private String codeLinkedToId;
	private String codeLinkType;
	private String datetimeValidWef;
	private String datetimeValidTil;
	private String txtRmk;
	private String source;
	
	public Obstacle(
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
		
		this.codeId = Integer.valueOf(no);
		this.txtName = name_of_obstacle.trim().toUpperCase();
		this.codeLgt = light_character.trim().isEmpty() ? "N" : "Y";
		this.codeMarking = "";
		this.txtDescrLgt = light_character.trim();
		this.txtDescrMarking = "";
		this.coordinates = new WGS84Coordinates(latitude.trim(), longitude.trim());
		this.valGeoAccuracy = 0;
		this.uomGeoAccuracy = "M";
		this.valElev = Integer.valueOf(elevation_ft);
		this.valElevAccuracy = 0;
		this.valHgt = Integer.valueOf(height_ft);
		this.codeHgtAccuracy = quality_not_certified.trim().isEmpty() ? "Y" : "N";
		this.uomDistVer = "FT";
		this.valRadius = type_of_obstacle.toLowerCase().contains("radius") ? Integer.valueOf(type_of_obstacle.replaceAll("[\\D.]", "")) : 0;
		this.uomRadius = "M";
		this.codeGroupId = "";
		this.txtGroupName = "";
		this.codeLinkedToId = "";
		this.codeLinkType = "";
		this.txtRmk = type_of_obstacle;
		
		// Convert time (horrible APIs require horrible solutions)
		String[] months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		for (int i = 0; i <= 11; i++) {
			if (wef.contains(months[i])) {
				wef = wef.replaceFirst(months[i], i + 1 + "");
				break;
			}
		}
		LocalDate fromDate = LocalDate.parse(wef, DateTimeFormatter.ofPattern("d M yyyy"));
		LocalDate toDate = fromDate.plusYears(2);
		this.datetimeValidWef = fromDate.toString() + "T00:00:00Z";
		this.datetimeValidTil = toDate.toString() + "T23:59:59Z";
		
		this.source = "ES|ENR|5.4|" + fromDate.toString() + "|CSV";
		
		// Search for the type of obstacle
		type_of_obstacle = type_of_obstacle.trim().toLowerCase();
		if (type_of_obstacle.contains("antenna")) {
			this.codeType = "ANTENNA";
		} else if (type_of_obstacle.contains("bridge")) { 
			this.codeType = "BRIDGE";
		} else if (type_of_obstacle.contains("building")) { 
			this.codeType = "BUILDING";
		} else if (type_of_obstacle.contains("chimney")) { 
			this.codeType = "CHIMNEY";
		} else if (type_of_obstacle.contains("crane")) { 
			this.codeType = "CRANE";
		} else if (type_of_obstacle.contains("mast")) { 
			this.codeType = "MAST";
		} else if (type_of_obstacle.contains("tower")) { 
			this.codeType = "TOWER";
		} else if (type_of_obstacle.contains("tree")) { 
			this.codeType = "TREE";
		} else if (type_of_obstacle.contains("turbine")) { 
			this.codeType = "WINDTURBINE";
		} else {
			this.codeType = "OTHER";
		}
		
	}

	public String getOFMline() {
		
		// Some cleanup to keep to comply with: https://github.com/openflightmaps/ofmx/wiki/OFMX-CSV
		this.txtName = this.txtName.replaceAll("\"", "\"\""); // Replace all <"> with <"">
		this.txtName = this.txtName.replaceAll(" +", " "); // Replace all multiple spaces with single spaces
		this.txtName = this.txtName.replaceAll("\\s/\\s", "/"); // Replace < / > with </>
		this.txtName = this.txtName.replaceAll("\\s/", "/"); // Replace < /> with </>
		this.txtName = this.txtName.replaceAll("/\\s", "/"); // Replace </ > with </>

		this.txtDescrLgt = this.txtDescrLgt.replaceAll("\"", "\"\""); // Replace all <"> with <"">
		this.txtDescrLgt = this.txtDescrLgt.replaceAll(" +", " "); // Replace all multiple spaces with single spaces
		
		this.txtRmk = this.txtRmk.replaceAll("\"", "\"\""); // Replace all <"> with <"">
		this.txtRmk = this.txtRmk.replaceAll(" +", " "); // Replace all multiple spaces with single spaces
		
		return 
				"\"" +
				this.codeId + "\",\"" + 
				this.codeType + "\",\"" + 
				this.txtName + "\",\"" + 
				this.codeLgt + "\",\"" + 
				this.codeMarking + "\",\"" + 
				this.txtDescrLgt +  "\",\"" +
				this.txtDescrMarking + "\",\"" + 
				this.coordinates.getLat() + "\",\"" + 
				this.coordinates.getLon() + "\",\"" + 
				this.valGeoAccuracy + "\",\"" + 
				this.uomGeoAccuracy + "\",\"" + 
				this.valElev + "\",\"" + 
				this.valElevAccuracy + "\",\"" + 
				this.valHgt + "\",\"" + 
				this.codeHgtAccuracy + "\",\"" + 
				this.uomDistVer + "\",\"" + 
				this.valRadius + "\",\"" + 
				this.uomRadius + "\",\"" + 
				this.codeGroupId + "\",\"" + 
				this.txtGroupName + "\",\"" + 
				this.codeLinkedToId + "\",\"" + 
				this.codeLinkType + "\",\"" + 
				this.datetimeValidWef + "\",\"" + 
				this.datetimeValidTil + "\",\"" + 
				this.txtRmk + "\",\"" + 
				this.source + "\"";
	}

	public String toString() {
		return getOFMline();
	}
}
