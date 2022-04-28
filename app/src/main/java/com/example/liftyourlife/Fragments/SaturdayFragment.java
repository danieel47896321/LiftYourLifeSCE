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
import com.example.liftyourlife.Class.User;
import com.example.liftyourlife.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SaturdayFragment extends Fragment {
    private RecyclerView recyclerView;
    private Context context;
    private FloatingActionButton floatingActionButtonOpen;
    private ExtendedFloatingActionButton floatingActionButtonAdd, floatingActionButtonRemove;
    private Animation rotateOpen, rotateClose, toBottom, fromBottom;
    private Boolean isOpen = false;
    private User user;
    private Dialog dialog;
    private ListView ListViewSearch;
    private EditText EditTextSearch;
    private TextView TextViewSearch;
    private Button ButtonAdd, ButtonRemove, ButtonCancel;
    private TextInputLayout TextInputLayoutPlan;
    private ArrayList<Plan> plans;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saturday,container,false);
        context = view.getContext();
        plans = new ArrayList<>();
        floatingActionButtonOpen = view.findViewById(R.id.floatingActionButtonOpen);
        floatingActionButtonAdd = view.findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonRemove = view.findViewById(R.id.floatingActionButtonRemove);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        setExercises();
        setAddAndRemove();
        return view;
    }
    private void setExercises(){
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(getContext(), plans, user);
        recyclerView.setAdapter(workoutAdapter);
    }
    private void setAddAndRemove(){
        floatingActionButtonOpen.setVisibility(View.VISIBLE);
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
                    String currentDateTime = new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date());
                    plans.add(new Plan(TextInputLayoutPlan.getEditText().getText().toString(), currentDateTime));
                    setExercises();
                }
            }
        });
    }
    private void RemovePlanDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_remove_plan,null);
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
                    for(int i=0; i<plans.size();i++)
                        if(TextInputLayoutPlan.getEditText().getText().toString().equals(plans.get(i).getPlanName() + " - " + plans.get(i).getDate()))
                            plans.remove(i);
                    setExercises();
                }
            }
        });
    }
    private void ExercisePick(){
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

    public void setUser(User user) { this.user = user; }
}