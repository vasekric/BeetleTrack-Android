package cz.vasekric.beetletrack;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.URL;

import cz.vasekric.beetletrack.models.Project;

public class ProjectDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

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
    }




    private class DownloadProjectTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {

            return 0L;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {

        }
    }
}
