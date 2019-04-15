package dev.cryo.aip.model;

public abstract class Obstacle {

	protected int codeId;
	protected String codeType;
	protected String txtName;
	protected String codeLgt;
	protected String codeMarking;
	protected String txtDescrLgt;
	protected String txtDescrMarking;
	protected WGS84Coordinates coordinates;
	protected int valGeoAccuracy;
	protected String uomGeoAccuracy;
	protected int valElev;
	protected int valElevAccuracy;
	protected int valHgt;
	protected String codeHgtAccuracy;
	protected String uomDistVer;
	protected int valRadius;
	protected String uomRadius;
	protected String codeGroupId;
	protected String txtGroupName;
	protected String codeLinkedToId;
	protected String codeLinkType;
	protected String datetimeValidWef;
	protected String datetimeValidTil;
	protected String txtRmk;
	protected String source;
	
	// Get a line just a specified in the OFMX Obstacle CSV file 
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
	
	// Seriously Java?
	protected String capitalize(String input) {
		if (input == null || input.length() == 0)
			return "";
		
		return input.substring(0,1).toUpperCase() + input.substring(1, input.length()).toLowerCase();
	}
}
