package weatherhandler.processor;

import java.lang.InterruptedException;
import java.util.List;

import weatherhandler.data.Measurement;
import weatherhandler.Logger;

public class UpdatesMonitor implements Processor {
    private int interval;
    private int current;
    private Thread kicker;
    private Processor inner;
    private Logger logger = new Logger("UpdatesMonitor");

    public UpdatesMonitor(Processor inner) {
        this(1000, inner);
    }
    public UpdatesMonitor(int interval, Processor inner) {
        this.interval = interval;
        this.inner = inner;
        this.current = 0;
        this.kicker = new Thread(new Kicker(this.interval, this));
        this.kicker.start();
    }

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
