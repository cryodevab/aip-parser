package dev.cryo.aip.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class WGS84Coordinates {
	
	/**
	 * Positive is N or E and negative is S or W.
	 */
	private double lat, lon;
	private String latHemi = "N", lonHemi = "E";
	DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
	
	public WGS84Coordinates(String lat, String lon) throws NumberFormatException {
		if (lat.length() < 7 || lon.length() < 8) {
			throw new NumberFormatException("Coordinates are too short");
		}
		
		// Figure out the hemispheres
		latHemi = lat.substring(lat.length() - 1, lat.length());
		lonHemi = lon.substring(lon.length() - 1, lon.length());
		
		// Sanity check
		if (!latHemi.matches("[NS]") | !lonHemi.matches("[EW]")) {
			throw new NumberFormatException("Wrong hemispheres");
		}
		
		// Remove last character which is a letter
		lat = lat.substring(0, lat.length() - 1);
		lon = lon.substring(0, lon.length() - 1);
		
		// Get float values from the strings
		double latDeg = Float.valueOf(lat.substring(0,2));
		double latMin = Float.valueOf(lat.substring(2,4));
		double latSec = Float.valueOf(lat.substring(4,lat.length()));

		// Get float values from the strings
		double lonDeg = Float.valueOf(lon.substring(0,3));
		double lonMin = Float.valueOf(lon.substring(3,5));
		double lonSec = Float.valueOf(lon.substring(5,lon.length()));
		
		// Convert to decimal format
		this.lat = latDeg + latMin / 60 + latSec / 3600;
		this.lon = lonDeg + lonMin / 60 + lonSec / 3600;
	}
	
	public WGS84Coordinates(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public String getLat() {
		formatter.applyPattern("00.00000000");	
		return formatter.format(this.lat) + latHemi;
	}
	
	public String getLon() {
		formatter.applyPattern("000.00000000");
		return formatter.format(this.lon) + lonHemi;
	}
	
	public String toString() {
		return getLat() + " " + getLon();
	}
}
