package weatherhandler.processor;

import java.util.List;

import weatherhandler.data.Measurement;

public interface Processor {
    public void processMeasurements(List<Measurement> measurements);
}
