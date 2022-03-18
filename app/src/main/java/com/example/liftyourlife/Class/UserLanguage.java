package com.example.liftyourlife.Class;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;

import com.example.liftyourlife.R;

import java.util.Locale;

public class UserLanguage {
    private EditText EditTextSearch;
    private TextView TextViewSearch;
    private Dialog dialog;
    private ListView ListViewSearch;
    private Context context;
    private User user;
    public UserLanguage(Context context, User user) {
        this.context = context;
        this.user = user;
    }
    public void setDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_search_spinner);
        dialog.getWindow().setLayout(800, 770);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditTextSearch = dialog.findViewById(R.id.EditTextSearch);
        ListViewSearch = dialog.findViewById(R.id.ListViewSearch);
        TextViewSearch = dialog.findViewById(R.id.TextViewSearch);
        TextViewSearch.setText(context.getResources().getString(R.string.SelectLanguage));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdwon_item, context.getResources().getStringArray(R.array.Lagnuage));
        ListViewSearch.setAdapter(adapter);
        EditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { adapter.getFilter().filter(charSequence); }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        ListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                Locale locale;
                if (adapterView.getItemAtPosition(i).toString().equals("English") || adapterView.getItemAtPosition(i).toString().equals("אנגלית")) {
                    if (!context.getResources().getConfiguration().locale.getDisplayLanguage().equals("English") && !context.getResources().getConfiguration().locale.getDisplayLanguage().equals("אנגלית")) {
                        locale = new Locale("en");
                        Locale.setDefault(locale);
                        Resources resources = context.getResources();
                        Configuration configuration = resources.getConfiguration();
                        configuration.setLocale(locale);
                        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
                        StartActivity(locale);
                    }
                } else if (adapterView.getItemAtPosition(i).toString().equals("Hebrew") || adapterView.getItemAtPosition(i).toString().equals("עברית")) {
                    if (!context.getResources().getConfiguration().locale.getDisplayLanguage().equals("Hebrew") && !context.getResources().getConfiguration().locale.getDisplayLanguage().equals("עברית")) {
                        locale = new Locale("he");
                        Locale.setDefault(locale);
                        Resources resources = context.getResources();
                        Configuration configuration = resources.getConfiguration();
                        configuration.setLocale(locale);
                        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
                        StartActivity(locale);
                    }
                }
            }
        });
    }
    public void StartActivity(Locale locale) {
        Intent intent = new Intent(context, context.getClass());
        intent.putExtra("user", user);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}