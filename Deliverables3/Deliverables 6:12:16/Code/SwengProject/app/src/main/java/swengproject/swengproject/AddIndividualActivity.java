package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static swengproject.swengproject.R.layout.add_individual;
import static swengproject.swengproject.R.layout.add_project;

/**
 * Created by McGroarty on 15/11/2016.
 */

public class AddIndividualActivity extends AppCompatActivity {

    final int TYPE = 2;
    private ProgressBar pb;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(add_individual);

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
                Intent i = new Intent(AddIndividualActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public void gather_info() {

        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE+"");


        EditText fn = (EditText) (findViewById(R.id.firstName));
        String fname = fn.getText().toString();
        EditText ln = (EditText) findViewById(R.id.secondName);
        String lname = ln.getText().toString();
        meta.add("NAME");
        data.add(fname+" "+lname);

        EditText i = (EditText) (findViewById(R.id.teamName));
        String list = i.getText().toString();
        String[] separated = list.split(",");
        meta.add("TEAM_NUM");
        data.add(""+separated.length);
        for(int x=0;x<separated.length;x++){
            meta.add("TEAM"+x);
            data.add(separated[x]);
        }


        Intent passData = (new Intent(AddIndividualActivity.this, QueryActivity.class));
        passData.putExtra("TYPE",TYPE);
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",add_individual);
        passData.putExtra("PREVIOUS_ACTIVITY",AddProjectActivity.class);

        startActivity(passData);

    }
}