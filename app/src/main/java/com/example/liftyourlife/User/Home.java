package com.example.liftyourlife.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liftyourlife.Adapters.LiftYourLifeAdapter;
import com.example.liftyourlife.Class.GuestLanguage;
import com.example.liftyourlife.Class.Tag;
import com.example.liftyourlife.Guest.LiftYourLife;
import com.example.liftyourlife.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private NavigationView navigationView;
    private ImageView BackIcon, MenuIcon;
    private DrawerLayout drawerLayout;
    private TextView Title, TextViewSearchLanguage;
    private GuestLanguage guestLanguage;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private List<Tag> tagList;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }
    private void init(){
        setID();
        MenuIcon();
        SignOutIcon();
        setLanguage();
        setTags();
    }
    private void setID(){
        navigationView = findViewById(R.id.UserNavigationView);
        BackIcon = findViewById(R.id.BackIcon);
        MenuIcon = findViewById(R.id.MenuIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        Title.setText(R.string.Home);
        TextViewSearchLanguage = findViewById(R.id.TextViewSearchLanguage);
        guestLanguage = new GuestLanguage(Home.this);
        tagList = new ArrayList<>();
        recyclerView = findViewById(R.id.HomeRycyclerView);
    }
    private void setTags(){
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.Profile));
        list.add(getResources().getString(R.string.MyWorkOut));
        list.add(getResources().getString(R.string.WorkOutUpdate));
        list.add(getResources().getString(R.string.ChangePassword));
        list.add(getResources().getString(R.string.Logout));
        for(int i=0; i< list.size();i++)
            tagList.add(new Tag(list.get(i),R.drawable.tag));
        LiftYourLifeAdapter liftYourLifeAdapter = new LiftYourLifeAdapter(Home.this,tagList);
        recyclerView.setLayoutManager(new GridLayoutManager(Home.this,2));
        recyclerView.setAdapter(liftYourLifeAdapter);
    }
    private void setLanguage(){
        TextViewSearchLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestLanguage.setDialog();
            }
        });
    }
    private void MenuIcon(){
        MenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });
    }
    private void SignOutIcon(){
        BackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed(); }
        });
    }
    public void onBackPressed(){
        if(firebaseAuth.getCurrentUser() != null)
            firebaseAuth.signOut();
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Home.this.getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSignInClient googleClient = GoogleSignIn.getClient(Home.this, options);
        googleClient.signOut();
        startActivity(new Intent(Home.this, LiftYourLife.class));
        finish();
    }
}