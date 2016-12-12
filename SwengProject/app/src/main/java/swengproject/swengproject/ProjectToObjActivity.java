package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static swengproject.swengproject.R.layout.proj_to_obj;

/**
 * Created by McGroarty on 15/11/2016.
 */

public class ProjectToObjActivity extends AppCompatActivity {

    final int TYPE = 7;
    private ProgressBar pb;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(proj_to_obj);

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
                Intent i = new Intent(ProjectToObjActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean gather_info() {

        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE+"");


        EditText fn = (EditText) (findViewById(R.id.personET));
        if( fn.getText().toString().length() == 0 ) {
            fn.setError("Object name is required!");
            return false;
        }
        String fname = fn.getText().toString();
        meta.add("OBJECT");
        data.add(fname);
        EditText i = (EditText) (findViewById(R.id.objectET));
        if( i.getText().toString().length() == 0 ) {
            i.setError("Project name is required!");
            return false;
        }
        String pro = i.getText().toString();
        meta.add("PROJECT");
        data.add(pro);


        Intent passData = (new Intent(ProjectToObjActivity.this, QueryActivity.class));
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",proj_to_obj);
        passData.putExtra("PREVIOUS_ACTIVITY",IndividualToObj.class);

        startActivity(passData);
        return true;

    }
}