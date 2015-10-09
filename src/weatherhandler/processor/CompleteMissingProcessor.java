package weatherhandler.processor;

import java.util.List;
import java.util.function.Function;
import java.util.function.BiConsumer;

import weatherhandler.data.Measurement;
import weatherhandler.data.MeasurementsCache;

/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 * Processor that will fill in missing or defective data based on previous measurements.
 * It will access a {@link MeasurementsCache} and base the new value on that.
 * Also it will keep the cachesize equal
 */
public class CompleteMissingProcessor implements Processor {
    private int cacheSize;
    private Processor inner;

    /**
     * Create a new {@link CompleteMissingProcessor} 
     * @param cacheSize the size of the {@link MeasurementsCache} 
     * @param inner {@link Processor} that will get the data and process it
     */
    public CompleteMissingProcessor(int cacheSize, Processor inner) {
        this.cacheSize = cacheSize;
        this.inner = inner;
    }

    /**
     * Takes an average from a list using the given map function.
     * Using floats here because they can be coerced to ints and we don't want
     * the same method twice _everywhere_.
     */
    private float average(List<Measurement> sample, Function<Measurement, Float> mapper) {
        float average = 0.0f;
        for (Measurement m : sample) {
            average += mapper.apply(m);
        }
        return (average / sample.size());
    }

    /**
     * Compares one field of the given measurement to the previous sample by
     * applying a map function to all of them, and then updates the field if
     * it seems out of place using the given consumer function.
     */
    private void replaceIfNecessary(
        Measurement m,
        List<Measurement> sample,
        Function<Measurement, Float> mapper,
        BiConsumer<Measurement, Float> consumer
    ) {
        float avg = this.average(sample, mapper);
        Float value = mapper.apply(m);
        if (value == null) {
            consumer.accept(m, avg);
            return;
        }
        float d = Math.abs(value - avg);
        if (d / value > 0.2 /* 20% */) {
            // set to average initially
            consumer.accept(m, avg);
            return;
        }
    }

    public void processMeasurements(List<Measurement> measurements) throws ProcessorException {
        for (Measurement m : measurements) {
            List<Measurement> prev = MeasurementsCache.getCache(m.getStation());
            synchronized(prev) {
                this.replaceIfNecessary(m, prev,
                    Measurement::getTemperature, Measurement::setTemperature);
                this.replaceIfNecessary(m, prev,
                    Measurement::getDewPoint, Measurement::setDewPoint);
                this.replaceIfNecessary(m, prev,
                    Measurement::getStationPressure, Measurement::setStationPressure);
                this.replaceIfNecessary(m, prev,
                    Measurement::getSeaLevelPressure, Measurement::setSeaLevelPressure);
                this.replaceIfNecessary(m, prev,
                    Measurement::getVisibility, Measurement::setVisibility);
                this.replaceIfNecessary(m, prev,
                    Measurement::getWindSpeed, Measurement::setWindSpeed);
                this.replaceIfNecessary(m, prev,
                    Measurement::getPrecipitation, Measurement::setPrecipitation);
                this.replaceIfNecessary(m, prev,
                    Measurement::getSnowDepth, Measurement::setSnowDepth);
                this.replaceIfNecessary(m, prev,
                    Measurement::getCloudCover, Measurement::setCloudCover);
                // Wind Direction is stored as an integer, so we "floatify" it
                // when mapping and round it to an integer when storing it.
                this.replaceIfNecessary(m, prev,
                    p -> (float) p.getWindDirection(),
                    (measurement, dir) -> measurement.setWindDirection(Math.round(dir)));

                prev.add(m);
                if (prev.size() > this.cacheSize) {
                    prev.remove(0);
                }
            }
        }
        inner.processMeasurements(measurements);
    }
}
