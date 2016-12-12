package swengproject.swengproject;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static swengproject.swengproject.R.layout.assign_object;
import static swengproject.swengproject.R.layout.generate_list_obj;


/**
 * Created by Dervla on 28/11/2016.
 */

public class AssignObjectActivity extends AppCompatActivity {

    final int TYPE1 = 4;
    final int TYPE2 = 7;
    Button datePick;
    int yearX, dayX, monthX;
    static final int dialog = 0;
    private String barcode;
    private String[] info;
    private String listType;
    String TAG = "AssignObjectActivity";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        info = extras.getStringArray("INFO");
        listType = extras.getString("LIST_TYPE");
        boolean found = extras.getBoolean("FOUND");
        barcode = info[0];

        if(found){
            found_object(listType);
        }

        else {
            add_clicked();
        }
    }

    public void found_object(String listType)
    {
        setContentView(generate_list_obj);


        for(int m=0;!info[m].equals("!");m++) {
            Log.d(TAG, info.toString());
        }

        Button addObj = (Button) findViewById(R.id.addButton);
        addObj.setVisibility(View.INVISIBLE);
        addObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_clicked();
            }
        });


        String[] test = new String[info.length/4];
        TextView bc = (TextView) findViewById(R.id.barcodeName);

        int i=1;
        switch (listType)
        {
            case "BROKEN":
                bc.setText("List of Broken Objects");
                break;
            case "ATTACHED":
                bc.setText("List of Objects Attached as of a User Specified Date");
                break;
            case "RECLAIMED":
                bc.setText("List of Objects to be Reclaimed");
                break;
            default:bc.setText("Barcode Number "+barcode);
                addObj.setVisibility(View.VISIBLE);
                i=0;


        }

        int j=0;
       final ArrayList<String> obj_ids = new ArrayList<>();
        String tmp;
        while(!info[i].equals("!")) {
            tmp="";
            String person = info[++i];
            if(person.length()==0)
                tmp += "Person = unassigned \n ";

            else
                tmp += "Person = " + person+" \n" ;

            String project = info[++i];
            if(person.length()==0)
                tmp += "Project = unassigned \n";
            else
                tmp += "Project = " + project+" \n";

            tmp += "Object Name = " + info[++i] +" \n"; //ObjectName
            tmp += "Object ID = " + info[++i] +" \n"; //ObjectID
            tmp += "\n\n";
            obj_ids.add(info[i]);
            i++;
            test[j]=tmp;
            j++;

        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, test);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);


        final Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        final String[] items = new String[obj_ids.size()+1];
        for(int x=0;x<obj_ids.size();x++){
            items[x] = obj_ids.get(x);

        }
        items[items.length-1] = "";

        ArrayAdapter<String> adapterS = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapterS);
        dropdown.setSelection(items.length-1);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"Pos = "+position);
                if(position!=items.length-1) {
                    markBroken(obj_ids.get(position));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    dropdown.setSelected(false);
                    dropdown.setSelection(items.length-1);
            }
        });
    }

    public void markBroken(String id)
    {
        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE2+"");

        meta.add("OBJECT_ID");
        data.add(id);

        Intent passData = (new Intent(AssignObjectActivity.this, QueryActivity.class));
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",generate_list_obj);
        passData.putExtra("PREVIOUS_ACTIVITY",AddProjectActivity.class);

        startActivity(passData);
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
        datePick = (Button) findViewById(R.id.chooseDate);
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
            String showDate =  "" + dayX + "/" + monthX + "/" + yearX;
            TextView dateShow = (TextView) findViewById(R.id.dateShow);
            dateShow.setText(showDate);
        }
    };


    public boolean gather_info() {

        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        meta.add("TYPE");
        data.add(TYPE1+"");

        EditText fn = (EditText) (findViewById(R.id.objectId));
        if( fn.getText().toString().length() == 0 ) {
            fn.setError("Object name is required!");
            return false;
        }
        String name = fn.getText().toString();

        String date = "" + dayX + "/" + monthX + "/" + yearX;

        meta.add("OBJECT_NAME");
        data.add(name);
        meta.add("DATE");
        data.add(date);

        EditText i = (EditText) (findViewById(R.id.personET));
        String person = i.getText().toString();
        meta.add("PERSON_NAME");
        data.add(person);

        EditText p = (EditText) (findViewById(R.id.groupId));
        String group = p.getText().toString();
        meta.add("PROJECT_NAME");
        data.add(group);

        meta.add("BARCODE");
        data.add(barcode);


        meta.add("BROKEN");
        data.add("false");

        meta.add("TYPE");
        data.add(""+TYPE1);


        Intent passData = (new Intent(AssignObjectActivity.this, QueryActivity.class));
        passData.putExtra("DATA",data);
        passData.putExtra("META_DATA",meta);
        passData.putExtra("ACTIVITY",assign_object);
        passData.putExtra("PREVIOUS_ACTIVITY",AddProjectActivity.class);

        startActivity(passData);
        return true;

    }
}