package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    EditText id;
    EditText pass;
    TextView warn;
    Button bt_login;
    Button bt_register;
    TextView test;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Anh xa
        id = (EditText) findViewById(R.id.login_id);
        pass = (EditText) findViewById(R.id.login_pass);
        warn = (TextView) findViewById(R.id.login_warn);
        bt_login = (Button) findViewById(R.id.login_bt_login);
        bt_register = (Button) findViewById(R.id.login_bt_register);
        test = (TextView) findViewById(R.id.login_tv_forgot);

        //Clear or setValue
        Intent intentf = getIntent();
        id.setText(intentf.getStringExtra("id"));
        pass.setText(intentf.getStringExtra("pass"));

        //test button
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("id","congson");
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "Test mode", Toast.LENGTH_LONG).show();
                pass.setText("");
                warn.setText("");
            }
        });

        //Login_button
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Link to Node Account
                databaseReference = FirebaseDatabase.getInstance().getReference("Main").child("Account").child(id.getText().toString()).child("Pass");
                //Check account
                if (id.getText().toString().equals("")){
                    id.setError("Enter your ID!");
                }else {
                    if (pass.getText().toString().equals("")){
                        pass.setError("Enter your password!");
                    }else {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Check account exist
                                if(dataSnapshot.getValue() != null) {
                                    //check password
                                    if(dataSnapshot.getValue().toString().equals(pass.getText().toString())){
                                        //Move to home
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.putExtra("id",id.getText().toString());
                                        startActivity(intent);
                                        pass.setText("");
                                        warn.setText("");
                                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        pass.setError("Wrong password!");
                                        pass.setText("");
                                    }
                                }
                                else {
                                    id.setError("This account does not exist!");
                                    pass.setText("");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }
        });

        //Register_button
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

    }
}