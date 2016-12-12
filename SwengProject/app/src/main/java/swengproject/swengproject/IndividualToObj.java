package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static swengproject.swengproject.R.layout.indiv_to_obj;

/**
 * Created by McGroarty on 15/11/2016.
 */

public class IndividualToObj extends AppCompatActivity {

    final int TYPE = 6;
    private ProgressBar pb;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(indiv_to_obj);

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
                Intent i = new Intent(IndividualToObj.this, MainActivity.class);
                startActivity(i);

            }
        });
    }




    public void gather_info() {

        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE+"");


        EditText fn = (EditText) (findViewById(R.id.personET));
        String fname = fn.getText().toString();
        meta.add("NAME");
        data.add(fname);
        EditText i = (EditText) (findViewById(R.id.objectET));
        String obj = i.getText().toString();
        meta.add("OBJECT");
        data.add(obj);


        Intent passData = (new Intent(IndividualToObj.this, QueryActivity.class));
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",indiv_to_obj);
        passData.putExtra("PREVIOUS_ACTIVITY",IndividualToObj.class);

        startActivity(passData);

    }
}