package weatherhandler.processor;

import java.util.List;

import weatherhandler.data.Measurement;

abstract public class EachProcessor implements Processor {
    abstract public void processMeasurement(Measurement measurement) throws ProcessorException;

    public void processMeasurements(List<Measurement> measurements) throws ProcessorException {
        for (Measurement m : measurements) {
            this.processMeasurement(m);
        }
    }
}
