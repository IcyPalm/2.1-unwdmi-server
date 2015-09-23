package weatherhandler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import weatherhandler.data.Measurement;

public class WeatherHandler extends DefaultHandler {
    private List<Measurement> measurements = new ArrayList<Measurement>();

    private boolean bSTN = false;
    private boolean bDATE = false;
    private boolean bTIME = false;
    private boolean bTEMP = false;
    private boolean bDEWP = false;
    private boolean bSTP = false;
    private boolean bSLP = false;
    private boolean bVISIB = false;
    private boolean bWDSP = false;
    private boolean bPRCP = false;
    private boolean bSNDP = false;
    private boolean bFRSHTT = false;
    private boolean bCLDC = false;
    private boolean bWNDDIR = false;

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
        } else if (qName.equalsIgnoreCase("STN")) {
            bSTN = true;
        } else if (qName.equalsIgnoreCase("DATE")) {
            bDATE = true;
        } else if (qName.equalsIgnoreCase("TIME")) {
            bTIME = true;
        } else if (qName.equalsIgnoreCase("TEMP")) {
            bTEMP = true;
        } else if (qName.equalsIgnoreCase("DEWP")) {
            bDEWP = true;
        } else if (qName.equalsIgnoreCase("STP")) {
            bSTP = true;
        } else if (qName.equalsIgnoreCase("SLP")) {
            bSLP = true;
        } else if (qName.equalsIgnoreCase("VISIB")) {
            bVISIB = true;
        } else if (qName.equalsIgnoreCase("WDSP")) {
            bWDSP = true;
        } else if (qName.equalsIgnoreCase("PRCP")) {
            bPRCP = true;
        } else if (qName.equalsIgnoreCase("SNDP")) {
            bSNDP = true;
        } else if (qName.equalsIgnoreCase("FRSHTT")) {
            bFRSHTT = true;
        } else if (qName.equalsIgnoreCase("CLDC")) {
            bCLDC = true;
        } else if (qName.equalsIgnoreCase("WNDDIR")) {
            bWNDDIR = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("MEASUREMENT")) {
            //add Measurement object to list
            measurements.add(mes);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (bSTN) {
            mes.setStation(new String(ch, start, length));
            bSTN = false;
        } else if (bDATE) {
            mes.setDate(new String(ch, start, length));
            bDATE = false;
        } else if (bTIME) {
            mes.setTime(new String(ch, start, length));
            bTIME = false;
        } else if (bTEMP) {
            mes.setTemperature(new String(ch, start, length));
            bTEMP = false;
        } else if (bDEWP) {
            mes.setDewPoint(new String(ch, start, length));
            bDEWP = false;
        } else if (bSTP) {
            mes.setStationPressure(new String(ch, start, length));
            bSTP = false;
        } else if (bSLP) {
            mes.setSeaLevelPressure(new String(ch, start, length));
            bSLP = false;
        } else if (bVISIB) {
            mes.setVisibility(new String(ch, start, length));
            bVISIB = false;
        } else if (bWDSP) {
            mes.setWindSpeed(new String(ch, start, length));
            bWDSP = false;
        } else if (bPRCP) {
            mes.setPrecipitation(new String(ch, start, length));
            bPRCP = false;
        } else if (bSNDP) {
            mes.setSnowDepth(new String(ch, start, length));
            bSNDP = false;
        } else if (bFRSHTT) {
            mes.setEvents(new String(ch, start, length));
            bFRSHTT = false;
        } else if (bCLDC) {
            mes.setCloudCover(new String(ch, start, length));
            bCLDC = false;
        } else if (bWNDDIR) {
            mes.setWindDirection(new String(ch, start, length));
            bWNDDIR = false;
        }
    }
}
