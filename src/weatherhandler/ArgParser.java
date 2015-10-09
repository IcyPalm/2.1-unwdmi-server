package weatherhandler;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Marijn Pool
 * @author René Kooi
 *
 * Rudimentary argument parser. Parses "--key value" style arguments and nothing
 * else.
 */
public class ArgParser {
    /**
     * Argument strings passed by OS.
     */
    private String[] args;

    /**
     * Parsed Key→Value arguments.
     */
    private Map<String, String> parsed;

    /**
     * Initialise argument parser.
     *
     * @param args Argument strings as passed by the OS.
     */
    public ArgParser(String[] args) {
        this.args = args;
    }

    /**
     * Parses argument strings into a key→value map.
     */
    public Map<String, String> parse() {
        this.parsed = new HashMap<>();
        for (int i = 0, l = args.length; i < l; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                this.parsed.put(
                    arg.substring(2),
                    args[i + 1]
                );
                i++;
            } else {
                System.err.println("Unknown command line parameter \"" + arg + "\"");
                System.exit(1);
            }
        }
        return this.parsed;
    }
}
