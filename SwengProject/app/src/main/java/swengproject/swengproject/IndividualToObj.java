package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import static swengproject.swengproject.R.layout.proj_to_obj;

/**
 * Created by McGroarty on 15/11/2016.
 */

public class IndividualToObj extends AppCompatActivity {

    final int TYPE = 8;
    private String id="";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(proj_to_obj);

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
                Intent i = new Intent(IndividualToObj.this, MainActivity.class);
                startActivity(i);

            }
        });

        Bundle extras = getIntent().getExtras();
        id = extras.getString("OBJECT_ID");
    }




    public boolean gather_info() {

        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE+"");


        EditText fn = (EditText) (findViewById(R.id.projectET));
        EditText i = (EditText) (findViewById(R.id.objectET));
        if( i.getText().toString().length() == 0 && fn.getText().toString().length() == 0 ) {
            i.setError("Person or Project name is required!");
            return false;
        }


        String fname = fn.getText().toString();
        meta.add("PROJECT_NAME");
        data.add(fname);
        String p = i.getText().toString();
        meta.add("PERSON_NAME");
        data.add(p);
        meta.add("OBJECT_ID");
        data.add(id);
        meta.add("TYPE");
        data.add(""+TYPE);


        Intent passData = (new Intent(IndividualToObj.this, QueryActivity.class));
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);

        startActivity(passData);
        return true;

    }
}