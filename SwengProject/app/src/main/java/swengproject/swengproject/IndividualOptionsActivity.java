package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static swengproject.swengproject.R.layout.individual_options;

/**
 * Created by markmurtagh on 08/12/2016.
 */

public class IndividualOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(individual_options);

        Button objectToIndividule = (Button) findViewById(R.id.objToIndividule);
        objectToIndividule.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IndividualOptionsActivity.this, IndividualToObj.class);
                startActivity(i);

            }
        });

        Button individualToProject = (Button) findViewById(R.id.individuleToProj);
        individualToProject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IndividualOptionsActivity.this, AttachProjectToIndividualActivity.class);
                startActivity(i);

            }
        });

        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IndividualOptionsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });



    }

}
