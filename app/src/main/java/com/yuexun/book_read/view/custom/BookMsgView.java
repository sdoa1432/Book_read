package com.yuexun.book_read.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.view.activity.BookDetailActivity;

import static com.yuexun.book_read.utils.Utils.startActivityForClassNameAndBookId;

/**
 * Created by yuexun on 2018/5/16.
 */

public class BookMsgView extends LinearLayout {

    private View root;
    private ImageView bookimg;
    private TextView bookname, bookintroduction, booknum, bookstatus, booktype;

    public BookMsgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_book_msg, this);

        bookimg = findViewById(R.id.bookimg);
        bookname = findViewById(R.id.bookname);
        bookintroduction = findViewById(R.id.bookintroduction);
        booknum = findViewById(R.id.booknum);
        bookstatus = findViewById(R.id.bookstatus);
        booktype = findViewById(R.id.booktype);
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

    public void setBookintroduction(String s) {
        bookintroduction.setText(s);
    }

    public void setBooknum(String s) {
        booknum.setText(s);
    }

    public void setBookstatus(String s) {
        bookstatus.setText(s);
    }

    public void setBooktype(String s) {
        booktype.setText(s);
    }

    public void setBookInfoModel(final BookModel bookModel) {
        setBookname(bookModel.bookname);
        setBookintroduction(bookModel.introduction);
        setBooknum(bookModel.bookNum);
        setBookstatus(bookModel.bookStatus);
        setBooktype(bookModel.bookType);
        setBookimg(bookModel.coverurl, MyApplication.getContext());
        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForClassNameAndBookId(BookDetailActivity.class,bookModel.bookid);
            }
        });
    }
}
