package weatherhandler.processor;

import java.util.ArrayList;
import java.util.List;

import weatherhandler.Logger;
import weatherhandler.data.Measurement;

/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 *         Processor that batches updates together in larger chunks for possibly
 *         quicker further processing. It does _NOT_ guarantee that exactly
 *         `batchSize` updates are passed through--just that _AT LEAST_
 *         `batchSize` updates are passed through to the next processor.
 */
public class BatchUpdatesProcessor implements Processor {
    // Default amount of measurements per batch
    final private static int DEFAULT_BATCH_SIZE = 1000;
    // Added to initial list size to avoid resizing the allocated list when only
    // a few too many items come in at once
    final private int BATCH_BUFFER = 50;

    // Amount of Measurements per batch
    private int batchSize;
    // Current batch
    private List<Measurement> batch;
    // Processor to pass batched data to
    private Processor next;

    // For printing a log line every batch
    private Logger logger = new Logger("BatchUpdates");

    /**
     * Create a new BatchUpdate Processor with the default batch size(1000)
     * 
     * @param next
     *            The Processor that will take the data and parse it.
     */
    public BatchUpdatesProcessor(Processor next) {
        this(DEFAULT_BATCH_SIZE, next);
    }

    /**
     * Create a new BatchUpdate Processor with the default batch size(1000)
     * 
     * @param batchSize
     *            The size of the batch that should be gathered
     * @param next
     *            The Processor that will take the data and parse it.
     */
    public BatchUpdatesProcessor(int batchSize, Processor next) {
        this.next = next;
        this.batchSize = batchSize;
        this.batch = new ArrayList<Measurement>(batchSize + BATCH_BUFFER);
    }

    /**
     * Concurrently append new items to the current batch. Replace the batch if
     * the current one is "complete".
     */
    private synchronized List<Measurement> append(List<Measurement> measurements) {
        this.batch.addAll(measurements);
        if (this.batch.size() >= this.batchSize) {
            // Replace batch by a new list immediately, so threads can read from
            // this batch and other threads can write to the next batch
            List<Measurement> current = this.batch;
            this.batch = new ArrayList<Measurement>(this.batchSize + BATCH_BUFFER);
            return current;
        }
        return null;
    }

    /**
     * Batch & push new updates.
     */
    public void processMeasurements(List<Measurement> measurements) throws ProcessorException {
        List<Measurement> nextBatch = this.append(measurements);
        if (nextBatch != null) {
            this.logger.debug("pushing " + nextBatch.size() + " measurements");
            this.next.processMeasurements(nextBatch);
        }
    }
}
