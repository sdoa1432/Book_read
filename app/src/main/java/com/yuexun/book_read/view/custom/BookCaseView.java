package com.yuexun.book_read.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.control.SpfControl;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.view.activity.BookDetailActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

import static com.yuexun.book_read.utils.Utils.startActivityForClassNameAndBookId;

public class BookCaseView extends LinearLayout {

    private ImageView bookimg;
    private ImageView bookdel;
    private TextView bookname, bookupdate;
    private View root;

    public BookCaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.book_case_view, this);

        bookimg = findViewById(R.id.bookimg);
        bookname = findViewById(R.id.bookname);
        bookupdate = findViewById(R.id.bookupdate);
        bookdel = findViewById(R.id.book_del);
        root = getRootView();
    }

    public void setBookimg(String url, Context context) {
        Glide.with(context)
                .load(url)
                .error(R.mipmap.error)
                .into(bookimg);
    }


    public void setBookname(String s) {
        bookname.setText(s);
    }

    public void setBookupdate(String s) {
        bookupdate.setText(s);
    }

    public void setBookCaseModel(final BookModel bookModel, boolean delflag/*, View.OnClickListener dellistener*/) {
        setBookname(bookModel.bookname);
        setBookupdate("最新章节：\n" + bookModel.bookupdate);
        setBookimg(bookModel.coverurl, this.getContext());
        if (delflag) {
            bookdel.setVisibility(VISIBLE);
            bookdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delBook(bookModel.bookid);
                }
            });
        } else {
            bookdel.setVisibility(INVISIBLE);
            root.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForClassNameAndBookId(BookDetailActivity.class, bookModel.bookid);
                }
            });
        }
    }

    private DelListener delListener;

    public void setdelListener(DelListener delListener) {
        this.delListener = delListener;
    }

    public void delBook(int bookid) {
        if (SpfControl.getInstance().getString(DataConstants.SPF_KEY_SESSION).equals("")) return;
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_DEL_BOOK_CASE)
                .addParams("bookIdList", bookid + "")
                .addParams("session", SpfControl.getInstance().getString(DataConstants.SPF_KEY_SESSION))
                .tag(DataConstants.API_DEL_BOOK_CASE)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        Log.i("yc.zhang", jsonObject.toString());
                        if (jsonObject.optInt("code", 0) == 200) {
                            if (delListener != null)
                                delListener.onDelSuccee();
                        } else {
                            if (delListener != null)
                                delListener.onDelDefeat();
                        }
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        if (delListener != null)
                            delListener.onDelDefeat();
                    }

                    @Override
                    public void onResponse(Call call, Object o) {

                    }
                });
    }

    public interface DelListener {
        void onDelSuccee();

        void onDelDefeat();
    }

}
