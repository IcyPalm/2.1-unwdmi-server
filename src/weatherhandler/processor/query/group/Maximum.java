package weatherhandler.processor.query.group;

/**
 * Group class for computing maxima.
 */
public class Maximum implements Group {
    /**
     * Lowest number seen so far.
     */
    private double max = Double.MIN_VALUE;

    /**
     * Check a new contestant for the title of "Highest Number Known To This
     * Group".
     *
     * @param n
     *            The daring new contestant.
     */
    public void add(double n) {
        if (n > max) {
            max = n;
        }
    }

    /**
     * @return The highest value in this group.
     */
    public double get() {
        return max;
    }
}
