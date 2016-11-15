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

        Button b =  (Button) findViewById(R.id.new_p_btn);     //declares the button

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = (new Intent(MainActivity.this, AddProjectActivity.class));
                startActivity(i);
            }
        });

        // Below is an example of how data is passed from one activity to the next
        // The "DATA NAME" is how the data is refernced in the next activity
        // Lots of information can be passed through this
        // startActivity(passData) starts the next activity



    }


}
