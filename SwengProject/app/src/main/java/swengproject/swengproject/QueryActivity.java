package swengproject.swengproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    final String SERVER_URL = "SERVER URL";
    private ProgressBar pb;
    private Button btn;
    ArrayList<String> DATA;
    ArrayList<String> META_DATA;
    private String response;


    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();                //How to get Data from previous activity
        String view = extras.getString("VIEW");
        //setContentView(R.layout.view);

      //  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
      //  StrictMode.setThreadPolicy(policy);

        DATA  = extras.getStringArrayList("DATA");
        META_DATA = extras.getStringArrayList("META_DATA");

        pb = (ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.VISIBLE);
        new MyAsyncTask().execute();

    }

    /*
    * insertMySQLPost()
    * Params: ArrayList<String> = Data
    *         ArrayList<String> = Meta
    * Description: Function appends data to URL and attempts to POST data to PHP script. Also receives response.
    * Return: String - Response from server; NULL if unsuccessfull
    */

    public String insertMySQLPost() throws IOException {

        StringBuilder result = new StringBuilder();

        for(int i=0;i<META_DATA.size()&&i<DATA.size();i++) {
            String x = URLEncoder.encode(META_DATA.get(i), "UTF-8")
                    + "=" + URLEncoder.encode(DATA.get(i), "UTF-8");
            result.append(x);
            Log.d("INFO","Meta = "+META_DATA.get(i) + "Data = "+DATA.get(i));
        }

        // Send data
        try {
            // Defined URL  where to send data
            URL url = new URL(SERVER_URL);
            // Send POST data request
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(result.toString());
            wr.flush();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("Tag", "Success");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                insertSuccess();

            } else {
                Log.d("Tag", "Failure");
                insertFail();
                return null;
            }

        }catch(Exception ex) {
            ex.printStackTrace();
            Log.d("Tag", "Failure");
            return null;
        }
        response = result.toString();
        return result.toString();
    }

    /* insertSuccess()
     * Param: None
     * Description: Function is called when the information is successfully sent to the database
     * Return: None
     *
     */
    public void insertSuccess(){
        Context context = getApplicationContext();
        CharSequence text = "Information has been submitted to the database";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setMargin(toast.getHorizontalMargin() / 2, toast.getVerticalMargin() / 2);
        toast.show();
    }

    /* insertFail()
     * Param: None
     * Description: Function is called when the information is not successfully sent to the database
     * Return: None
     */
    public void insertFail(){
        Context context = getApplicationContext();
        CharSequence text = "Error. Please try again.";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setMargin(toast.getHorizontalMargin() / 2, toast.getVerticalMargin() / 2);
        toast.show();
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                insertMySQLPost();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(){
            pb.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
        }
        protected void onProgressUpdate(Integer... progress){
            pb.setProgress(progress[0]);
        }


}
