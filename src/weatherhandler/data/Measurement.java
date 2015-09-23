package weatherhandler.data;

public class Measurement {
    String station, date, time, temperature, dewpoint, stationPressure, sealevelPressure, visibility, windspeed, prercipitation, snowdepth, events, cloudcover, winddirection;
    
   
    // Getters
    public String getStation() {return station;}
    public String getDate() {return date;}
    public String getTime() {return time;}
    public String getTemperature() {return temperature;}
    public String getDewpoint() {return dewpoint;}
    public String getStationPressure() {return stationPressure;}
    public String getSealevelPressure() {return sealevelPressure;}
    public String getVisibility() {return visibility;}
    public String getWindspeed() {return windspeed;}
    public String getPrercipitation() {return prercipitation;}
    public String getSnowdepth() {return snowdepth;}
    public String getEvents() {return events;}
    public String getCloudcover() {return cloudcover;}
    public String getWinddirection() {return winddirection;}
    
    // Setters
    public void setStation(String station) {this.station = station;}
    public void setDate(String date) {this.date = date;}
    public void setTime(String time) {this.time = time;}
    public void setTemperature(String temperature) {this.temperature = temperature;}
    public void setDewpoint(String dewpoint) {this.dewpoint = dewpoint;}
    public void setStationPressure(String stationPressure) {this.stationPressure = stationPressure;}
    public void setSealevelPressure(String sealevelPressure) {this.sealevelPressure = sealevelPressure;}
    public void setVisibility(String visibility) {this.visibility = visibility;}
    public void setWindspeed(String windspeed) {this.windspeed = windspeed;}
    public void setPrercipitation(String prercipitation) {this.prercipitation = prercipitation;}
    public void setSnowdepth(String snowdepth) {this.snowdepth = snowdepth;}
    public void setEvents(String events) {this.events = events;}
    public void setCloudcover(String cloudcover) {this.cloudcover = cloudcover;}
    public void setWinddirection(String winddirection) {this.winddirection = winddirection;}



    public String toXML(){
        StringBuffer sb = new StringBuffer();
        sb.append("<MEASUREMENT>\n");
        sb.append( "\t<STN>"+station+"</STN>\n");
        sb.append( "\t<DATE>"+date+"</DATE>\n");
        sb.append( "\t<TIME>"+time+"</TIME>\n");
        sb.append( "\t<TEMP>"+temperature+"</TEMP>\n");
        sb.append( "\t<DEWP>"+dewpoint+"</DEWP>\n");
        sb.append( "\t<STP>"+stationPressure+"</STP>\n");
        sb.append( "\t<SLP>"+sealevelPressure+"</SLP>\n");
        sb.append( "\t<VISIB>"+visibility+"</VISIB>\n");
        sb.append( "\t<WDSP>"+windspeed+"</WDSP>\n");
        sb.append( "\t<PRCP>"+prercipitation+"</PRCP>\n");
        sb.append( "\t<SNDP>"+snowdepth+"</SNDP>\n");
        sb.append( "\t<FRSHTT>"+events+"</FRSHTT>\n");
        sb.append( "\t<CLDC>"+cloudcover+"</CLDC>\n");
        sb.append( "\t<WNDDIR>"+winddirection+"</WNDDIR>\n");
        sb.append( "</MEASUREMENT>\n");
        return sb.toString();
    }
    
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append( "STN:"+station);
        sb.append( "DATE:"+date);
        sb.append( "TIME:"+time);
        sb.append( "TEMP:"+temperature);
        sb.append( "DEWP:"+dewpoint);
        sb.append( "STP:"+stationPressure);
        sb.append( "SLP:"+sealevelPressure);
        sb.append( "VISIB:"+visibility);
        sb.append( "WDSP:"+windspeed);
        sb.append( "PRCP:"+prercipitation);
        sb.append( "SNDP:"+snowdepth);
        sb.append( "FRSHTT:"+events);
        sb.append( "CLDC:"+cloudcover);
        sb.append( "WNDDIR:"+winddirection);
        return sb.toString();
    }
}
