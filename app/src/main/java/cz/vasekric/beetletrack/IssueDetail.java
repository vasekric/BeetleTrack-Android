package cz.vasekric.beetletrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.vasekric.beetletrack.models.IssueCommon;
import cz.vasekric.beetletrack.models.SpendTime;

public class IssueDetail extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;
    private int issueId;
    private IssueCommon actualIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);

        final Intent intent = getIntent();
        issueId = intent.getIntExtra("issueId", -1);
        if(issueId != -1) {
            new DownloadIssue().execute("http://185.8.164.56:8888/beetletrack.restapi-exploded/api/issues/", Integer.toString(issueId));
        }

        findViewById(R.id.dLogWork).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logHours();
            }
        });

        findViewById(R.id.dPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        final Intent showPhoto = new Intent(this, IssueDetail.class);
        findViewById(R.id.dPhotoView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String filename = "/beetletrack/" + issueId + ".png";
                File sd = Environment.getExternalStorageDirectory();
                File dest = new File(sd, filename);
                intent.setDataAndType(Uri.fromFile(dest), "image/*");
                startActivity(intent);
            }
        });

        try {
            String filename = "/beetletrack/"+issueId+".png";
            File sd = Environment.getExternalStorageDirectory();
            File dest = new File(sd, filename);
            FileInputStream in = new FileInputStream(dest);
            imageBitmap = BitmapFactory.decodeStream(in);
            final ImageView mImageView = (ImageView) findViewById(R.id.dPhotoView);
            mImageView.setImageBitmap(imageBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logHours() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log hours to task");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String hours = input.getText().toString();
                try {
                    final String url = "http://185.8.164.56:8888/beetletrack.restapi-exploded/api/issues/logwork/"+issueId+"?hours="+hours+"&force=true";
                    new LogHours().execute(url, hours);
                } catch (Exception e ) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            final ImageView mImageView = (ImageView) findViewById(R.id.dPhotoView);
            mImageView.setImageBitmap(imageBitmap);

            if(issueId != -1 && imageBitmap != null) {
                String filename = "/beetletrack/"+issueId+".png";
                File sd = Environment.getExternalStorageDirectory();
                new File(sd, "/beetletrack/").mkdir();
                File dest = new File(sd, filename);

                try {
                    FileOutputStream out = new FileOutputStream(dest);
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class LogHours extends AsyncTask<String, Integer, Integer> {
        protected Integer doInBackground(String ...args) {
            final String url = args[0];
            final RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                restTemplate.exchange(url, HttpMethod.PUT, null, null);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(url);
            }
            return Integer.parseInt(args[1]);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Integer hours) {
            final SpendTime spendTime = new SpendTime();
            spendTime.time = hours.longValue();
            actualIssue.spentTime.add(spendTime);
            reRender();
        }
    }

    private class DownloadIssue extends AsyncTask<String, Integer, IssueCommon> {
        protected IssueCommon doInBackground(String ...args) {
            final String url = args[0]+args[1];
            final RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
            final IssueCommon issue = restTemplate.getForObject(url, IssueCommon.class);
            return issue;
            }
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(url);
                return new IssueCommon();
            }
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(IssueCommon issue) {
            actualIssue = issue;
            reRender();
        }
    }

    public void reRender() {
        ((TextView)findViewById(R.id.dIssueName)).setText(actualIssue.name);
        ((TextView)findViewById(R.id.dDescription)).setText(actualIssue.description);
        ((TextView)findViewById(R.id.dAssignedTo)).setText(actualIssue.assignedTo != null
                ? "Assigned To: "+actualIssue.assignedTo.fullName
                : "No Assigned");
        Long estimated = 0L;
        if(actualIssue.estimatedTime != null) {
            estimated = actualIssue.estimatedTime;
        }
        ((TextView)findViewById(R.id.dEstimatedTime)).setText("Estimated: "+estimated+"h");
        Long spendTime = 0L;
        if (actualIssue.spentTime != null) {
            for(SpendTime time : actualIssue.spentTime) {
                spendTime += time.time;
            }
        }
        ((TextView)findViewById(R.id.dSpendTime)).setText("Spent: "+spendTime.toString()+"h");
        Long left = estimated-spendTime;
        ((TextView)findViewById(R.id.dHoursLeft)).setText("Left: "+left.toString()+"h");
        ((TextView)findViewById(R.id.dType)).setText(actualIssue.type != null ? "Type: "+actualIssue.type.getText() : "Type: none");
    }
}
