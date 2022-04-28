package com.example.liftyourlife.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liftyourlife.Class.Plan;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Class.UserNavigationHeader;
import com.example.liftyourlife.Class.UserNavigationView;
import com.example.liftyourlife.Fragments.FridayFragment;
import com.example.liftyourlife.Fragments.MondayFragment;
import com.example.liftyourlife.Fragments.SaturdayFragment;
import com.example.liftyourlife.Fragments.SundayFragment;
import com.example.liftyourlife.Fragments.ThursdayFragment;
import com.example.liftyourlife.Fragments.TuesdayFragment;
import com.example.liftyourlife.Fragments.WednesdayFragment;
import com.example.liftyourlife.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;

public class GenericPlan extends AppCompatActivity {
    private NavigationView UserNavigationView;
    private ImageView BackIcon, MenuIcon;
    private DrawerLayout drawerLayout;
    private TextView Title;
    private Intent intent;
    private Plan plan;
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_plan);
        init();
    }
    private void init(){
        setID();
        MenuIcon();
        BackIcon();
        NavigationView();
        MenuItem();
    }
    private void setID(){
        intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        plan = (Plan) intent.getSerializableExtra("plan");
        UserNavigationView = findViewById(R.id.navigationView);
        BackIcon = findViewById(R.id.BackIcon);
        MenuIcon = findViewById(R.id.MenuIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        Title.setText(plan.getPlanName());
        new UserNavigationHeader(user,GenericPlan.this);
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
                new UserNavigationView(GenericPlan.this, item.getItemId(), user);
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
        intent = new Intent(GenericPlan.this, WorkOut.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}