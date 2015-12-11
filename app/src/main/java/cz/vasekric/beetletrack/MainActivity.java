package cz.vasekric.beetletrack;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.vasekric.beetletrack.models.Project;

public class MainActivity extends Activity {

    ListView projectsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Project> projects = new ArrayList<>();

        projectsList = (ListView)findViewById(R.id.projectListView);
        new DownloadAllProjects().execute("http://demo9290911.mockable.io/api/projects/all");


        projectsList.setAdapter(new ProjectListAdapter(this, projects));
        final Intent projectDetail = new Intent(this, ProjectDetails.class);
        projectsList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Project project = (Project) projectsList.getItemAtPosition(position);
                final Bundle bundle = new Bundle();
                bundle.putSerializable("project", project);

                projectDetail.putExtra("entry", bundle);
                startActivityForResult(projectDetail, 1);
            }
        });
    }


    private class DownloadAllProjects extends AsyncTask<String, Integer, List<Project>> {
        protected List<Project> doInBackground(String ...urls) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Project[] projects = restTemplate.getForObject(urls[0], Project[].class);

            return Arrays.asList(projects);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(List<Project> projects) {
            final ProjectListAdapter listAdapter = (ProjectListAdapter) projectsList.getAdapter();
            final List<Project> data = listAdapter.getData();
            data.clear();
            data.addAll(projects);
            listAdapter.notifyDataSetChanged();
        }
    }
}