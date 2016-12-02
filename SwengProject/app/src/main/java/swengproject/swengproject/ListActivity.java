package swengproject.swengproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by McGroarty on 02/12/2016.
 */

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(list_options);

        Button reclaimBttn = (Button) findViewById(R.id.reclaimBttn);
        Button attachedBttn  = (Button) findViewById(R.id.attachedBttn);
        Button brokenBttn = (Button) findViewById(R.id.brokenBttn);
        reclaimBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        attachedBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        brokenBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
