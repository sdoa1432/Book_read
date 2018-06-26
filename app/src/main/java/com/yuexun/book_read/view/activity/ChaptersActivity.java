package com.yuexun.book_read.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.model.ChapterInfoModel;
import com.yuexun.book_read.utils.Utils;
import com.yuexun.book_read.view.adapter.ChapterListAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ChaptersActivity extends AppCompatActivity {

    private int mBookId;
    private String mBookName;
    private TextView title;
    private ListView chapters;
    private Button result_previous, result_next;
    private TextView result_page;
    private int pageindex = 1;
    private int pagecount = 1;
    private List<ChapterInfoModel> chapterInfoModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        mBookId = getIntent().getIntExtra("bookid", 0);
        mBookName = getIntent().getStringExtra("bookname");

        chapterInfoModels = new ArrayList<>();

        title = findViewById(R.id.chapter_title);
        chapters = findViewById(R.id.chapter_list);
        result_next = findViewById(R.id.result_next);
        result_previous = findViewById(R.id.result_previous);
        result_page = findViewById(R.id.result_page);

        title.setText(mBookName);
        result_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageindex < pagecount)
                    initchapterdata(++pageindex);
            }
        });
        result_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageindex > 1)
                    initchapterdata(--pageindex);
            }
        });

        initchapterdata(1);
    }

    private void initchapterdata(int page) {
        Log.i("yc.zhang", "page : " + page);
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_BOOK_CHAPTERS)
                .addParams("bookId", mBookId + "")
                .addParams("page", page + "")
                .tag(DataConstants.API_BOOK_CHAPTERS)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        Log.i("yc.zhang", jsonObject.toString());
                        if (jsonObject.getInt("code") == 200) {
                            chapterInfoModels.clear();
                            pagecount = jsonObject.optInt("pages", 1);
                            JSONArray chapters = jsonObject.getJSONArray("data");
                            for (int i = 0; i < chapters.length(); i++) {
                                ChapterInfoModel chapterInfoModel = new ChapterInfoModel();
                                chapterInfoModel.chapterId = chapters.getJSONObject(i).getInt("chapterId");
                                chapterInfoModel.chapterTitle = chapters.getJSONObject(i).getString("chapterTitle");
                                if (chapters.getJSONObject(i).optInt("isVip", 0) == 1) {
                                    chapterInfoModel.isVip = true;
                                } else {
                                    chapterInfoModel.isVip = false;
                                }
                                chapterInfoModels.add(chapterInfoModel);
                            }
                            show.sendEmptyMessage(0);
                        }
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("yc.zhang", e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Object o) {

                    }
                });
    }

    private ChapterListAdapter chapterListAdapter;

    Handler show = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            chapterListAdapter = new ChapterListAdapter(MyApplication.getContext(), chapterInfoModels);
            Utils.setListHeight(chapters, chapterListAdapter);
            chapters.setAdapter(chapterListAdapter);
            chapters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyApplication.getContext(), ContentActivity.class);
                    intent.putExtra("bookid", mBookId);
                    intent.putExtra("chapterId", chapterInfoModels.get(position).chapterId);
                    intent.putExtra("bookName", mBookName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            result_page.setText(pageindex + "/" + pagecount);
            if (pageindex == pagecount) {
                result_next.setClickable(false);
            } else if (pageindex == 1) {
                result_previous.setClickable(false);
            } else {
                result_next.setClickable(true);
                result_previous.setClickable(true);
            }
        }
    };
}
