package com.example.liftyourlife;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.liftyourlife.Class.Loading;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Guest.About;
import com.example.liftyourlife.Guest.Contact;
import com.example.liftyourlife.Guest.LiftYourLife;
import com.example.liftyourlife.User.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OpeningScreen extends AppCompatActivity {
    private Loading loading;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://liftyourlife-9d039-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        init();
    }
    private void init(){ HomePage(); }
    private void HomePage() {
        if(firebaseAuth.getCurrentUser() != null ) {
            if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                loading = new Loading(OpeningScreen.this);
                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Home(user);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
            else
                LiftYourLife();
        }
        else
            LiftYourLife();
    }
    private void Home(User user){
        Intent intent = new Intent(OpeningScreen.this, Home.class);
        intent.putExtra("user", user);
        loading.stop();
        startActivity(intent);
        finish();
    }
    private void LiftYourLife(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(OpeningScreen.this, LiftYourLife.class));
                finish();
            }
        }, 300);
    }
}