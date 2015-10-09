package weatherhandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 *         Logger class will log errors, warnings or other messages to the
 *         standarderror
 */
public class Logger {
    /**
     * 
     * ERROR level is 0
     */
    final public static int ERROR = 0;
    /**
     * WARNing level is 1
     */
    final public static int WARN = 1;
    /**
     * INFO level is 2
     */
    final public static int INFO = 2;
    /**
     * DEBUG level is 3
     */
    final public static int DEBUG = 3;

    private static int level = Logger.DEBUG;
    private String name;

    /**
     * Create a new logger with a name
     * 
     * @param name
     *            The name for the Logger
     */
    public Logger(String name) {
        this.name = name;
    }

    private void log(String level, String[] text) {
        String time = new SimpleDateFormat("hh:mm:ss").format(new Date());
        System.err.println("[" + level + " " + time + "] " + this.name + ": " + String.join(" ", text));
    }

    /**
     * Output a debug message only if the log level is equal or higher than
     * DEBUG
     * 
     * @param args
     *            Strings to print
     */
    public void debug(String... args) {
        if (Logger.level >= Logger.DEBUG) {
            this.log("DEBUG", args);
        }
    }

    /**
     * Output an message only if the log level is equal or higher than INFO
     * 
     * @param args
     *            Strings to print
     */
    public void info(String... args) {
        if (Logger.level >= Logger.INFO) {
            this.log("INFO", args);
        }
    }

    /**
     * Output a warning message only if the log level is equal or higher than
     * WARN
     * 
     * @param args
     *            Strings to print
     */
    public void warn(String... args) {
        if (Logger.level >= Logger.WARN) {
            this.log("WARN", args);
        }
    }

    /**
     * Output an error message only if the log level is equal or higher than
     * ERROR
     * 
     * @param args
     *            Strings to print
     */
    public void error(String... args) {
        this.log("ERROR", args);
    }
}
