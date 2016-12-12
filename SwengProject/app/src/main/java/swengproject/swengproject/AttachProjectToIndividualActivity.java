package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import static swengproject.swengproject.R.layout.indiv_to_project;


/**
 * Created by McGroarty on 07/12/2016.
 */

public class AttachProjectToIndividualActivity extends AppCompatActivity {

    protected String TYPE = "6";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(indiv_to_project);

        Button goBttn = (Button) findViewById(R.id.submitButton);
        goBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_info();
            }
        });
    }
    public void get_info(){
        EditText proj  = (EditText) findViewById(R.id.projectET);
        EditText nam  = (EditText) findViewById(R.id.personET);
        String project = proj.getText().toString();
        String name = nam.getText().toString();
        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE+"");
        meta.add("PROJECT_NAME");
        data.add(project);
        meta.add("PERSON_NAME");
        data.add(name);

        Intent passData = (new Intent(AttachProjectToIndividualActivity.this, QueryActivity.class));
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",indiv_to_project);
        passData.putExtra("PREVIOUS_ACTIVITY",AttachProjectToIndividualActivity.class);
        startActivity(passData);
    }
}
