package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ChangeInfoActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    Button bt_take;
    Button bt_choose;
    Button bt_change;
    ImageView img;
    EditText name;
    EditText email;
    EditText phone;
    EditText dob;
    TextView location;
    ImageView bt_next;
    ImageView bt_back;

    int REQUEST_CODE = 1;
    Integer PHOTO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        bt_take = (Button) findViewById(R.id.button_change_avatar);
        bt_choose = (Button) findViewById(R.id.button_remove_avatar);
        bt_change = (Button) findViewById(R.id.button_save);
        img = (ImageView) findViewById(R.id.img_avatar_profile);
        name = (EditText) findViewById(R.id.changeinfo_name);
        email = (EditText) findViewById(R.id.changeinfo_email);
        phone = (EditText) findViewById(R.id.changeinfo_phone);
        dob = (EditText) findViewById(R.id.changeinfo_dob);
        location = (TextView) findViewById(R.id.changeinfo_location);
        bt_next = (ImageView) findViewById(R.id.changeinfo_bt_next);
        bt_back = (ImageView) findViewById(R.id.changeinfo_bt_back);

        final Intent intent = getIntent();
        final String id = intent.getStringExtra("id");

        final String[] locationsarray = {
                "Hà Nội",               //0 Ha Noi
                "Hồ Chí Minh",          //1 Ho Chi Minh
                "Đà Nẵng",              //2 Da Nang
                "Hải Phòng",            //3 Hai Phong
                "Cần Thơ",              //4 Can Tho
                "Hạ Long",              //5 Ha Long
                "Thanh Hoá",            //6 Thanh Hoa
                "Huế",                  //7 Hue
                "Nha Trang",            //8 Nha Trang
                "Pleiku",               //9 Pleiku
                "Thủ Dầu Một"};         //10 Thu Dau 1

        final int[] location_temp = new int[1];

        databaseReference = FirebaseDatabase.getInstance().getReference("Main");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                img.setImageResource(R.drawable.farmericon);
                if(!dataSnapshot.child("Account").child(id).child("Avatar").getValue().toString().equals("None")){
                    byte[] mangGet = Base64.decode(dataSnapshot.child("Account").child(id).child("Avatar").getValue().toString(), Base64.DEFAULT);
                    Bitmap bm = BitmapFactory.decodeByteArray(mangGet, 0, mangGet.length);
                    img.setImageBitmap(bm);
                }

                name.setText(dataSnapshot.child("Account").child(id).child("Name").getValue().toString());
                email.setText(dataSnapshot.child("Account").child(id).child("Email").getValue().toString());
                phone.setText(dataSnapshot.child("Account").child(id).child("Phone").getValue().toString());
                dob.setText(dataSnapshot.child("Account").child(id).child("DOB").getValue().toString());
                location_temp[0] = Integer.parseInt(dataSnapshot.child("Location").getValue().toString());
                location.setText(locationsarray[Integer.parseInt(dataSnapshot.child("Location").getValue().toString())]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location_temp[0]++;
                if (location_temp[0] > 10) location_temp[0] -= 11;
                location.setText(locationsarray[(location_temp[0])%11]);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location_temp[0]--;
                if (location_temp[0] < 0) location_temp[0] += 11;
                location.setText(locationsarray[(location_temp[0])%11]);
            }
        });

        bt_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CODE);

            }
        });


        bt_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent1.createChooser(intent1, "photo"), PHOTO);
            }
        });

        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("")){
                    name.setError("Enter your name!");
                }
                else {
                    byte[] hinh = ImageViewToByte(img);
                    String chuoiHinh = Base64.encodeToString(hinh, Base64.DEFAULT);
                    databaseReference.child("Account").child(id).child("Avatar").setValue(chuoiHinh);

                    databaseReference.child("Account").child(id).child("Name").setValue(name.getText().toString());
                    databaseReference.child("Account").child(id).child("Email").setValue(email.getText().toString());
                    databaseReference.child("Account").child(id).child("Phone").setValue(phone.getText().toString());
                    databaseReference.child("Account").child(id).child("DOB").setValue(dob.getText().toString());
                    databaseReference.child("Location").setValue(location_temp[0]%11);

                    Toast.makeText(ChangeInfoActivity.this, "Change successful!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
        }
        else {
            if(requestCode == PHOTO && data != null){
                Uri selecImageURI = data.getData();
                img.setImageURI(selecImageURI);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public byte[] ImageViewToByte(ImageView view){
        BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}