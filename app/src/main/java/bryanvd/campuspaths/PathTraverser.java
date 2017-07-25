package bryanvd.campuspaths;


import java.util.Iterator;

/**
 * <b>PathTraverser</b> represents a <b>mutable</b> divider which segments the
 * components of a Path one element at a time from the initial element to
 * the final element of the Path.
 * <p>
 * PathTraverser functions similarly to an Iterator but with additional functionality.
 * The traverser is capable of returning the unique elements which distinguish the Path
 * for a the current component of the path including the starting node, destination node, and
 * distance between the two elements on the specific (current) segment.
 *
 * @param <T> Node type
 */
public class PathTraverser <T> {

    private final Iterator<Edge<T, Double>> traverser;
    private Edge<T, Double> current;

    // Abstraction Function:
    // The current Edge represents the current segment being considered by the PathTraverser
    // and thus the most recent element from the path.  The traverser holds a placeholder to
    // the next element to be considered from the initial path provided and therefore the next
    // element in the path which will replace the current Edge as the current segment of that path.

    // Representation Invariant for every PathTraverser p:
    // * traverser of p is non-null.

    /**
     * A debugging constant that affirms whether the representation invariant should be
     * checked during execution.
     */
    private static final boolean DEBUG = false;

    /**
     * @requires path is non-null
     * @param path The path to traverse
     * @effects Constructs a new PathTraverser around the path provided
     * where the next segment of the traversal corresponds to the first
     * element in the path.  Any paths with a distance of zero are removed
     * from the segments considered by the traverser prior to any methods
     * being invoked on 'this'.
     */
    public PathTraverser(Path<T> path) {
        this.traverser = new Path<T>(path).getPath().iterator();
        this.current = null;
        checkRep();
    }

    /**
     * Traverses to the next segment in the attached path.
     *
     * @modifies this
     * @effects the current segment being considered by the traverser
     * is now the next element in the path.  If all the elements in the
     * path have been traversed and no more remain, the current element
     * is set to null.
     */
    public void next() {
        checkRep();
        if (this.hasNext()) {
            this.current = this.traverser.next();
        } else {
            this.current = null;
        }
        checkRep();
    }

    /**
     * Checks whether subsequent elements exist in the attached path which
     * could be considered the current segment by the PathFinder with
     * invocations of 'next()'
     *
     * @return true if and only if an element exists after the most
     * recent element traversed by the traverser (and now being considered
     * as the current segment by the PathTraverser) which could be considered
     * as the new current segment by the traverser with an invocation of 'next()'
     */
    public boolean hasNext() {
        return this.traverser.hasNext();
    }

    /**
     * @return The starting node of the current segment of the path being
     * considered by the traverser.  If no segment is currently being considered
     * by the traverser (either because no calls of 'next()' have been invoked or
     * no more elements remain in the path to be traversed) null is returned.
     */
    public T getStart() {
        if (current != null) {
            return this.current.parent;
        }
        return null;
    }

    /**
     * @return The destination node of the current segment of the path being
     * considered by the traverser.  If no segment is currently being considered
     * by the traverser (either because no calls of 'next()' have been invoked
     * or no more elements remain in the path to be traversed) null is returned.
     */
    public T getDest() {
        if (current != null) {
            return this.current.child;
        }
        return null;
    }

    /**
     * @return The distance associated with the current segment of the path
     * being considered by the traverser.  If no segment is currently being
     * considered by the traverser (either because no calls of 'next()' have been
     * invoked or no more elements remain in the path to be traversed) null is returned.
     */
    public Double getDist() {
        if (current != null) {
            return this.current.label;
        }
        return null;
    }

    private void checkRep() {
        if (DEBUG) {
            assert (this.traverser != null) : "Path cannot be null";
        }
    }
}

