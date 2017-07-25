package bryanvd.campuspaths;

import java.util.*;

/** <b>Path</b> represents a <b>mutable</b> sequence of directed edges between nodes in
 *  a graph.
 *  <p>
 *  Paths of the particular node type are <b>comparable</b> based on the total weighted
 *  cost of the path.
 *  <p>
 *  Examples of Paths include '0.5: <A, B>, 0.2 <B, C>' which comprises the hypothetical
 *  path from A to C with a total cost of 0.7.
 *
 *  @param <T> Node type
 */
public class Path<T> implements Comparable<Path<T>> {

    private List<Edge<T, Double>> edges;
    private double cost;

    // Abstraction Function:
    // Every edge in edges constitutes a segment of the current path between the
    // parent node in the very first edge in the path and the child node of the
    // very last edge in the path.  Together, the sequential edges comprise the
    // entire path from the first node to the final node.  The total weighted cost
    // of the path is determined by the sum of the labels or weights of the individual
    // edges which make up the path.

    // Representation Invariant for every path p:
    // * edges is non-null
    // * cost is always greater than or equal to 0
    // * cost is always the sum of all the individual edge labels/weights in the path
    // * any edges contained in edges is non-null
    // * the edges contained in edges are sequential such that the child node of the
    //   first edge is the parent node of the next immediate (adjacent) edge in the
    //   in the list
    // * the edges contained in edges all have non-null labels

    /** A debugging constant that affirms whether the representation invariant should be
     *  checked during execution.
     */
    private static final boolean DEBUG = false;

    /**
     *  @effects Constructs a new Path comprised of only the provided edge with the
     *  total cost being equivalent to the label or cost of the edge.
     *  @requires edge is non-null with a non-null label
     *  @param edge the Edge constituting the entire initial Path between nodes.
     */
    public Path (Edge<T, Double> edge) {
        this.edges = new ArrayList<Edge<T, Double>>();
        this.edges.add(edge);
        this.cost = edge.label;
        checkRep();
    }

    /**
     *  @effects Constructs a new Path comprised of a copy of the same connections
     *  between the nodes in the provided path and thus the same total cost of the path.
     *  Any edges in the provided path that have a weight of zero are not copied in the
     *  new instance of Path.
     *  @requires path is non-null
     *  @param path the Path constituting the connections between nodes to be copied.
     */
    public Path(Path<T> path) {
        this.edges = new ArrayList<Edge<T, Double>>();
        for (Edge<T, Double> edge : path.getPath()) {
            if (Double.compare(edge.label, 0.0) != 0) {
                this.addEdge(edge);
            }
        }
        checkRep();
    }

    /**
     * Returns a list of all the edges comprising the current path.
     *
     * @return an unmodifiable list of all the edges forming the connections
     * between nodes in the current path
     */
    public List<Edge<T, Double>> getPath() {
        return Collections.unmodifiableList(this.edges);
    }

    /**
     * Add the specified edge to the path
     *
     * @requires edge is non-null and the parent node of the edge is sequential
     * in that it appends to the current path (the child node of the most recent
     * edge in the path is the parent node in the edge provided)
     * @requires edge has a non-null label
     * @modifies this
     * @effects adds the edge to the end of the path
     * @param edge The Edge to be added to the path
     */
    public void addEdge(Edge<T, Double> edge) {
        checkRep();
        this.edges.add(edge);
        this.cost += edge.label;
        checkRep();
    }

    /**
     * Returns the most recent node at the end of the path
     *
     * @return the node at the end of the current path (thus the
     * most recent node, or the current 'destination' or terminal
     * point of the path).  If the current path is empty, null is
     * returned.
     */
    public T getDest() {
        if (this.edges.size() > 0) {
            return this.edges.get(this.edges.size() - 1).child;
        }
        return null;
    }

    /**
     * Returns the total cost of the path
     *
     * @return the total cost of the current path comprised of the
     * sum of costs of all the edge weights that comprise the path
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Standard compareTo method for Path objects (Comparable implementation).
     * Compares Paths based on their total cost of the edge weights which comprise
     * the individual paths.  Paths with a greater total cost are considered 'greater
     * than' paths with a lesser total cost.
     *
     * @param other The other Path being compared to such that if the this has a
     * greater total cost than other, this > other thus returning a positive value.
     * @return a positive integer if the cost of this is greater than the
     * cost of other, a negative integer if the cost of this is less than
     * the cost of other, and zero if the cost of this is equal to the cost of other.
     */
    @Override
    public int compareTo(Path<T> other) {
        return Double.compare(this.getCost(), other.getCost());
    }

    /**
     *  Checks that the representation invariant holds (if any).
     */
    private void checkRep() {
        if (DEBUG) {
            assert(this.edges != null) : "Combined path cannot be null";
            assert(this.cost >= 0) : "Total cost of a path cannot be less than 0";
            T previousNode = null;
            double sumWeights = 0.0;
            for (Edge<T, Double> edge : this.edges) {
                assert(edge != null) : "Edges comprising path cannot be null";
                if (previousNode != null) {
                    assert(edge.parent.equals(previousNode)) : "Edges must be sequential connections between nodes";
                }
                assert(edge.label != null) : "Edge labels cannot be null";
                sumWeights += edge.label;
                previousNode = edge.child;
            }
            assert(Double.compare(sumWeights, this.cost) == 0) : "Total cost of path must be equal to the sum of edge weights";
        }
    }
}


