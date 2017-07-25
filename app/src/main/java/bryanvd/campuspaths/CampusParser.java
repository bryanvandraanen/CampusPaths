package bryanvd.campuspaths;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Parser utility to load the UW Campus buildings and paths datasets.
 */
@SuppressWarnings("null")
public class CampusParser {

    // CampusParser does not represent an ADT (and therefore does not have a representation
    // invariant or abstraction function) as it only has two static methods.

    /**
     * Reads the Campus paths dataset.
     * Each section of the input file contains a coordinate and associated
     * indented coordinates to the other locations the coordinate is connected
     * to and how far the distance between the two is in feet, separated on
     * individual lines.
     *
     * @requires file is valid
     * @param file the file that will be read
     * @param campus Graph in which all coordinates will be stored
     * @modifies campus
     * @effects fills campus with the connections between coordinates specified
     *          in the input file
     * @throws MalformedDataException if the file is not well-formed:
     *          each line contains either a single, unindented coordinate in the form of x,y
     *          and each indented coordinate is associated with a non-indented coordinate and
     *          has an associated distance in the form of x,y: dist
     */
    public static void parsePathData(InputStream file, Graph<Landmark, Double> campus) throws MarvelParser.MalformedDataException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(file));
            String inputLine = reader.readLine();

            // Parses each unindented coordinate provided - the initial coordinate with its associated
            // destination coordinates
            while (inputLine != null) {

                // Parse the data, separating the starting coordinates into x and y components
                // and throwing an exception if the data is malformed
                String[] tokens = inputLine.split(",");
                if (tokens.length != 2) {
                    throw new MarvelParser.MalformedDataException("Line should contain exactly 1 comma: "
                            + inputLine);
                }
                Double xCoord = Double.parseDouble(tokens[0]);
                Double yCoord = Double.parseDouble(tokens[1]);
                Landmark initialLocation = new Landmark(xCoord, yCoord);

                // Parses each indented coordinate provided - the destination coordinate to the associated
                // initial coordinate - and constructs an edge between the two coordinates in campus
                while ((inputLine = reader.readLine()) != null && inputLine.startsWith("\t")) {

                    // Parse the data, separating the coordinate from the associated distance
                    // and throwing an exception if the data is malformed
                    String[] destTokens = inputLine.split(": ");
                    if (destTokens.length != 2) {
                        throw new MarvelParser.MalformedDataException("Line should contain exactly 1 ': ': "
                                + inputLine);
                    }

                    // Parse the data, separating the destination coordinates into x and y components
                    // and throwing an exception if the data is malformed
                    String[] destCoords = destTokens[0].split(",");
                    if (destCoords.length != 2) {
                        throw new MarvelParser.MalformedDataException("Line should contain exactly 1 comma: "
                                + inputLine);
                    }
                    Double distance = Double.parseDouble(destTokens[1]);
                    Double xCoordDest = Double.parseDouble(destCoords[0]);
                    Double yCoordDest = Double.parseDouble(destCoords[1]);
                    Landmark destinationLocation = new Landmark(xCoordDest, yCoordDest);

                    // Construct an edge between the starting coordinate and the destination coordinate
                    // distinguishing the distance between the two locations
                    campus.addEdge(initialLocation, destinationLocation, distance);
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    /**
     * Reads the Campus buildings dataset.
     * Each section of the input file contains a coordinate and associated
     * indented coordinates to the other locations the coordinate is connected
     * to and how far the distance between the two is in feet, separated on
     * individual lines.
     *
     * @requires file is valid
     * @param file the file that will be read
     * @return a Set of Landmarks containing all the buildings in the Campus dataset.
     * @throws MalformedDataException if the file is not well-formed:
     *          each line contains a single building in the form of:
     *          shortName       longName        x       y
     *          where shortName represents the abbreviated name of the building,
     *          longName represents the full name of the building,
     *          and x and y represent the coordinates of where the building is located -
     *          all separated by a tab indent
     */
    public static Set<Landmark> parseBuildingData(InputStream file) throws MarvelParser.MalformedDataException {
        BufferedReader reader = null;
        Set<Landmark> buildings = new HashSet<Landmark>();
        try {
            reader = new BufferedReader(new InputStreamReader(file));
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {

                // Parses the data separating the necessary information based of the tab
                // characters distinguishing the components and throwing an
                // exception if the data is malformed
                String[] tokens = inputLine.split("\t");
                if (tokens.length != 4) {
                    throw new MarvelParser.MalformedDataException("Line should contain exactly 4 tabs: "
                            + inputLine);
                }
                Double xCoord = Double.parseDouble(tokens[2]);
                Double yCoord = Double.parseDouble(tokens[3]);
                Landmark building = new Landmark(tokens[0], tokens[1], xCoord, yCoord);

                // Add the parsed data to the collection of buildings
                buildings.add(building);
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
                    e.printStackTrace(System.err);
                }
            }
        }
        return buildings;
    }
}

