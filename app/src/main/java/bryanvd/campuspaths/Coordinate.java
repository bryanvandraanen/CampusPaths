package bryanvd.campuspaths;


/**
 * <b>Coordinate</b> represents an <b>immutable</b> pair of unsigned Cartesian coordinates.
 * <p>
 * Examples of Coordinates include "(x,y)", "(0,1)", "(0.25,5.72)" etc.
 */
public class Coordinate {

    /** the x coordinate of the Cartesian pair */
    public final Double x;

    /** the y coordinate of the Cartesian pair */
    public final Double y;

    /** A debugging constant that affirms whether the representation
     * invariant should be checked during execution.
     */
    private static final boolean DEBUG = false;

    // Abstraction Function:
    // A Coordinate c represents the location distinguished at
    // the point (x,y) in the unsigned Cartesian coordinate system.
    // The direction of increasing x is horizontally to the right
    // while the direction of increasing y is vertically downward.

    // Representation Invariant for every Coordinate c:
    // * c always has an x value that is non-null
    // * c always has a y value that is non-null
    // * c always has a positive x value
    // * c always has a positive y value

    /**
     * @requires x is nonnegative and non-null
     * @requires y is nonnegative and non-null
     * @param x the x value of the Cartesian coordinate pair
     * @param y the y value of the Cartesian coordinate pair
     * @effects Constructs a new Coordinate (x,y)
     */
    public Coordinate(Double x, Double y) {
        this.x = x;
        this.y = y;
        checkRep();
    }

    /**
     * Standard equality operation.
     * @param o The object to be compared for equality.
     * @return true if and only if 'o' is an instance of coordinate and
     * 'this' and 'o' represent the equivalent Cartesian coordinate pair
     * i.e. the x coordinate of 'this' is equivalent to the x coordinate of 'o'
     * and the y coordinate of 'this' is equivalent to the y coordinate of 'o'.
     */
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Coordinate)) {
            return false;
        }
        Coordinate c = (Coordinate) o;
        return this.x.equals(c.x) && this.y.equals(c.y);
    }

    /**
     * Standard hashCode function.
     * @return an int that all objects equal to 'this' will also return.
     */
    @Override
    public int hashCode() {
        return this.x.hashCode() * 3 + this.y.hashCode();
    }

    /**
     * @return a String representing this.  The returned string will be in the form
     * of "(x,y)" where x is the x value of the current Cartesian coordinate and y is
     * the y value of the current Cartesian coordinate.
     */
    @Override
    public String toString() {
        return "(" + x.toString() + "," + y.toString() + ")";
    }

    /**
     * Checks that the representation invariant holds (if any).
     */
    private void checkRep() {
        if (DEBUG) {
            assert (this.x != null) : "X coordinate cannot be null";
            assert (this.y != null) : "Y coordinate cannot be null";
            assert (this.x >= 0) : "X coordinate cannot be negative";
            assert (this.y >= 0) : "Y coordinate cannot be negative";
        }
    }
}


