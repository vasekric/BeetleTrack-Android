package cz.vasekric.beetletrack;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cz.vasekric.beetletrack.models.IssueCommon;
import cz.vasekric.beetletrack.models.IssueType;
import cz.vasekric.beetletrack.models.Project;
import cz.vasekric.beetletrack.models.SpendTime;

public class CreateIssue extends AppCompatActivity {

    private int issueId = -1;
    private int projectId = -1;
    private Intent issueActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);

        issueActivity = new Intent(this, IssueList.class);

        final Spinner spinner = (Spinner) findViewById(R.id.cType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.issue_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        final Intent intent = getIntent();
        issueId = intent.getIntExtra("issueId", -1);
        projectId = intent.getIntExtra("projectId", -1);

        findViewById(R.id.cCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String selectedItem = (String)((Spinner) findViewById(R.id.cType)).getSelectedItem();
                final String name = ((TextView) findViewById(R.id.cName)).getText().toString();
                final String desc = ((TextView) findViewById(R.id.cDescription)).getText().toString();
                final String est = ((TextView) findViewById(R.id.cEstimated)).getText().toString();
                if(issueId != -1) {
                    new UploadIssue().execute("http://185.8.164.56:8888/beetletrack.restapi-exploded/api/issues/children/", Integer.toString(issueId), selectedItem, name, desc, est);
                }
                else if(projectId != -1) {
                    new UploadIssue().execute("http://185.8.164.56:8888/beetletrack.restapi-exploded/api/issues/project/", Integer.toString(projectId), selectedItem, name, desc, est);
                }
            }
        });
    }


    private class UploadIssue extends AsyncTask<String, Integer, IssueCommon> {
        protected IssueCommon doInBackground(String ...args) {
            final String url = args[0]+args[1];
            final RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            String type = args[2];
            IssueType issueType = IssueType.TASK;
            String clazz = "IssueLeaf";
            if(type.equals("Story")) {
                clazz = "IssueNode";
                issueType = IssueType.USER_STORY;
            }
            else if(type.equals("Epic")) {
                clazz = "IssueNode";
                issueType = IssueType.EPIC;
            }

            final IssueCommon issue = new IssueCommon();
            issue.clazz = clazz;
            issue.type = issueType;
            issue.name = args[3];
            issue.description = args[4];
            issue.estimatedTime = Long.parseLong(args[5]);


            try {
//                restTemplate.exchange(url, HttpMethod.POST, );
                restTemplate.postForLocation(url, issue);
//                final IssueCommon created = restTemplate.postForObject(url, issue, IssueCommon.class);
                return null;
            }
            catch (HttpClientErrorException e) {
                e.printStackTrace();
                System.err.println(url);
                return new IssueCommon();
            }
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(IssueCommon issue) {
            issueActivity.putExtra("issueId", issueId);
            issueActivity.putExtra("projectId", projectId);
            startActivityForResult(issueActivity, 1);

        }
    }
}
