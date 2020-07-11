package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    EditText new_pass;
    EditText new_passcf;
    EditText old_pass;
    Button bt_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        //Intent from Home
        Intent intentf = getIntent();
        final String id = intentf.getStringExtra("id");

        //Link to Node pass
        databaseReference = FirebaseDatabase.getInstance().getReference("Main").child("Account").child(id).child("Pass");

        //Anh xa
        new_pass = (EditText) findViewById(R.id.changepass_new_pass);
        new_passcf = (EditText) findViewById(R.id.changepass_new_passcf);
        old_pass = (EditText) findViewById(R.id.changepass_old_pass);
        bt_change = (Button) findViewById(R.id.changepass_bt_change);

        //change_button
        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check old pass null
                if(old_pass.getText().toString().equals("")){
                    old_pass.setError("Enter your current password!");
                }
                else {
                    //Check new pass null
                    if(new_pass.getText().toString().equals("")){
                        new_pass.setError("Enter new password!");
                    }
                    else {
                        //Check new passcf null
                        if(new_passcf.getText().toString().equals("")){
                            new_passcf.setError("Enter the new password again!");
                        }
                        else {
                            //nothing null
                            //Check pass
                            if(!new_pass.getText().toString().equals(new_passcf.getText().toString())){
                                new_pass.setError("Password does not match!");
                                new_passcf.setError("Password does not match!");
                                new_pass.setText("");
                                new_passcf.setText("");
                            }
                            else {
                                //check old pass true
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(!old_pass.getText().toString().equals(dataSnapshot.getValue().toString())){
                                            if(!old_pass.getText().toString().equals("")){
                                                old_pass.setError("Wrong Password");
                                            }
                                            old_pass.setText("");
                                        }
                                        else {
                                            if(old_pass.getText().toString().equals(dataSnapshot.getValue().toString())){
                                                databaseReference.setValue(new_pass.getText().toString());
                                                Toast.makeText(ChangePassActivity.this, "Change successful!", Toast.LENGTH_LONG).show();
                                                new_pass.setText("");
                                                new_passcf.setText("");
                                                old_pass.setText("");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                }
            }
        });
    }
}