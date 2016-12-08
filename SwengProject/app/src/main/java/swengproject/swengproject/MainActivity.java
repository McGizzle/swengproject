package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add_proj =  (Button) findViewById(R.id.new_p_btn);     //declares the button
        Button scanBtn = (Button) findViewById(R.id.button4);
        Button add_idv = (Button) findViewById(R.id.new_indv_btn);
        Button listBttn = (Button) findViewById(R.id.listBttn);
        Button individuals = (Button) findViewById(R.id.individual);

        add_proj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG","button clicked");
                Intent i = (new Intent(MainActivity.this, AddProjectActivity.class));
                startActivity(i);
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG","button clicked");
                Intent i = (new Intent(MainActivity.this, ScanBarcodeActivity.class));
                startActivity(i);
            }
        });

        add_idv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG","button clicked");
                Intent i = new Intent(MainActivity.this, AddIndividualActivity.class);
                startActivity(i);
            }
        });

        listBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG","list button");
                Intent i =new Intent(MainActivity.this, ListActivity.class);
                startActivity(i);
            }
        });

        individuals.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, IndividualOptionsActivity.class);
                startActivity(i);
            }
        });

    }


}
