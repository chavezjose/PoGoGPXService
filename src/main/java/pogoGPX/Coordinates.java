package pogoGPX;

public class Coordinates {

    private String lat;
    private String lng;

    public Coordinates(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
    
    public String toString() {
    	return lat + "," + lng + "\n";
    }
}