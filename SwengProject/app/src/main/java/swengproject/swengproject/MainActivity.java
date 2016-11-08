package swengproject.swengproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = new Button(findViewById(R.test_button));     //declares the button
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code for what happens when button pressed goes here
            }
        });

        // Below is an example of how data is passed from one activity to the next
        // The "DATA NAME" is how the data is refernced in the next activity
        // Lots of information can be passed through this
        // startActivity(passData) starts the next activity

        String DATA = "";
        Intent passData = (new Intent(MainActivity.this, QueryActivity.class));
        passData.putExtra("DATA NAME",DATA);
        passData.putExtra("DATA2 NAME",DATA);
        startActivity(passData);

    }


}
