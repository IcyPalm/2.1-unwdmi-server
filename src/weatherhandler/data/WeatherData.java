package weatherhandler.data;

import java.util.ArrayList;

public class WeatherData {

    ArrayList<Measurement> measurements = new ArrayList<>();

    public void addMeasurement(Measurement m) {
        measurements.add(m);
    }


    // accessor methods
    public int getSize() {
        return measurements.size();
    }
    public Measurement getMeasurement(int i) {
        return (Measurement) measurements.get(i);
    }

    // toXML method
    public String toXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\"?>\n");
        sb.append("<WEATHERDATA>\n\n");
        for (int i = 0; i < measurements.size(); i++) {
            sb.append(getMeasurement(i).toXML());
            sb.append("\n");
        }
        sb.append("</WEATHERDATA>");
        return sb.toString();
    }
}
