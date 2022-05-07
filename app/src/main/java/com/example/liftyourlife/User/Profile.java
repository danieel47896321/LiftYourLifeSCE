package com.example.liftyourlife.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liftyourlife.Class.PopUpMSG;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Class.UserNavigationHeader;
import com.example.liftyourlife.Class.UserNavigationView;
import com.example.liftyourlife.R;
import com.example.liftyourlife.Server.RetrofitInterface;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {
    private TextView Title,TextViewSearch;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    private TextInputLayout TextInputLayoutFirstName, TextInputLayoutLastName, TextInputLayoutHeight, TextInputLayoutWeight, TextInputLayoutBirthDay, TextInputLayoutGender;;
    private Button Confirm;
    private DrawerLayout drawerLayout;
    private ImageView BackIcon, MenuIcon;
    private Calendar calendar = Calendar.getInstance();
    private int Year = calendar.get(Calendar.YEAR), Month = calendar.get(Calendar.MONTH), Day = calendar.get(Calendar.DAY_OF_MONTH), UserYear, UserMonth, UserDay;
    private Intent intent;
    private NavigationView navigationView;
    private Dialog dialog;
    private EditText EditTextSearch;
    private ListView ListViewSearch;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }
    void init(){
        setID();
        MenuItem();
        BackIcon();
        MenuIcon();
        NavigationView();
        Confirm();
        ShowInfo();
    }
    public void setID(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        intent = getIntent();
        TextInputLayoutFirstName = findViewById(R.id.TextInputLayoutFirstName);
        TextInputLayoutLastName = findViewById(R.id.TextInputLayoutLastName);
        TextInputLayoutHeight = findViewById(R.id.TextInputLayoutHeight);
        TextInputLayoutWeight = findViewById(R.id.TextInputLayoutWeight);
        TextInputLayoutBirthDay = findViewById(R.id.TextInputLayoutBirthDay);
        TextInputLayoutGender = findViewById(R.id.TextInputLayoutGender);
        Confirm = findViewById(R.id.confirm);
        navigationView = findViewById(R.id.navigationView);
        MenuIcon = findViewById(R.id.MenuIcon);
        BackIcon = findViewById(R.id.BackIcon);
        Title = findViewById(R.id.Title);
        Title.setText(getResources().getString(R.string.Profile));
        user = (User)intent.getSerializableExtra("user");
        String[] date1 = user.getBirthDay().split("/");
        Year = Integer.valueOf(date1[2]);
        UserYear = Integer.valueOf(date1[2]);
        Month = Integer.valueOf(date1[1]);
        UserMonth = Integer.valueOf(date1[1]);
        Day = Integer.valueOf(date1[0]);
        UserDay = Integer.valueOf(date1[0]);
        drawerLayout = findViewById(R.id.drawerLayout);
        new UserNavigationHeader(user,Profile.this);
    }
    private void MenuItem(){
        Menu menu= navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.ItemProfile);
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
                new UserNavigationView(Profile.this, item.getItemId(), user);
                return false;
            }
        });
    }
    private void ShowInfo(){
        TextInputLayoutFirstName.getEditText().setText(user.getFirstName());
        TextInputLayoutLastName.getEditText().setText(user.getLastName());
        TextInputLayoutGender.getEditText().setText(user.getGender());
        TextInputLayoutBirthDay.getEditText().setText(user.getBirthDay());
        TextInputLayoutHeight.getEditText().setText(user.getHeight() + " " + getResources().getString(R.string.Cm) );
        TextInputLayoutWeight.getEditText().setText(user.getWeight() + " " + getResources().getString(R.string.Kg) );
        HeightPick();
        WeightPick();
        BirthDayPick();
        GenderPick();
    }
    private void Confirm(){
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInput()) {
                    String Height="",Weight="";
                    user.setFirstName(TextInputLayoutFirstName.getEditText().getText().toString());
                    user.setLastName(TextInputLayoutLastName.getEditText().getText().toString());
                    user.setGender(TextInputLayoutGender.getEditText().getText().toString());
                    user.setBirthDay(TextInputLayoutBirthDay.getEditText().getText().toString());
                    for(int i=0; i < 301 ; i++)
                        if(TextInputLayoutHeight.getEditText().getText().toString().equals( i + " " + getResources().getString(R.string.Cm)))
                            Height = i +"";
                    user.setHeight(Height);
                    for(int i=0; i < 261 ; i++)
                        if(TextInputLayoutWeight.getEditText().getText().toString().equals( (i+20)+" "+getResources().getString(R.string.Kg)))
                            Weight = (i+20)+"";
                    user.setWeight(Weight);
                    user.setFullName(user.getFirstName() + " " + user.getLastName());
                    updateData();

                }
            }
        });
    }
    private void updateData(){
        HashMap<String,String> map = new HashMap<>();
        map.put("uid",user.getUid());
        map.put("email",user.getEmail());
        map.put("fullName",user.getFullName());
        map.put("firstName",user.getFirstName());
        map.put("lastName",user.getLastName());
        map.put("gender",user.getGender());
        map.put("birthDay",user.getBirthDay());
        map.put("startDate",user.getStartDate());
        map.put("height",user.getHeight());
        map.put("weight",user.getWeight());
        Call<Void> call = retrofitInterface.updateProfile(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    new PopUpMSG(Profile.this, getResources().getString(R.string.Profile), getResources().getString(R.string.ProfileUpdated));
                } else if (response.code() == 404) {
                    new PopUpMSG(Profile.this, getResources().getString(R.string.Profile), getResources().getString(R.string.Error));
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                new PopUpMSG(Profile.this, getResources().getString(R.string.CreateAccount), t.getMessage());
            }
        });

        //DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://liftyourlife-9d039-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(user.getUid());
    }
    private int getYears(){
        int years = calendar.get(Calendar.YEAR) - UserYear;
        if( UserMonth >  (calendar.get(Calendar.MONTH) + 1))
            years -= 1;
        else if( UserMonth ==  (calendar.get(Calendar.MONTH) + 1))
            if (UserDay > calendar.get(Calendar.DAY_OF_MONTH))
                years -= 1;
        return years;
    }
    private boolean CheckInput(){
        if(TextInputLayoutFirstName.getEditText().getText().length() < 1 || TextInputLayoutLastName.getEditText().getText().length() < 1 ) {
            if(TextInputLayoutFirstName.getEditText().getText().length() < 1 )
                TextInputLayoutFirstName.setHelperText(getResources().getString(R.string.Required));
            else
                TextInputLayoutFirstName.setHelperText("");
            if(TextInputLayoutLastName.getEditText().getText().length() < 1 )
                TextInputLayoutLastName.setHelperText(getResources().getString(R.string.Required));
            else
                TextInputLayoutLastName.setHelperText("");
            return false;
        }
        if(getYears() < 18){
            TextInputLayoutBirthDay.setHelperText(getResources().getString(R.string.RequiredAge18OrMore));
            return false;
        }
        else
            TextInputLayoutBirthDay.setHelperText("");
        TextInputLayoutFirstName.setHelperText("");
        TextInputLayoutLastName.setHelperText("");
        return true;
    }
    private void HeightPick(){
        String height[] = new String[301];
        int index =0;
        for(int i=0; i < height.length ; i++)
            height[index++] = i + " " + getResources().getString(R.string.Cm);
        TextInputLayoutHeight.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog(height ,getResources().getString(R.string.SelectHeight),TextInputLayoutHeight.getEditText());
            }
        });
    }
    private void WeightPick(){
        String weight[] = new String[301];
        for(int i=0; i < weight.length ; i++)
            weight[i] = ""+(i+0)+" "+getResources().getString(R.string.Kg);
        TextInputLayoutWeight.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog(weight ,getResources().getString(R.string.SelectWeight),TextInputLayoutWeight.getEditText());
            }
        });
    }
    private void BirthDayPick(){
        TextInputLayoutBirthDay.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Profile.this, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        UserMonth = month;
                        UserYear = year;
                        UserDay = dayOfMonth;
                        Year = year;
                        Month = month;
                        Day = dayOfMonth;
                        String Date = dayOfMonth + "/" + month + "/" + year;
                        TextInputLayoutBirthDay.getEditText().setText(Date);
                    }
                },Year, Month-1, Day);
                datePickerDialog.show();
            }
        });
    }
    private void GenderPick(){
        TextInputLayoutGender.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog(getResources().getStringArray(R.array.Gender),getResources().getString(R.string.SelectGender),TextInputLayoutGender.getEditText());
            }
        });
    }
    private void setDialog(String[] array, String title,TextView textViewPick){
        dialog = new Dialog(Profile.this);
        dialog.setContentView(R.layout.dialog_search_spinner);
        dialog.getWindow().setLayout(1000,950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditTextSearch = dialog.findViewById(R.id.EditTextSearch);
        ListViewSearch = dialog.findViewById(R.id.ListViewSearch);
        TextViewSearch = dialog.findViewById(R.id.TextViewSearch);
        TextViewSearch.setText(title);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Profile.this, R.layout.dropdown_item, array);
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
            public void onClick(View v) { StartActivity(Home.class); }
        });
    }
    @Override
    public void onBackPressed() { StartActivity(Home.class); }
    private void StartActivity(Class Destination){
        intent = new Intent(Profile.this, Destination);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

}