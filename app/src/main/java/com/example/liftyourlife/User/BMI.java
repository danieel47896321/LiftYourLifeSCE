package com.example.liftyourlife.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Class.UserNavigationHeader;
import com.example.liftyourlife.Class.UserNavigationView;
import com.example.liftyourlife.R;
import com.google.android.material.navigation.NavigationView;


public class BMI extends AppCompatActivity {
    private NavigationView UserNavigationView;
    private ImageView BackIcon, MenuIcon;
    private SeekBar seekBarHeight, seekBarWeight;
    private DrawerLayout drawerLayout;
    private ConstraintLayout constraintLayoutResult;
    private TextView Title, Height, Weight, UnderWeightRange, NormalRange, OverWeightRange, ObeseRange, ExtremelyObeseRange, BmiResult;
    private Intent intent;
    private Button CalcBMI;
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        init();
    }
    private void init(){
        setID();
        MenuIcon();
        BackIcon();
        NavigationView();
        MenuItem();
        BMI();
    }
    private void setID(){
        intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        UserNavigationView = findViewById(R.id.navigationView);
        BackIcon = findViewById(R.id.BackIcon);
        MenuIcon = findViewById(R.id.MenuIcon);
        CalcBMI = findViewById(R.id.CalcBMI);
        Height = findViewById(R.id.Height);
        constraintLayoutResult = findViewById(R.id.constraintLayoutResult);
        Weight = findViewById(R.id.Weight);
        seekBarHeight = findViewById(R.id.seekBarHeight);
        seekBarWeight = findViewById(R.id.seekBarWeight);
        drawerLayout = findViewById(R.id.drawerLayout);
        BmiResult = findViewById(R.id.BmiResult);
        UnderWeightRange = findViewById(R.id.UnderWeightRange);
        NormalRange = findViewById(R.id.NormalRange);
        OverWeightRange = findViewById(R.id.OverWeightRange);
        ObeseRange = findViewById(R.id.ObeseRange);
        ExtremelyObeseRange = findViewById(R.id.ExtremelyObeseRange);
        Title = findViewById(R.id.Title);
        Title.setText(R.string.Bmi);
        new UserNavigationHeader(user, BMI.this);
        UnderWeightRange.setText("x < 18.5");
        NormalRange.setText("18.5 < x < 24.9");
        OverWeightRange.setText("25 < x < 29.9");
        ObeseRange.setText("30 < x < 34.9");
        ExtremelyObeseRange.setText("35 < x");
    }
    private void BMI(){
        seekBarWeight.setProgress(Integer.valueOf(user.getWeight()));
        Weight.setText(getResources().getString(R.string.Weight) + ": " + user.getWeight() + "(" + getResources().getString(R.string.Kg) + ")");
        seekBarHeight.setProgress(Integer.valueOf(user.getHeight()));
        Height.setText(getResources().getString(R.string.Height) + ": " + user.getHeight() + "(" + getResources().getString(R.string.Cm) + ")");
        seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int i = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                i = progress;
                Height.setText(getResources().getString(R.string.Height) + ": " + i + "(" + getResources().getString(R.string.Cm) + ")");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        seekBarWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int i = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                i = progress;
                Weight.setText(getResources().getString(R.string.Weight) + ": " + i + "(" + getResources().getString(R.string.Kg) + ")");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        CalcBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seekBarWeight.getProgress() > 0  && seekBarHeight.getProgress() > 0)
                    CheckBMI();
            }
        });
    }
    public void CheckBMI(){
        constraintLayoutResult.setVisibility(View.VISIBLE);
        double bmi = (100*100*(double)seekBarWeight.getProgress()) / (double)(seekBarHeight.getProgress() * seekBarHeight.getProgress());
        BmiResult.setText("My BMI calculation: " +  String.format("%.1f",bmi));
        if(bmi < 18.5) {
            constraintLayoutResult.setBackgroundColor(getResources().getColor(R.color.UNDERWEIGHT));
        }else if (bmi < 25) {
            constraintLayoutResult.setBackgroundColor(getResources().getColor(R.color.NORMAL));
        }else if (bmi < 30) {
            constraintLayoutResult.setBackgroundColor(getResources().getColor(R.color.OVERWEIGHT));
        }else if (bmi < 35) {
            constraintLayoutResult.setBackgroundColor(getResources().getColor(R.color.OBESE));
        }else {
            constraintLayoutResult.setBackgroundColor(getResources().getColor(R.color.EXTREMELYOBESE));
        }
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
                new UserNavigationView(BMI.this, item.getItemId(), user);
                return false;
            }
        });
    }
    private void MenuItem(){
        Menu menu= UserNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.ItemBmi);
        menuItem.setCheckable(false);
        menuItem.setChecked(true);
        menuItem.setEnabled(false);
    }
    public void onBackPressed(){
        intent = new Intent(BMI.this, Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}