package weatherhandler.processor.query.group;

/**
 * Group class for computing minima.
 */
public class Minimum implements Group {
    /**
     * Lowest number seen so far.
     */
    private double min = Double.MAX_VALUE;

    /**
     * Check a new contestant for the title of "Lowest Number Known To This
     * Group".
     *
     * @param n
     *            The daring new contestant.
     */
    public void add(double n) {
        if (n < min) {
            min = n;
        }
    }

    /**
     * @return The lowest value in this group.
     */
    public double get() {
        return min;
    }
}
