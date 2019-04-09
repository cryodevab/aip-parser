package dev.cryo.aip.model;

public class WGS84Coordinates {
	
	/**
	 * Positive is N or E and negative is S or W.
	 */
	private float lat = 1, lon = 1;
	
	public WGS84Coordinates(String lat, String lon) throws NumberFormatException {
		if (lat.length() < 7 || lon.length() < 8) {
			throw new NumberFormatException("Coordinates are too short");
		}
		
		// Check if we are in the south hemisphere
		if (lat.toLowerCase().contains("s")) {
			this.lat = -1;
		}
		
		// Check if we are in the west hemisphere
		if (lon.toLowerCase().contains("w")) {
			this.lon = -1;
		}
		
		// Remove last character which is a letter
		lat = lat.substring(0, lat.length() - 1);
		lon = lon.substring(0, lon.length() - 1);
		
		// Get float values from the strings
		float latDeg = Float.valueOf(lat.substring(0,2));
		float latMin = Float.valueOf(lat.substring(2,4));
		float latSec = Float.valueOf(lat.substring(4,lat.length()));

		// Get float values from the strings
		float lonDeg = Float.valueOf(lon.substring(0,3));
		float lonMin = Float.valueOf(lon.substring(3,5));
		float lonSec = Float.valueOf(lon.substring(5,lon.length()));
		
		// Convert to decimal format
		this.lat *= latDeg + latMin / 60 + latSec / 3600;
		this.lon *= lonDeg + lonMin / 60 + lonSec / 3600;
	}
	
	public WGS84Coordinates(float lat, float lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public float getLat() {
		return lat;
	}
	
	public float getLon() {
		return lon;
	}
	
	public String toString() {
		return lat + " " + lon;
	}
}
