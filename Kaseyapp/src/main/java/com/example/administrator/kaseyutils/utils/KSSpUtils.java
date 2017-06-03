package com.example.administrator.kaseyutils.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kasey on 2017/4/27/027;
 * 作用：sp存储的工具类
 * 微信：mkx0425
 */

public class KSSpUtils {


    /**
     * 存入自定义的标识的数据 可以近似的看作网络下载数据的缓存
     * 单条方式存入
     *
     * @param context 使用的上下文
     * @param tag     存入内容的标记，约定俗成的tag用当前的类名命名来区分不同的sp
     * @param content 存入的内
     */
    public static void putContent(Context context, String tag, String content) {
        putString(context, tag, content);
    }


    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }



}
