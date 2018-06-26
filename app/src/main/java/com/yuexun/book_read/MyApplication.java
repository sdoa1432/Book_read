package com.yuexun.book_read;

import android.app.Application;
import android.content.Context;

import com.yuexun.book_read.control.SpfControl;
import com.yuexun.book_read.view.custom.factory.ContentPageFactory;

/**
 * Created by yuexun on 2018/6/11.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SpfControl.getInstance().init();
        ContentPageFactory.createContentPageFactory(this);
    }

    public static Context getContext() {
        return context;
    }
}
