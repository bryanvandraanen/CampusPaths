package bryanvd.campuspaths;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model of campus providing abstracted access to campus representation.  Provides means of finding
 * shortest path between two landmarks on campus and accommodations for preparing group header
 * and child data for expandable list view.
 */
public class CampusPathsModel {

    /** Start group header associated with the expandable list view of starting landmarks on the path */
    public final String startHeader = "Start";

    /** Destination group header associated with the expandable list view of destination landmarks on the path */
    public final String destHeader = "Destination";

    /** the campus and path model */
    private PathFinder pathFinder;

    /** Collection housing data for start header group */
    private List<String> startHeaderData;

    /** Collection housing data for destination header group */
    private List<String> destHeaderData;

    /** Collection holding associations between start header and all landmarks on campus */
    private Map<String, List<String>> startData;

    /** Collection holding associations between destination header and all landmarks on campus */
    private Map<String, List<String>> destData;

    /**
     * Constructs a new CampusPathsModel based on the building and path data in the provided InputStreams.
     * @requires InputStreams provided are in standard format - exits application otherwise.
     * @param pathsInputStream InputStream housing the path data between landmarks on campus
     * @param buildingsInputStream InputStream housing the building data for all landmarks on campus
     */
    public CampusPathsModel(InputStream pathsInputStream, InputStream buildingsInputStream) {

        startHeaderData = new ArrayList<>();
        destHeaderData = new ArrayList<>();
        startData = new HashMap<>();
        destData = new HashMap<>();

        // Attempts to create a PathFinder to represent the campus model - exits the application otherwise
        try {
            pathFinder = new PathFinder(pathsInputStream, buildingsInputStream);
        } catch (MarvelParser.MalformedDataException e) {
            System.err.println("Campus Paths Model - path finder could not be initialized.");
            System.exit(0);
        }

        // Prepares start and destination header and child data for all landmarks on campus
        prepareData();
    }

    /**
     * Provides the start group header data of the current campus model
     * @return An unmodifiable collection of the start header data for the current campus
     */
    public List<String> getStartHeaderData() {
        return Collections.unmodifiableList(startHeaderData);
    }

    /**
     * Provides the destination group header data of the current campus model
     * @return An unmodifiable collection of the destination header data for the current campus
     */
    public List<String> getDestHeaderData() {
        return Collections.unmodifiableList(destHeaderData);
    }

    /**
     * Provides the building data associated with the start header of all the individual landmarks
     * on campus
     * @return An unmodifiable collection of the building data associated with the start header
     */
    public Map<String, List<String>> getStartData() {
        return Collections.unmodifiableMap(startData);
    }

    /**
     * Provides the building data associated with the destination header of all the individual landmarks
     * on campus
     * @return An unmodifiable collection of the building data associated with the destination header
     */
    public Map<String, List<String>> getDestData() {
        return Collections.unmodifiableMap(destData);
    }

    /**
     * Finds the shortest path between the two buildings on campus specified in the current model
     * of campus
     * @param startAbrv Abbreviation of the starting building name
     * @param destAbrv Abbreviation of the destination building name
     * @return A path representing the shortest route between the starting landmark and destination
     * landmark on campus
     */
    public Path<Landmark> findShortestPath(String startAbrv, String destAbrv) {
        return pathFinder.findShortestPath(startAbrv, destAbrv);
    }

    /**
     * Provides the landmark data of the specified building in the current campus model
     * @param markAbrv Abbreviation of the landmark on campus
     * @return A landmark representing the specified building on campus.  If the building is not found,
     * null is returned instead.
     */
    public Landmark getLandmarkData(String markAbrv) {
        return pathFinder.getBuilding(markAbrv);
    }

    /**
     * Prepares the data populating the collections for the start header groups,
     * destination header groups, and all the landmarks on campus (associating the landmarks
     * with both the start and header groups).
     * Creates convenient way of preparing expandable list views to access model information
     * for application control.
     * Landmarks associated with the start and header groups are unmodifiable thus maintaining
     * stability of child IDs in expandable list adapter.
     */
    private void prepareData() {
        List<String> landmarkNames = new ArrayList<>();
        startHeaderData.add(startHeader);
        destHeaderData.add(destHeader);

        for (Landmark landmark : pathFinder.listBuildings()) {
            landmarkNames.add(landmark.shortName);
        }
        Collections.sort(landmarkNames);
        landmarkNames = Collections.unmodifiableList(landmarkNames);
        startData.put(startHeaderData.get(0), landmarkNames);
        destData.put(destHeaderData.get(0), landmarkNames);
    }
}

