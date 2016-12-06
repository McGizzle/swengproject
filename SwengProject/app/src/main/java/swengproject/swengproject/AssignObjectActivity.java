package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static swengproject.swengproject.R.layout.assign_object;
import static swengproject.swengproject.R.layout.generate_list_obj;

/**
 * Created by Dervla on 28/11/2016.
 */

public class AssignObjectActivity extends AppCompatActivity {
    final int TYPE = 4;
    private ProgressBar pb;
    private String barcode;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String[] info = extras.getStringArray("INFO");
        barcode = info[1];
        boolean found = extras.getBoolean("FOUND");
        if(found)
        {
          found_object(barcode);
        }


    }

    public void found_object(String barcode)
    {
        setContentView(generate_list_obj);
        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);

        Button addObj = (Button) findViewById(R.id.submitButton); //CHANGE TO ADD BUTTON ID
        addObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_clicked();
            }
        });

        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AssignObjectActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    public void add_clicked()
    {
        setContentView(assign_object);
        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);

        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gather_info();
            }
        });

        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AssignObjectActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }


    public void gather_info() {

        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE+"");

        EditText fn = (EditText) (findViewById(R.id.objectId));
        String name = fn.getText().toString();
        EditText ln = (EditText) findViewById(R.id.datePick);
        String date = ln.getText().toString();
        meta.add("NAME");
        data.add(name);
        meta.add("DATE");
        data.add(date);

        EditText i = (EditText) (findViewById(R.id.groupId));
        String group = i.getText().toString();
        meta.add("GROUP");
        data.add(group);

        meta.add("BARCODE");
        data.add(barcode);

        EditText p = (EditText) (findViewById(R.id.groupId)); // change to whatever the personID is
        String person = p.getText().toString();
        meta.add("INDIVIDUAL");
        data.add(person);

        meta.add("DAMAGED");
        data.add("false");


        Intent passData = (new Intent(AssignObjectActivity.this, QueryActivity.class));
        passData.putExtra("TYPE",TYPE);
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",assign_object);
        passData.putExtra("PREVIOUS_ACTIVITY",AddProjectActivity.class);

        startActivity(passData);

    }
}