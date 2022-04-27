package com.example.liftyourlife.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liftyourlife.Adapters.HomeAdapter;
import com.example.liftyourlife.Adapters.LiftYourLifeAdapter;
import com.example.liftyourlife.Class.Tag;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Class.UserNavigationHeader;
import com.example.liftyourlife.Class.UserNavigationView;
import com.example.liftyourlife.Guest.LiftYourLife;
import com.example.liftyourlife.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private NavigationView UserNavigationView;
    private ImageView BackIcon, MenuIcon;
    private DrawerLayout drawerLayout;
    private TextView Title;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private List<Tag> tagList;
    private RecyclerView recyclerView;
    private Intent intent;
    private int TagPhotos[] = {R.drawable.statistics, R.drawable.statistics, R.drawable.person, R.drawable.reset_password,R.drawable.logout};
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }
    private void init(){
        setID();
        setTags();
        MenuIcon();
        SignOutIcon();
        MenuView();
        MenuItem();

    }
    private void setID(){
        intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        UserNavigationView = findViewById(R.id.navigationView);
        BackIcon = findViewById(R.id.BackIcon);
        BackIcon.setImageResource(R.drawable.logout);
        MenuIcon = findViewById(R.id.MenuIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        Title.setText(R.string.Home);
        tagList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        new UserNavigationHeader(user,Home.this);
    }
    private void setTags(){
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.WorkOut));
        list.add(getResources().getString(R.string.Statistics));
        list.add(getResources().getString(R.string.Profile));
        list.add(getResources().getString(R.string.ChangePassword));
        list.add(getResources().getString(R.string.Logout));
        for(int i=0; i< list.size();i++)
            tagList.add(new Tag(list.get(i),TagPhotos[i]));
        HomeAdapter homeAdapter = new HomeAdapter(Home.this,tagList,user);
        recyclerView.setLayoutManager(new GridLayoutManager(Home.this,1));
        recyclerView.setAdapter(homeAdapter);
    }

    private void MenuIcon(){
        MenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });
    }
    private void SignOutIcon(){
        BackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed(); }
        });
    }
    private void MenuView(){
        UserNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                new UserNavigationView(Home.this, item.getItemId(), user);
                return false;
            }
        });
    }
    private void MenuItem(){
        Menu menu= UserNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.ItemHome);
        menuItem.setCheckable(false);
        menuItem.setChecked(true);
        menuItem.setEnabled(false);
    }
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle(getResources().getString(R.string.Logout)).setMessage(getResources().getString(R.string.AreYouSure)).setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(firebaseAuth.getCurrentUser() != null)
                            firebaseAuth.signOut();
                        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(Home.this.getString(R.string.default_web_client_id)).requestEmail().build();
                        GoogleSignInClient googleClient = GoogleSignIn.getClient(Home.this, options);
                        googleClient.signOut();
                        startActivity(new Intent(Home.this, LiftYourLife.class));
                        finish();
                    }
                }).setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).show();
    }
}