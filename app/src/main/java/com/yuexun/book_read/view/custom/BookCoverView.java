package com.yuexun.book_read.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuexun.book_read.R;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.view.activity.BookDetailActivity;

import static com.yuexun.book_read.utils.Utils.startActivityForClassNameAndBookId;

/**
 * Created by yuexun on 2018/5/17.
 */

public class BookCoverView extends RelativeLayout {

    private TextView bookname;
    private ImageView bookcover;
    private View root;

    public BookCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_book_cover, this);

        bookname = findViewById(R.id.cover_book_name);
        bookcover = findViewById(R.id.book_cover);
        root = getRootView();

    }

    public void setBookcover(String url, Context context) {
        Glide.with(context)
                .load(url)
                .error(R.mipmap.error)
                .into(bookcover);
    }

    public void setBookname(String s) {
        bookname.setText(s);
    }


    public void setBookInfoModel(final BookModel bookInfoModel) {
        setBookname(bookInfoModel.bookname);
        setBookcover(bookInfoModel.coverurl, this.getContext());
        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForClassNameAndBookId(BookDetailActivity.class,bookInfoModel.bookid);
            }
        });
    }
}
