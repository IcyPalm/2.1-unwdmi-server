package weatherhandler.data;

public class Station {
    int id;
    String name;
    String country;
    float latitude;
    float longitude;
    float elevation;

    // Getters
    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCountry() {
        return country;
    }
    public float getLatitude() {
        return latitude;
    }
    public float getLongitude() {
        return longitude;
    }
    public float getElevation() {
        return elevation;
    }

    // Setters
    public void setID(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
    public void setElevation(float elevation) {
        this.elevation = elevation;
    }


    public String toXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<STATION>\n");
        sb.append("\t<STN>" + id + "</STN>\n");
        sb.append("\t<NAME>" + name + "</NAME>\n");
        sb.append("\t<COUNTRY>" + country + "</COUNTRY>\n");
        sb.append("\t<LAT>" + latitude + "</LAT>\n");
        sb.append("\t<LONG>" + longitude + "</LONG>\n");
        sb.append("\t<ELV>" + elevation + "</ELV>\n");
        sb.append("</STATION>\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("STN:" + id);
        sb.append("NAME:" + name);
        sb.append("COUNTRY:" + country);
        sb.append("LAT:" + latitude);
        sb.append("LONG:" + longitude);
        sb.append("ELV:" + elevation);
        return sb.toString();
    }
}
