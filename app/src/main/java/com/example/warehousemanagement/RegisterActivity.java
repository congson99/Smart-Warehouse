package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

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

        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

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
                    id.setError("Enter ID!");
                    pass.setText("");
                    passcf.setText("");
                }
                else {
                    //check null name
                    if(name.getText().toString().equals("")){
                        name.setError("Enter your name!");
                        pass.setText("");
                        passcf.setText("");
                    }
                    else {
                        //check pass
                        if (pass.getText().toString().equals("")){
                            pass.setError("Enter password!");
                            passcf.setText("");
                        }
                        else {
                            if (pass.getText().toString().length() < 6){
                                pass.setError("Password must be at least 6 characters");
                            }else {
                                //check passcf
                                if (passcf.getText().toString().equals(""))
                                {
                                    passcf.setError("Enter the password again!");
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
                                                    id.setError("This account already exists!");
                                                    pass.setText("");
                                                    passcf.setText("");
                                                }
                                            }
                                            else {
                                                //check pass
                                                if(!pass.getText().toString().equals(passcf.getText().toString())){
                                                    pass.setError("Password does not match!");
                                                    passcf.setError("Password does not match!");
                                                    pass.setText("");
                                                    passcf.setText("");
                                                }
                                                else {
                                                    if(!id.getText().toString().equals("")){
                                                        databaseReference.child(id.getText().toString()).child("Name").setValue(name.getText().toString());
                                                        databaseReference.child(id.getText().toString()).child("Pass").setValue(AESEncryptionMethod(pass.getText().toString()));
                                                        databaseReference.child(id.getText().toString()).child("Avatar").setValue("None");
                                                        databaseReference.child(id.getText().toString()).child("Email").setValue("Email");
                                                        databaseReference.child(id.getText().toString()).child("Phone").setValue("Phone Number");
                                                        databaseReference.child(id.getText().toString()).child("DOB").setValue("DOB");
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        intent.putExtra("id", id.getText().toString());
                                                        intent.putExtra("pass", pass.getText().toString());
                                                        startActivity(intent);
                                                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
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
            }
        });
    }

    private String AESEncryptionMethod(String string){
        byte[] encryptionKey = {21, 35, 44, 69, 11, 55, 19, 99, 18, 20, 15, 44, 77, 23, 76, 12};
        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
        byte[] stringByte = string.getBytes();
        byte[] encryptedByte = new byte[stringByte.length];
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedByte = cipher.doFinal(stringByte);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        String returnString = null;
        try {
            returnString = new String(encryptedByte, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnString;
    };

    private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {
        byte[] encryptionKey = {21, 35, 44, 69, 11, 55, 19, 99, 18, 20, 15, 44, 77, 23, 76, 12};
        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
        byte[] EncryptedByte = string.getBytes("ISO-8859-1");
        String decryptedString = string;
        byte[] decryption;
        try {
            decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decryption = decipher.doFinal(EncryptedByte);
            decryptedString = new String(decryption);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedString;
    };
}