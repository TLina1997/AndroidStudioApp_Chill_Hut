package com.example.tlina.chill_new;

import android.support.v7.widget.SearchView;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Admin_View extends AppCompatActivity{

    ListView adminListView;

    private ArrayAdapter adapter;

    DatabaseReference databaseJuices;

    List<Juice> adminList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__view);

        databaseJuices = FirebaseDatabase.getInstance().getReference("juices");

        adminListView = (ListView) findViewById(R.id.adminView);

        adminList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adminList);
        adminListView.setAdapter(adapter);
//
//        adminListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                String text = adminListView.getItemAtPosition(1).toString();
//                Toast.makeText(getApplicationContext(), "" + text, Toast.LENGTH_SHORT).show();
//            }
//        });

//        searchList.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//                (Admin_View.this).adapter.getFilter().filter(charSequence);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    public void filte(){

    }

    @Override
    protected void onStart() {
        super.onStart();


        databaseJuices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                adminList.clear();


                for (DataSnapshot juiceSnapshot : dataSnapshot.getChildren()) {

                    Juice juice = juiceSnapshot.getValue(Juice.class);

                    adminList.add(juice);


                }

                JuiceList adapter = new JuiceList(Admin_View.this, adminList);

                adminListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);

        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                List<Juice> newList = new ArrayList<>();
                newList.addAll(adminList);

                newText = newText.toLowerCase(Locale.getDefault());

                adminList.clear();

                if(newText.isEmpty()){
                    adminList.addAll(newList);
                }else{
                    for(Juice juice : newList){
                        if(juice.getJuiceName().trim().toLowerCase(Locale.getDefault()).contains(newText)){
                            adminList.add(juice);
                        }
                    }

                }

//                for(Juice admin : adminList){
//
//                    if(newText.toLowerCase().contains(newText.toLowerCase())){
//
//                        newList.add(admin);
//                    }
//                }

                ArrayAdapter<Juice> adapter = new ArrayAdapter<>(Admin_View.this, android.R.layout.simple_list_item_1, adminList);
                adminListView.setAdapter(adapter);

                return true;
            }
        });

        return true;
    }
//
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//
//        String userInput = newText.toLowerCase();
//        List<Juice> newList = new ArrayList<>();
//
//        for(Juice admin : adminList){
//
//            if(userInput.toLowerCase().contains(userInput)){
//
//                newList.add(admin);
//
//            }
//        }
//
//        adapter.updateList(newList);
//        return true;
//    }
}

