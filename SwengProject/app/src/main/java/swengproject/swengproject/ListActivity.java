package swengproject.swengproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by McGroarty on 02/12/2016.
 */

public class ListActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(list_options);

        Button reclaimBttn = (Button) findViewById(R.id.reclaimBttn);
        Button attachedBttn = (Button) findViewById(R.id.attachedBttn);
        Button brokenBttn = (Button) findViewById(R.id.brokenBttn);
        reclaimBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclaimed();
            }
        });
        attachedBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attached();
            }
        });
        brokenBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broken();
            }
        });

    }
    public void reclaimed(){
        setContentView(reclaimed_list);
    }
    public void attached(){
        setContentView(attached_list);
    }
    public void broken(){
        setContentView(broken_list);
    }
    public void onResult(){
        Bundle extras = getIntent().getExtras();
        final String[] info = extras.getStringArray("INFO");
        tv = (TextView) findViewById(R.id.textViewList);
        String output="List\n\n";
        for(int i=1;i<info.length;i++){
            output += info[i];
            output += "\n";
        }
        tv.setText(output);
    }

}
