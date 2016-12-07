package swengproject.swengproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by McGroarty on 08/11/16.
 */
public class QueryActivity extends AppCompatActivity {
    final String SERVER_URL = "http://humancentredmovement.ie/database.php";
    private ProgressBar pb;
    ArrayList<String> DATA;
    ArrayList<String> META_DATA;
    private MyAsyncTask task;
    private Class PREV_ACTIVITY;
    private final String OBJECT_NOT_FOUND = "0";
    private final String SUCCESS_RESPONSE = "1";
    private final String OBJECT_FOUND = "2";
    private final String LIST = "3";
    private final String DUPLICATE = "4";



    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();                //How to get Data from previous activity
        int activity =  extras.getInt("ACTIVITY");
        setContentView(activity);
        PREV_ACTIVITY = (Class) extras.get("PREVIOUS_ACTIVITY");

        pb = (ProgressBar) findViewById(R.id.progressBar);
        DATA  = extras.getStringArrayList("DATA");
        META_DATA = extras.getStringArrayList("META_DATA");
        if(DATA.get(0)==null){
            finish();
        }
        else {
            task = new MyAsyncTask();
            task.execute();
        }

    }

    /*
    * insertMySQLPost()
    * Params: ArrayList<String> = Data
    *         ArrayList<String> = Meta
    * Description: Function appends data to URL and attempts to POST data to PHP script. Also receives response.
    * Return: String - Response from server; NULL if unsuccessfull
    */

    public String insertMySQLPost() throws IOException {

        StringBuilder send = new StringBuilder();
        StringBuilder result = new StringBuilder();

        String x = URLEncoder.encode(META_DATA.get(0), "UTF-8")
                + "=" + URLEncoder.encode(DATA.get(0), "UTF-8");
        send.append(x);

        for(int i=1;i<META_DATA.size()&&i<DATA.size();i++) {

            x += "&" + URLEncoder.encode(META_DATA.get(i), "UTF-8") + "="
                    + URLEncoder.encode(DATA.get(i), "UTF-8");
            send.append(x);
            Log.d("INFO","Meta = "+META_DATA.get(i) + " Data = "+DATA.get(i));
        }

        // Send data
        try {
            // Defined URL  where to send data
            URL url = new URL(SERVER_URL);
            // Send POST data request
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(send.toString());
            wr.flush();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("Tag", "Success");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("TAG",line);
                    result.append(line);
                }
                return result.toString();

            } else {
                Log.d("Tag 1", "Failure");
                return null;
            }

        }catch(Exception ex) {
            Log.d("Tag 2", "Failure");
            return null;
        }

       // return result.toString();
    }

    /* insertSuccess()
     * Param: None
     * Description: Function is called when the information is successfully sent to the database
     * Return: None
     *
     */
    public void insertSuccess(){
        new AlertDialog.Builder(QueryActivity.this)
                .setTitle("Success!")
                .setMessage("Your Information was successfully submitted.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent =  new Intent(QueryActivity.this , PREV_ACTIVITY);
                        startActivity(intent);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /* insertFail()
     * Param: None
     * Description: Function is called when the information is not successfully sent to the database
     * Return: None
     */
    public void insertFail(){
        new AlertDialog.Builder(QueryActivity.this)
                .setTitle("Uh Oh!")
                .setMessage("Something went wrong and it probably was not our fault." +
                        " Check your internet connection and try again")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent =  new Intent(QueryActivity.this , PREV_ACTIVITY);
                        finish();
                        startActivity(intent);

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
    public void insertDuplicate(){
        new AlertDialog.Builder(QueryActivity.this)
                .setTitle("Sorry :(")
                .setMessage("A project with this name already exists.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent =  new Intent(QueryActivity.this , PREV_ACTIVITY);
                        finish();
                        startActivity(intent);

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            Log.d("TAG","pre exe");
        }

        @Override
        protected String doInBackground(String... params) {
            String r = null;
            try {
                r =  insertMySQLPost();
                Log.d("RESULT",r);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }
        @Override
        protected void onPostExecute(String r){

            String[] result = r.split("#");
            pb.setVisibility(View.GONE);
            Log.d("RESULT",result[0]);

            if(result[0].equals(SUCCESS_RESPONSE)){
                insertSuccess();
            }
            else if(result[0].equals(OBJECT_FOUND)){
                Intent i = new Intent(QueryActivity.this,AssignObjectActivity.class);
                i.putExtra("INFO",result);
                i.putExtra("FOUND",true);
                startActivity(i);
            }
            else if(result[0].equals(OBJECT_NOT_FOUND)){
                Log.d("TAG","OBJECT NOT FOUND");
                Intent i = new Intent(QueryActivity.this,AssignObjectActivity.class);
                i.putExtra("INFO",result);
                i.putExtra("FOUND",false);
                startActivity(i);
            }
            else if(result[0].equals(LIST)){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("INFO",result);
                setResult(ListActivity.RESULT_OK,returnIntent);
                finish();
            }
            else if(result[0].equals(DUPLICATE)){
                insertDuplicate();
            }
            else {
                insertFail();
            }

        }
        protected void onProgressUpdate(Integer... progress){

        }
}
}
