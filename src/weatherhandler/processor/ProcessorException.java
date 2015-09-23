package weatherhandler.processor;

import java.lang.Exception;

public class ProcessorException extends Exception {
    public ProcessorException(Exception e) {
        super("Processor exception:", e);
    }
}
