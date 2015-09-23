package weatherhandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import weatherhandler.data.Measurement;

public class WeatherParser {
    public WeatherParser(String input){
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try{
            SAXParser saxParser = saxParserFactory.newSAXParser();
            WeatherHandler handler = new WeatherHandler();
            saxParser.parse(new InputSource(new StringReader(input)), handler);
            List<Measurement> measurements = handler.getList();
            for (Measurement measurement : measurements) {
                //System.out.println(measurement.toString());
            }
        }catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

