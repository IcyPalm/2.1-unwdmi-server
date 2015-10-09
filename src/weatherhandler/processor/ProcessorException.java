package weatherhandler.processor;

/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 *         A custom {@link Exception}
 */
public class ProcessorException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new {@link ProcessorException} with the {@link Exception} that
     * is passed to the superclass
     * 
     * @param e
     */
    public ProcessorException(Exception e) {
        super("Processor exception:", e);
    }
}
