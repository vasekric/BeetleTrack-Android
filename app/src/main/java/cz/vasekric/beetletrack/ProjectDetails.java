package cz.vasekric.beetletrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cz.vasekric.beetletrack.models.Project;

public class ProjectDetails extends AppCompatActivity {

    Intent issueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        issueList = new Intent(this, IssueList.class);

        loadProject();
    }

    private void loadProject() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        final Bundle entry = extras.getBundle("entry");
        assert entry != null;
        final Project project = (Project)entry.getSerializable("project");
        if(project == null) {
            return;
        }
        ((TextView)findViewById(R.id.projectName)).setText(project.name);
        ((TextView)findViewById(R.id.readme)).setText(project.readme);
        ((TextView)findViewById(R.id.projectManager)).setText("Project manager: "+project.projectManager.fullName);

        findViewById(R.id.seeIssuesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issueList.putExtra("projectId", project.id);
                startActivityForResult(issueList, 1);
            }
        });
    }
}
