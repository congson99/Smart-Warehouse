package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

public class HistoryActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    ListView lv;
    private String stringMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lv = (ListView) findViewById(R.id.history_listView);

        databaseReference = FirebaseDatabase.getInstance().getReference("Main").child("History");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stringMessage = dataSnapshot.getValue().toString();
                stringMessage = stringMessage.substring(1, stringMessage.length() - 1);
                String[] stringMessageArray = stringMessage.split(", ");
                Arrays.sort(stringMessageArray);

                String[] stringFinal = new String[stringMessageArray.length*3];

                for (int i = 0; i < stringMessageArray.length; i++) {
                    String[] stringKeyValue = stringMessageArray[i].split("=", 2);
                    stringFinal[3*i] = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss", Long.parseLong(stringKeyValue[0].substring(1,13)));
                    stringFinal[3*i] += "\n" + stringKeyValue[0].substring(13);
                    stringFinal[3*i+1] = stringKeyValue[1];
                    stringFinal[3*i+2] = "";
                }

                lv.setAdapter(new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_expandable_list_item_1, stringFinal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}