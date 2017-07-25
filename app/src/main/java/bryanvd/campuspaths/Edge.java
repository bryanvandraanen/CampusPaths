package bryanvd.campuspaths;

/** <b>Edge</b> represents an <b>immutable</b> directed edge between two nodes in
 *  a graph.  Edges are distinguished by the parent-child relationship between their
 *  nodes and the label further identifying the edge.
 *  <p>
 *  Examples of Edges include "A to B: <A, B>", "A to A: <A, A>", "Label: <B, A>"
 *  Edge notation dictates that the first element in the pair of nodes is the parent
 *  and the second element in the nodes is the child. i.e. A: Parent, B: Child = <A, B>
 *
 *  @param <T1> Node type
 *  @param <T2> Weight type
 */
public class Edge<T1, T2> {

    /** The parent node in the edge */
    public final T1 parent;

    /** The child node in the edge */
    public final T1 child;

    /** The label distinguishing the edge */
    public final T2 label;

    /** Standard precomputed hashCode value for the edge */
    private final int hashCode;

    /** A debugging constant that affirms whether the representation invariant should be
     *  checked during execution.
     */
    private static final boolean DEBUG = false;

    // Abstraction Function:
    // An Edge e is the directed connection from parent to child where parent represents
    // the parent node and child represents the child node of the direct connection as
    // from the parent node to the child node as shown in the notation:
    // "label: <parent, child>"

    // Representation Invariant for every Edge e:
    // * e always has a parent that is non-null
    // * e always has a child that is non-null
    // * e always has a valid hashCode determined by the parent and child nodes

    /** @param parent The parent node of the new Edge.
     *  @param child The child node of the new Edge.
     *  @effects Constructs a new edge <parent, child> with a null label.
     *  @throws IllegalArgumentException if parent or child is null
     */
    public Edge(T1 parent, T1 child) {
        this(parent, child, null);
    }

    /**
     *  @param parent The parent node of the new Edge.
     *  @param child The child node of the new Edge.
     *  @param label The label distinguishing the connection between the nodes.
     *  @effects Constructs a new edge label : <parent, child>
     *  @throws IllegalArgumentException if parent or child is null
     */
    public Edge(T1 parent, T1 child, T2 label) {
        if (parent == null || child == null) {
            throw new IllegalArgumentException("Edges cannot have a null parent or child");
        }
        this.label = label;
        this.parent = parent;
        this.child = child;
        this.hashCode = 37 * ((this.parent.hashCode() * 2)  + (this.child.hashCode() * 3));
        checkRep();
    }

    /** Standard equality operation.
     *  @param o The object to be compared for equality.
     *  @return true if and only if 'o' is an instance of edge and 'this' and 'o' represent the
     *  identical directed connection between a parent node and a child node i.e. the parent nodes
     *  have the same name and the child nodes have the same name.  Note that the labels of each edge
     *  are only compared if both 'this' and 'o' have non-null labels, otherwise, equivalence is simply
     *  determined by the parent-child node relationship and any label is irrelevant.
     */
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Edge<?, ?>)) {
            return false;
        }
        Edge<?, ?> e = (Edge<?, ?>) o;
        if (this.label == null || e.label == null) {
            return this.parent.equals(e.parent) &&
                    this.child.equals(e.child);
        }
        return this.parent.equals(e.parent) &&
                this.child.equals(e.child) &&
                this.label.equals(e.label);
    }

    /** Standard hashCode function.
     *  @return an int that all objects equal to 'this' will also return.
     */
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /** @return a String representing this.  The returned String will be in the form
     *  of "label: <parent, child>" where label is the label of the current edge, parent
     *  is the parent node of the current edge, and child is the child node of the current
     *  edge.  Note that if the label of the current edge is null, the returned String will
     *  simply be "<parent, child>".
     */
    @Override
    public String toString() {
        String result = "";
        if (this.label != null) {
            result += this.label + ": ";
        }
        return result + "<" + this.parent + ", " + this.child + ">";
    }

    /**
     *  Checks that the representation invariant holds (if any).
     */
    private void checkRep() {
        if (DEBUG) {
            assert (this.parent != null) : "Parent node cannot be null";
            assert (this.child != null) : "Child node cannot be null";
            assert (this.hashCode == 37 * ((this.parent.hashCode() * 2)  + (this.child.hashCode() * 3))) : "Inconsistent Hash Code";
        }
    }
}

