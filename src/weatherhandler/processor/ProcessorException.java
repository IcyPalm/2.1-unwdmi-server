package weatherhandler.processor;

import java.lang.Exception;

public class ProcessorException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ProcessorException(Exception e) {
        super("Processor exception:", e);
    }
}
