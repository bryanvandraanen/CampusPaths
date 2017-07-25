package bryanvd.campuspaths;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 *  <b>Graph</b> represents a <b>mutable</b> finite collection of nodes and their corresponding edges.
 *  Nodes are associated with all edges in the graph of which they are the parent node in the directed
 *  connection.
 *  <p>
 *  Graphs can be described as {Node=[label: <Node, B>, edge: <Node, Child>], B=[], Child=[], ...}
 *  where the initial "Node" corresponds to the parent node of edges in which "Node" is the parent
 *  and "label: <Node, B>" demonstrates the labeled edge from the parent, "Node" and child, "B".
 *  <p>
 *  Graphs may contain multiple directed edges in the same direction between two nodes.  However,
 *  no duplicate edges may exist between two nodes in the same direction which also have the same
 *  edge label.
 *
 *  @param <T1> Node type
 *  @param <T2> Edge weight type
 */
public class Graph<T1, T2> {

    private Map<T1, Set<Edge<T1, T2>>> nodes;

    // Abstraction Function:
    // Each key in nodes represents the parent node of the edges in the associated collection.
    // The corresponding edges associated with each key are all the edges in which the key is the
    // parent node in that edge.  The associations between parent nodes and their directly connected
    // child nodes (also contained as keys in nodes) comprise a network of connections as dictated
    // by the individual directed edges between one another.

    // Representation Invariant for every graph g:
    // * nodes is non-null.
    // * No duplicate keys exist in nodes
    // * Any Set of edges associated with a key in g is non-null.
    // * Any edge contained in g has both the parent and the child node as keys in nodes.
    // * Any edge associated with a key has a parent node of that key.
    // * Any edge contained in the g has a non-null label.
    // * The size of g corresponds to the total number of nodes in the graph.
    // * Nodes and edges inside nodes cannot be null.
    // * g is limited to one unique edge such that any edge which has the same
    //   parent node, child node, and edge label will only be contained once in the graph

    /** A debugging constant that affirms whether the representation invariant should be
     *  checked during execution.
     */
    private static final boolean DEBUG = false;

    /**
     *  @effects Constructs a new Graph with no nodes, {}.
     */
    public Graph() {
        this.nodes = new HashMap<T1, Set<Edge<T1, T2>>>();
        checkRep();
    }

