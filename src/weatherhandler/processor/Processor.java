package weatherhandler.processor;

import java.util.List;

import weatherhandler.data.Measurement;

/**
 * @author Marijn Pool
 * @author René Kooi
 *
 *         The {@link Processor} interface that is implemented by all Processors
 */
public interface Processor {
    /**
     * The method that every {@link Processor} should have to be able to
     * actually process data
     * 
     * @param measurements
     *            The measurements that are processed
     * @throws ProcessorException
     */
    public void processMeasurements(List<Measurement> measurements) throws ProcessorException;
}
