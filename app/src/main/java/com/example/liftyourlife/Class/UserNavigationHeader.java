package com.example.liftyourlife.Class;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.liftyourlife.R;
import com.google.android.material.navigation.NavigationView;

public class UserNavigationHeader {
    public UserNavigationHeader(User user, Context context){
        NavigationView UserNavigationView = ((Activity) context).findViewById(R.id.navigationView);
        View UserImage = UserNavigationView.getHeaderView(0).findViewById(R.id.UserImage);
        TextView UserFullName = UserNavigationView.getHeaderView(0).findViewById(R.id.user_fullname);
        TextView UserWeight = UserNavigationView.getHeaderView(0).findViewById(R.id.user_weight);
        TextView UserHeight = UserNavigationView.getHeaderView(0).findViewById(R.id.user_height);
        TextView UserEmail = UserNavigationView.getHeaderView(0).findViewById(R.id.user_email);
        UserFullName.setText(user.getFirstName()+" "+user.getLastName());
        UserHeight.setText(context.getResources().getString(R.string.Height)+": "+user.getHeight() +" "+context.getResources().getString(R.string.Meters));
        UserWeight.setText(context.getResources().getString(R.string.Weight)+": "+user.getWeight() +" "+context.getResources().getString(R.string.Kg));
        UserEmail.setText(user.getEmail());
        if(!user.getImage().equals("Image")){
            Glide.with(context).asBitmap().load(user.getImage()).into(new CustomTarget<Bitmap>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) { UserImage.setBackground(new BitmapDrawable(context.getResources(), resource)); }
                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) { }
            });
        }
    }
}
