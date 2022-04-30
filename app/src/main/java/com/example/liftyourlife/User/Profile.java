package com.example.liftyourlife.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.liftyourlife.Class.Loading;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Class.UserNavigationHeader;
import com.example.liftyourlife.Class.UserNavigationView;
import com.example.liftyourlife.Guest.CreateAccount;
import com.example.liftyourlife.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Profile extends AppCompatActivity {
    private TextView Title,TextViewSearch;
    private TextInputLayout TextInputLayoutFirstName, TextInputLayoutLastName, TextInputLayoutHeight, TextInputLayoutWeight, TextInputLayoutBirthDay, TextInputLayoutGender;;
    private Button Confirm;
    private DrawerLayout drawerLayout;
    private ImageView BackIcon, MenuIcon,addImage;
    private Loading loading;
    private Calendar calendar = Calendar.getInstance();
    private int Year = calendar.get(Calendar.YEAR), Month = calendar.get(Calendar.MONTH), Day = calendar.get(Calendar.DAY_OF_MONTH), UserYear, UserMonth, UserDay;
    private Intent intent;
    private View UserProfileImage, UserImage;
    private NavigationView navigationView;
    private Dialog dialog;
    private Uri uri = null;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView Camera;
    private EditText EditTextSearch;
    private ListView ListViewSearch;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private User user, newUser = new User();
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
        AddImage();
        ShowInfo();
    }
    public void setID(){
        intent = getIntent();
        UserProfileImage = findViewById(R.id.UserProfileImage);
        TextInputLayoutFirstName = findViewById(R.id.TextInputLayoutFirstName);
        TextInputLayoutLastName = findViewById(R.id.TextInputLayoutLastName);
        TextInputLayoutHeight = findViewById(R.id.TextInputLayoutHeight);
        TextInputLayoutWeight = findViewById(R.id.TextInputLayoutWeight);
        TextInputLayoutBirthDay = findViewById(R.id.TextInputLayoutBirthDay);
        TextInputLayoutGender = findViewById(R.id.TextInputLayoutGender);
        addImage = findViewById(R.id.addImage);
        Confirm = findViewById(R.id.confirm);
        navigationView = findViewById(R.id.navigationView);
        UserImage = navigationView.getHeaderView(0).findViewById(R.id.UserImage);
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
        TextInputLayoutHeight.getEditText().setText(user.getHeight() + " " + getResources().getString(R.string.Meters) );
        TextInputLayoutWeight.getEditText().setText(user.getWeight() + " " + getResources().getString(R.string.Kg) );
        if(!user.getImage().equals("Image")) {
            Glide.with(Profile.this).asBitmap().load(user.getImage()).into(new CustomTarget<Bitmap>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) { UserProfileImage.setBackground(new BitmapDrawable(getResources(), resource)); }
                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) { }
            });
        }
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
                    for(int i=0; i < 3 ; i++)
                        for(int j=0; j<100; j++)
                            if(TextInputLayoutHeight.getEditText().getText().toString().equals( i+ "."+ j + " " + getResources().getString(R.string.Meters)))
                                Height = i+ "."+ j ;
                    user.setHeight(Height);
                    for(int i=0; i < 261 ; i++)
                        if(TextInputLayoutWeight.getEditText().getText().toString().equals( (i+20)+" "+getResources().getString(R.string.Kg)))
                            Weight = (i+20)+"";
                    user.setWeight(Weight);
                    user.setFullName(user.getFirstName() + " " + user.getLastName());
                    if(uri != null) {
                        loading = new Loading(Profile.this);
                        UploadImage();
                    }
                    else
                        SuccessfullyUpdatedMSG();
                }
            }
        });
    }
    private void SuccessfullyUpdatedMSG(){
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
    }
    private void updateData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://liftyourlife-9d039-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(user.getUid());
        databaseReference.setValue(user);
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
        String height[] = new String[300];
        int index =0;
        for(int i=0; i < 3 ; i++)
            for(int j=0; j<100; j++)
                height[index++] = "" + i+ "."+ j + " " + getResources().getString(R.string.Meters);
        TextInputLayoutHeight.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog(height ,getResources().getString(R.string.SelectHeight),TextInputLayoutHeight.getEditText());
            }
        });
    }
    private void WeightPick(){
        String weight[] = new String[261];
        for(int i=0; i < weight.length ; i++)
            weight[i] = ""+(i+20)+" "+getResources().getString(R.string.Kg);
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
        Camera = dialogView.findViewById(R.id.Cammera);
        ImageView Gallery = dialogView.findViewById(R.id.Gallery);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        Camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 2);
                }
                alertDialog.cancel();
            }
        });
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryPicture();
                alertDialog.cancel();
            }
        });
    }
    private void GalleryPicture(){
        Intent photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photo.setType("image/*");
        startActivityForResult(photo, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    uri = data.getData();
                    Bitmap bitmap = null;
                    try { bitmap = MediaStore.Images.Media.getBitmap(Profile.this.getContentResolver(), uri);
                    } catch (IOException e) { e.printStackTrace(); }
                    UserProfileImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                    UserImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    UserProfileImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                    UserImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                    uri = Uri.parse(path);
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
        StorageReference reference = storageReference.child(user.getUid()+".jpg");
        reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        loading.stop();
                        user.setImage(uri.toString());
                        SuccessfullyUpdatedMSG();
                    }
                });
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