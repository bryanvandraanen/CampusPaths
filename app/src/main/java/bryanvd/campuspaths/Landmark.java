package bryanvd.campuspaths;

/**
 * <b>Landmark</b> represents an <b>immutable</b> descriptive location at a point.  The
 * coordinate where the Landmark is located exists in the unsigned Cartesian plane where
 * increasing x is horizontally right and increasing y is vertically downward.
 * <p>
 * Landmarks are <b>comparable</b> based on the abbreviated name of the location then
 * the full name of the location.  If no names exist, the landmarks are compared based
 * on their respective coordinates.
 * <p>
 * Examples of Landmarks include CSE or the Paul G. Allen Center for Computer Science
 * & Engineering at the location "(2259.7112,1715.5273)"
 *
 */
public class Landmark implements Comparable<Landmark> {

    /** The abbreviated name of the Landmark */
    public final String shortName;

    /** The full name of the Landmark */
    public final String longName;

    /** The Cartesian coordinate where the Landmark is located */
    public final Coordinate coordinate;

    /** Standard precomputed hashCode value for the Landmark */
    private final int hashCode;

    /**
     * A debugging constant that affirms whether the representation invariant should be
     * checked during execution.
     */
    private static final boolean DEBUG = false;

    // Abstraction Function:
    // A Landmark l is the specific distinguished place located at the coordinate.
    // The shortName and fullName of the l correspond to the abbreviated
    // and full names of the actual location being represented.  When no names exist
    // for l, the Landmark represents simply a specific location at the coordinate.

    // Representation Invariant for every Landmark l:
    // * l always has a non-null unsigned coordinate
    // * l always has a valid hashCode determined by the coordinate

    /**
     * @requires x is nonnegative and non-null
     * @requires y is nonnegative and non-null
     * @param x The x value in the unsigned Cartesian coordinate pair of the location of the Landmark
     * @param y The y value in the unsigned Cartesian coordinate pair of the location of the Landmark
     * @effects constructs a new Landmark at the coordinate (x,y) with null abbreviated and full names
     */
    public Landmark(Double x, Double y) {
        this(null, null, x, y);
    }

    /**
     * @requires x is nonnegative and non-null
     * @requires y is nonnegative and non-null
     * @requires if either of shortName or longName is non-null, both are non-null
     * @param shortName The abbreviated name of the Landmark
     * @param longName The full name of the Landmark
     * @param x The x value in the unsigned Cartesian coordinate pair of the location of the Landmark
     * @param y The y value in the unsigned Cartesian coordinate pair of the location of the Landmark
     * @effects constructs a new Landmark at the coordinate (x,y) with the abbreviated name and full
     * name specified.
     */
    public Landmark(String shortName, String longName, Double x, Double y) {
        this.shortName = shortName;
        this.longName = longName;
        this.coordinate = new Coordinate(x, y);
        this.hashCode = this.coordinate.hashCode();
        checkRep();
    }

    /**
     * Checks whether the current instance of Landmark is a 'building' or a simply a 'location'.
     * Buildings are denoted as having a specific abbreviated name and full name while locations
     * exist solely as a coordinate.
     *
     * @return true if and only if the current instance of Landmark has a non-null short (abbreviated)
     * name and a non-null long (full) name.
     */
    public boolean isBuilding() {
        return this.shortName != null && this.longName != null;
    }

    /**
     * Standard equality operation.
     * @param o The object to be compared for equality.
     * @return true if and only if 'o' is an instance of Landmark and 'this' and 'o'
     * represent the identical coordinate location.  Note that 'this' and 'o' are
     * only compared based on their respective coordinates and not on their individual
     * short names or long names (which could be differing).
     */
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Landmark)) {
            return false;
        }
        Landmark b = (Landmark) o;
        return this.coordinate.equals(b.coordinate);
    }

    /**
     * Standard compareTo method for Landmark objects (Comparable implementation).
     * Compares Landmarks based on their short (abbreviated) names and then based
     * on their long (full) names.
     * <p>
     * If either Landmark is not considered a 'building'
     * (and thus does not have a non-null short name or long name), Landmarks are
     * compared based on the x value of their coordinate location and then based on the
     * y value of their coordinate location.
     *
     * @param other The other Landmark being compared to such that if the short name of 'this'
     * is alphabetically and lexicographically before the short name of 'other', 'this' < 'other'
     * and vice versa; then the long name of 'this' is alphabetically and lexicographically before
     * the long name of 'other', 'this' < 'other' - If a short name or long name does not exist,
     * 'other' is compared to 'this' based off their coordinates.
     * @return a positive integer if the short name of 'this' is alphabetically and lexicographically
     * after the short name of 'other', a negative integer if the short name of 'this' is alphabetically and
     * lexicographically before the short name of 'other'; if the short names are equivalent, then a positive integer
     * if the long name of 'this' is alphabetically and lexicographically after the long name of 'other', a negative
     * integer if the long name of 'this' is alphabetically and lexicographically before the long name of 'other',
     * and zero if the long name of 'this' is equivalent to the long name of 'other'.
     * <p>
     * If either object is not a building, a positive integer is returned if the x value in the coordinate of 'this'
     * is greater than the x value in the coordinate of 'other', a negative integer if the x value in the coordinate
     * of 'this' is less than the x value in the coordinate of 'other'; if the x coordinate values are equivalent,
     * then a positive integer is returned if the y value in the coordinate of 'this' is greater than the y value in
     * the coordinate of 'other', a negative integer if the y value in the coordinate of 'this' is less than the
     * y value in the coordinate of 'other', and zero if the y value in the coordinate of 'this' is equivalent to
     * the y value in the coordinate of 'other'.
     */
    @Override
    public int compareTo(Landmark other) {
        if (!this.isBuilding() || !other.isBuilding()) {
            if (this.coordinate.x.equals(other.coordinate.x)) {
                return Double.compare(this.coordinate.y, other.coordinate.y);
            }
            return Double.compare(this.coordinate.x, other.coordinate.x);
        }
        if (!this.shortName.equals(other.shortName)) {
            return this.shortName.compareTo(other.shortName);
        }
        return this.longName.compareTo(other.longName);
    }

    /**
     * Standard hashCode function.
     * @return an int that all objects equal to 'this' will also return.
     */
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /**
     * @return a String representing this.  The returned String will be in the form
     * (x,y) where x represents the x value of the coordinate where the Landmark is
     * located and y represents the y value of the coordinate where the Landmark is
     * located.
     */
    @Override
    public String toString() {
        return this.coordinate.toString();
    }

    /**
     * Checks the representation invariant holds (if any).
     */
    public void checkRep() {
        if (DEBUG) {
            assert (this.coordinate != null) : "Coordinate of the Landmark must not be null";
            assert (this.coordinate.x >= 0) : "X value of the coordinate must be unsigned";
            assert (this.coordinate.y >= 0) : "Y value of the coordinate must be unsigned";
        }
    }
}


