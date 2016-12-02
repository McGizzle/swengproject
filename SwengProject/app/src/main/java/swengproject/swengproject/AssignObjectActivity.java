package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static swengproject.swengproject.R.layout.add_project;
import static swengproject.swengproject.R.layout.assign_object;

/**
 * Created by Dervla on 28/11/2016.
 */

public class AssignObjectActivity extends AppCompatActivity {
    final int TYPE = 4;
    private ProgressBar pb;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //who to add as person, is there a group leader?


        Intent passData = (new Intent(AssignObjectActivity.this, QueryActivity.class));
        passData.putExtra("TYPE",TYPE);
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",add_project);
        passData.putExtra("PREVIOUS_ACTIVITY",AddProjectActivity.class);

        startActivity(passData);

    }
}