package weatherhandler.processor.query.group;

/**
 * A single group/reduction. Values belonging to this group are added using
 * `add`. The `get` method should return a single reduced value.
 */
public interface Group {
    /**
     * @param n
     *            some double
     */
    public void add(double n);

    /**
     * @return the double
     */
    public double get();
}
