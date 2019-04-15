package dev.cryo.aip.countries;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import dev.cryo.aip.countries.LKObstacle;
import dev.cryo.aip.model.ObstacleParser;
import dev.cryo.aip.model.ParseException;

public class LKObstacleParser extends ObstacleParser {

	public void parse() throws ParseException, IOException {
		
		// Parse the text of the CSV file
		boolean titleFound = false;
		HashMap<Integer, String> groupUUIDs = new HashMap<Integer, String>();
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IOException("Use the UTF-8 encoded file for Czechia!");
		}
		
		try {
			for (String line : lines) {

				// Look for the starting line of the CSV data			
				if (!titleFound) {
					if (line.equals("obs_id;aip_id;obs_txt_name;obs_txt_name_cz;obs_txt_descr_type;obs_txt_descr_type_cz;obs_val_elev;obs_val_hgt;obs_uom_dist_ver;obs_txt_ver_datum;obs_geo_lat;obs_geo_long;obs_code_datum;obs_txt_descr_marking;obs_txt_descr_marking_cz;obs_txt_descr_lgt;obs_txt_descr_lgt_cz;obs_code_lgt;obs_txt_rmk;obs_txt_rmk_cz")) { 
						titleFound = true;
					}
					continue;
				}
	
				// Avoid empty lines like the last one
				if (line.trim().isEmpty()) {
					continue;
				}
				
				// Check if the line has the right amount of elements
				line = line.replaceAll("(ValueEmpty|without\\smarking|unknown)", "");
				line = line.replaceAll("(\\s;|;\\s)", ";");
				String[] lineData = line.split(";", -1);
				if (lineData.length != 20) fail();
				
				/*
				0: obs_id
				1: aip_id
				2: obs_txt_name
				3: obs_txt_name_cz - ignore
				4: obs_txt_descr_type
				5: obs_txt_descr_type_cz - ignore
				6: bs_val_elev
				7: obs_val_hgt
				8: obs_uom_dist_ver
				9: obs_txt_ver_datum
				10: obs_geo_lat
				11: obs_geo_long
				12: obs_code_datum
				13: obs_txt_descr_marking
				14: obs_txt_descr_marking_cz - ignore
				15: obs_txt_descr_lgt
				16: obs_txt_descr_lgt_cz - ignore
				17: obs_code_lgt
				18: obs_txt_rmk
				19: obs_txt_rmk_cz - ignore
				*/
				
				obstacles.add(new LKObstacle(
						lineData[0],
						lineData[1],
						lineData[2],
						lineData[4],
						lineData[6],
						lineData[7],
						lineData[8],
						lineData[9],
						lineData[10],
						lineData[11],
						lineData[12],
						lineData[13],
						lineData[15],
						lineData[17],
						lineData[18],
						groupUUIDs
						));
						
				
			}
		} catch (NumberFormatException e) {
			fail();
		}
		
		if (!titleFound) {
			fail();
		}
	}

}