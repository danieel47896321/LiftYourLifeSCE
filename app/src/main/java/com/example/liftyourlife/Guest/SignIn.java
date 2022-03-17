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

import com.example.liftyourlife.Class.GuestLanguage;
import com.example.liftyourlife.Class.GuestMenuView;
import com.example.liftyourlife.Class.Loading;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.R;
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

public class SignIn extends AppCompatActivity {
    private TextView Title, TextViewSearchLanguage;
    private DrawerLayout drawerLayout;
    private ImageView BackIcon, MenuIcon;
    private NavigationView GuestMenuView;
    private TextView ResetPassword, CreateAccount;
    private TextInputLayout TextInputLayoutEmail, TextInputLayoutPassword;
    private Button ButtonSignIn, Google;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 4;
    private static final String TAG = "GoogleActivity";
    private Loading loading;
    private Intent intent;
    private User user = new User();
    private GuestLanguage guestLanguage;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://liftyourlife-9d039-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
    }
    private void init(){
        setID();
        setLanguage();
        MenuItem();
        BackIcon();
        MenuIcon();
        EndIcon();
        NavView();
        SignInCheck();
        ResetPassword();
        CreateAccount();
        Google();
    }
    private void setID(){
        firebaseAuth = FirebaseAuth.getInstance();
        MenuIcon = findViewById(R.id.MenuIcon);
        BackIcon = findViewById(R.id.BackIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        GuestMenuView = findViewById(R.id.GuestNavView);
        Title.setText(R.string.SignIn);
        CreateAccount = findViewById(R.id.CreateAccount);
        Google = findViewById(R.id.Google);
        ResetPassword = findViewById(R.id.ResetPassword);
        TextInputLayoutEmail = findViewById(R.id.TextInputLayoutEmail);
        TextInputLayoutPassword = findViewById(R.id.TextInputLayoutPassword);
        ButtonSignIn = findViewById(R.id.ButtonSignIn);
        TextViewSearchLanguage = findViewById(R.id.TextViewSearchLanguage);
        guestLanguage = new GuestLanguage(SignIn.this);
    }
    private void setLanguage(){
        TextViewSearchLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { guestLanguage.setDialog(); }
        });
    }
    private void MenuItem(){
        Menu menu= GuestMenuView.getMenu();
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
    private void ResetPassword(){
        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartActivity(ResetPassword.class); }
        });
    }
    private void MenuIcon(){
        MenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.open(); }
        });
    }
    private void NavView(){
        GuestMenuView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                new GuestMenuView(SignIn.this, item.getItemId());
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
                if(task.getResult().getSignInMethods().isEmpty())
                    TextInputLayoutEmail.setHelperText(getResources().getString(R.string.EmailNotExist));
                else {
                    TextInputLayoutEmail.setHelperText("");
                    if(TextInputLayoutPassword.getEditText().getText().length()<1)
                        TextInputLayoutPassword.setHelperText(getResources().getString(R.string.Required));
                    else if(TextInputLayoutPassword.getEditText().getText().length()<6)
                        TextInputLayoutPassword.setHelperText(getResources().getString(R.string.Must6Chars));
                    else
                        TextInputLayoutPassword.setHelperText(getResources().getString(R.string.WrongPassword));
                }
                loading.stop();
            }
        });
    }
    private void Google(){
        Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() != null)
                    firebaseAuth.getInstance().signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
                mGoogleSignInClient = GoogleSignIn.getClient(SignIn.this, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                loading = new Loading(SignIn.this);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) { loading.stop(); }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            // Sign in success, update UI with the signed-in user's information
                            if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                int firstSpace = firebaseUser.getDisplayName().indexOf(" ");
                                user.setEmail(firebaseUser.getEmail());
                                user.setFirstName(firebaseUser.getDisplayName().substring(0,firstSpace));
                                user.setLastName(firebaseUser.getDisplayName().substring(firstSpace+1));
                                user.setFullName(user.getFirstName()+" "+user.getLastName());
                                user.setUid(firebaseAuth.getCurrentUser().getUid());
                                databaseReference.child(firebaseUser.getUid()).setValue(user);
                                intent = new Intent(SignIn.this, Home.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                finish();
                            }
                            getUser();
                        }
                        else {
                            Toast.makeText(SignIn.this, "signInWithCredential:failure", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
    private void SignIn(){
        firebaseAuth.signInWithEmailAndPassword(TextInputLayoutEmail.getEditText().getText().toString(), TextInputLayoutPassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loading = new Loading(SignIn.this);
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
    }
    private void Home(User user){
        Intent intent = new Intent(SignIn.this, Home.class);
        intent.putExtra("user", user);
        loading.stop();
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
        startActivity(new Intent(SignIn.this, LiftYourLife.class));
        finish();
    }
}