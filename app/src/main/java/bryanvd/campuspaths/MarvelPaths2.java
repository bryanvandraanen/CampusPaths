package bryanvd.campuspaths;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class MarvelPaths2 {

    // MarvelPaths2 does not represent an ADT (and therefore does not have a representation
    // invariant or abstraction function) as it only has two static methods.

    /**
     * Creates a graphical network of connections between all the data provided in
     * the input file.  At most one directed symmetric edge exists between two nodes in the graph
     * once loaded.  Nodes connected to themselves have a weight of 0.0.  The multiplicative inverse of
     * the connecting occurrences which both nodes share.
     *
     * @returns a new Graph based on the connections between nodes specified in the
     * provided file.  If the data of the specified file is malformed, null is returned.
     * @requires fileName is a valid file path (non-null).
     * @param fileName the .tsv file to be parsed into the Graph network of paths
     */
    public static Graph<String, Double> loadWeightedGraph(String fileName) {
        Graph<String, Double> graph = new Graph<String, Double>();
        Map<String, List<String>> books = new HashMap<String, List<String>>();
        try {
            MarvelParser.parseData("src/hw7/data/" + fileName, books);
            Map<Edge<String, Double>, Integer> weightedEdges = new HashMap<Edge<String, Double>, Integer>();
            for (String book : books.keySet()) {
                List<String> charactersInBook = books.get(book);
                for (String character : charactersInBook) {
                    for (String child : charactersInBook) {
                        // Increments the number of occurrences between two characters based on the
                        // number of mutual books they appear in
                        Edge<String, Double> edge = new Edge<String, Double>(character, child);
                        if (!weightedEdges.containsKey(edge)) {
                            weightedEdges.put(edge, 0);
                        }
                        if (!edge.parent.equals(edge.child)) {
                            weightedEdges.put(edge, weightedEdges.get(edge) + 1);
                        }
                    }
                }
            }
            // Establishes the weights of the edges between characters as the multiplicative inverse
            // of the number of books which the two characters mutually appeared in.
            for (Edge<String, Double> edge : weightedEdges.keySet()) {
                double weight = weightedEdges.get(edge);
                if (Double.compare(weight, 0.0) != 0) {
                    weight = 1 / weight;
                }
                graph.addEdge(new Edge<String, Double>(edge.parent, edge.child, weight));
            }
            return graph;
        } catch (MarvelParser.MalformedDataException e) {
            return null;
        }
    }

    /**
     * Finds the shortest weighted directed path, if any, from the starting node to the destination
     * node in the Graph provided.
     *
     * @requires graph is a valid, non-null graph.
     * @requires graph contains only non-negative weights
     * @param <T> Node type found in the graph
     * @param graph the Graph representing the network of nodes to be searched in
     * @param start the starting node of the path to be found
     * @param dest the destination node of the path to be found
     * @return A Path of nodes and their connecting edges which comprise the shortest weighted path
     * connecting the start node to the dest node.  If no path exists between the start and dest
     * node, null is returned.
     * @throws IllegalArgumentException if start or dest are not valid nodes in the
     * provided Graph.
     */
    public static <T> Path<T> findPath(Graph<T, Double> graph, T start, T dest) {
        if (!graph.containsNode(start) || !graph.containsNode(dest)) {
            throw new IllegalArgumentException("Start and Destination node must be valid nodes in Graph");
        }
        PriorityQueue<Path<T>> active = new PriorityQueue<Path<T>>();
        Set<T> finished = new HashSet<T>();
        active.add(new Path<T>(new Edge<T, Double>(start, start, 0.0)));

        // Using Dijkstra's legendary, renowned algorithm, finds the shortest weighted path
        // from the starting node to the destination node.
        while (!active.isEmpty()) {
            Path<T> minPath = active.remove();
            T minDest = minPath.getDest();
            if (minDest.equals(dest)) {
                return minPath;
            }
            // If the minimum weighted cost path to the destination to this node has already
            // been found, skip searching its children again because it has already been
            // performed previously.
            if (finished.contains(minDest)) {
                continue;
            }
            for (Edge<T, Double> e : graph.getEdges(minDest)) {
                if (!finished.contains(e.child)) {
                    Path<T> newPath = new Path<T>(minPath);
                    newPath.addEdge(e);
                    active.add(newPath);
                }
            }
            // Since this node has been directly visited, the algorithm assumes no negative
            // edge waits so it is ensured that the shorted path to this node has been found.
            finished.add(minDest);
        }
        return null;
    }

}


