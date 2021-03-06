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

import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Class.UserNavigationHeader;
import com.example.liftyourlife.Class.UserNavigationView;
import com.example.liftyourlife.Fragments.WorkoutFragment;
import com.example.liftyourlife.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;

public class WorkOut extends AppCompatActivity {
    private NavigationView UserNavigationView;
    private ImageView BackIcon, MenuIcon;
    private DrawerLayout drawerLayout;
    private TextView Title;
    private Intent intent;
    private User user = new User();
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private String[] titles;
    private ViewPagerAdapter fragmentPager;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out);
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(){
        setID();
        MenuIcon();
        BackIcon();
        NavigationView();
        MenuItem();
        setCurrentDay();
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
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        fragmentPager = new ViewPagerAdapter(WorkOut.this);
        viewPager2.setAdapter(fragmentPager);
        titles = new String[7];
        titles = getResources().getStringArray(R.array.Workout);
        new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> tab.setText(titles[position]))).attach();
        new UserNavigationHeader(user,WorkOut.this);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        switch (day) {
            case Calendar.SUNDAY:
                tab = tabLayout.getTabAt(0);
                break;
            case Calendar.MONDAY:
                tab = tabLayout.getTabAt(1);
                break;
            case Calendar.TUESDAY:
                tab = tabLayout.getTabAt(2);
                break;
            case Calendar.WEDNESDAY:
                tab = tabLayout.getTabAt(3);
                break;
            case Calendar.THURSDAY:
                tab = tabLayout.getTabAt(4);
                break;
            case Calendar.FRIDAY:
                tab = tabLayout.getTabAt(5);
                break;
            case Calendar.SATURDAY:
                tab = tabLayout.getTabAt(6);
                break;
        }
        tab.select();
    }
    public static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            WorkoutFragment workoutFragment = new WorkoutFragment();
            switch (position){
                case 0:
                    workoutFragment.setDay("SUNDAY");
                    return workoutFragment;
                case 1:
                    workoutFragment.setDay("MONDAY");
                    return workoutFragment;
                case 2:
                    workoutFragment.setDay("TUESDAY");
                    return workoutFragment;
                case 3:
                    workoutFragment.setDay("WEDNESDAY");
                    return workoutFragment;
                case 4:
                    workoutFragment.setDay("THURSDAY");
                    return workoutFragment;
                case 5:
                    workoutFragment.setDay("FRIDAY");
                    return workoutFragment;
                case 6:
                    workoutFragment.setDay("SATURDAY");
                    return workoutFragment;
            }
            workoutFragment.setDay("SUNDAY");
            return workoutFragment;
        }
        @Override
        public int getItemCount() { return 7; }
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
        MenuItem menuItem = menu.findItem(R.id.ItemWorkOut);
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