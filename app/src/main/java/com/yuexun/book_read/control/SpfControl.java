package com.yuexun.book_read.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;

import java.util.HashSet;
import java.util.Set;

import static com.yuexun.book_read.control.DataConstants.BOOK_BG_DEFAULT;
import static com.yuexun.book_read.control.DataConstants.BOOK_BG_KEY;
import static com.yuexun.book_read.control.DataConstants.FONTTYPE_DEFAULT;
import static com.yuexun.book_read.control.DataConstants.FONTTYPE_QIHEI;
import static com.yuexun.book_read.control.DataConstants.FONT_SIZE_KEY;
import static com.yuexun.book_read.control.DataConstants.FONT_TYPE_KEY;
import static com.yuexun.book_read.control.DataConstants.LIGHT_KEY;
import static com.yuexun.book_read.control.DataConstants.NIGHT_KEY;
import static com.yuexun.book_read.control.DataConstants.PAGE_MODE_KEY;
import static com.yuexun.book_read.control.DataConstants.PAGE_MODE_SIMULATION;
import static com.yuexun.book_read.control.DataConstants.SYSTEM_LIGHT_KEY;

/**
 * Created by yuexun on 2018/6/7.
 */

public class SpfControl {

    //字体
    private Typeface typeface;
    //字体大小
    private float mFontSize = 0;
    //亮度值
    private float light = 0;

    private static SpfControl spfControl;
    private SharedPreferences sharedPreferences;

    private SpfControl() {
    }

    public static SpfControl getInstance() {
        if (spfControl == null)
            spfControl = new SpfControl();
        return spfControl;
    }

    public void init() {
        sharedPreferences = MyApplication.getContext().getSharedPreferences("BOOK", Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public Set<String> getStringSet(String key) {
        return sharedPreferences.getStringSet(key, new HashSet<String>());
    }

    public void putStringSet(String key, Set<String> strings) {
        sharedPreferences.edit().putStringSet(key, strings).apply();
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public int getPageMode(){
        return sharedPreferences.getInt(PAGE_MODE_KEY,PAGE_MODE_SIMULATION);
    }

    public void setPageMode(int pageMode){
        sharedPreferences.edit().putInt(PAGE_MODE_KEY,pageMode).commit();
    }

    public int getBookBgType(){
        return sharedPreferences.getInt(BOOK_BG_KEY,BOOK_BG_DEFAULT);
    }

    public void setBookBg(int type){
        sharedPreferences.edit().putInt(BOOK_BG_KEY,type).commit();
    }

    public Typeface getTypeface(){
        if (typeface == null) {
            String typePath = sharedPreferences.getString(FONT_TYPE_KEY,FONTTYPE_QIHEI);
            typeface = getTypeface(typePath);
        }
        return typeface;
    }

    public String getTypefacePath(){
        String path = sharedPreferences.getString(FONT_TYPE_KEY,FONTTYPE_QIHEI);
        return path;
    }

    public Typeface getTypeface(String typeFacePath){
        Typeface mTypeface;
        if (typeFacePath.equals(FONTTYPE_DEFAULT)){
            mTypeface = Typeface.DEFAULT;
        }else{
            mTypeface = Typeface.createFromAsset(MyApplication.getContext().getAssets(),typeFacePath);
        }
        return mTypeface;
    }

    public void setTypeface(String typefacePath){
        typeface = getTypeface(typefacePath);
        sharedPreferences.edit().putString(FONT_TYPE_KEY,typefacePath).apply();
    }

    public float getFontSize(){
        if (mFontSize == 0){
            mFontSize = sharedPreferences.getFloat(FONT_SIZE_KEY, MyApplication.getContext().getResources().getDimension(R.dimen.reading_default_text_size));
        }
        return mFontSize;
    }

    public void setFontSize(float fontSize){
        mFontSize = fontSize;
        sharedPreferences.edit().putFloat(FONT_SIZE_KEY,fontSize).apply();
    }

    /**
     * 获取夜间还是白天阅读模式,true为夜晚，false为白天
     */
    public boolean getDayOrNight() {
        return sharedPreferences.getBoolean(NIGHT_KEY, false);
    }

    public void setDayOrNight(boolean isNight){
        sharedPreferences.edit().putBoolean(NIGHT_KEY,isNight).apply();
    }

    public Boolean isSystemLight(){
        return sharedPreferences.getBoolean(SYSTEM_LIGHT_KEY,true);
    }

    public void setSystemLight(Boolean isSystemLight){
        sharedPreferences.edit().putBoolean(SYSTEM_LIGHT_KEY,isSystemLight).apply();
    }

    public float getLight(){
        return sharedPreferences.getFloat(LIGHT_KEY,0.1f);
    }
    /**
     * 记录配置文件中亮度值
     */
    public void setLight(float light) {
        this.light = light;
        sharedPreferences.edit().putFloat(LIGHT_KEY,light).apply();
    }

}
