package weatherhandler.data;

public class Measurement {
    String station, date, time, temperature, dewpoint, stationPressure, seaLevelPressure, visibility, windSpeed, precipitation, snowDepth, events, cloudCover, windDirection;

    // Getters
    public String getStation() {
        return station;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getTemperature() {
        return temperature;
    }
    public String getDewpoint() {
        return dewpoint;
    }
    public String getStationPressure() {
        return stationPressure;
    }
    public String getSeaLevelPressure() {
        return seaLevelPressure;
    }
    public String getVisibility() {
        return visibility;
    }
    public String getWindSpeed() {
        return windSpeed;
    }
    public String getPrecipitation() {
        return precipitation;
    }
    public String getSnowDepth() {
        return snowDepth;
    }
    public String getEvents() {
        return events;
    }
    public String getCloudCover() {
        return cloudCover;
    }
    public String getWindDirection() {
        return windDirection;
    }

    // Setters
    public void setStation(String station) {
        this.station = station;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public void setDewpoint(String dewpoint) {
        this.dewpoint = dewpoint;
    }
    public void setStationPressure(String stationPressure) {
        this.stationPressure = stationPressure;
    }
    public void setSeaLevelPressure(String seaLevelPressure) {
        this.seaLevelPressure = seaLevelPressure;
    }
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }
    public void setSnowDepth(String snowDepth) {
        this.snowDepth = snowDepth;
    }
    public void setEvents(String events) {
        this.events = events;
    }
    public void setCloudCover(String cloudCover) {
        this.cloudCover = cloudCover;
    }
    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String toXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<MEASUREMENT>\n");
        sb.append("\t<STN>" + station + "</STN>\n");
        sb.append("\t<DATE>" + date + "</DATE>\n");
        sb.append("\t<TIME>" + time + "</TIME>\n");
        sb.append("\t<TEMP>" + temperature + "</TEMP>\n");
        sb.append("\t<DEWP>" + dewpoint + "</DEWP>\n");
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
        sb.append("DEWP:" + dewpoint);
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
