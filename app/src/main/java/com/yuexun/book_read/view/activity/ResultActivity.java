package com.yuexun.book_read.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.control.callback.OkHttpCallback;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.view.adapter.BookMsgListAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static com.yuexun.book_read.utils.Utils.setListHeight;


public class ResultActivity extends AppCompatActivity {

    private ListView listview;
    private BookMsgListAdapter bookMsgListAdapter;
    private Button result_previous, result_next;
    private TextView result_page;
    private ScrollView scrollView;

    private List<BookModel> bookModels = new ArrayList<>();
    private String keyword;
    private int pageindex = 1;
    private int pagecount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listview = findViewById(R.id.result_list);
        result_next = findViewById(R.id.result_next);
        result_previous = findViewById(R.id.result_previous);
        result_page = findViewById(R.id.result_page);
        scrollView = findViewById(R.id.scrollView);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getSearchResultForPage(1);
        result_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageindex < pagecount)
                    getSearchResultForPage(++pageindex);
            }
        });
        result_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageindex > 1)
                    getSearchResultForPage(--pageindex);
            }
        });
    }

    private void getSearchResultForPage(int page) {
        keyword = getIntent().getStringExtra("keyword");
        OkHttpUtils.get()
                .url(DataConstants.CONNECT_IP + DataConstants.API_SEARCH)
                .addParams("keyword", keyword)
                .addParams("page", page + "")
                .tag(DataConstants.API_SEARCH)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new OkHttpCallback() {
                    @Override
                    public Object disposeResponse(Response response) throws Exception {
                        String s = response.body().string();
//                        Log.i("yc.zhang", s);
                        JSONObject responsejson = new JSONObject(s);
                        int code = responsejson.optInt("code", 0);
                        if (code == 200) {
                            bookModels.clear();
                            JSONArray data = responsejson.optJSONArray("data");
                            pageindex = responsejson.optInt("page", 1);
                            pagecount = responsejson.optInt("pages", 1);
                            for (int j = 0; j < data.length(); j++) {
                                JSONObject jsonObject = data.getJSONObject(j);
                                BookModel bookModel = new BookModel();
                                bookModel.bookid = jsonObject.getInt("bookId");
                                bookModel.bookname = jsonObject.getString("bookName");
                                bookModel.bookNum = jsonObject.getString("bookNum");
                                bookModel.bookType = jsonObject.getString("bookType");
                                bookModel.introduction = jsonObject.getString("introduction");
                                bookModel.bookStatus = jsonObject.getString("status");
                                bookModel.coverurl = jsonObject.getString("url");
                                bookModels.add(bookModel);
                            }
                            showlist.sendEmptyMessage(0);
                        }
                        return null;
                    }
                });
    }

    Handler showlist = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bookMsgListAdapter = new BookMsgListAdapter(bookModels, MyApplication.getContext());
            setListHeight(listview, bookMsgListAdapter);
            listview.setAdapter(bookMsgListAdapter);
            result_page.setText(pageindex + "/" + pagecount);
            if (pageindex == pagecount) {
                result_next.setClickable(false);
            } else if (pageindex == 1) {
                result_previous.setClickable(false);
            } else {
                result_next.setClickable(true);
                result_previous.setClickable(true);
            }
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, 0);
                }
            });
        }

    };

}
