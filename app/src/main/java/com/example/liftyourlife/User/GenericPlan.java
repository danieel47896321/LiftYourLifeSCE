package com.example.liftyourlife.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liftyourlife.Adapters.GenericPlanAdapter;
import com.example.liftyourlife.Adapters.WorkoutAdapter;
import com.example.liftyourlife.Class.Exercise;
import com.example.liftyourlife.Class.Plan;
import com.example.liftyourlife.Class.PopUpMSG;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.Class.UserNavigationHeader;
import com.example.liftyourlife.Class.UserNavigationView;
import com.example.liftyourlife.R;
import com.example.liftyourlife.Server.RetrofitInterface;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenericPlan extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    private RecyclerView recyclerView;
    private NavigationView UserNavigationView;
    private ImageView BackIcon, MenuIcon;
    private DrawerLayout drawerLayout;
    private TextView Title;
    private FloatingActionButton floatingActionButtonOpen;
    private ExtendedFloatingActionButton floatingActionButtonAdd, floatingActionButtonRemove;
    private Animation rotateOpen, rotateClose, toBottom, fromBottom;
    private Boolean isOpen = false;
    private Intent intent;
    private Dialog dialog;
    private ListView ListViewSearch;
    private EditText EditTextSearch;
    private TextView TextViewSearch;
    private Button ButtonAdd, ButtonRemove, ButtonCancel;
    private TextInputLayout TextInputLayoutExerciseName, TextInputLayoutDescription, TextInputLayoutTypeOfMuscle, TextInputLayoutReps,TextInputLayoutPlan;
    private Plan plan;
    private Context context;
    private User user = new User();
    private ArrayList<Exercise> exercises;
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
        setAddAndRemove();
        setExercises();
    }
    private void setID(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        context = this;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        exercises = new ArrayList<>();
        intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        plan = (Plan) intent.getSerializableExtra("plan");
        UserNavigationView = findViewById(R.id.navigationView);
        BackIcon = findViewById(R.id.BackIcon);
        MenuIcon = findViewById(R.id.MenuIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        Title = findViewById(R.id.Title);
        Title.setText(plan.getPlanName());
        floatingActionButtonOpen = findViewById(R.id.floatingActionButtonOpen);
        floatingActionButtonAdd = findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonRemove = findViewById(R.id.floatingActionButtonRemove);
        new UserNavigationHeader(user,GenericPlan.this);
    }
    private void setAddAndRemove(){
        floatingActionButtonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = !isOpen;
                rotateOpen = AnimationUtils.loadAnimation(context,R.anim.rotate_open);
                rotateClose = AnimationUtils.loadAnimation(context,R.anim.rotate_close);
                fromBottom = AnimationUtils.loadAnimation(context,R.anim.from_bottom);
                toBottom = AnimationUtils.loadAnimation(context,R.anim.to_bottom);
                if (isOpen) {
                    floatingActionButtonAdd.setVisibility(View.VISIBLE);
                    floatingActionButtonRemove.setVisibility(View.VISIBLE);
                    floatingActionButtonAdd.setAnimation(fromBottom);
                    floatingActionButtonRemove.setAnimation(fromBottom);
                    floatingActionButtonOpen.setAnimation(rotateOpen);
                    floatingActionButtonAdd.setClickable(true);
                    floatingActionButtonRemove.setClickable(true);
                    floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { AddExerciseDialog(); }
                    });
                    floatingActionButtonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { RemoveExerciseDialog(); }
                    });
                } else {
                    floatingActionButtonAdd.setVisibility(View.INVISIBLE);
                    floatingActionButtonRemove.setVisibility(View.INVISIBLE);
                    floatingActionButtonAdd.setAnimation(toBottom);
                    floatingActionButtonRemove.setAnimation(toBottom);
                    floatingActionButtonOpen.setAnimation(rotateClose);
                    floatingActionButtonAdd.setClickable(false);
                    floatingActionButtonRemove.setClickable(false);
                }
            }
        });
    }
    private void setExercises(){
        HashMap<String, String> map = new HashMap<>();
        map.put("Uid",plan.getUid());
        map.put("PlanName",plan.getPlanName());
        map.put("Date",plan.getDate());
        map.put("Day",plan.getDay());
        Call<List<Exercise>> call = retrofitInterface.setExercises(map);
        call.enqueue(new Callback<List<Exercise>>() {
            @Override
            public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                if (response.code() == 200) {
                    exercises.clear();
                    for(Exercise exercise : response.body())
                        exercises.add(exercise);
                    GenericPlanAdapter genericPlan = new GenericPlanAdapter(context, exercises, user);
                    recyclerView.setAdapter(genericPlan);
                } else if (response.code() == 404) {
                    new PopUpMSG(context, plan.getPlanName(), getResources().getString(R.string.Error));
                }
            }
            @Override
            public void onFailure(Call<List<Exercise>> call, Throwable t) { }
        });
    }
    private void AddExerciseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_exercise,null);
        builder.setCancelable(false);
        builder.setView(dialogView);
        TextInputLayoutExerciseName = dialogView.findViewById(R.id.TextInputLayoutExerciseName);
        TextInputLayoutDescription = dialogView.findViewById(R.id.TextInputLayoutDescription);
        TextInputLayoutTypeOfMuscle = dialogView.findViewById(R.id.TextInputLayoutTypeOfMuscle);
        TextInputLayoutReps = dialogView.findViewById(R.id.TextInputLayoutReps);
        ButtonAdd = dialogView.findViewById(R.id.ButtonAdd);
        ButtonCancel = dialogView.findViewById(R.id.ButtonCancel);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MuscleType();
        ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { alertDialog.cancel(); }
        });
        ButtonAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(TextInputLayoutExerciseName.getEditText().getText().toString().equals(""))
                    TextInputLayoutExerciseName.setHelperText(getResources().getString(R.string.Required));
                else
                    TextInputLayoutExerciseName.setHelperText("");
                if(TextInputLayoutTypeOfMuscle.getEditText().getText().toString().equals(""))
                    TextInputLayoutTypeOfMuscle.setHelperText(getResources().getString(R.string.Required));
                else
                    TextInputLayoutTypeOfMuscle.setHelperText("");
                if(TextInputLayoutReps.getEditText().getText().toString().equals(""))
                    TextInputLayoutReps.setHelperText(getResources().getString(R.string.Required));
                else
                    TextInputLayoutReps.setHelperText("");
                if(!(TextInputLayoutExerciseName.getEditText().getText().toString().equals("")) && !(TextInputLayoutTypeOfMuscle.getEditText().getText().toString().equals("")) && !(TextInputLayoutReps.getEditText().getText().toString().equals(""))){
                    alertDialog.cancel();
                    Exercise exercise = new Exercise(plan.getPlanName(),plan.getDate(),plan.getDay(),plan.getUid(),TextInputLayoutExerciseName.getEditText().getText().toString(),TextInputLayoutDescription.getEditText().getText().toString(),TextInputLayoutTypeOfMuscle.getEditText().getText().toString(),"",TextInputLayoutReps.getEditText().getText().toString());
                    HashMap<String,String> map = new HashMap<>();
                    map.put("Uid",exercise.getUid());
                    map.put("PlanName",exercise.getPlanName());
                    map.put("Date",exercise.getDate());
                    map.put("Day",exercise.getDay());
                    map.put("Exercise",exercise.getExercise());
                    map.put("Description",exercise.getDescription());
                    map.put("MuscleType",exercise.getMuscleType());
                    map.put("Sets",exercise.getSets());
                    map.put("Image",exercise.getImage());
                    Call<Void> call = retrofitInterface.AddExercise(map);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                new PopUpMSG(context, getResources().getString(R.string.AddExercise), getResources().getString(R.string.ExerciseSuccessfullyAdded));
                                setExercises();
                                for(int i=0; i<Integer.valueOf(exercise.getSets());i++) {
                                    HashMap<String,String> map = new HashMap<>();
                                    map.put("Uid",exercise.getUid());
                                    map.put("PlanName",exercise.getPlanName());
                                    map.put("Date",exercise.getDate());
                                    map.put("Day",exercise.getDay());
                                    map.put("Exercise",exercise.getExercise());
                                    map.put("SetNumber",i + "");
                                    map.put("Weight",10 + "");
                                    map.put("Reps",8 + "");
                                    map.put("Finish","false");
                                    Call<Void> call2 = retrofitInterface.AddSet(map);
                                    call2.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.code() == 200) {
                                            } else if (response.code() == 400) { }
                                        }
                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) { }
                                    });
                                }

                            } else if (response.code() == 400) {
                                new PopUpMSG(context, getResources().getString(R.string.AddExercise), getResources().getString(R.string.ExerciseNameExists));
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) { }
                    });
                }
            }
        });
    }
    private void MuscleType(){
        String types[] = getResources().getStringArray(R.array.MuscleType);
        TextInputLayoutTypeOfMuscle.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { setDialog(types ,getResources().getString(R.string.TypeOfMuscle),TextInputLayoutTypeOfMuscle.getEditText()); }
        });
    }
    private void RemoveExerciseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_remove,null);
        builder.setCancelable(false);
        builder.setView(dialogView);
        TextInputLayoutPlan = dialogView.findViewById(R.id.TextInputLayoutPlan);
        ButtonRemove = dialogView.findViewById(R.id.ButtonRemove);
        ButtonCancel = dialogView.findViewById(R.id.ButtonCancel);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ExercisePick();
        ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { alertDialog.cancel(); }
        });
        ButtonRemove.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(TextInputLayoutPlan.getEditText().getText().toString().equals(""))
                    TextInputLayoutPlan.setHelperText(getResources().getString(R.string.Required));
                else
                    TextInputLayoutPlan.setHelperText("");
                if(!TextInputLayoutPlan.getEditText().getText().toString().equals("")) {
                    alertDialog.cancel();
                    for(int i=0; i<exercises.size();i++)
                        if(TextInputLayoutPlan.getEditText().getText().toString().equals(exercises.get(i).getExercise())) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("Uid", exercises.get(i).getUid());
                            map.put("PlanName", exercises.get(i).getPlanName());
                            map.put("Date", exercises.get(i).getDate());
                            map.put("Day", exercises.get(i).getDay());
                            map.put("Exercise", exercises.get(i).getExercise());
                            Call<Void> call = retrofitInterface.RemoveExercise(map);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.code() == 200) {
                                        new PopUpMSG(context, getResources().getString(R.string.RemoveExercise), getResources().getString(R.string.ExerciseSuccessfullyRemoved));
                                        setExercises();
                                    } else if (response.code() == 400) {
                                        new PopUpMSG(context, getResources().getString(R.string.RemoveExercise), getResources().getString(R.string.Error));
                                    }
                                }
                                @Override
                                public void onFailure(Call<Void> call, Throwable t) { }
                            });
                        }
                }
            }
        });
    }
    private void ExercisePick(){
        TextInputLayoutPlan.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Remove_plans[] = new String[exercises.size()];
                for(int i=0; i<exercises.size();i++)
                    Remove_plans[i] = exercises.get(i).getExercise();
                setDialog(Remove_plans,getResources().getString(R.string.RemoveExercise), TextInputLayoutPlan.getEditText());
            }
        });
    }
    private void setDialog(String[] array, String title, TextView textViewPick){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_search_spinner);
        dialog.getWindow().setLayout(1000,950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditTextSearch = dialog.findViewById(R.id.EditTextSearch);
        ListViewSearch = dialog.findViewById(R.id.ListViewSearch);
        TextViewSearch = dialog.findViewById(R.id.TextViewSearch);
        TextViewSearch.setText(title);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_item, array);
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
        MenuItem menuItem = menu.findItem(R.id.ItemPlan);
        menuItem.setTitle(plan.getPlanName());
        menuItem.setVisible(true);
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