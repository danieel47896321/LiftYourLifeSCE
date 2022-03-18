package com.example.liftyourlife.Class;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.example.liftyourlife.Guest.LiftYourLife;
import com.example.liftyourlife.R;
import com.example.liftyourlife.User.ChangePassword;
import com.example.liftyourlife.User.Home;
import com.example.liftyourlife.User.MyWorkOut;
import com.example.liftyourlife.User.Profile;
import com.example.liftyourlife.User.Statistics;
import com.example.liftyourlife.User.WorkOutUpdate;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class UserNavigationView {
    private Intent intent;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public UserNavigationView(Context context, int id, User user) {
        if (id == R.id.ItemHome)
            StartActivity(context, Home.class, user);
        else if (id == R.id.ItemProfile)
            StartActivity(context, Profile.class, user);
        else if (id == R.id.ItemWorkOutUpdate)
            StartActivity(context, WorkOutUpdate.class, user);
        else if (id == R.id.ItemMyWorkOut)
            StartActivity(context, MyWorkOut.class, user);
        else if (id == R.id.ItemChangePassword)
            StartActivity(context, ChangePassword.class, user);
        else if (id == R.id.ItemStatistics)
            StartActivity(context, Statistics.class, user);
        else if (id == R.id.ItemSignOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(R.string.Logout)).setMessage(context.getResources().getString(R.string.AreYouSure)).setCancelable(true).setPositiveButton(context.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (firebaseAuth.getCurrentUser() != null)
                        firebaseAuth.signOut();
                    GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail().build();
                    GoogleSignInClient googleClient = GoogleSignIn.getClient(context, options);
                    googleClient.signOut();
                    context.startActivity(new Intent(context, LiftYourLife.class));
                    ((Activity) context).finish();
                }
            }).setNegativeButton(context.getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { }
            }).show();
        }
    }

    private void StartActivity(Context context, Class Destination, User user) {
        intent = new Intent(context, Destination);
        intent.putExtra("user", user);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
