package com.example.liftyourlife.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liftyourlife.Adapters.WorkoutAdapter;
import com.example.liftyourlife.Class.Plan;
import com.example.liftyourlife.Class.PopUpMSG;
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.R;
import com.example.liftyourlife.Server.RetrofitInterface;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class WorkoutFragment extends Fragment {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    private RecyclerView recyclerView;
    private Context context;
    private FloatingActionButton floatingActionButtonOpen;
    private ExtendedFloatingActionButton floatingActionButtonAdd, floatingActionButtonRemove;
    private Animation rotateOpen, rotateClose, toBottom, fromBottom;
    private Boolean isOpen = false;
    private User user = null;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Dialog dialog;
    private ListView ListViewSearch;
    private EditText EditTextSearch;
    private TextView TextViewSearch;
    private Button ButtonAdd, ButtonRemove, ButtonCancel;
    private TextInputLayout TextInputLayoutPlan;
    private ArrayList<Plan> plans;
    private String DAY = "SUNDAY";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout,container,false);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        context = view.getContext();
        plans = new ArrayList<>();
        floatingActionButtonOpen = view.findViewById(R.id.floatingActionButtonOpen);
        floatingActionButtonAdd = view.findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonRemove = view.findViewById(R.id.floatingActionButtonRemove);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        getUser();
        setAddAndRemove();
        return view;
    }
    private void getUser(){
        if(firebaseAuth.getCurrentUser() != null) {
            if(user == null) {
                HashMap<String, String> map = new HashMap<>();
                map.put("uid", firebaseAuth.getCurrentUser().getUid());
                Call<User> call = retrofitInterface.getUser(map);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            user = response.body();
                            setPlans();
                        } else if (response.code() == 404) {
                            new PopUpMSG(context, getResources().getString(R.string.WorkOut), getResources().getString(R.string.Error));
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) { }
                });
            }
        }
    }
    private void setPlans(){
        HashMap<String, String> map = new HashMap<>();
        map.put("Uid", user.getUid());
        map.put("Day", DAY);
        Call<List<Plan>> call = retrofitInterface.setPlans(map);
        call.enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                if (response.code() == 200) {
                    plans.clear();
                    for(Plan plan : response.body())
                        plans.add(plan);
                    WorkoutAdapter workoutAdapter = new WorkoutAdapter(getContext(), plans, user);
                    recyclerView.setAdapter(workoutAdapter);
                } else if (response.code() == 404) {
                    new PopUpMSG(context, getResources().getString(R.string.WorkOut), getResources().getString(R.string.Error));
                }
            }
            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) { }
        });
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
                        public void onClick(View v) {
                            AddPlanDialog();
                        }
                    });
                    floatingActionButtonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RemovePlanDialog();
                        }
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
    private void AddPlanDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_plan,null);
        builder.setCancelable(false);
        builder.setView(dialogView);
        TextInputLayoutPlan = dialogView.findViewById(R.id.TextInputLayoutPlan);
        ButtonAdd = dialogView.findViewById(R.id.ButtonAdd);
        ButtonCancel = dialogView.findViewById(R.id.ButtonCancel);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { alertDialog.cancel(); }
        });
        ButtonAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(TextInputLayoutPlan.getEditText().getText().toString().equals(""))
                    TextInputLayoutPlan.setHelperText(getResources().getString(R.string.Required));
                else
                    TextInputLayoutPlan.setHelperText("");
                if(!(TextInputLayoutPlan.getEditText().getText().toString().equals(""))){
                    alertDialog.cancel();
                    String currentDateTime = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date());
                    Plan plan = new Plan(TextInputLayoutPlan.getEditText().getText().toString(), currentDateTime,DAY,user.getUid());
                    HashMap<String,String> map = new HashMap<>();
                    map.put("Uid",plan.getUid());
                    map.put("PlanName",plan.getPlanName());
                    map.put("Date",plan.getDate());
                    map.put("Day",plan.getDay());
                    Call<Void> call = retrofitInterface.AddPlan(map);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                new PopUpMSG(context, getResources().getString(R.string.AddPlan), getResources().getString(R.string.PlanSuccessfullyAdded));
                                setPlans();
                            } else if (response.code() == 404) {
                                new PopUpMSG(context, getResources().getString(R.string.AddPlan), getResources().getString(R.string.Error));
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) { }
                    });
                }
            }
        });
    }
    private void RemovePlanDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        PlanPick();
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
                    for(int i=0; i<plans.size();i++)
                        if(TextInputLayoutPlan.getEditText().getText().toString().equals(plans.get(i).getPlanName() + " - " + plans.get(i).getDate())) {
                            HashMap<String,String> map = new HashMap<>();
                            map.put("Uid",plans.get(i).getUid());
                            map.put("PlanName",plans.get(i).getPlanName());
                            map.put("Date",plans.get(i).getDate());
                            map.put("Day",plans.get(i).getDay());
                            Call<Void> call = retrofitInterface.RemovePlan(map);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.code() == 200) {
                                        new PopUpMSG(context, getResources().getString(R.string.RemovePlan), getResources().getString(R.string.PlanSuccessfullyRemoved));
                                        setPlans();
                                    } else if (response.code() == 404) {
                                        new PopUpMSG(context, getResources().getString(R.string.AddPlan), getResources().getString(R.string.Error));
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
    private void PlanPick(){
        TextInputLayoutPlan.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Remove_plans[] = new String[plans.size()];
                for(int i=0; i<plans.size();i++)
                    Remove_plans[i] = plans.get(i).getPlanName() + " - " + plans.get(i).getDate();
                setDialog(Remove_plans,getResources().getString(R.string.RemovePlan), TextInputLayoutPlan.getEditText());
            }
        });
    }
    private void setDialog(String[] array, String title, TextView textViewPick){
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_search_spinner);
        dialog.getWindow().setLayout(1000,950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditTextSearch = dialog.findViewById(R.id.EditTextSearch);
        ListViewSearch = dialog.findViewById(R.id.ListViewSearch);
        TextViewSearch = dialog.findViewById(R.id.TextViewSearch);
        TextViewSearch.setText(title);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, array);
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
    public void setDay(String day) {
        DAY = day;
    }
}