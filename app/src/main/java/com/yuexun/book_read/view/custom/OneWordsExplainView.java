package com.yuexun.book_read.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuexun.book_read.R;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.view.activity.BookDetailActivity;

import static com.yuexun.book_read.utils.Utils.startActivityForClassNameAndBookId;

/**
 * Created by yuexun on 2018/5/17.
 */

public class OneWordsExplainView extends RelativeLayout {

    private TextView typeview, onewordsview;
    private View rootview;

    public OneWordsExplainView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_one_words_explain, this);

        typeview = findViewById(R.id.explain_booktype);
        onewordsview = findViewById(R.id.explain_oneword);
        rootview = getRootView();
    }

    public void setType(String type) {
        typeview.setText("[" + type + "]");
    }

    public void setOnewords(String onewords) {
        onewordsview.setText(onewords);
    }


    public void setBookInfoModel(final BookModel bookModel) {
        setType(bookModel.bookType);
        setOnewords(bookModel.introduction);

        rootview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForClassNameAndBookId(BookDetailActivity.class,bookModel.bookid);
            }
        });
    }

}
