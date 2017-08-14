package bryanvd.campuspaths;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Main Activity for CampusPaths Application
 *
 * Developed as prototype and example for University of Washington CSE 331 Campus Paths Project.
 * Created by Bryan Van Draanen
 * Under direction of Professor Kevin Zatloukal
 * Summer 2017
 */
public class CampusPathsMain extends AppCompatActivity {

    /** Model of Campus housing Landmark information about
     * campus and accommodations for expandable list view
     */
    private CampusPathsModel model;

    /** Start building abbreviation of current campus path being considered */
    private String start;

    /** Destination building abbreviation of current campus path being considered */
    private String dest;

    /** the building-selection box displaying the choices for the starting location for a path on campus */
    private ExpandableListView startBox;

    /** the building-selection box displaying the choices for the destination location for a path on campus */
    private ExpandableListView destBox;

    /** button that finds the path between two buildings specified  */
    private Button findPath;

    /** button that resets the view of the campus map to the original view */
    private Button reset;

    /** the panel displaying the campus map */
    private DrawView imageView;

    /**
     * Called when the application is starting, before any activity, service, or receiver objects
     * excluding content providers) have been created.
     *
     *
     * @effects Constructs a viewer displaying the campus map and a controller for the given campus
     * allowing the selection of buildings and finding a path between the two and resetting the
     * current path.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_paths_main);

        // InputStreams for the raw input data housing the campus path distances and building information
        InputStream pathsInputStream = this.getResources().openRawResource(R.raw.campus_paths);
        InputStream buildingsInputStream = this.getResources().openRawResource(R.raw.campus_buildings);

        model = new CampusPathsModel(pathsInputStream, buildingsInputStream);

        startBox = (ExpandableListView) findViewById(R.id.Start);
        destBox = (ExpandableListView) findViewById(R.id.Destination);
        findPath = (Button) findViewById(R.id.FindPath);
        reset = (Button) findViewById(R.id.Reset);
        imageView = (DrawView) findViewById(R.id.imageView);

        // Constructs expandable list adapters based on campus path informations and sets them up for
        // the start and destination expandable lists
        ExpandableListAdapter startAdapter = new ExpandableListAdapter(this, model.getStartHeaderData(), model.getStartData());
        ExpandableListAdapter destAdapter = new ExpandableListAdapter(this, model.getDestHeaderData(), model.getDestData());
        startBox.setAdapter(startAdapter);
        destBox.setAdapter(destAdapter);

        // Sets the functionality when a building is selected in the start building expandable list
        // view updating the current start building being considered to the abbreviation of the
        // selected start name.  Outputs the abbreviation of the building to the application.
        startBox.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                start = model.getStartData().get(model.startHeader).get(childPosition);
                Toast.makeText(getApplicationContext(), "Start: " + start, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Sets the functionality when a building is selected in the destination building expandable
        // list view updating the current destination building being considered to the abbreviation of
        // the selected destination name.  Outputs the abbreviation of the building to the application.
        destBox.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                dest = model.getDestData().get(model.destHeader).get(childPosition);
                Toast.makeText(getApplicationContext(), "Destination: " + dest, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Sets the functionality when the "FIND PATH!" button is pressed to draw the path of the
        // shortest route between the two current buildings being considered.  If only one building
        // is being considered as either the start of destination, the landmark is marked on the map.
        // If no buildings are being considered, graph is left unchanged and a message is output
        // to the application that no start or destination is selected.
        findPath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((start == null) != (dest == null) || (start != null && start.equals(dest))) {
                    if (start != null) {
                        imageView.setSingleLandmark(model.getLandmarkData(start));
                    } else {
                        imageView.setSingleLandmark(model.getLandmarkData(dest));
                    }
                } else if (start != null && dest != null) {
                    imageView.updatePathTraverser(model.findShortestPath(start, dest));
                } else {
                    Toast.makeText(getApplicationContext(), "No start or destination selected!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Sets the functionality when the "RESET" button is pressed to reset the current path
        // drawn on the map clearing all landmarks marked and paths drawn over the campus map.
        // Clears any selection of start and destination landmarks and collapses the expandable
        // list views shown.
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start = null;
                dest = null;
                startBox.collapseGroup(0);
                destBox.collapseGroup(0);
                imageView.resetPathTraverser();
            }
        });
    }
}

