package com.example.liftyourlife.Guest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liftyourlife.Class.GuestLanguage;
import com.example.liftyourlife.Class.GuestNavigationView;
import com.example.liftyourlife.Class.Loading;
import com.example.liftyourlife.Class.PopUpMSG;
import com.example.liftyourlife.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ResetPassword extends AppCompatActivity {
    private Button ButtonFinish;
    private TextInputLayout TextInputLayoutEmail;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView BackIcon, MenuIcon;
    private TextView CreateAccount, Title, TextViewSearchLanguage;
    private GuestLanguage guestLanguage;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
    private Loading loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
    }
    private void init(){
        setID();
        setLanguage();
        MenuItem();
        BackIcon();
        MenuIcon();
        EndIcon();
        NavigationView();
        ResetPassword();
        CreateAccount();
    }
    private void setID(){
        MenuIcon = findViewById(R.id.MenuIcon);
        BackIcon = findViewById(R.id.BackIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        navigationView = findViewById(R.id.navigationView);
        Title.setText(R.string.ResetPassword);
        ButtonFinish = findViewById(R.id.ButtonFinish);
        CreateAccount = findViewById(R.id.CreateAccount);
        TextInputLayoutEmail = findViewById(R.id.TextInputLayoutEmail);
        TextViewSearchLanguage = findViewById(R.id.TextViewSearchLanguage);
        guestLanguage = new GuestLanguage(ResetPassword .this);
    }
    private void setLanguage(){
        TextViewSearchLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { guestLanguage.setDialog(); }
        });
    }
    private void MenuItem(){
        Menu menu= navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.ItemResetPassword);
        menuItem.setCheckable(false);
        menuItem.setChecked(true);
        menuItem.setEnabled(false);
    }
    private void EndIcon() {
        TextInputLayoutEmail.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Clear(TextInputLayoutEmail); }
        });
    }
    private void Clear(TextInputLayout input){
        input.setHelperText("");
        input.getEditText().setText("");
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
                new GuestNavigationView(ResetPassword.this, item.getItemId());
                return false;
            }
        });
    }
    private boolean isEmailValid(CharSequence email) { return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); }
    private void ResetPassword(){
        ButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextInputLayoutEmail.getEditText().getText().length() > 0) {
                    String Email = TextInputLayoutEmail.getEditText().getText().toString();
                    if(!isEmailValid(Email))
                        TextInputLayoutEmail.setHelperText(getResources().getString(R.string.InvalidEmail));
                    else {
                        loading = new Loading(ResetPassword.this);
                        firebaseAuth.fetchSignInMethodsForEmail(TextInputLayoutEmail.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                loading.stop();
                                if(!task.getResult().getSignInMethods().isEmpty()) {
                                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                    firebaseAuth.sendPasswordResetEmail(Email);
                                    new PopUpMSG(ResetPassword.this,getResources().getString(R.string.ResetPassword),getResources().getString(R.string.ResetLink),SignIn.class);
                                }
                                else
                                    TextInputLayoutEmail.setHelperText(getResources().getString(R.string.EmailNotExist));
                            }
                        });
                    }
                }
                if(TextInputLayoutEmail.getEditText().getText().length() < 1)
                    TextInputLayoutEmail.setHelperText(getResources().getString(R.string.Required));
            }
        });
    }
    private void CreateAccount(){
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartActivity(CreateAccount.class); }
        });
    }
    private void BackIcon(){
        BackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartActivity(LiftYourLife.class); }
        });
    }
    private void StartActivity(Class Destination){
        startActivity(new Intent(ResetPassword.this, Destination));
        finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResetPassword.this, LiftYourLife.class));
        finish();
    }
}