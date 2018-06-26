package com.yuexun.book_read.control.callback;

import android.util.Log;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by yuexun on 2018/6/6.
 */

public abstract class OkHttpCallback extends Callback {
    @Override
    public Object parseNetworkResponse(Response response) throws Exception {

        return disposeResponse(response);
    }

    @Override
    public void onError(Call call, Exception e) {
        Log.i("yc.zhang", "code : " + call.request().toString() + " " + e + " " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Object o) {
        //empty
    }

    public abstract Object disposeResponse(Response response)throws Exception;
}
