package swengproject.swengproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import static swengproject.swengproject.R.layout.scan_page;


/**
 * Created by McGroarty on 15/11/2016.
 */
public class ScanBarcodeActivity extends AppCompatActivity {

    final private int TYPE = 3;
    private final String TAG = "ScanBarcodeActivity";
    private Button scanBtn;
    private ArrayList<String> DATA = new ArrayList<String>();
    private ArrayList<String> META_DATA = new ArrayList<String>();
    final String SERVER_URL = "SERVER URL";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(scan_page);



        scanBtn = (Button) findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               IntentIntegrator scanIntegrator = new IntentIntegrator(ScanBarcodeActivity.this);
               scanIntegrator.initiateScan();
           }
       });


        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();


        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScanBarcodeActivity.this, MainActivity.class);
                startActivity(i);
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        Log.d(TAG,"Scan result = "+scanningResult);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            META_DATA.add("BARCODE_INFO");
            DATA.add(scanContent);
            Log.d(TAG,scanContent);

            META_DATA.add("BARCODE_FORMAT");
            DATA.add(scanFormat);
            META_DATA.add("TYPE");
            DATA.add(""+TYPE);

            Log.d("ScanBarcodeActivity", scanFormat + "\n" + scanContent + "\n" + scanningResult);
            Intent passData = (new Intent(ScanBarcodeActivity.this, QueryActivity.class));
            passData.putExtra("DATA", DATA);
            passData.putExtra("META_DATA", META_DATA);
            passData.putExtra("PREVIOUS_ACTIVITY", ScanBarcodeActivity.class);
            passData.putExtra("ACTIVITY", scan_page);
            startActivityForResult(passData,0);

        } else {
            Context context = getApplicationContext();
            CharSequence text = "Error. Could not scan.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setMargin(toast.getHorizontalMargin() / 2, toast.getVerticalMargin() / 2);
           // toast.show();
            scanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator scanIntegrator = new IntentIntegrator(ScanBarcodeActivity.this);
                    scanIntegrator.initiateScan();
                }
            });
        }
    }



}
