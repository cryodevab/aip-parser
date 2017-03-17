package se.cryodev.aip.model;

public class Obstacle {

	private int number;
	private String designation;
	private WGS84Coordinates coordinates;
	private int height;
	private int elevation;
	private String light;
	private String type;

	public Obstacle(int number, String designation, WGS84Coordinates coordinates, int height, int elevation,
			String light, String type) {
		this.number = number;
		this.designation = designation;
		this.coordinates = coordinates;
		this.height = height;
		this.elevation = elevation;
		this.light = light;
		this.type = type;
	}

	public String getOFMAline() {
		boolean lighted = true;
		if (light.trim().equals("-"))
			lighted = false;

		type = type.replaceAll("[,]", "\\\\,");
		designation = designation.replaceAll("[,]", "\\\\,");
		designation = designation.replaceAll("\\s/\\s", "/");
		designation = designation.replaceAll("/\\s", "/");

		return ",," + designation + "," + type + "," + lighted + ",,ft," + height + "," + elevation * 0.3048 + ","
				+ coordinates.getLat() + "," + coordinates.getLon() + ",false,,,,,";
	}

	public String toString() {
		return number + ";" + designation + ";" + coordinates + ";" + height + ";" + elevation + ";" + light + ";"
				+ type;
	}
}
