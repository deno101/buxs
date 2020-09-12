package com.dnz.local.buxs.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.view.View;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class Permissions {

    private static final String TAG = "Permissions";

    public static void getPermissions(Activity context, String s, int permissionCode) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(context, s)) {
            // TODO: 5/21/20 Show dialog
            Log.d(TAG, "getPermissions: permission denied");
        }
        {
            ActivityCompat.requestPermissions(context, new String[]{s}, permissionCode);
        }


    }

    public static boolean checkPermission(String s, Activity context) {
        return ContextCompat.checkSelfPermission(context, s) == PackageManager.PERMISSION_GRANTED;
    }

    public static void getStoragePermissions(Activity context) {

        getPermissions(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Const.WRITE_EXTERNAL_PERMISSION);
    }

    public static boolean checkStoragePermission(Activity context) {
        return checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context);
    }
}
