package weatherhandler.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import weatherhandler.data.Measurement;
import weatherhandler.Logger;
import weatherhandler.processor.Processor;
import weatherhandler.processor.ProcessorException;

/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 * The WeatherParser will receive XML input and parse it into Measurements
 * It is SAX based so that if the XML is not 100% valid it will still parse.
 * Another reason for SAX is the lighter RAM use over DOM
 * 
 */
public class WeatherParser {
    private Logger logger = new Logger("WeatherParser");

    /**
     * Create a new WeatherParser and immediately parse the data
     * @param input The input String containing the XML data
     * @param out The Processor that will receive the data
     */
    public WeatherParser(String input, Processor out) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            WeatherHandler handler = new WeatherHandler();
            saxParser.parse(new InputSource(new StringReader(input)), handler);
            List<Measurement> measurements = handler.getList();
            out.processMeasurements(measurements);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } catch (ProcessorException e) {
            logger.error("Processor error:");
            e.printStackTrace();
        }
    }
}

