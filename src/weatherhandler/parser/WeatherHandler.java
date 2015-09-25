package weatherhandler.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import weatherhandler.data.Measurement;

public class WeatherHandler extends DefaultHandler {
    private List<Measurement> measurements = new ArrayList<Measurement>();

    private String tagName = null;

    private Measurement mes = null;

    public List<Measurement> getList() {
        return measurements;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (qName.equalsIgnoreCase("MEASUREMENT")) {
            //initialize Measurement object
            mes = new Measurement();
        } else {
            this.tagName = qName;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("MEASUREMENT")) {
            //add Measurement object to list
            measurements.add(mes);
        }
        this.tagName = null;
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (tagName == null || length == 0) {
            return;
        }
        String slice = new String(ch, start, length);
        if (tagName.equalsIgnoreCase("STN")) {
            mes.setStation(Integer.parseInt(slice, 10));
        } else if (tagName.equalsIgnoreCase("DATE")) {
            mes.setDate(slice);
        } else if (tagName.equalsIgnoreCase("TIME")) {
            mes.setTime(slice);
        } else if (tagName.equalsIgnoreCase("TEMP") && slice.length() > 0) {
            mes.setTemperature(Float.parseFloat(slice));
        } else if (tagName.equalsIgnoreCase("DEWP") && slice.length() > 0) {
            mes.setDewPoint(Float.parseFloat(slice));
        } else if (tagName.equalsIgnoreCase("STP") && slice.length() > 0) {
            mes.setStationPressure(Float.parseFloat(slice));
        } else if (tagName.equalsIgnoreCase("SLP") && slice.length() > 0) {
            mes.setSeaLevelPressure(Float.parseFloat(slice));
        } else if (tagName.equalsIgnoreCase("VISIB") && slice.length() > 0) {
            mes.setVisibility(Float.parseFloat(slice));
        } else if (tagName.equalsIgnoreCase("WDSP") && slice.length() > 0) {
            mes.setWindSpeed(Float.parseFloat(slice));
        } else if (tagName.equalsIgnoreCase("PRCP") && slice.length() > 0) {
            mes.setPrecipitation(Float.parseFloat(slice));
        } else if (tagName.equalsIgnoreCase("SNDP") && slice.length() > 0) {
            mes.setSnowDepth(Float.parseFloat(slice));
        } else if (tagName.equalsIgnoreCase("FRSHTT") && slice.length() > 0) {
            mes.setEvents(slice);
        } else if (tagName.equalsIgnoreCase("CLDC") && slice.length() > 0) {
            mes.setCloudCover(Float.parseFloat(slice));
        } else if (tagName.equalsIgnoreCase("WNDDIR") && slice.length() > 0) {
            mes.setWindDirection(Integer.parseInt(slice, 10));
        }
    }
}
