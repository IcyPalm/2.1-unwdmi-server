package weatherhandler;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Logger {
    final public static int ERROR = 0;
    final public static int WARN = 1;
    final public static int INFO = 2;
    final public static int DEBUG = 3;

    private static int level = Logger.DEBUG;
    private String name;

    public Logger(String name) {
        this.name = name;
    }

    private void log(String level, String[] text) {
        String time = new SimpleDateFormat("hh:mm:ss").format(new Date());
        System.out.println(
            "[" + level + " " + time + "] " +
            this.name + ": " + String.join(" ", text)
        );
    }

    public void debug(String... args) {
        if (Logger.level >= Logger.DEBUG) {
            this.log("DEBUG", args);
        }
    }

    public void info(String... args) {
        if (Logger.level >= Logger.INFO) {
            this.log("INFO", args);
        }
    }

    public void warn(String... args) {
        if (Logger.level >= Logger.WARN) {
            this.log("WARN", args);
        }
    }

    public void error(String... args) {
        this.log("ERROR", args);
    }
}
