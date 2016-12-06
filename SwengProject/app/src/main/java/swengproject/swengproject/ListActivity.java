package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static swengproject.swengproject.R.layout.assigned_obj;
import static swengproject.swengproject.R.layout.broken_obj;
import static swengproject.swengproject.R.layout.list_options;
import static swengproject.swengproject.R.layout.reclaim_obj;


/**
 * Created by McGroarty on 02/12/2016.
 */

public class ListActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(list_options);

        Button reclaimBttn = (Button) findViewById(R.id.reclaimButton);
        Button attachedBttn = (Button) findViewById(R.id.assignedButton);
        Button brokenBttn = (Button) findViewById(R.id.brokenButton);
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
        setContentView(reclaim_obj);
        EditText dateET = (EditText) findViewById(R.id.dateText);
        String date = dateET.getText().toString();
        Intent i = new Intent(ListActivity.this,QueryActivity.class);
        i.putExtra("DATE",date);
        i.putExtra("LIST_TYPE","RECLAIMED");
        startActivityForResult(i,1);

    }
    public void attached(){
        setContentView(assigned_obj);
        EditText dateET = (EditText) findViewById(R.id.dateText);
        String date = dateET.getText().toString();

        Intent i = new Intent(ListActivity.this,QueryActivity.class);
        i.putExtra("DATE",date);
        i.putExtra("LIST_TYPE","ATTACHED");
        startActivityForResult(i,1);

    }
    public void broken(){
        setContentView(broken_obj);
        Intent i = new Intent(ListActivity.this,QueryActivity.class);
        i.putExtra("LIST_TYPE","BROKEN");
        startActivityForResult(i,1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                setContentView(show_list);
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

}
