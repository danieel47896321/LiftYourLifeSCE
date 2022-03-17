package com.example.liftyourlife.Class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.example.liftyourlife.Guest.About;
import com.example.liftyourlife.Guest.Contact;
import com.example.liftyourlife.Guest.CreateAccount;
import com.example.liftyourlife.Guest.LiftYourLife;
import com.example.liftyourlife.Guest.ResetPassword;
import com.example.liftyourlife.Guest.SignIn;
import com.example.liftyourlife.R;

public class GuestMenuView {
    private Intent intent;
    public GuestMenuView(Context context, int id){
        if (id == R.id.ItemEasyLearnSCE)
            intent = new Intent(context, LiftYourLife.class);
        else if(id == R.id.ItemSignIn)
            intent = new Intent(context, SignIn.class);
        else if(id == R.id.ItemCreateAccount)
            intent = new Intent(context, CreateAccount.class);
        else if(id == R.id.ItemResetPassword)
            intent = new Intent(context, ResetPassword.class);
        else if(id == R.id.ItemAbout)
            intent = new Intent(context, About.class);
        else if(id == R.id.ItemContact)
            intent = new Intent(context, Contact.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
