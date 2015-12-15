package cz.vasekric.beetletrack;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.vasekric.beetletrack.models.IssueCommon;

public class IssueList extends AppCompatActivity {

    ListView issueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_list);
        issueList = (ListView) findViewById(R.id.issueList);
        issueList.setAdapter(new IssueListAdapter(this, new ArrayList<IssueCommon>()));

        final Intent intent = getIntent();
        final int projectId = intent.getIntExtra("projectId", -1);
        final int issueId = intent.getIntExtra("issueId", -1);
        if (projectId != -1) {
            new DownloadIssues().execute("http://185.8.164.56:8888/beetletrack.restapi-exploded/api/issues/project/", Integer.toString(projectId));
        } else if (issueId != -1) {
            new DownloadIssues().execute("http://185.8.164.56:8888/beetletrack.restapi-exploded/api/issues/children/", Integer.toString(issueId));
        }

        final Intent issueNode = new Intent(this, IssueList.class);
        final Intent issueLeaf = new Intent(this, IssueDetail.class);
        issueList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final IssueCommon issue = (IssueCommon) issueList.getItemAtPosition(position);
                if (issue.clazz.equals("IssueNode")) {
                    issueNode.putExtra("issueId", issue.id);
                    startActivityForResult(issueNode, 1);
                } else if (issue.clazz.equals("IssueLeaf")) {
                    issueLeaf.putExtra("issueId", issue.id);
                    startActivityForResult(issueLeaf, 1);
                }
            }
        });

        final Intent createIssue = new Intent(this, CreateIssue.class);
        findViewById(R.id.createIssue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createIssue.putExtra("issueId", issueId);
                createIssue.putExtra("projectId", projectId);
                startActivityForResult(createIssue, 1);
            }
        });
    }


    private class DownloadIssues extends AsyncTask<String, Integer, List<IssueCommon>> {
        protected List<IssueCommon> doInBackground(String... args) {
            final String url = args[0] + args[1];
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                IssueCommon[] issues = restTemplate.getForObject(url, IssueCommon[].class);
                return Arrays.asList(issues);
            } catch (HttpClientErrorException e) {
                e.printStackTrace();
                System.err.println(url);
                return new ArrayList<>();
            }
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(List<IssueCommon> issues) {
            final IssueListAdapter listAdapter = (IssueListAdapter) issueList.getAdapter();
            final List<IssueCommon> data = listAdapter.getData();
            data.clear();
            data.addAll(issues);
            listAdapter.notifyDataSetChanged();
        }
    }
}
