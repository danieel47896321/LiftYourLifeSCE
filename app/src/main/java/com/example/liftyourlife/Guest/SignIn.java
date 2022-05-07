package com.example.liftyourlife.Guest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liftyourlife.Class.GuestNavigationView;
import com.example.liftyourlife.Class.Loading;
import com.example.liftyourlife.Class.PopUpMSG;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.R;
import com.example.liftyourlife.Server.RetrofitInterface;
import com.example.liftyourlife.User.Home;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
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


public class SignIn extends AppCompatActivity {
    private TextView Title;
    private DrawerLayout drawerLayout;
    private ImageView BackIcon, MenuIcon;
    private NavigationView navigationView;
    private TextView CreateAccount;
    private TextInputLayout TextInputLayoutEmail, TextInputLayoutPassword;
    private Button ButtonSignIn;
    private User user = new User();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
    }
    private void init(){
        setID();
        MenuItem();
        BackIcon();
        MenuIcon();
        EndIcon();
        NavigationView();
        SignInCheck();
        CreateAccount();
    }
    private void setID(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        firebaseAuth = FirebaseAuth.getInstance();
        MenuIcon = findViewById(R.id.MenuIcon);
        BackIcon = findViewById(R.id.BackIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        navigationView = findViewById(R.id.navigationView);
        Title.setText(R.string.SignIn);
        CreateAccount = findViewById(R.id.CreateAccount);
        TextInputLayoutEmail = findViewById(R.id.TextInputLayoutEmail);
        TextInputLayoutPassword = findViewById(R.id.TextInputLayoutPassword);
        ButtonSignIn = findViewById(R.id.ButtonSignIn);
    }
    private void MenuItem(){
        Menu menu= navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.ItemSignIn);
        menuItem.setCheckable(false);
        menuItem.setChecked(true);
        menuItem.setEnabled(false);
    }
    private void EndIcon() {
        TextInputLayoutEmail.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayoutEmail.setHelperText("");
                TextInputLayoutEmail.getEditText().setText("");
            }
        });
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
                new GuestNavigationView(SignIn.this, item.getItemId());
                return false;
            }
        });
    }
    private void CreateAccount(){
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartActivity(CreateAccount.class); }
        });
    }
    private boolean isEmailValid(CharSequence email) { return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); }
    private void CheckValues(){
        if(TextInputLayoutEmail.getEditText().getText().length()<1)
            TextInputLayoutEmail.setHelperText(getResources().getString(R.string.Required));
        else if(!isEmailValid(TextInputLayoutEmail.getEditText().getText().toString()))
            TextInputLayoutEmail.setHelperText(getResources().getString(R.string.InvalidEmail));
        else
            TextInputLayoutEmail.setHelperText("");
        if(TextInputLayoutPassword.getEditText().getText().length()<1)
            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.Required));
        else if(TextInputLayoutPassword.getEditText().getText().length()<6)
            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.Must6Chars));
        else
            TextInputLayoutPassword.setHelperText("");
    }
    private void SignInCheck(){
        ButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() != null)
                    firebaseAuth.getInstance().signOut();
                CheckValues();
                if(TextInputLayoutEmail.getEditText().getText().length() > 0 && TextInputLayoutPassword.getEditText().getText().length() > 5 && isEmailValid(TextInputLayoutEmail.getEditText().getText().toString()) && TextInputLayoutPassword.getEditText().getText().length() > 0)
                    SignIn();
            }
        });
    }
    private void CheckEmailExists(){
        firebaseAuth.fetchSignInMethodsForEmail(TextInputLayoutEmail.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().getSignInMethods().isEmpty())
                        TextInputLayoutEmail.setHelperText(getResources().getString(R.string.EmailNotExist));
                    else {
                        TextInputLayoutEmail.setHelperText("");
                        if (TextInputLayoutPassword.getEditText().getText().length() < 1)
                            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.Required));
                        else if (TextInputLayoutPassword.getEditText().getText().length() < 6)
                            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.Must6Chars));
                        else
                            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.WrongPassword));
                    }
                }
            }
        });
    }
    private void SignIn(){
        firebaseAuth.signInWithEmailAndPassword(TextInputLayoutEmail.getEditText().getText().toString(), TextInputLayoutPassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                        getUser();
                    }
                    else {
                        Toast.makeText(SignIn.this, R.string.CheckEmailVerify, Toast.LENGTH_LONG).show();
                    }
                } else
                    CheckEmailExists();
            }
        });
    }
    private void getUser(){
        if(firebaseAuth.getCurrentUser() != null) {
            HashMap<String,String> map = new HashMap<>();
            map.put("uid",firebaseAuth.getCurrentUser().getUid());
            Call<User> call = retrofitInterface.getUser(map);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        user = response.body();
                        Home(user);
                    } else if (response.code() == 404) { new PopUpMSG(SignIn.this, getResources().getString(R.string.SignIn), getResources().getString(R.string.Error)); }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) { }
            });
        }
    }
    private void Home(User user){
        Intent intent = new Intent(SignIn.this, Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
    private void BackIcon(){
        BackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartActivity(LiftYourLife.class); }
        });
    }
    private void StartActivity(Class Destination){
        startActivity(new Intent(SignIn.this, Destination));
        finish();
    }
    @Override
    public void onBackPressed() {
        StartActivity(LiftYourLife.class);
    }
}