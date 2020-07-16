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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ChangePassActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

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

        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

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
                        if (new_pass.getText().toString().length() < 6){
                            new_pass.setError("Password must be at least 6 characters");
                        }else {
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
                                            try {
                                                if(!old_pass.getText().toString().equals(AESDecryptionMethod(dataSnapshot.getValue().toString()))){
                                                    if(!old_pass.getText().toString().equals("")){
                                                        old_pass.setError("Wrong Password");
                                                    }
                                                    old_pass.setText("");
                                                }
                                                else {
                                                    if(old_pass.getText().toString().equals(AESDecryptionMethod(dataSnapshot.getValue().toString()))){
                                                        databaseReference.setValue(AESEncryptionMethod(new_pass.getText().toString()));
                                                        Toast.makeText(ChangePassActivity.this, "Change successful!", Toast.LENGTH_LONG).show();
                                                        new_pass.setText("");
                                                        new_passcf.setText("");
                                                        old_pass.setText("");
                                                    }
                                                }
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
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