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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class LoginActivity extends AppCompatActivity {

    EditText id;
    EditText pass;
    TextView warn;
    Button bt_login;
    Button bt_register;
    TextView test;

    DatabaseReference databaseReference;
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

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

        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

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
                                    try {
                                        if(AESDecryptionMethod(dataSnapshot.getValue().toString()).equals(pass.getText().toString())){
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
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
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