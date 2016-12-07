package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


/**
 * Created by McGroarty on 07/12/2016.
 */

public class AttachProjectToIndividualActivity extends AppCompatActivity {

    protected String TYPE = "";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attach_proj_indiv);

        Button goBttn = (Button) findViewById(R.id.goBttn);
        goBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_info();
            }
        });
    }
    public void get_info(){
        EditText proj  = (EditText) findViewById(R.id.projET);
        EditText nam  = (EditText) findViewById(R.id.nameET);
        String project = proj.getText().toString();
        String name = nam.getText().toString();
        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE+"");
        meta.add("PROJECT_NAME");
        data.add(project);
        meta.add("NAME");
        data.add(name);

        Intent passData = (new Intent(AttachProjectToIndividualActivity.this, QueryActivity.class));
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",attach_proj_indiv);
        passData.putExtra("PREVIOUS_ACTIVITY",AttachProjectToIndividualActivity.class);
        startActivity(passData);
    }
}
