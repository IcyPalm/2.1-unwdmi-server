package weatherhandler.processor.query.group;

/**
 * Group class for computing averages.
 */
public class Average implements Group {
    /**
     * Sum of the numbers seen so far.
     */
    private double sum = 0;

    /**
     * Amount of numbers seen so far.
     */
    private double count = 0;

    /**
     * Add a new number to the computation.
     *
     * @param n
     *            The new number.
     */
    public void add(double n) {
        sum += n;
        count++;
    }

    /**
     * @return The average of all added numbers.
     */
    public double get() {
        return sum / count;
    }
}
