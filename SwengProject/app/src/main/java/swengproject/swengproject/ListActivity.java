package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;

import static swengproject.swengproject.R.layout.assigned_obj;
import static swengproject.swengproject.R.layout.list_options;
import static swengproject.swengproject.R.layout.reclaim_obj;
import static swengproject.swengproject.R.layout.show_list;


/**
 * Created by McGroarty on 02/12/2016.
 */

public class ListActivity extends AppCompatActivity {

    private TextView tv;
    private final String TYPE = "5";
    private final String TAG = "ListActivity";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(list_options);

        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


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

        final Button retryBttn = (Button) findViewById(R.id.retryBttn);
        if(retryBttn != null) {
            retryBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"RETRY BUTTON");
                    broken();
                }
            });
        }
    }
    public void reclaimed(){
        setContentView(reclaim_obj);
        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        final Button findBttn = (Button) findViewById(R.id.findBttn);
        findBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
                String date="";
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                date += year + "-"+ month + "-" +day;
                ArrayList<String> data = new ArrayList<String>();
                ArrayList<String> meta = new ArrayList<String>();
                meta.add("TYPE");
                data.add(TYPE+"");
                meta.add("LIST_TYPE");
                data.add("RECLAIMED");
                meta.add("DATE");
                data.add(date);
                Intent i = new Intent(ListActivity.this,QueryActivity.class);
                i.putExtra("DATA", data);
                i.putExtra("META_DATA", meta);
                i.putExtra("PREVIOUS_ACTIVITY", ListActivity.class);
                i.putExtra("ACTIVITY", reclaim_obj);
                startActivityForResult(i,1);
            }
        });




    }
    public void attached(){
        setContentView(assigned_obj);
        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        final Button findBttn = (Button) findViewById(R.id.findBttn);
        findBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
                String date="";
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                date += year + "-"+ month + "-" +day;
                ArrayList<String> data = new ArrayList<String>();
                ArrayList<String> meta = new ArrayList<String>();
                meta.add("TYPE");
                data.add(TYPE+"");
                meta.add("LIST_TYPE");
                data.add("ATTACHED");
                meta.add("DATE");
                data.add(date);
                Intent i = new Intent(ListActivity.this,QueryActivity.class);
                i.putExtra("DATA", data);
                i.putExtra("META_DATA", meta);
                i.putExtra("PREVIOUS_ACTIVITY", ListActivity.class);
                i.putExtra("ACTIVITY", assigned_obj);
                startActivityForResult(i,1);
            }
        });

    }
    public void broken(){
        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE+"");
        meta.add("LIST_TYPE");
        data.add("BROKEN");
        Intent i = new Intent(ListActivity.this,QueryActivity.class);
        i.putExtra("DATA", data);
        i.putExtra("META_DATA", meta);
        i.putExtra("PREVIOUS_ACTIVITY", ListActivity.class);
        startActivityForResult(i,1);
        final Button retryBttn = (Button) findViewById(R.id.retryBttn);
        if(retryBttn != null) {
            retryBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "RETRY BUTTON");
                    broken();
                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                setContentView(show_list);
                Bundle extras = getIntent().getExtras();
                final String[] info = extras.getStringArray("INFO");
                tv = (TextView) findViewById(R.id.listTextView);
                String output="List\n\n";
                for(int i=1;i<info.length;i++){
                    Log.d("ListActivity",info[i]);
                    output += info[i];
                    output += "\n";
                }
                tv.setText(output);
            }
    }

}
