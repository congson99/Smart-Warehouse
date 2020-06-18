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
    TextView w1;
    TextView w2;
    TextView w3;
    TextView w;
    Button bt_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        //Intent from Home
        Intent intentf = getIntent();
        final String id = intentf.getStringExtra("id");

        //Link to Node pass
        databaseReference = FirebaseDatabase.getInstance().getReference("Account").child(id).child("Pass");

        //Anh xa
        new_pass = (EditText) findViewById(R.id.changepass_new_pass);
        new_passcf = (EditText) findViewById(R.id.changepass_new_passcf);
        old_pass = (EditText) findViewById(R.id.changepass_old_pass);
        w1 = (TextView) findViewById(R.id.changepass_warn1);
        w2 = (TextView) findViewById(R.id.changepass_warn2);
        w3 = (TextView) findViewById(R.id.changepass_warn3);
        w = (TextView) findViewById(R.id.changepass_warn);
        bt_change = (Button) findViewById(R.id.changepass_bt_change);

        //change_button
        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check new pass null
                if(new_pass.getText().toString().equals("")){
                    w1.setText("Nhập mật khẩu mới!");
                }
                else {
                    //Check new passcf null
                    if(new_passcf.getText().toString().equals("")){
                        w2.setText("Nhập lại mật khẩu!");
                    }
                    else {
                        //Check old pass null
                        if(old_pass.getText().toString().equals("")){
                            w3.setText("Nhập mật khẩu cũ!");
                        }
                        else {
                            //nothing null
                            //Check pass
                            if(!new_pass.getText().toString().equals(new_passcf.getText().toString())){
                                w2.setText("Mật khẩu không trùng khớp!");
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
                                                w3.setText("Mật khẩu cũ không đúng!");
                                            }
                                            old_pass.setText("");
                                        }
                                        else {
                                            if(old_pass.getText().toString().equals(dataSnapshot.getValue().toString())){
                                                databaseReference.setValue(new_pass.getText().toString());
                                                w.setText("Bạn vừa đổi mật khẩu thành công!");
                                                new_pass.setText("");
                                                new_passcf.setText("");
                                                old_pass.setText("");
                                                w1.setText("");
                                                w2.setText("");
                                                w3.setText("");
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