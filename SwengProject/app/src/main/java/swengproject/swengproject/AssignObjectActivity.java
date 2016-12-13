package swengproject.swengproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static swengproject.swengproject.R.layout.assign_object;
import static swengproject.swengproject.R.layout.generate_list_obj;


/**
 * Created by Dervla on 28/11/2016.
 */

public class AssignObjectActivity extends AppCompatActivity {

    final int TYPE1 = 4;
    final int TYPE2 = 7;
    private String barcode;
    private String[] info;
    private String listType;
    String TAG = "AssignObjectActivity";
    ArrayAdapter adapter;
    ArrayAdapter adapterS;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button homeBttn = (Button) findViewById(R.id.homeButton);
        if(homeBttn!=null) {
            homeBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(AssignObjectActivity.this, MainActivity.class);
                    startActivity(i);
                }
            });
        }

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

        final Spinner dropdown = (Spinner)findViewById(R.id.spinner1);

        Button addObj = (Button) findViewById(R.id.addButton);
        addObj.setVisibility(View.INVISIBLE);
        addObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_clicked();
            }
        });


        ArrayList<String> details = new ArrayList<>();
        TextView bc = (TextView) findViewById(R.id.barcodeName);

        int i=1;
        if(listType!=null) {
            switch (listType) {
                case "BROKEN":
                    bc.setText("List of Broken Objects");
                    dropdown.setVisibility(View.INVISIBLE);
                    TextView t = (TextView) findViewById(R.id.brokenSetTV);
                    t.setVisibility(View.INVISIBLE);
                    break;
                case "ATTACHED":
                    bc.setText("List of Objects Attached as of a User Specified Date");
                    break;
                case "RECLAIMED":
                    bc.setText("List of Objects to be Reclaimed");
                    break;
                default:
                    bc.setText("Barcode Number " + barcode);
                    addObj.setVisibility(View.VISIBLE);
                    i = 0;

            }
        }
        else{
            bc.setText("Barcode Number " + barcode);
            addObj.setVisibility(View.VISIBLE);
            i = 0;
        }

       final ArrayList<String> obj_ids = new ArrayList<>();
        while(!info[i].equals("!")) {

            StringBuilder tmp = new StringBuilder();
            String person = info[++i];
            if(person.equals("")) {
                person = "Unassigned";
            }
            String project = info[++i];
            if(project.equals("")) {
                project = "Unassigned";
            }

            tmp.append("Person = " + person+" \n");
            tmp.append("Project = " + project+" \n");
            tmp.append("Object Name = " + info[++i] +" \n"); //ObjectName
            tmp.append("Object ID = " + info[++i] +" \n"); //ObjectID
            tmp.append("\n\n");
            obj_ids.add(info[i]);
            i++;
            details.add(tmp.toString());
        }

         adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, details);

        ListView listView = (ListView) findViewById(R.id.listview);
        if(adapter!=null)
            listView.setAdapter(adapter);



        final String[] items = new String[obj_ids.size()+1];
        for(int x=0;x<obj_ids.size();x++){
            items[x] = obj_ids.get(x);

        }
        items[items.length-1] = "";

        adapterS = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items);
        if(adapterS!=null) {
            dropdown.setAdapter(adapterS);
            dropdown.setSelection(items.length - 1);
        }

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
                   // dropdown.setSelected(false);
                  //  dropdown.setSelection(items.length-1);
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


        meta.add("OBJECT_NAME");
        data.add(name);

        EditText i = (EditText) (findViewById(R.id.personET));
        if( i.getText().toString().length() == 0 ) {
            i.setError("Person name is required!");
            return false;
        }
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
        data.add("1");

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