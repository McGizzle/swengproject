package swengproject.swengproject;

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
    }


}
