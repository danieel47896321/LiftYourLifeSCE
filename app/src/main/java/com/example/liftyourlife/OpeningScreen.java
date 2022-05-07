package com.example.liftyourlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.liftyourlife.Class.Loading;
import com.example.liftyourlife.Class.PopUpMSG;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Guest.LiftYourLife;
import com.example.liftyourlife.Guest.SignIn;
import com.example.liftyourlife.Server.RetrofitInterface;
import com.example.liftyourlife.User.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpeningScreen extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        init();
    }
    private void init(){ HomePage(); }
    private void HomePage() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        if(firebaseAuth.getCurrentUser() != null ) {
            if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                HashMap<String,String> map = new HashMap<>();
                map.put("uid",firebaseAuth.getCurrentUser().getUid());
                Call<User> call = retrofitInterface.getUser(map);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            User user = response.body();
                            Home(user);
                        } else if (response.code() == 404) {
                            new PopUpMSG(OpeningScreen.this, getResources().getString(R.string.SignIn), getResources().getString(R.string.Error),LiftYourLife.class);
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        LiftYourLife();
                    }
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
        }, 1000);
    }
}