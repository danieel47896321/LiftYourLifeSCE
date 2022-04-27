package com.example.liftyourlife.Guest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liftyourlife.Class.GuestNavigationView;
import com.example.liftyourlife.Class.Loading;
import com.example.liftyourlife.Class.PopUpMSG;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {
    private ImageView BackIcon, MenuIcon;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextInputLayout TextInputLayoutFirstName, TextInputLayoutLastName ,TextInputLayoutEmail, TextInputLayoutPassword, TextInputLayoutPasswordConfirm;
    private TextView Title, SignIn, TextViewSearchHeight, TextViewSearchWeight, TextViewSearchAge, TextViewSearchGender,TextViewSearch;
    private Dialog dialog;
    private ListView ListViewSearch;
    private EditText EditTextSearch;
    private Button ButtonNext, next_btn;
    private Loading loading;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://liftyourlife-9d039-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        init();
    }
    private void init(){
        setID();
        MenuItem();
        BackIcon();
        MenuIcon();
        EndIcon();
        NavigationView();
        SignOut();
        AlreadyHaveAccount();
        CreateAccountCheck();
    }
    private void setID(){
        MenuIcon = findViewById(R.id.MenuIcon);
        BackIcon = findViewById(R.id.BackIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        navigationView = findViewById(R.id.navigationView);
        Title.setText(R.string.CreateAccount);
        SignIn = findViewById(R.id.SignIn);
        TextInputLayoutFirstName = findViewById(R.id.TextInputLayoutFirstName);
        TextInputLayoutLastName = findViewById(R.id.TextInputLayoutLastName);
        TextInputLayoutEmail = findViewById(R.id.TextInputLayoutEmail);
        TextInputLayoutPassword = findViewById(R.id.TextInputLayoutPassword);
        TextInputLayoutPasswordConfirm = findViewById(R.id.TextInputLayoutPasswordConfirm);
        ButtonNext = findViewById(R.id.ButtonNext);
    }
    private void MenuItem(){
        Menu menu= navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.ItemCreateAccount);
        menuItem.setCheckable(false);
        menuItem.setChecked(true);
        menuItem.setEnabled(false);
    }
    private void EndIcon() {
        TextInputLayoutFirstName.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Clear(TextInputLayoutFirstName); }
        });
        TextInputLayoutLastName.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Clear(TextInputLayoutLastName); }
        });
        TextInputLayoutEmail.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Clear(TextInputLayoutEmail); }
        });
    }
    private void Clear(TextInputLayout input){
        input.setHelperText("");
        input.getEditText().setText("");
    }
    private void SignOut(){
        if(firebaseAuth.getCurrentUser() != null)
            firebaseAuth.getInstance().signOut();
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
                new GuestNavigationView(CreateAccount.this, item.getItemId());
                return false;
            }
        });
    }
    private void AlreadyHaveAccount(){
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartActivity(SignIn.class); }
        });
    }
    private boolean isEmailValid(CharSequence email) { return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); }
    private void CheckEmailExists(){
        firebaseAuth.fetchSignInMethodsForEmail(TextInputLayoutEmail.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                loading.stop();
                if(!task.getResult().getSignInMethods().isEmpty()){
                    TextInputLayoutEmail.setHelperText(getResources().getString(R.string.EmailExist));
                }
                else {
                    TextInputLayoutEmail.setHelperText("");
                    fillInfo();
                }
            }
        });
    }

    private boolean CheckEmail(){
        if(TextInputLayoutEmail.getEditText().getText().length()<1) {
            TextInputLayoutEmail.setHelperText(getResources().getString(R.string.Required));
            return false;
        }
        else if(!isEmailValid(TextInputLayoutEmail.getEditText().getText().toString())) {
            TextInputLayoutEmail.setHelperText(getResources().getString(R.string.InvalidEmail));
            return false;
        }
        else
            TextInputLayoutEmail.setHelperText("");
        return true;
    }
    private boolean CheckFirstName(){
        if(TextInputLayoutFirstName.getEditText().getText().length()<1) {
            TextInputLayoutFirstName.setHelperText(getResources().getString(R.string.Required));
            return false;
        }
        else
            TextInputLayoutFirstName.setHelperText("");
        return true;
    }
    private boolean CheckLastName(){
        if(TextInputLayoutLastName.getEditText().getText().length()<1) {
            TextInputLayoutLastName.setHelperText(getResources().getString(R.string.Required));
            return false;
        }
        else
            TextInputLayoutLastName.setHelperText("");
        return true;
    }
    private boolean CheckPassword(){
        if(TextInputLayoutPassword.getEditText().getText().length()<1) {
            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.Required));
            return false;
        }
        else if(TextInputLayoutPassword.getEditText().getText().length()<6) {
            TextInputLayoutPassword.setHelperText(getResources().getString(R.string.Must6Chars));
            return false;
        }
        else
            TextInputLayoutPassword.setHelperText("");
        return true;
    }
    private boolean CheckConfirmPassword(){
        if(TextInputLayoutPasswordConfirm.getEditText().getText().length()<1) {
            TextInputLayoutPasswordConfirm.setHelperText(getResources().getString(R.string.Required));
            return false;
        }
        else if(TextInputLayoutPasswordConfirm.getEditText().getText().length()<6){
            TextInputLayoutPasswordConfirm.setHelperText(getResources().getString(R.string.Must6Chars));
            return false;
        }
        else
            TextInputLayoutPasswordConfirm.setHelperText("");
        return true;
    }
    private void CheckValues(){
        if( CheckFirstName() && CheckLastName() && CheckEmail() && CheckPassword() && CheckConfirmPassword()){
            if(!(TextInputLayoutPassword.getEditText().getText().toString().equals(TextInputLayoutPasswordConfirm.getEditText().getText().toString())))
                TextInputLayoutPasswordConfirm.setHelperText(getResources().getString(R.string.DifferentPassword));
            else{
                loading = new Loading(CreateAccount.this);
                CheckEmailExists();
            }
        }
    }
    private void CreateAccountCheck(){
        ButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckFirstName();
                CheckLastName();
                CheckEmail();
                CheckPassword();
                CheckConfirmPassword();
                CheckValues();
            }
        });
    }
    private void fillInfo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_createaccount,null);
        builder.setCancelable(false);
        builder.setView(dialogView);
        TextViewSearchHeight = dialogView.findViewById(R.id.TextViewSearchHeight);
        TextViewSearchWeight = dialogView.findViewById(R.id.TextViewSearchWeight);
        TextViewSearchAge = dialogView.findViewById(R.id.TextViewSearchAge);
        TextViewSearchGender = dialogView.findViewById(R.id.TextViewSearchGender);
        next_btn  = dialogView.findViewById(R.id.next_btn);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        HeightPick();
        WeightPick();
        AgePick();
        GenderPick();
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(TextViewSearchHeight.getText().toString().equals("")) && !(TextViewSearchWeight.getText().toString().equals("")) && !(TextViewSearchAge.getText().toString().equals("")) && !(TextViewSearchGender.getText().toString().equals(""))){
                    alertDialog.cancel();
                    CreateAccount();
                }
            }
        });
    }
    private void CreateAccount(){
        user = new User(TextInputLayoutFirstName.getEditText().getText().toString(), TextInputLayoutLastName.getEditText().getText().toString(), TextInputLayoutEmail.getEditText().getText().toString());
        user.setHeight(TextViewSearchHeight.getText().toString());
        user.setWeight(TextViewSearchWeight.getText().toString());
        user.setAge(TextViewSearchAge.getText().toString());
        user.setGender(TextViewSearchGender.getText().toString());
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(),TextInputLayoutPassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.setUid(firebaseAuth.getUid());
                            user.setFullName(user.getFirstName()+" "+user.getLastName());
                            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                            new PopUpMSG(CreateAccount.this, getResources().getString(R.string.CreateAccount), getResources().getString(R.string.CompleteCreateAccount), SignIn.class);
                        }
                    });
                }
            }
        });
    }
    private void HeightPick(){
        String height[] = new String[300];
        int index =0;
        for(int i=0; i < 3 ; i++) {
            for(int j=0; j<100; j++)
                height[index++] = "" + i+ "."+ j;
        }
        TextViewSearchHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog(height ,getResources().getString(R.string.SelectHeight),TextViewSearchHeight);
            }
        });
    }
    private void WeightPick(){
        String weight[] = new String[261];
        for(int i=0; i < weight.length ; i++)
            weight[i] = ""+(i+40)+" "+getResources().getString(R.string.Kg);
        TextViewSearchWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog(weight ,getResources().getString(R.string.SelectWeight),TextViewSearchWeight);
            }
        });
    }
    private void AgePick(){
        String age[] = new String[102];
        for(int i=0; i < age.length ; i++)
            age[i] = ""+(i+18);
        TextViewSearchAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog(age ,getResources().getString(R.string.SelectAge),TextViewSearchAge);
            }
        });
    }
    private void GenderPick(){
        TextViewSearchGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog(getResources().getStringArray(R.array.Gender),getResources().getString(R.string.SelectGender),TextViewSearchGender);
            }
        });
    }
    private void setDialog(String[] array, String title,TextView textViewPick){
        dialog = new Dialog(CreateAccount.this);
        dialog.setContentView(R.layout.dialog_search_spinner);
        dialog.getWindow().setLayout(1000,950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditTextSearch = dialog.findViewById(R.id.EditTextSearch);
        ListViewSearch = dialog.findViewById(R.id.ListViewSearch);
        TextViewSearch = dialog.findViewById(R.id.TextViewSearch);
        TextViewSearch.setText(title);
        if(title.equals(getResources().getString(R.string.SelectHeight))){
            for(int i=0; i<array.length;i++)
                array[i] += " "+ getResources().getString(R.string.Meters);
        }
        if(title.equals(getResources().getString(R.string.SelectWeight))){
            for(int i=0; i<array.length;i++)
                array[i] += " "+ getResources().getString(R.string.Kg);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateAccount.this, R.layout.dropdwon_item, array);
        ListViewSearch.setAdapter(adapter);
        EditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        ListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                textViewPick.setText(adapterView.getItemAtPosition(i).toString());
            }
        });
    }
    private void BackIcon(){
        BackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartActivity(LiftYourLife.class); }
        });
    }
    private void StartActivity(Class Destination){
        startActivity(new Intent(CreateAccount.this, Destination));
        finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateAccount.this, LiftYourLife.class));
        finish();
    }
}