package bryanvd.campuspaths;

import java.util.Map;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * ExpandableListAdapter, based on code and tutorial by Ravi Tamada
 * (https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/),
 * provides an adapter for an ExpandableListView with the provided group headers,
 * child items, and means of getting the position and information of each
 * member of the list.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    /** Current context of the application */
    private Context context;

    /* Group Headers of the expandable list */
    private List<String> listDataHeader;

    /* Children items associated with each header of the expandable list */
    private Map<String, List<String>> listDataChild;

    /**
     * Constructs a new ExpandableListAdapter in the provided application context with provided group
     * headers and associated child items.
     * @param context Current application context
     * @param listDataHeader Group headers for the expandable list view
     * @param listChildData Associated child items of the group headers provided in the expandable list view.
     */
    public ExpandableListAdapter(Context context, List<String> listDataHeader, Map<String, List<String>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    /**
     * Gets the data associated with the given child within the given group.
     * @param groupPosition Position of the group header
     * @param childPosititon Position of the child item in the group header
     * @return the child item of held in the expandable list view at the location of the group header
     * and child position.
     */
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }

    /**
     * Returns the child ID of the expandable list view.
     * @param groupPosition Position of group header **unnecessary**
     * @param childPosition Position of the child item in the group header
     * @return the child position of the expandable list view.
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Gets a View that displays the data for the given child within the given group.
     * @param groupPosition Position of the group header
     * @param childPosition Position of the child item in the group header
     * @param isLastChild True if the child in the group header is the last child, false otherwise.
     * @param convertView View to be converted to the child view of the expandable list view
     * @param parent View group of the parent of this child
     * @return a view that displays the information of the specified child in the group header
     */
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        return convertView;
    }

    /**
     * Gets the number of children in a specified group.
     * @param groupPosition Position of the group header
     * @return a count of all the children associated with the specified group header
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    /**
     * Gets the data associated with the given group.
     * @param groupPosition Position of the group header
     * @return all children data associated with the provided group header
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    /**
     * Gets the number of groups.
     * @return the number of group headers in this expandable list view
     */
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    /**
     * Gets the ID for the group at the given position.
     * @param groupPosition Position of the group header
     * @return the position of the group header in this expandable list view
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Gets a View that displays the given group.
     * @param groupPosition Position of the group header
     * @param isExpanded True if the current group header is expanded, false otherwise
     * @param convertView View to be converted to the child view of the expandable list view
     * @param parent View group of the parent of this child
     * @return a view that displays the information of the specified group header
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    /**
     * Indicates whether the child and group IDs are stable across changes to the underlying data.
     * @return false
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Whether the child at the specified position is selectable.
     * @param groupPosition Position of the group header
     * @param childPosition Position of the child item in the group header
     * @return true
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

