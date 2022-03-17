package com.example.liftyourlife.Guest;

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
import com.example.liftyourlife.Class.GuestLanguage;
import com.example.liftyourlife.Class.GuestMenuView;
import com.example.liftyourlife.R;
import com.google.android.material.navigation.NavigationView;

public class About extends AppCompatActivity {
    private TextView Title, TextViewSearchLanguage;
    private ImageView BackIcon, MenuIcon;
    private NavigationView GuestMenuView;
    private DrawerLayout drawerLayout;
    private GuestLanguage guestLagnuage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
    }
    private void init(){
        setID();
        MenuItem();
        BackIcon();
        MenuIcon();
        MenuView();
        setLanguage();
    }
    private void setID(){
        MenuIcon = findViewById(R.id.MenuIcon);
        BackIcon = findViewById(R.id.BackIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        GuestMenuView = findViewById(R.id.GuestMenuView);
        Title.setText(R.string.About);
        TextViewSearchLanguage = findViewById(R.id.TextViewSearchLanguage);
        guestLagnuage = new GuestLanguage(About.this);
    }
    private void setLanguage(){
        TextViewSearchLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { guestLagnuage.setDialog(); }
        });
    }
    private void MenuItem(){
        Menu menu= GuestMenuView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.ItemAbout);
        menuItem.setCheckable(false);
        menuItem.setChecked(true);
        menuItem.setEnabled(false);
    }
    private void MenuIcon(){
        MenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.open(); }
        });
    }
    private void MenuView(){
        GuestMenuView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                new GuestMenuView(About.this, item.getItemId());
                return false;
            }
        });
    }
    private void BackIcon(){
        BackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(About.this, LiftYourLife.class));
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(About.this, LiftYourLife.class));
        finish();
    }
}