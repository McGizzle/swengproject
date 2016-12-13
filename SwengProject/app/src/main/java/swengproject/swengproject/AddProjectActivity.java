package swengproject.swengproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static swengproject.swengproject.R.layout.add_project;

/**
 * Created by McGroarty on 15/11/2016.
 */

public class AddProjectActivity extends AppCompatActivity {

    final int TYPE = 1;
    Button datePick;
    int yearX, dayX, monthX;
    static final int dialog = 0;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(add_project);

        final Calendar cal = Calendar.getInstance();
        yearX = cal.get(Calendar.YEAR);
        monthX = cal.get(Calendar.MONTH);
        dayX = cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonClick();



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
                Intent i = new Intent(AddProjectActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public void showDialogOnButtonClick()
    {
        datePick = (Button) findViewById(R.id.endDate);
        datePick.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog(dialog);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id == dialog)
        {
            return new DatePickerDialog(this, dPickerListner, yearX, monthX, dayX);
        }
        else
        {
            return null;
        }
    }

    private DatePickerDialog.OnDateSetListener dPickerListner
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            yearX = i;
            monthX = i1 + 1;
            dayX = i2;
            Toast.makeText(AddProjectActivity.this, dayX + "/" + monthX + "/" + yearX, Toast.LENGTH_LONG).show();
        }
    };
    public boolean gather_info() {

        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE+"");

        EditText n = (EditText) (findViewById(R.id.editText));
        if( n.getText().toString().length() == 0 ) {
            n.setError("Project name is required!");
            return false;
        }
        String name = n.getText().toString();
        meta.add("NAME");
        data.add(name);

        EditText i = (EditText) (findViewById(R.id.editText4));

        if( i.getText().toString().length() != 0 ) {
            String list = i.getText().toString();
            String[] separated = list.split(", ");
            meta.add("INDIVIDUALS_NUM");
            data.add("" + separated.length);
            for (int x = 0; x < separated.length; x++) {
                meta.add("INDIVIDUALS" + x);
                data.add(separated[x]);
            }
        }

        String end_date = yearX + "-" + monthX + "-" + dayX;

        meta.add("END_DATE");
        data.add(end_date);

        Intent passData = (new Intent(AddProjectActivity.this, QueryActivity.class));
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",add_project);
        passData.putExtra("PREVIOUS_ACTIVITY",AddProjectActivity.class);

        startActivityForResult(passData,0);
        return true;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode==0){
            if(resultCode==RESULT_OK){
                //TODO display List of people
            }
        }
    }
    }