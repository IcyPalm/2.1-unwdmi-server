package weatherhandler.data;

/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 *         Measurement is a representation of one measurement of a weather
 *         station. This class is purely a dataclass
 */
@SuppressWarnings("javadoc")
public class Measurement {
    int station;
    String date;
    String time;
    float temperature;
    float dewPoint;
    float stationPressure;
    float seaLevelPressure;
    float visibility;
    float windSpeed;
    float precipitation;
    float snowDepth;
    String events = "000000";
    float cloudCover;
    int windDirection;

    // Getters
    public int getStation() {
        return station;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getDewPoint() {
        return dewPoint;
    }

    public float getStationPressure() {
        return stationPressure;
    }

    public float getSeaLevelPressure() {
        return seaLevelPressure;
    }

    public float getVisibility() {
        return visibility;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public float getPrecipitation() {
        return precipitation;
    }

    public float getSnowDepth() {
        return snowDepth;
    }

    public String getEvents() {
        return events;
    }

    public float getCloudCover() {
        return cloudCover;
    }

    public int getWindDirection() {
        return windDirection;
    }

    // Setters
    public void setStation(int station) {
        this.station = station;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setDewPoint(float dewPoint) {
        this.dewPoint = dewPoint;
    }

    public void setStationPressure(float stationPressure) {
        this.stationPressure = stationPressure;
    }

    public void setSeaLevelPressure(float seaLevelPressure) {
        this.seaLevelPressure = seaLevelPressure;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setPrecipitation(float precipitation) {
        this.precipitation = precipitation;
    }

    public void setSnowDepth(float snowDepth) {
        this.snowDepth = snowDepth;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public void setCloudCover(float cloudCover) {
        this.cloudCover = cloudCover;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * Creates a XML entry of this measurement, including TABS
     * 
     * @return XML of this Measurement
     */
    public String toXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<MEASUREMENT>\n");
        sb.append("\t<STN>" + station + "</STN>\n");
        sb.append("\t<DATE>" + date + "</DATE>\n");
        sb.append("\t<TIME>" + time + "</TIME>\n");
        sb.append("\t<TEMP>" + temperature + "</TEMP>\n");
        sb.append("\t<DEWP>" + dewPoint + "</DEWP>\n");
        sb.append("\t<STP>" + stationPressure + "</STP>\n");
        sb.append("\t<SLP>" + seaLevelPressure + "</SLP>\n");
        sb.append("\t<VISIB>" + visibility + "</VISIB>\n");
        sb.append("\t<WDSP>" + windSpeed + "</WDSP>\n");
        sb.append("\t<PRCP>" + precipitation + "</PRCP>\n");
        sb.append("\t<SNDP>" + snowDepth + "</SNDP>\n");
        sb.append("\t<FRSHTT>" + events + "</FRSHTT>\n");
        sb.append("\t<CLDC>" + cloudCover + "</CLDC>\n");
        sb.append("\t<WNDDIR>" + windDirection + "</WNDDIR>\n");
        sb.append("</MEASUREMENT>\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("STN:" + station);
        sb.append("DATE:" + date);
        sb.append("TIME:" + time);
        sb.append("TEMP:" + temperature);
        sb.append("DEWP:" + dewPoint);
        sb.append("STP:" + stationPressure);
        sb.append("SLP:" + seaLevelPressure);
        sb.append("VISIB:" + visibility);
        sb.append("WDSP:" + windSpeed);
        sb.append("PRCP:" + precipitation);
        sb.append("SNDP:" + snowDepth);
        sb.append("FRSHTT:" + events);
        sb.append("CLDC:" + cloudCover);
        sb.append("WNDDIR:" + windDirection);
        return sb.toString();
    }
}
