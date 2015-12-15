package cz.vasekric.beetletrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cz.vasekric.beetletrack.models.IssueCommon;

/**
 * Created by vasek on 23.11.2015.
 */
public class IssueListAdapter extends ArrayAdapter<IssueCommon> {

    private List<IssueCommon> issues;
    private int resourceId;

    public IssueListAdapter(Context context, List<IssueCommon> issues) {
        super(context, R.layout.issue_list_item, issues);
        this.resourceId = R.layout.issue_list_item;
        this.issues = issues;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        EntryHolder holder;

        if(row == null) {
            final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(resourceId, parent, false);
            holder = new EntryHolder();
            holder.issueName = (TextView)row.findViewById(R.id.itemIssueName);

        }
        else {
            holder = (EntryHolder)row.getTag();
        }

        if(holder != null) {
            final IssueCommon issue = issues.get(position);
            holder.issueName.setText(issue.name);
        }

        return row;
    }

    public List<IssueCommon> getData() {
        return issues;
    }

    public static class EntryHolder
    {
        TextView issueName;
    }
}
