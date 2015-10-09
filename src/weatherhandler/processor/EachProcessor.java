package weatherhandler.processor;

import java.util.List;

import weatherhandler.data.Measurement;

/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 */
abstract public class EachProcessor implements Processor {
    /**
     * @param measurement
     *            The {@link Measurement} you want to process
     * @throws ProcessorException
     *             That is thrown when a {@link Processor} encounters an
     *             exception
     */
    abstract public void processMeasurement(Measurement measurement) throws ProcessorException;

    public void processMeasurements(List<Measurement> measurements) throws ProcessorException {
        for (Measurement m : measurements) {
            this.processMeasurement(m);
        }
    }
}
