package com.example.liftyourlife.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.liftyourlife.Class.Loading;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Class.UserLanguage;
import com.example.liftyourlife.Class.UserNavigationHeader;
import com.example.liftyourlife.Class.UserNavigationView;
import com.example.liftyourlife.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Profile extends AppCompatActivity {
    private TextView Title,TextViewSearchCity, TextViewSearchAge, TextViewSearchGender,TextViewSearch, TextViewSearchLanguage;
    private TextInputLayout TextInputLayoutFirstName, TextInputLayoutLastName ,TextInputLayoutEmail;
    private Button Confirm;
    private DrawerLayout drawerLayout;
    private ImageView BackIcon, MenuIcon,addImage;
    private Loading loading;
    private Intent intent;
    private UserLanguage userLanguage;
    private View UserProfileImage, UserImage;
    private NavigationView navigationView;
    private Dialog dialog;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView Cammera;
    private EditText EditTextSearch;
    private ListView ListViewSearch;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private User user, newUser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }
    void init(){
        setID();
        setLanguage();
        MenuItem();
        BackIcon();
        MenuIcon();
        NavigationView();
        Confirm();
        AddImage();
        ShowInfo();
    }
    public void setID(){
        intent = getIntent();
        UserProfileImage = findViewById(R.id.UserProfileImage);
        TextInputLayoutFirstName = findViewById(R.id.TextInputLayoutFirstName);
        TextInputLayoutLastName = findViewById(R.id.TextInputLayoutLastName);
        TextInputLayoutEmail = findViewById(R.id.TextInputLayoutEmail);
        TextViewSearchCity = findViewById(R.id.TextViewSearchCity);
        TextViewSearchAge = findViewById(R.id.TextViewSearchAge);
        TextViewSearchGender = findViewById(R.id.TextViewSearchGender);
        addImage = findViewById(R.id.addImage);
        Confirm = findViewById(R.id.confirm);
        navigationView = findViewById(R.id.navigationView);
        UserImage = navigationView.getHeaderView(0).findViewById(R.id.UserImage);
        MenuIcon = findViewById(R.id.MenuIcon);
        BackIcon = findViewById(R.id.BackIcon);
        Title = findViewById(R.id.Title);
        Title.setText(getResources().getString(R.string.Profile));
        user = (User)intent.getSerializableExtra("user");
        drawerLayout = findViewById(R.id.drawerLayout);
        TextViewSearchLanguage = findViewById(R.id.TextViewSearchLanguage);
        userLanguage = new UserLanguage(Profile.this, user);
        new UserNavigationHeader(user,Profile.this);
    }
    private void setLanguage(){
        TextViewSearchLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { userLanguage.setDialog(); }
        });
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
        TextInputLayoutEmail.getEditText().setText(user.getEmail());
        TextViewSearchCity.setText(user.getCity());
        TextViewSearchAge.setText(user.getAge());
        TextViewSearchGender.setText(user.getGender());
        CityPick();
        AgePick();
        GenderPick();
    }
    private void Confirm(){
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInput()){
                    newUser.setEmail(user.getEmail());
                    newUser.setUid(user.getUid());
                    newUser.setFirstName(TextInputLayoutFirstName.getEditText().getText().toString());
                    newUser.setLastName(TextInputLayoutLastName.getEditText().getText().toString());
                    newUser.setGender(TextViewSearchGender.getText().toString());
                    newUser.setAge(TextViewSearchAge.getText().toString());
                    newUser.setCity(TextViewSearchCity.getText().toString());
                    newUser.setFullName(newUser.getFirstName()+" "+newUser.getLastName());
                    /*
                    else {
                        newUser.setImage(user.getImage());
                        AlertDialog.Builder Builder;
                        Builder = new AlertDialog.Builder(Profile.this, R.style.AppCompatAlertDialogStyle);
                        Builder.setTitle(getResources().getString(R.string.Profile));
                        Builder.setMessage(getResources().getString(R.string.ProfileUpdated));
                        Builder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateData();
                            }
                        });
                        Builder.setCancelable(false);
                        Builder.create().show();
                    }*/
                }
            }
        });
    }
    private void updateData(){

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
        else if(TextViewSearchCity.getText().toString().equals("")){
            return false;
        }
        else if(TextViewSearchAge.getText().toString().equals("")){
            return false;
        }
        else if(TextViewSearchGender.getText().toString().equals("")){
            return false;
        }
        return true;
    }
    private void CityPick(){
        TextViewSearchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog(getResources().getStringArray(R.array.City),getResources().getString(R.string.SelectCity),TextViewSearchCity);
            }
        });
    }
    private void AgePick(){
        String age[] = new String[103];
        for(int i=0; i<103 ; i++)
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
    private void AddImage(){
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { chooseProfilePicture(); }
        });
    }
    private void chooseProfilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_picture,null);
        builder.setCancelable(false);
        builder.setView(dialogView);
        Cammera = dialogView.findViewById(R.id.Cammera);
        ImageView Gallery = dialogView.findViewById(R.id.Gallery);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        Cammera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                /*if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 2);
                }*/
                alertDialog.cancel();
            }
        });
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GalleryPicture();
                alertDialog.cancel();
            }
        });
    }
    private void GalleryPicture(){
       /* Intent photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photo.setType("image/*");
        startActivityForResult(photo, 1);*/
    }
    private void CammeraPicture(){
        /*Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager()) != null){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takePicture, 2);
        }*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    /*uri = data.getData();
                    Bitmap bitmap = null;
                    try { bitmap = MediaStore.Images.Media.getBitmap(Profile.this.getContentResolver(), uri);
                    } catch (IOException e) { e.printStackTrace(); }
                    UserProfileImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                    UserImage.setBackground(new BitmapDrawable(getResources(), bitmap));*/
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                   /* Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    UserProfileImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                    UserImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                    uri = Uri.parse(path);*/
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2);
            }
        }
    }
    private void UploadImage(){

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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Profile.this, R.layout.dropdwon_item, array);
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
    private void StartActivity(Class Destination){
        intent = new Intent(Profile.this, Destination);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        intent = new Intent(Profile.this, Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}