package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    ImageView img;
    TextView tv_id;
    TextView tv_name;
    TextView tv_location;
    Button bt_changeinfo;
    Button bt_changepass;
    Button bt_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Intent from Home
        Intent intentf = getIntent();
        final String id = intentf.getStringExtra("id");

        //Anh xa
        img = (ImageView) findViewById(R.id.info_image);
        tv_id = (TextView) findViewById(R.id.info_id);
        tv_name = (TextView) findViewById(R.id.info_name);
        tv_location = (TextView) findViewById(R.id.info_location);
        bt_changeinfo = (Button) findViewById(R.id.info_bt_changeinfo);
        bt_changepass = (Button) findViewById(R.id.info_bt_changepass);
        bt_logout = (Button) findViewById(R.id.info_bt_logout);

        //List Locations
        final String[] locationsarray = { "Hà Nội",             //0 Ha Noi
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

        //link to account info
        databaseReference = FirebaseDatabase.getInstance().getReference("Account");

        //Set value
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Set value
                img.setImageResource(R.drawable.farmericon);
                if(!dataSnapshot.child(id).child("Avatar").getValue().toString().equals("None")){
                    byte[] mangGet = Base64.decode(dataSnapshot.child(id).child("Avatar").getValue().toString(), Base64.DEFAULT);
                    Bitmap bm = BitmapFactory.decodeByteArray(mangGet, 0, mangGet.length);
                    img.setImageBitmap(bm);
                }
                tv_id.setText(id);
                tv_name.setText(dataSnapshot.child(id).child("Name").getValue().toString());
                tv_location.setText(locationsarray[Integer.parseInt(dataSnapshot.child("Location").child("Location").getValue().toString())]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Change info button
        bt_changeinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, ChangeInfoActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        //Change pass button
        bt_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, ChangePassActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        //Logout button
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
                intent.putExtra("id", "");
                intent.putExtra("pass", "");
                startActivity(intent);
            }
        });

    }
}