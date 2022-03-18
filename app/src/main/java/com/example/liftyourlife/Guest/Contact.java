package com.example.liftyourlife.Guest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.liftyourlife.Class.GuestLanguage;
import com.example.liftyourlife.Class.GuestNavigationView;
import com.example.liftyourlife.R;
import com.google.android.material.navigation.NavigationView;

public class Contact extends AppCompatActivity {
    private TextView SendEmail, Title, TextViewSearchLanguage;
    private ImageView BackIcon, MenuIcon;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private GuestLanguage guestLanguage;
    private Intent intent;
    private String Email = "LiftYourLifeSCE@gmail.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        init();
    }
    private void init(){
        setID();
        MenuItem();
        BackIcon();
        MenuIcon();
        NavigationView();
        SendEmail();
        setLanguage();
    }
    private void setID() {
        intent = getIntent();
        MenuIcon = findViewById(R.id.MenuIcon);
        BackIcon = findViewById(R.id.BackIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        navigationView = findViewById(R.id.navigationView);
        SendEmail = findViewById(R.id.SendEmail);
        Title.setText(R.string.Contact);
        TextViewSearchLanguage = findViewById(R.id.TextViewSearchLanguage);
        guestLanguage = new GuestLanguage(Contact.this);
    }
    private void setLanguage(){
        TextViewSearchLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { guestLanguage.setDialog(); }
        });
    }
    private void MenuItem(){
        Menu menu= navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.ItemContact);
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
    private void NavigationView(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                new GuestNavigationView(Contact.this, item.getItemId());
                return false;
            }
        });
    }
    private void SendEmail(){
        SendEmail.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                intent = new Intent(intent.ACTION_SEND);
                String toArray[] = new String[1];
                Email = SendEmail.getText().toString();
                toArray[0] = Email;
                intent.putExtra(intent.EXTRA_EMAIL, toArray);
                intent.putExtra(intent.EXTRA_SUBJECT, getResources().getString(R.string.ContactSubject));
                intent.putExtra(intent.EXTRA_TEXT, getResources().getString(R.string.ContactText));
                intent.setType("message/rfc822");
                startActivity(intent.createChooser(intent, getResources().getString(R.string.ContactEmailChooser)));
            }
        });
    }
    private void BackIcon(){
        BackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Contact.this, LiftYourLife.class));
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Contact.this, LiftYourLife.class));
        finish();
    }
}