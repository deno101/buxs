package com.dnz.local.buxs.utils;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dnz.local.buxs.R;

import androidx.appcompat.app.AppCompatActivity;

public class MyAnimations {

    public static void showLoading(AppCompatActivity activity){
        RelativeLayout superContainer = activity.findViewById(R.id.info_status_container);
        RelativeLayout loadingDiv = activity.findViewById(R.id.info_showing_loading);

        loadingDiv.setAlpha(1f);

        superContainer.animate().alpha(1f).setDuration(200);
    }

    public static void dismissLoading(AppCompatActivity activity){
        RelativeLayout superContainer = activity.findViewById(R.id.info_status_container);
        RelativeLayout loadingDiv = activity.findViewById(R.id.info_showing_loading);

        loadingDiv.setAlpha(0f);

        superContainer.animate().alpha(0f).setDuration(200);
    }

    public static void showSuccess(AppCompatActivity activity){
        RelativeLayout superContainer = activity.findViewById(R.id.info_status_container);
        RelativeLayout successDiv = activity.findViewById(R.id.info_showing_success);

        successDiv.setAlpha(1f);

        superContainer.animate().alpha(1f).setDuration(200);
    }

    public static void dismissSuccess(AppCompatActivity activity){
        RelativeLayout superContainer = activity.findViewById(R.id.info_status_container);
        RelativeLayout successDiv = activity.findViewById(R.id.info_showing_success);

        successDiv.setAlpha(0f);

        superContainer.animate().alpha(0f).setDuration(500);
    }

    public static void showError(AppCompatActivity activity, String errorMessage){
        RelativeLayout superContainer = activity.findViewById(R.id.info_status_container);
        RelativeLayout errorDiv = activity.findViewById(R.id.info_showing_error);
        TextView errorTextView = activity.findViewById(R.id.info_error_text_view);

        errorTextView.setText(errorMessage);
        errorDiv.setAlpha(1f);

        superContainer.animate().alpha(1f).setDuration(200);
    }

    public static void dismissError(AppCompatActivity activity){
        RelativeLayout superContainer = activity.findViewById(R.id.info_status_container);
        RelativeLayout errorDiv = activity.findViewById(R.id.info_showing_error);

        errorDiv.setAlpha(0f);

        superContainer.animate().alpha(0f).setDuration(500);
    }
}
