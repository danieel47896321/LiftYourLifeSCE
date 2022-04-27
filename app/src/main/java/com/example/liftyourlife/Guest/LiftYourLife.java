package com.example.liftyourlife.Guest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liftyourlife.Adapters.LiftYourLifeAdapter;
import com.example.liftyourlife.Class.GuestNavigationView;
import com.example.liftyourlife.Class.Tag;
import com.example.liftyourlife.R;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class LiftYourLife extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private List<Tag> tagList;
    private NavigationView navigationView;
    private TextView Title;
    private ImageView BackIcon, MenuIcon;
    private final int SIZE = 5;
    private String LiftYourLifeTagsName[] = new String[SIZE];
    private int TagsPhoto[] = {R.drawable.sign_in,R.drawable.create_account,R.drawable.reset_password,R.drawable.about,R.drawable.contact};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lift_your_life);
        init();
    }
    private void init(){
        tagList = new ArrayList<>();
        setID();
        setTags();
        MenuItem();
        MenuIcon();
        NavigationView();
    }
    private void setID(){
        MenuIcon = findViewById(R.id.MenuIcon);
        BackIcon = findViewById(R.id.BackIcon);
        BackIcon.setVisibility(View.INVISIBLE);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        Title.setText("");
        navigationView = findViewById(R.id.navigationView);
        recyclerView = findViewById(R.id.recyclerView);
        LiftYourLifeTagsName = getResources().getStringArray(R.array.LiftYourLifeTagsName);
    }
    private void MenuItem(){
        Menu menu= navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.ItemEasyLearnSCE);
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
                new GuestNavigationView(LiftYourLife.this, item.getItemId());
                return false;
            }
        });
    }
    private void setTags(){
        for(int i=0; i<SIZE; i++)
            tagList.add(new Tag(LiftYourLifeTagsName[i], TagsPhoto[i]));
        ShowTags(tagList);
    }
    private void ShowTags(List<Tag> selects){
        LiftYourLifeAdapter liftYourLifeAdapter = new LiftYourLifeAdapter(this,selects);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(liftYourLifeAdapter);
    }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LiftYourLife.this);
        builder.setTitle(getResources().getString(R.string.Exit)).setMessage(getResources().getString(R.string.AreYouSureToExit)).setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).show();
    }
}