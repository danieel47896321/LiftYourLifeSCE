package com.example.liftyourlife.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Class.UserLanguage;
import com.example.liftyourlife.Class.UserNavigationHeader;
import com.example.liftyourlife.Class.UserNavigationView;
import com.example.liftyourlife.R;
import com.google.android.material.navigation.NavigationView;

public class WorkOut extends AppCompatActivity {
    private NavigationView UserNavigationView;
    private ImageView BackIcon, MenuIcon;
    private DrawerLayout drawerLayout;
    private TextView Title, TextViewSearchLanguage;
    private UserLanguage userLanguage;
    private Intent intent;
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out);
        init();
    }
    private void init(){
        setID();
        setLanguage();
        MenuIcon();
        BackIcon();
        NavigationView();
        MenuItem();
    }
    private void setID(){
        intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        UserNavigationView = findViewById(R.id.navigationView);
        BackIcon = findViewById(R.id.BackIcon);
        MenuIcon = findViewById(R.id.MenuIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        Title.setText(R.string.WorkOut);
        TextViewSearchLanguage = findViewById(R.id.TextViewSearchLanguage);
        userLanguage = new UserLanguage(WorkOut.this,user);
        new UserNavigationHeader(user,WorkOut.this);
    }
    private void setLanguage(){
        TextViewSearchLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLanguage.setDialog();
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
    private void BackIcon(){
        BackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed(); }
        });
    }
    private void NavigationView(){
        UserNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                new UserNavigationView(WorkOut.this, item.getItemId(), user);
                return false;
            }
        });
    }
    private void MenuItem(){
        Menu menu= UserNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.ItemStatistics);
        menuItem.setCheckable(false);
        menuItem.setChecked(true);
        menuItem.setEnabled(false);
    }
    public void onBackPressed(){
        intent = new Intent(WorkOut.this, Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}