package pogoGPX;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;

@Component
public class GPXGenerationService {
	
	@Value("${file.path}")
	private String filePath;
	
	/**
	 * Constructor.
	 */
	public GPXGenerationService() {
		super();
	}
	
	/**
	 * This method is generate and save a GPX file.
	 * @return The name of the create file.
	 * @throws filename IOException
	 */
	public String generateGPXQuest(List<Coordinates> coordsArray) throws IOException {

		GPX.Builder builder = GPX.builder();
		
		for (int i = 0; i < coordsArray.size(); i++) {
			Coordinates curr = coordsArray.get(i);
			builder.addWayPoint(WayPoint.builder().lat(Double.parseDouble(curr.getLat())).lon(Double.parseDouble(curr.getLng())).build());
		}
		
		GPX gpx = builder.build();
		
		// Write file to system.
		String filename = "" + (System.currentTimeMillis() / 1000L);
		GPX.write(gpx, filePath + filename);
		
		return filename;
	}
}