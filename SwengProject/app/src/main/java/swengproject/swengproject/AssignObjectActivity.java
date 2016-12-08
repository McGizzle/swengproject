package swengproject.swengproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static swengproject.swengproject.R.layout.assign_object;
import static swengproject.swengproject.R.layout.generate_list_obj;

/**
 * Created by Dervla on 28/11/2016.
 */

public class AssignObjectActivity extends AppCompatActivity {
    final int TYPE = 4;
    private ProgressBar pb;
    EditText datePick;
    int yearX, dayX, monthX;
    static final int dialog = 0;
    private String barcode;
    private String[] info;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        info = extras.getStringArray("INFO");
        boolean found = extras.getBoolean("FOUND");
        if(found)
        {
          found_object();
        }

        else
        {
            barcode = info[1];
            add_clicked();
        }


    }

    public void found_object()
    {
        setContentView(generate_list_obj);

        int i=1;

        while(!info[i].equals("!"))
        {

        }
        EditText t = (EditText) findViewById(R.id.editText2);

        Button addObj = (Button) findViewById(R.id.addButton);
        addObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_clicked();
            }
        });

        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AssignObjectActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    public void add_clicked()
    {
        setContentView(assign_object);
        final Calendar cal = Calendar.getInstance();
        yearX = cal.get(Calendar.YEAR);
        monthX = cal.get(Calendar.MONTH);
        dayX = cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonClick();

        Log.d("TAG", barcode);

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
                Intent i = new Intent(AssignObjectActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public void showDialogOnButtonClick()
    {
        datePick = (EditText) findViewById(R.id.chooseDate);
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
            Toast.makeText(AssignObjectActivity.this, dayX + "/" + monthX + "/" + yearX, Toast.LENGTH_LONG).show();
        }
    };


    public void gather_info() {

        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE+"");

        EditText fn = (EditText) (findViewById(R.id.objectId));
        String name = fn.getText().toString();
        EditText ln = (EditText) findViewById(R.id.chooseDate);
        String date = ln.getText().toString();
        meta.add("NAME");
        data.add(name);
        meta.add("DATE");
        data.add(date);

        EditText i = (EditText) (findViewById(R.id.groupId));
        String group = i.getText().toString();
        meta.add("GROUP");
        data.add(group);

        meta.add("BARCODE");
        data.add(barcode);


        meta.add("BROKEN");
        data.add("false");

        meta.add("TYPE");
        data.add(""+TYPE);


        Intent passData = (new Intent(AssignObjectActivity.this, QueryActivity.class));
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",assign_object);
        passData.putExtra("PREVIOUS_ACTIVITY",AddProjectActivity.class);

        startActivity(passData);

    }
}