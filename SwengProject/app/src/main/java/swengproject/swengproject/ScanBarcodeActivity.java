package swengproject.swengproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static swengproject.swengproject.R.layout.scan_page;


/**
 * Created by McGroarty on 15/11/2016.
 */
public class ScanBarcodeActivity extends AppCompatActivity implements View.OnClickListener {

    final private int TYPE = 3;
    final private String CONTENT_VIEW = "";
    final public String RET_SERVER_NULL = "";
    private Button scanBtn;
    private ArrayList<String> DATA = new ArrayList<String>();
    private ArrayList<String> META_DATA = new ArrayList<String>();
    final String SERVER_URL = "SERVER URL";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(scan_page);

        scanBtn = (Button) findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);

        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();

    }

    public void onClick(View v) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            META_DATA.add("BARCODE_INFO");
            DATA.add(scanContent);

            META_DATA.add("BARCODE_FORMAT");
            DATA.add(scanFormat);

            Log.d("ScanBarcodeActivity", scanFormat + "\n" + scanContent);
            MyAsyncTask task = new ScanBarcodeActivity.MyAsyncTask();
            task.execute();
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Error. Could not scan.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setMargin(toast.getHorizontalMargin() / 2, toast.getVerticalMargin() / 2);
            toast.show();
        }
    }


    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            Log.d("ScanBarcodeActivity", "pre exe");
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder send = new StringBuilder();
            StringBuilder result = new StringBuilder();

            String x = null;
            String y = null;
            try {
                x = URLEncoder.encode(META_DATA.get(0), "UTF-8")
                        + "=" + URLEncoder.encode(DATA.get(0), "UTF-8");
                y = URLEncoder.encode(META_DATA.get(1), "UTF-8")
                        + "=" + URLEncoder.encode(DATA.get(1), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            send.append(x);
            send.append(y);

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
                    Log.d("ScanBarcodeActivity", "Success");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    Log.d("ScanBarcodeActivity", "Failure");

                } else {
                    Log.d("ScanBarcodeActivity", "Failure");
                    return null;
                }

            }catch(Exception ex) {
                Log.d("ScanBarcodeActivity", "Failure");
                return null;
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals(RET_SERVER_NULL)) {
                Intent passData = (new Intent(ScanBarcodeActivity.this, QueryActivity.class));
                passData.putExtra("DATA", DATA);
                passData.putExtra("META_DATA", META_DATA);
                passData.putExtra("TYPE", TYPE);
                passData.putExtra("ACTIVITY", scan_page);
                startActivity(passData);
            } else {
                Log.d("ScanBarcodeActivity","Object Found");
                //  setContentView(R.id.found_object);
            }
        }

        protected void onProgressUpdate(Integer... progress) {

        }

    }
}
