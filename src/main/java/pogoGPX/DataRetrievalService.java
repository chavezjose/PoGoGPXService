package pogoGPX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataRetrievalService {

	private static HttpURLConnection con;
	
	/**
	 * Calls the NYCPokeMap API and returns the JSON.
	 * @return String representation of JSON payload.
	 * @throws IOException
	 */
	 
	private String questURI = "QUEST_URI";
	private String cookie = "COOKIE";
	
	public String quests(String pokedexNumber) throws IOException {
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
    	
    	return content.toString();
	}
}