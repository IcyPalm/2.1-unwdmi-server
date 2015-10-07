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

public class WeatherParser {
    private Logger logger = new Logger("WeatherParser");

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

