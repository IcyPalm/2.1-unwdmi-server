package weatherhandler.processor;

import java.util.List;

import weatherhandler.data.Measurement;
import weatherhandler.data.MeasurementsCache;

public class CompleteMissingProcessor implements Processor {
    private int cacheSize;
    private Processor inner;

    public CompleteMissingProcessor(int cacheSize, Processor inner) {
        this.cacheSize = cacheSize;
        this.inner = inner;
    }

    public void processMeasurements(List<Measurement> measurements) throws ProcessorException {
        for (Measurement m : measurements) {
            List<Measurement> prev = MeasurementsCache.getCache(m.getStation());
            synchronized(prev) {
                prev.add(m);
                if (prev.size() > this.cacheSize) {
                    prev.remove(0);
                }
            }
        }
        inner.processMeasurements(measurements);
    }
}
