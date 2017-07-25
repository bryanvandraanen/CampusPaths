package bryanvd.campuspaths;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Displays the campus map and paints a specified path between two buildings found on campus.
 */
public class DrawView extends AppCompatImageView {

    /** Constant scaling original data values of campus path/building locations due to image
     * resizing to accommodate Android BitMap display constraints
     */
    public static final float RESOLUTION_SCALE_VAL = 0.22f;

    /** Constant scaling circular indicator of buildings on campus */
    public static final float LOCATION_MARKER_SCALE_VAL = 7;

    /** Stroke width (path width) of the current paint component depicting paths between landmarks
     * on campus
     */
    public static final int PAINT_STROKE_WIDTH = 5;

    /** Current traversable path between two buildings being considered */
    private PathTraverser<Landmark> traverser;

    /** Single landmark specified if only a single building is being considered in the
     * application rather than a path connecting two separate, distinct buildings
     */
    private Landmark singleLandmark;

    /**
     * Paint component of the campus paths view drawing paths and locations on campus
     */
    private Paint paint;

    /**
     * Constructs a new DrawView for the campus in the provided context configuring
     * the paint component of this view accordingly.
     */
    public DrawView(Context context) {
        super(context);
        configurePaint();
    }

    /**
     * Constructs a new DrawView for the campus in the provided context configuring
     * the paint component of this view accordingly.
     */
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        configurePaint();
    }

    /**
     * Constructs a new DrawView for the campus in the provided context configuring
     * the paint component of this view accordingly.
     */
    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        configurePaint();
    }

    /**
     * @modifies this
     * @effects Repaint the campus map view by depicting the map with the current
     * scale and drawing the path between two buildings if one exists.
     * @param canvas Canvas to paint campus path alterations to
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (traverser != null) {
            drawPath(canvas);
        } else if (singleLandmark != null) {
            canvas.drawCircle(scaleVal(singleLandmark.coordinate.x.floatValue()), scaleVal(singleLandmark.coordinate.y.floatValue()), LOCATION_MARKER_SCALE_VAL, paint);
        }
    }

    /**
     * Configures the paint component of this draw view.
     * Sets the paint color to RED and establishes a reasonably wide stroke width based on the
     * PAINT_STROKE_WIDTH of this DrawView.
     */
    private void configurePaint() {
        paint = new Paint();
        paint.setStrokeWidth(PAINT_STROKE_WIDTH);
        paint.setColor(Color.RED);
    }

    /**
     * Draws the current path being considered by the traverser on the Canvas provided.
     * Start and Destination Landmarks are indicated by a circle while the path connecting the
     * two is depicted with straight painted lines.
     * @param canvas
     */
    private void drawPath(Canvas canvas) {
        traverser.next();
        Landmark start = traverser.getStart();
        Landmark dest = traverser.getDest();
        canvas.drawCircle(scaleVal(start.coordinate.x.floatValue()), scaleVal(start.coordinate.y.floatValue()), LOCATION_MARKER_SCALE_VAL, paint);
        while (traverser.hasNext()) {
            traverser.next();
            start = traverser.getStart();
            dest = traverser.getDest();
            canvas.drawLine(scaleVal(start.coordinate.x.floatValue()), scaleVal(start.coordinate.y.floatValue()), scaleVal(dest.coordinate.x.floatValue()), scaleVal(dest.coordinate.y.floatValue()), paint);
        }
        canvas.drawCircle(scaleVal(dest.coordinate.x.floatValue()), scaleVal(dest.coordinate.y.floatValue()), LOCATION_MARKER_SCALE_VAL, paint);
    }

    /**
     * Scales the provided float value by the RESOLUTION_SCALE_VAL of this DrawView.
     * Accommodates changes and constraints of original campus map image being scaled down
     * to be functional in an Android application (both in resolution and BitMap memory constraints)
     * @param val Value to be scaled by the resolution scale value of this DrawView
     * @return A float representing the scaled product of the val provided and the RESOLUTION_SCALE_VAL
     */
    private float scaleVal(float val) {
        return val * RESOLUTION_SCALE_VAL;
    }


    /**
     * Sets the single landmark being considered by this DrawView to be drawn.
     * Clears any path previously being considered by the view.
     * @param mark Landmark of the current building being considered on campus
     */
    public void setSingleLandmark(Landmark mark) {
        singleLandmark = mark;
        traverser = null;
    }

    /**
     * Updates the current path being considered by this view to the path provided.
     * Clears any single Landmark previously being considered by this view.
     * @param path Path of the route between two buildings on campus
     */
    public void updatePathTraverser(Path<Landmark> path) {
        if (path != null) {
            traverser = new PathTraverser<Landmark>(path);
            singleLandmark = null;
        }
    }

    /**
     * Clears the current path or single Landmark being considered by this view.
     */
    public void resetPathTraverser() {
        traverser = null;
        singleLandmark = null;
    }
}
