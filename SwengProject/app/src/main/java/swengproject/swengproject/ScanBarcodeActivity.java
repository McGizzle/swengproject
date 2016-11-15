package swengproject.swengproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
public class ScanBarcodeActivity extends AppCompatActivity implements View.OnClickListener{

    final private int TYPE = 3;
    final private String CONTENT_VIEW = "";
    private Button scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(scan_page);

        scanBtn = (Button) findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);

        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();

    }
    public void onClick(View v){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        if(scanningResult!=null){
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            ArrayList<String> DATA = new ArrayList<String>();
            ArrayList<String> META_DATA = new ArrayList<String>();
            META_DATA.add("BARCODE_INFO");
            DATA.add(scanContent);
            META_DATA.add("BARCODE_FORMAT");
            DATA.add(scanFormat);

            Intent passData = (new Intent(ScanBarcodeActivity.this, QueryActivity.class));
            passData.putExtra("VIEW",CONTENT_VIEW);
            passData.putExtra("DATA",DATA);
            passData.putExtra("META_DATA",META_DATA);
            passData.putExtra("TYPE",TYPE);
            passData.putExtra("ACTIVITY",scan_page);
            startActivity(passData);

        }
        else {
            Context context = getApplicationContext();
            CharSequence text = "Error. Could not scan.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setMargin(toast.getHorizontalMargin() / 2, toast.getVerticalMargin() / 2);
            toast.show();
        }
    }

}
