package com.yuexun.book_read.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.control.DataService;
import com.yuexun.book_read.control.SpfControl;
import com.yuexun.book_read.control.callback.OkHttpCallback;
import com.yuexun.book_read.model.BookModel;
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

import static com.yuexun.book_read.utils.Utils.checkbookidinlist;
import static com.yuexun.book_read.utils.Utils.startActivityForClassName;

public class BookDetailActivity extends AppCompatActivity {

    private Context mContext;
    private int mBookId;
    private List<ChapterInfoModel> chapterInfoModels;
    private BookModel bookModel;
    private int chaptercount;
    private ImageView bookcover;
    private TextView bookname, bookid, bookauthor, booknumandtype, bookclick, bookintroduce, more_chapter;
    private ListView chapterlist;
    private Button startread, addbookcase;
    private ChapterListAdapter chapterListAdapter;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        mBookId = getIntent().getIntExtra("bookid", 0);
        chapterInfoModels = new ArrayList<>();
        initdata();

        mContext = this;
        bookcover = findViewById(R.id.detail_book_cover);
        bookname = findViewById(R.id.detail_book_name);
        bookid = findViewById(R.id.detail_book_id);
        bookauthor = findViewById(R.id.detail_book_author_name);
        bookintroduce = findViewById(R.id.detail_book_introduce);
        booknumandtype = findViewById(R.id.detail_book_num_type);
        bookclick = findViewById(R.id.detail_book_cilck);
        chapterlist = findViewById(R.id.detail_chapter_list);
        startread = findViewById(R.id.detail_start_read);
        addbookcase = findViewById(R.id.detail_add_book_case);
        more_chapter = findViewById(R.id.more_chapter);
        scrollView = findViewById(R.id.scrollView);

        startread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.getContext(), ContentActivity.class);
                intent.putExtra("bookid", mBookId);
                intent.putExtra("chapterId", 1);
                intent.putExtra("chapterName", chapterInfoModels.get(1).chapterTitle);
                intent.putExtra("bookName", bookModel.bookname);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        if (checkbookidinlist(DataService.bookcaseModels, mBookId)) {
            addbookcase.setText("已在书架");
        } else {
            addbookcase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SpfControl.getInstance().getString(DataConstants.SPF_KEY_SESSION).equals("")) {
                        Toast.makeText(MyApplication.getContext(), "未登录，请登录", Toast.LENGTH_LONG).show();
                        startActivityForClassName(LoginActivity.class);
                        return;
                    }
                    OkHttpUtils.post()
                            .url(DataConstants.CONNECT_IP + DataConstants.API_ADD_BOOK_CASE)
                            .addParams("bookId", mBookId + "")
                            .addParams("session", SpfControl.getInstance().getString(DataConstants.SPF_KEY_SESSION))
                            .tag(DataConstants.API_ADD_BOOK_CASE)
                            .build()
                            .connTimeOut(20 * 1000)
                            .execute(new Callback() {
                                @Override
                                public Object parseNetworkResponse(Response response) throws Exception {
                                    String s = response.body().string();
                                    JSONObject jsonObject = new JSONObject(s);
                                    Log.i("yc.zhang", jsonObject.toString());
                                    return null;
                                }

                                @Override
                                public void onError(Call call, Exception e) {

                                }

                                @Override
                                public void onResponse(Call call, Object o) {

                                }
                            });
                }
            });
        }

        more_chapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.getContext(), ChaptersActivity.class);
                intent.putExtra("bookid", mBookId);
                intent.putExtra("bookname", bookModel.bookname);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void initdata() {
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_BOOK_DETAIL)
                .addParams("bookId", mBookId + "")
                .tag(DataConstants.API_BOOK_DETAIL)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new OkHttpCallback() {
                    @Override
                    public Object disposeResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getInt("code") == 200) {
                            chapterInfoModels.clear();
                            chaptercount = jsonObject.getInt("all");
                            JSONObject info = jsonObject.getJSONObject("info");
                            bookModel = new BookModel();
                            bookModel.author = info.getString("author");
                            bookModel.bookid = info.getInt("bookId");
                            bookModel.click = info.getInt("click");
                            bookModel.collect = info.getInt("collect");
                            bookModel.bookNum = info.getString("bookNum");
                            bookModel.bookStatus = info.getString("bookStatus");
                            bookModel.bookupdate = info.getString("bookUpdate");
                            bookModel.introduction = info.getString("introduction");
                            bookModel.bookname = info.getString("name");
                            bookModel.coverurl = info.getString("picUrl");
                            JSONArray chapters = jsonObject.getJSONArray("chapter");
                            for (int i = 0; i < chapters.length(); i++) {
                                ChapterInfoModel chapterInfoModel = new ChapterInfoModel();
                                chapterInfoModel.chapterId = chapters.getJSONObject(i).getInt("chapterId");
                                chapterInfoModel.chapterTitle = chapters.getJSONObject(i).getString("chapterTitle");
                                if (chapters.getJSONObject(i).getInt("isVip") == 1) {
                                    chapterInfoModel.isVip = true;
                                } else {
                                    chapterInfoModel.isVip = false;
                                }
                                chapterInfoModels.add(chapterInfoModel);
                            }
                            ChapterInfoModel last = chapterInfoModels.get(chapterInfoModels.size() - 1);
                            chapterInfoModels.remove(chapterInfoModels.size() - 1);
                            chapterInfoModels.add(0, last);
                            show.sendEmptyMessage(0);
                        }
                        return null;
                    }
                });
    }

    Handler show = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Glide.with(mContext)
                    .load(bookModel.coverurl)
                    .error(R.mipmap.error)
                    .into(bookcover);
            bookname.setText(bookModel.bookname);
            bookid.setText("书号 " + bookModel.bookid);
            bookauthor.setText(bookModel.author);
            booknumandtype.setText(bookModel.bookNum + " | " + bookModel.bookStatus);
            bookclick.setText(bookModel.click + " 次点击");
            bookintroduce.setText(bookModel.introduction);

            chapterListAdapter = new ChapterListAdapter(MyApplication.getContext(), chapterInfoModels);
            Utils.setListHeight(chapterlist, chapterListAdapter);
            chapterlist.setAdapter(chapterListAdapter);
            chapterlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyApplication.getContext(), ContentActivity.class);
                    intent.putExtra("bookid", mBookId);
                    intent.putExtra("chapterId", chapterInfoModels.get(position).chapterId);
                    intent.putExtra("bookName", bookModel.bookname);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, 0);
                }
            });
        }
    };

}
