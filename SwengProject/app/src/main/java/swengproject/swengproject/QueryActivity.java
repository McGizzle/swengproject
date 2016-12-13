package swengproject.swengproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
import java.util.Arrays;

/**
 * Created by McGroarty on 08/11/16.
 */
public class QueryActivity extends AppCompatActivity {
    final String SERVER_URL = "http://humancentredmovement.ie/database.php";
    private ProgressDialog pd;
    ArrayList<String> DATA;
    ArrayList<String> META_DATA;
    private MyAsyncTask task;
    private final String FAIL_RESPONSE = "0";
    private final String SUCCESS_RESPONSE = "1";
    private final String OBJECT_FOUND = "2";
    private final String OBJECT_NOT_FOUND = "3";
    private final String LIST = "4";
    private final String TAG = "QueryActivity";



    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();                //How to get Data from previous activity


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
        Log.d(TAG,"Meta = "+META_DATA.get(0) + "  Data = "+DATA.get(0));

        for(int i=1;i<META_DATA.size()&&i<DATA.size();i++) {

            x += "&" + URLEncoder.encode(META_DATA.get(i), "UTF-8") + "="
                    + URLEncoder.encode(DATA.get(i), "UTF-8");
            send.append(x);
            Log.d(TAG,"Meta = "+META_DATA.get(i) + "  Data = "+DATA.get(i));
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
                Log.d(TAG, "Success");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();

            } else {
                Log.d(TAG, "Failure");
                insertFail("No internet connection :(");
                return null;
            }

        }catch(Exception ex) {
            Log.d(TAG, "Failure");
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
    public void insertSuccess(String message){
        new AlertDialog.Builder(QueryActivity.this)
                .setTitle("Success!")
                .setMessage(message)
                .setNeutralButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();

    }

    /* insertFail()
     * Param: String error message
     * Description: Function is called when the information is not successfully sent to the database
     * Return: None
     */
    public void insertFail(String error){
       AlertDialog x = new AlertDialog.Builder(QueryActivity.this)
                .setTitle("Uh Oh!")
                .setMessage(error)
                .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        x.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
         //   pb.setVisibility(View.VISIBLE);
            pd = ProgressDialog.show(QueryActivity.this, "Loading",
                    "1 2 3...", true);
            Log.d(TAG,"PRE EXECUTION");
        }

        @Override
        protected String doInBackground(String... params) {
            String r = null;
            try {
                r =  insertMySQLPost();
                Log.d(TAG,"RETURN FROM SERVER ="+r);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }
        @Override
        protected void onPostExecute(String r) {

            if (r == null) {
                Log.d(TAG, "Failed to connect to server");
                insertFail("Failed to connect to server. Please check your internet connection and try again");

            }
            else {
                String[] result = r.split("#");
                String[] object_info = Arrays.copyOfRange(result, 1, result.length);
                String response_code = result[0].replaceAll("\\s+", "");

                pd.dismiss();
                Log.d(TAG, "RESPONSE CODE =" + response_code);
                if (response_code.equals(FAIL_RESPONSE)) {
                    Log.d(TAG, "FAIL RESPONSE");
                    String error = result[1];
                    insertFail(error);
                } else if (response_code.equals(SUCCESS_RESPONSE)) {
                    Log.d(TAG, "SUCCESS RESPONSE");
                    insertSuccess(result[1]);
                } else if (response_code.equals(OBJECT_FOUND)) {
                    Log.d(TAG, "OBJECT FOUND");
                    Intent i = new Intent(QueryActivity.this, AssignObjectActivity.class);
                    i.putExtra("INFO", object_info);
                    i.putExtra("FOUND", true);
                    startActivity(i);
                    finish();

                } else if (response_code.equals(OBJECT_NOT_FOUND)) {
                    Log.d(TAG, "OBJECT NOT FOUND");
                    Intent i = new Intent(QueryActivity.this, AssignObjectActivity.class);
                    i.putExtra("INFO", object_info);
                    i.putExtra("FOUND", false);
                    startActivity(i);
                    finish();

                } else if (response_code.equals(LIST)) {
                    Log.d(TAG, "LIST FOUND");
                    Intent i = new Intent(QueryActivity.this, AssignObjectActivity.class);
                    i.putExtra("INFO", object_info);
                    i.putExtra("LIST_TYPE", result[1]);
                    i.putExtra("FOUND", true);
                    startActivity(i);
                    finish();

                } else {
                    Log.d(TAG, "UNKNOWN RESPONSE CODE =[" + result[0] + "]");
                }
            }
        }

}
}
