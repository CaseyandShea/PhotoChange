package com.example.administrator.kaseyutils.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.administrator.kaseyutils.onRequestPermissionsListener;

/**
 * Created by Kasey on 2017/4/26/026;
 * 作用：获取权限
 * 微信：mkx0425
 */

public class KSPermissionsUtils {
    //请求Camera权限
    public static void requestCamera(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }


    public static void requestReadExternalStorage(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }
}
