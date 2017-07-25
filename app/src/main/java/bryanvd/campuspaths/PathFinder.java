package bryanvd.campuspaths;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

/**
 * <b>PathFinder</b> represents an <b>immutable</b> collection of buildings and
 * other coordinate locations connected to one another.
 * <p>
 * PathFinders serve to find the shortest walkable routes connecting two buildings
 * in the UW campus.
 *
 */
public class PathFinder {

    private final Graph<Landmark, Double> campus;
    private final Set<Landmark> buildings;

    // Abstraction Function:
    // A PathFinder consists of a graph comprising the network of connections between
    // locations on the campus and the distances between these locations associated
    // with their connection.  The campus represented by the graphical network contains
    // both buildings and simple coordinate locations while the collection of Landmarks,
    // buildings, contains all the Landmarks in the current campus that are distinguished
    // buildings (and thus have an abbreviated name and a full name in addition to their
    // coordinate location).  Together, both campus and buildings function to let PathFinder
    // house the network of paths which represents the real campus and to be able to
    // search the campus for routes linking the buildings.

    // Representation Invariant for every PathFinder p:
    // * p always has a non-null campus
    // * p always has a non-null collection of buildings
    // * All Landmarks contained in the buildings of p must be buildings (have a non-null
    //   short name and full name)
    // * All Landmarks contained in the buildings of p must also be contained in the
    //   campus of p

    /**
     * A debugging constant that affirms whether the representation invariant should be
     * checked during execution.
     */
    private static final boolean DEBUG = false;

    /**
     * @effects Constructs a new PathFinder around the campus paths provided in the campus_paths.dat
     * file and buildings provided in the campus_buildings.dat file.
     * @throws MalformedDataException if the data formatted in the campus_paths.dat file or the
     * campus_buildings.dat file are malformed.
     */
    public PathFinder(InputStream pathInputStream, InputStream buildingInputStream) throws MarvelParser.MalformedDataException {
        this.campus = new Graph<Landmark, Double>();
        Set<Landmark> buildings = CampusParser.parseBuildingData(buildingInputStream);
        this.buildings = buildings;
        for (Landmark building : buildings) {
            this.campus.addNode(building);
        }
        CampusParser.parsePathData(pathInputStream, this.campus);
        checkRep();
    }

    /**
     * Finds the shortest path between two buildings in the campus which the current
     * instance of PathFinder searches in.
     *
     * @param startAbrv The short (abbreviated) name of the starting building
     * @param destAbrv The short (abbreviated) name of the destination building
     * @return a Path of Landmarks and the distances between each Landmark along
     * the path.  If no Path exists, null is returned.
     */
    public Path<Landmark> findShortestPath(String startAbrv, String destAbrv) {
        Landmark start = this.getBuilding(startAbrv);
        Landmark dest = this.getBuilding(destAbrv);
        if (start != null && dest != null) {
            return MarvelPaths2.findPath(campus, start, dest);
        }
        return null;
    }

    /**
     * Return a set of all buildings in the campus of the current PathFinder
     * @return an unmodifiable set of all the Landmarks which are buildings
     * being considered in the current PathFinder
     */
    public Set<Landmark> listBuildings() {
        return Collections.unmodifiableSet(this.buildings);
    }

    /**
     * Provides the building associated with the abbreviated name provided.
     *
     * @param abrv The short (abbreviated) name of the building contained
     * and being considered by the current PathFinder
     * @return the Landmark which is the building associated with the abbreviated
     * name provided.  If the abbreviated name does not correspond to any building
     * being considered by the current instance of PathFinder, null is returned.
     */
    public Landmark getBuilding(String abrv) {
        for (Landmark mark : this.listBuildings()) {
            if (mark.shortName.equals(abrv)) {
                return mark;
            }
        }
        return null;
    }

    private void checkRep() {
        if (DEBUG) {
            assert (this.campus != null) : "Campus of Landmarks being considered cannot be null";
            assert (this.buildings != null) : "Buildings considered by the PathFinder cannot be null";
            for (Landmark building : this.buildings) {
                assert (building.isBuilding()) : "Landmarks considered buildings must have a short name and full name";
                assert (this.campus.containsNode(building)) : "Buildings in PathFinder must be a part of the campus";
            }
        }
    }
}


