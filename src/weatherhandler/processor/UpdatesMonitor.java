package weatherhandler.processor;

import java.lang.InterruptedException;
import java.util.List;

import weatherhandler.data.Measurement;
import weatherhandler.Logger;

/**
 * @author Marijn Pool
 * @author René Kooi
 *
 * The {@link UpdatesMonitor} will check on the application and check on
 * the status and log the progress.
 */
public class UpdatesMonitor implements Processor {
    private int interval;
    private int current;
    private Thread kicker;
    private Processor inner;
    private Logger logger = new Logger("UpdatesMonitor");

    /**
     * Constructor for {@link UpdatesMonitor} that uses a one second update
     * frequency 
     * @param inner {@link Processor} that will receive the data
     */
    public UpdatesMonitor(Processor inner) {
        this(1000, inner);
    }
    /**
     * Constructor for {@link UpdatesMonitor} that uses a given interval
     * @param interval The interval for the {@link UpdatesMonitor}
     * @param inner {@link Processor} that will receive the data
     */
    public UpdatesMonitor(int interval, Processor inner) {
        this.interval = interval;
        this.inner = inner;
        this.current = 0;
        this.kicker = new Thread(new Kicker(this.interval, this));
        this.kicker.start();
    }

    /**
     * Log the current Processing speed
     */
    public synchronized void log() {
        this.logger.info(
            "Processing speed:",
            "" + Math.round((float) this.current / (float) this.interval * 1000),
            "measurements/s"
        );
        this.current = 0;
    }

    public void processMeasurements(List<Measurement> measurements) throws ProcessorException {
        this.current += measurements.size();
        inner.processMeasurements(measurements);
    }

    private class Kicker implements Runnable {
        private int interval;
        private UpdatesMonitor logger;

        public Kicker(int interval, UpdatesMonitor logger) {
            this.interval = interval;
            this.logger = logger;
        }

        public void run() {
            try {
                while (true) {
                    Thread.sleep(this.interval);
                    this.logger.log();
                }
            } catch (InterruptedException e) {
                // Process is shutting down, nothing to do here
            }
        }
    }
}
