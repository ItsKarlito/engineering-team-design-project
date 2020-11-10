package com.example.plantmonitor.PlantCatalog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantmonitor.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlantCatalogActivity extends AppCompatActivity {

    private String TAG = "PlantDatabaseActivity";

    Button buttonAddPlantCatalog = null;
    RecyclerView recyclerView;
    MyAdapter myAdapter;

    private FirebaseDatabase database;
    private DatabaseReference databasePlants;
    ArrayList<Plant> plantArray = new ArrayList<Plant>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_catalog);

        recyclerView = findViewById(R.id.recyclerView);
        buttonAddPlantCatalog = (Button) findViewById(R.id.buttonAddPlantCatalog);
        buttonAddPlantCatalog.setOnClickListener((view) -> {goToAddPlantCatalogActivity();});

        populateRecyclerView();
    }

    void populateRecyclerView() {

        database = FirebaseDatabase.getInstance();
        databasePlants = database.getReference("Plants");
        databasePlants.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Plant plant = child.getValue(Plant.class);
                    if (plant != null){
                        plantArray.add(plant);
                        Log.d(TAG, plant.toString());
                    }
                }

                //String plantName = dataSnapshot.child("plantName").getValue().toString();
                //int plantIdealLight = Integer.parseInt(dataSnapshot.child("plantIdealLight").getValue().toString());
                //int plantIdealMoisture = Integer.parseInt(dataSnapshot.child("plantIdealMoisture").getValue().toString());
                //int plantIdealTemperature = Integer.parseInt(dataSnapshot.child("plantIdealTemperature").getValue().toString());
                //String plantDescription = dataSnapshot.child("plantDescription").getValue().toString();

                myAdapter = new MyAdapter(getApplicationContext(), plantArray);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load post.", Toast.LENGTH_SHORT).show();
            }
        });

        DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
    }

    void goToAddPlantCatalogActivity() {
        Intent intent = new Intent(this, AddPlantCatalogActivity.class);
        startActivity(intent);
    }
}