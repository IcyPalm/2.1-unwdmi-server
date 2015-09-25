package weatherhandler.processor;

import java.util.List;
import java.util.function.Function;
import java.util.function.BiConsumer;

import weatherhandler.data.Measurement;
import weatherhandler.data.MeasurementsCache;

public class CompleteMissingProcessor implements Processor {
    private int cacheSize;
    private Processor inner;

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
                // TODO clean this up slightly, somehow…
                this.replaceIfNecessary(m, prev,
                    p -> p.getTemperature(),
                    (measurement, temp) -> measurement.setTemperature(temp));
                this.replaceIfNecessary(m, prev,
                    p -> p.getDewPoint(),
                    (measurement, dewp) -> measurement.setDewPoint(dewp));
                this.replaceIfNecessary(m, prev,
                    p -> p.getStationPressure(),
                    (measurement, stp) -> measurement.setStationPressure(stp));
                this.replaceIfNecessary(m, prev,
                    p -> p.getSeaLevelPressure(),
                    (measurement, slp) -> measurement.setSeaLevelPressure(slp));
                this.replaceIfNecessary(m, prev,
                    p -> p.getVisibility(),
                    (measurement, vis) -> measurement.setVisibility(vis));
                this.replaceIfNecessary(m, prev,
                    p -> p.getWindSpeed(),
                    (measurement, wind) -> measurement.setWindSpeed(wind));
                this.replaceIfNecessary(m, prev,
                    p -> p.getPrecipitation(),
                    (measurement, prec) -> measurement.setPrecipitation(prec));
                this.replaceIfNecessary(m, prev,
                    p -> p.getSnowDepth(),
                    (measurement, snow) -> measurement.setSnowDepth(snow));
                this.replaceIfNecessary(m, prev,
                    p -> p.getCloudCover(),
                    // https://www.youtube.com/watch?v=9GOCwqSeRLs
                    (measurement, clc) -> measurement.setCloudCover(clc));
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
