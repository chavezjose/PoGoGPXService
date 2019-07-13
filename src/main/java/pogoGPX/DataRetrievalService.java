package pogoGPX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataRetrievalService {

	@Autowired
	private static HttpURLConnection con;
	
	/**
	 * Calls the NYCPokeMap API and returns the JSON.
	 * @return String representation of JSON payload.
	 * @throws IOException
	 */
	 
	@Value("${quest.uri}")
	private String questURI;
	
	@Value("${cookie}")
	private String cookie;
	
	/**
	 * Constructor.
	 */
	public DataRetrievalService() {
		super();
	}
	
	public List<Coordinates> quests(String pokedexNumber) throws IOException {
		String url = questURI + pokedexNumber;
    	StringBuilder content;
    	try {

    		// Setting up the request.
            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty ("cookie", cookie);
            con.addRequestProperty("User-Agent", 
            		"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

           
            // Make the request and read it into a StringBuilder.
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            
        } finally {
            
            con.disconnect();
        }
    	
    	return parseRawData(content.toString());
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