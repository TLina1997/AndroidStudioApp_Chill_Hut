package com.example.tlina.chill_new;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chill_New extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    Button buttonAdd, buttonTime, buttonView;
    Spinner juiceName, juiceType;
    TextView textViewTime;

    String timeString;

    DatabaseReference databaseJuices;

    ListView listViewJuice;


    List<Juice> juiceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chill__new);

        databaseJuices = FirebaseDatabase.getInstance().getReference("juices");

        buttonAdd = (Button) findViewById(R.id.btnAdd);
        buttonTime = (Button) findViewById(R.id.btnTime);
        buttonView = (Button) findViewById(R.id.btnView);
        juiceName = (Spinner) findViewById(R.id.spJuiceName);
        juiceType = (Spinner) findViewById(R.id.spJuiceType);
        listViewJuice = (ListView) findViewById(R.id.listViewJuices);

        juiceList = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(textViewTime != null) {

                    addJuice();
                }

                else{

                    Toast.makeText(getApplicationContext(), "Add order Time", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment timePicker = new TimePicker();

                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });


        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                admin();
            }
        });

        listViewJuice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "Hold LIST VIEW to Update", Toast.LENGTH_SHORT).show();
            }
        });

        listViewJuice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                Juice juice = juiceList.get(position);

                showUpdateDialog(juice.getJuiceID(), juice.getJuiceName());
                return false;
            }
        });

    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

        textViewTime = (TextView) findViewById(R.id.txtTime);

        timeString = "Hour:" + hourOfDay + " Minute:" + minute;

        textViewTime.setText("  Hour:" + hourOfDay + " Minute:" + minute);
    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseJuices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                juiceList.clear();


                for (DataSnapshot juiceSnapshot : dataSnapshot.getChildren()) {

                    Juice juice = juiceSnapshot.getValue(Juice.class);

                    juiceList.add(juice);


                }

                JuiceList adapter = new JuiceList(Chill_New.this, juiceList);

                listViewJuice.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String juiceID, String juiceName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_layout, null);

        dialogBuilder.setView(dialogView);

        final Spinner spinnerUpdateName = (Spinner) dialogView.findViewById(R.id.spJuiceNameUpdate);
        final Spinner spinnerUpdateType = (Spinner) dialogView.findViewById(R.id.spJuiceTypeUpdate);
        final TextView textViewUpdateTime = (TextView) dialogView.findViewById(R.id.txtUpdateTime);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.btnDelete);

        //textViewUpdateTime.setText(timeString);


        dialogBuilder.setTitle("Updating Juice: " + juiceName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(timeString != null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Chill_New.this);

                    builder.setMessage("Are You Sure!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String spinnerName = spinnerUpdateName.getSelectedItem().toString();
                                    String spinnerType = spinnerUpdateType.getSelectedItem().toString();
                                    String textTime = textViewUpdateTime.getText().toString().trim();

                                    updateJuice(juiceID, spinnerName, spinnerType, textTime);

                                    alertDialog.dismiss();
                                }
                            })

                            .setNegativeButton("Cancel", null).setCancelable(false);

                    AlertDialog alertDialog1 = builder.create();
                    alertDialog1.show();
                }

                else{

                    Toast.makeText(getApplicationContext(), "Add new Time to UPDATE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Chill_New.this);

                builder.setMessage("Are You Sure!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteJuice(juiceID);

                                alertDialog.dismiss();

                            }
                        })

                        .setNegativeButton("Cancel", null).setCancelable(false);

                AlertDialog alertDialog1 = builder.create();
                alertDialog1.show();
            }


        });

        textViewUpdateTime.setText(timeString);

    }



    private void addJuice() {

        String spinnerName = juiceName.getSelectedItem().toString();
        String spinnerType = juiceType.getSelectedItem().toString();
        String textTime = textViewTime.getText().toString().trim();

            String id = databaseJuices.push().getKey();

            Juice juice = new Juice(id, spinnerName, spinnerType, textTime);

            databaseJuices.child(id).setValue(juice);

            Toast.makeText(this, "Juice added", Toast.LENGTH_LONG).show();

    }

    private boolean updateJuice(String id, String spinnerName, String spinnerType, String textTime) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("juices").child(id);

        Juice juice = new Juice(id, spinnerName, spinnerType, textTime);

        databaseReference.setValue(juice);

        Toast.makeText(this, "Juice Updated Successfully", Toast.LENGTH_LONG).show();

        return true;
    }

    private void deleteJuice(String juiceID) {

        DatabaseReference drjuice = FirebaseDatabase.getInstance().getReference("juices").child(juiceID);

        drjuice.removeValue();

        Toast.makeText(this, "Juice Deleted Successfully", Toast.LENGTH_LONG).show();
    }

    public void admin(){

        Intent intent = new Intent(this, Admin_View.class);

        startActivity(intent);

    }



}