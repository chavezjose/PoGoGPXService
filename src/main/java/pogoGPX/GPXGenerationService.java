package pogoGPX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;

public class GPXGenerationService {

	private static DataRetrievalService dataService = new DataRetrievalService();
	
	/**
	 * This method is generate and save a GPX file.
	 * @return The name of the create file.
	 * @throws filename IOException
	 */
	public String generateGPXQuest(String pokedexNumber) throws IOException {
		// A list of coordinates
		List<Coordinates> coordsArray = parseRawData(dataService.quests(pokedexNumber));
		
		GPX.Builder builder = GPX.builder();
		
		for (int i = 0; i < coordsArray.size(); i++) {
			Coordinates curr = coordsArray.get(i);
			builder.addWayPoint(WayPoint.builder().lat(Double.parseDouble(curr.getLat())).lon(Double.parseDouble(curr.getLng())).build());
		}
		
		GPX gpx = builder.build();
		
		// Write file to system.
		String filename = "" + (System.currentTimeMillis() / 1000L);
		GPX.write(gpx, "C:\\gpxFiles\\" + filename);
		
		return filename;
	}
	
	/**
	 * Parses raw data into a list of Coordinates Objects.
	 * @return list of Coordinates objects.
	 */
	private List<Coordinates> parseRawData(String incomingJSON) {
		
		JSONObject jo = new JSONObject(incomingJSON);
		//"quests" works for now, but if I add anything else, I'll have to make it variable
		JSONArray quests = jo.getJSONArray("quests");
		
		List<Coordinates> coordsArray = new ArrayList<>();
		for (int i = 0; i < quests.length(); i++) {
			JSONObject currObj = quests.getJSONObject(i);
			coordsArray.add(new Coordinates(currObj.getString("lat"), currObj.getString("lng")));
		}

		return coordsArray;
	}
}