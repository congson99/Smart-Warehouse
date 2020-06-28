package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    EditText id;
    EditText name;
    EditText pass;
    EditText passcf;
    TextView id_w;
    TextView name_w;
    TextView pass_w;
    TextView passcf_w;
    Button bt_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Anh xa
        id = (EditText) findViewById(R.id.register_id);
        name = (EditText) findViewById(R.id.register_name);
        pass = (EditText) findViewById(R.id.register_pass);
        passcf = (EditText) findViewById(R.id.register_pass_cf);

        id_w = (TextView) findViewById(R.id.register_id_warn);
        name_w = (TextView) findViewById(R.id.register_name_warn);
        pass_w = (TextView) findViewById(R.id.register_pass_warn);
        passcf_w = (TextView) findViewById(R.id.register_passcf_warn);

        bt_register = (Button) findViewById(R.id.register_bt_register);

        //Register_button
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Clear warn
                id_w.setText("");
                name_w.setText("");
                pass_w.setText("");
                passcf_w.setText("");

                //link to Node account
                databaseReference = FirebaseDatabase.getInstance().getReference("Main").child("Account");

                //Check null id
                if (id.getText().toString().equals("")){
                    id_w.setText("Nhập ID là bắt buộc!");
                    pass.setText("");
                    passcf.setText("");
                }
                else {
                    //check null name
                    if(name.getText().toString().equals("")){
                        name_w.setText(" Nhập tên là bắt buộc!");
                        pass.setText("");
                        passcf.setText("");
                    }
                    else {
                        //check pass
                        if (pass.getText().toString().equals("")){
                            pass_w.setText("Nhập mật khẩu");
                            passcf.setText("");
                        }
                        else {
                            //check passcf
                            if (passcf.getText().toString().equals(""))
                            {
                                passcf_w.setText("Nhập lại mật khẩu");
                                pass.setText("");
                            }
                            else {
                                //nothing null
                                databaseReference.child(id.getText().toString()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //check account exist
                                        if(dataSnapshot.getValue() != null){
                                            if(!id.getText().toString().equals("")){
                                                id_w.setText("Tài khoản đã tồn tại!");
                                                pass.setText("");
                                                passcf.setText("");
                                            }
                                        }
                                        else {
                                            //check pass
                                            if(!pass.getText().toString().equals(passcf.getText().toString())){
                                                passcf_w.setText("Mật khẩu không trùng khớp!");
                                                pass.setText("");
                                                passcf.setText("");
                                            }
                                            else {
                                                if(!id.getText().toString().equals("")){
                                                    databaseReference.child(id.getText().toString()).child("Name").setValue(name.getText().toString());
                                                    databaseReference.child(id.getText().toString()).child("Pass").setValue(pass.getText().toString());
                                                    databaseReference.child(id.getText().toString()).child("Avatar").setValue("None");
                                                    databaseReference.child(id.getText().toString()).child("Email").setValue("Email");
                                                    databaseReference.child(id.getText().toString()).child("Phone").setValue("Phone Number");
                                                    databaseReference.child(id.getText().toString()).child("DOB").setValue("DOB");
                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    intent.putExtra("id", id.getText().toString());
                                                    intent.putExtra("pass", pass.getText().toString());
                                                    startActivity(intent);
                                                    //Clear
                                                    id.setText("");
                                                    name.setText("");
                                                    pass.setText("");
                                                    passcf.setText("");
                                                    id_w.setText("");
                                                    name_w.setText("");
                                                    pass_w.setText("");
                                                    passcf_w.setText("");
                                                }

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