    /**
     *  Add a node to the graph.
     *
     * @param node The node to be added to this graph.
     * @modifies this
     * @effects node is added to the graph of nodes in this
     * @throws IllegalArgumentException if node is null.
     */
    public void addNode(T1 node) {
        checkRep();
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }
        if (!this.nodes.containsKey(node)) {
            this.nodes.put(node, new HashSet<Edge<T1, T2>>());
        }
        checkRep();
    }
    /**
     *  Add an Edge to the graph
     *
     *  @param parent The parent in the Edge to be added
     *  @param child The child in the Edge to be added
     *  @param label The label of the Edge distinguishing the connection
     *  @modifies this
     *  @effects the nodes parent and child are added to the graph (if not already
     *  in the graph) with an edge label: <parent, child> directly connecting the
     *  parent to the child.
     *  @throws IllegalArgumentException if parent or child is null.
     *  @throws IllegalArgumentException if label is null.
     */
    public void addEdge(T1 parent, T1 child, T2 label) {
        checkRep();
        if (label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }
        this.addNode(parent);
        this.addNode(child);
        this.nodes.get(parent).add(new Edge<T1, T2>(parent, child, label));
        checkRep();
    }

    /**
     *  Add the specified Edge to the graph
     *
     *  @param edge The Edge to be added to the graph
     *  @modifies this
     *  @effects the parent and child nodes in edge are added as individual
     *  nodes in graph (if not already in the graph) along with the specified edge
     *  directly connecting the nodes.
     *  @throws IllegalArgumentException if edge is null.
     *  @throws IllegalArgumentException if the parent or child node of edge is null.
     *  @throws IllegalArgmuentException if the label of edge is null.
     */
    public void addEdge(Edge<T1, T2> edge) {
        checkRep();
        if (edge == null) {
            throw new IllegalArgumentException("Edge cannot be null");
        }
        this.addEdge(edge.parent, edge.child, edge.label);
        checkRep();
    }

    /**
     *  Returns a set of all the nodes in the current graph
     *
     *  @return an unmodifiable set of all the nodes in the graph
     */
    public Set<T1> nodes() {
        return Collections.unmodifiableSet(this.nodes.keySet());
    }

    /**
     *  Returns true if the node provided is in the current graph
     *
     *  @param node The node to be checked if it is a part of the graph
     *  @return true if and only if the node is a part of the current graph
     */
    public boolean containsNode(T1 node) {
        return this.nodes.containsKey(node);
    }

    /**
     *  Returns true if any Edge exists between the provided parent and child node
     *
     *  @param parent The parent node in the Edge
     *  @param child The child node in the Edge
     *  @return true if and only if there is some Edge in the current graph that
     *  has an identical parent and child node to the nodes specified.
     *  @throws IllegalArgumentException if parent or child is null
     */
    public boolean containsEdge(T1 parent, T1 child) {
        return this.containsEdge(parent, child, null);
    }

    /**
     *  Returns true if the specific Edge is contained in the current graph
     *
     *  @param parent The parent node in the Edge
     *  @param child The child node in the Edge
     *  @param label The distinguishing label in the Edge
     *  @return true if and only if there is some Edge in the current graph that
     *  has an identical parent and child node and distinguishing label as the
     *  ones specified.
     *  @throws IllegalArgumentException if parent or child is null
     */
    public boolean containsEdge(T1 parent, T1 child, T2 label) {
        if (parent == null || child == null) {
            throw new IllegalArgumentException("Nodes cannot be null");
        }
        return this.containsEdge(new Edge<T1, T2>(parent, child, label));
    }

    /**
     *  Returns true if the Edge is contained in the current graph
     *
     *  @param edge The Edge being checked against the Edges in the graph
     *  @return true if there is an identical Edge in the graph that has
     *  the same parent and child node.  If the edge provided has a null
     *  label, returns true if there is any Edge in the current graph
     *  (regardless of label) that has an equal parent and child node.
     */
    public boolean containsEdge(Edge<T1, T2> edge) {
        if (edge == null || !this.containsNode(edge.parent)) {
            return false;
        }
        return this.nodes.get(edge.parent).contains(edge);
    }

    /**
     *  Returns a set of all the edges which of the specified node is the parent
     *
     *  @param node The specified parent node of the edges
     *  @return an unmodifiable set of all the edges of which the specified node is
     *  the parent.  If the node provided is not a node in the current graph, returns
     *  null.
     */
    public Set<Edge<T1, T2>> getEdges(T1 node) {
        if (!this.containsNode(node)) {
            return null;
        }
        return Collections.unmodifiableSet(this.nodes.get(node));
    }

    /**
     *  @return a String representing this.  The returned String will be in the form of
     *  "{Node=[label: <Node, A>, NodeToB: <Node, B>], A=[AToB: <A, B>], B=[]} where the
     *  initial "Node" prior to the equals sign '=' represents the parent node and the
     *  subsequent pairs of nodes (distinguished by the unique preceding edge label i.e.
     *  "label: ") in brackets '<' '>' represent first, the parent node, and then the
     *  child node in an edge connecting the two.
     */
    @Override
    public String toString() {
        return this.nodes.toString();
    }

    /**
     *  Checks that the representation invariant holds (if any).
     */
    private void checkRep() {
        if (DEBUG) {
            assert (this.nodes != null) : "Graph of nodes cannot be null";
            for (T1 node : this.nodes.keySet()) {
                assert (node != null) : "Nodes in graph cannot be null";
                Set<Edge<T1, T2>> edges = this.nodes.get(node);
                assert (edges != null) : "Associated collection of edges of node cannot be null";
                for (Edge<T1, T2> edge : edges) {
                    assert (edge != null) : "Edges in graph cannot be null";
                    assert (edge.label != null) : "Edges in graph cannot have null labels";
                    assert (node.equals(edge.parent)) : "Parent of edge must be same as key";
                    assert (this.nodes.containsKey(edge.parent)) : "Node contained in edge is not a key";
                    assert (this.nodes.containsKey(edge.child)) : "Node contained in edge is not a key";
                }
            }
            assert (this.nodes.size() == nodes().size()) : "Size of graph must equal total number of nodes in graph";
        }
    }
}

