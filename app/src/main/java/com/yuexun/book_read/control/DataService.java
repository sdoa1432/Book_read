package com.yuexun.book_read.control;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.yuexun.book_read.control.event.BookCaseAcquireEvent;
import com.yuexun.book_read.model.BookModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class DataService extends Service {


    private List<BookModel> mBannerDatas = new ArrayList<>();
    private List<BookModel> mHotDatas = new ArrayList<>();
    private List<BookModel> mBookFirstDatas = new ArrayList<>();
    private List<BookModel> mRecommendDatas = new ArrayList<>();
    private List<BookModel> mActiveDemandDatas = new ArrayList<>();
    public static List<BookModel> bookcaseModels = new ArrayList<>();
    public static boolean bookcaseflag = false;

    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }
        Log.i("yc.zhang","get intent");
        int index = intent.getIntExtra("index", 1);
        Log.i("yc.zhang","index = " + index);
        getBookcase(index);
        return super.onStartCommand(intent, flags, startId);
    }


    private void getBookcase(int index) {
        final BookCaseAcquireEvent bookCaseAcquireEvent = new BookCaseAcquireEvent();
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_BOOK_CASE)
                .addParams("pageIndex", index + "")
                .addParams("session", SpfControl.getInstance().getString(DataConstants.SPF_KEY_SESSION))
                .tag(DataConstants.API_BOOK_CASE)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        Log.i("yc.zhang", jsonObject.toString());
                        if (jsonObject.optInt("code", 0) == 200) {
                            bookcaseflag = true;
                            bookcaseModels.clear();
                            bookCaseAcquireEvent.pageindex = jsonObject.optInt("page", 1);
                            bookCaseAcquireEvent.pagecount = jsonObject.optInt("pages", 1);
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject datajson = data.getJSONObject(i);
                                BookModel bookModel = new BookModel();
                                bookModel.bookid = datajson.getInt("bookId");
                                bookModel.bookname = datajson.getString("bookName");
                                bookModel.bookupdate = datajson.getString("bookUpdate");
                                bookModel.coverurl = datajson.getString("url");
                                bookcaseModels.add(bookModel);
                            }
                            bookCaseAcquireEvent.acquireFlag = 0;
                        } else {
                            bookCaseAcquireEvent.acquireFlag = 1;
                        }
                        EventBus.getDefault().post(bookCaseAcquireEvent);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        bookCaseAcquireEvent.acquireFlag = 2;
                        Log.i("yc.zhang", e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Object o) {

                    }
                });
    }

}
