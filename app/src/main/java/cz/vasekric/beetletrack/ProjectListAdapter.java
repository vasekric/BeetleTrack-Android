package cz.vasekric.beetletrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cz.vasekric.beetletrack.models.Project;

/**
 * Created by vasek on 23.11.2015.
 */
public class ProjectListAdapter extends ArrayAdapter<Project> {

    private List<Project> projects;
    private int resourceId;

    public ProjectListAdapter(Context context, List<Project> projects) {
        super(context, R.layout.project_list_entry, projects);
        this.resourceId = R.layout.project_list_entry;
        this.projects = projects;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        EntryHolder holder;

        if(row == null) {
            final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(resourceId, parent, false);
            holder = new EntryHolder();
            holder.projectName = (TextView)row.findViewById(R.id.projectName);

        }
        else {
            holder = (EntryHolder)row.getTag();
        }

        final Project project = projects.get(position);
        holder.projectName.setText(project.name);


        return row;
    }

    public List<Project> getData() {
        return projects;
    }

    public static class EntryHolder
    {
        TextView projectName;
    }
}
