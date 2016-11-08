package swengproject.swengproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by McGroarty on 08/11/16.
 */
public class QueryActivity extends AppCompatActivity {
    final String serverURL = "SERVER URL";

    @Override
    protected void onCreate(final Bundle SavedInstanceState){
        super.onCreate(avedInstanceState);
        setContentView(R.layout.activity_query);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

    }

    /*
* insertMySQLPost()
* Params: None
* Description: Function appends data to URL and attempts to POST data to insert.php
* Return: Boolean: succesfully entered data
*/
    public boolean insertMySQLPost() throws IOException {

        String data = URLEncoder.encode("DATA NAME", "UTF-8")
                + "=" + URLEncoder.encode(DATA_VARIABLE, "UTF-8");


        // Send data
        try {
            // Defined URL  where to send data
            URL url = new URL(serverURL);
            // Send POST data request
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("Tag", "Success");
            } else {
                Log.d("Tag", "Failure");
                return false;
            }

        }catch(Exception ex) {
            ex.printStackTrace();
            Log.d("Tag", "Failure");
            return false;
        }

        return true;
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
        setContentView(R.layout.end_activity);
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
        Intent i = new Intent(QueryActivity.this, MainActivity.class);
        startActivity(i);
    }


}
