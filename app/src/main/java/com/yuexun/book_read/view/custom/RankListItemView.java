package com.yuexun.book_read.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuexun.book_read.R;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.utils.Utils;
import com.yuexun.book_read.view.activity.BookDetailActivity;

/**
 * Created by yuexun on 2018/5/30.
 */

public class RankListItemView extends RelativeLayout {

    private TextView num_text, name_text;
    private View rootview;

    public RankListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_rank_list_item, this);

        num_text = findViewById(R.id.rank_item_serial_num);
        name_text = findViewById(R.id.rank_item_name);
        rootview = getRootView();
    }

    public void setNum(int num) {
        num_text.setText(num + "");
    }

    public void setName(String name) {
        name_text.setText(name);
    }

    public void setBookInfoModel(final BookModel bookModel, int num) {
        if (num == 1)
            num_text.setBackground(getResources().getDrawable(R.drawable.circle_rank_serial_red));
        if (num == 2)
            num_text.setBackground(getResources().getDrawable(R.drawable.circle_rank_serial_orange));
        if (num == 3)
            num_text.setBackground(getResources().getDrawable(R.drawable.circle_rank_serial_blue));
        setNum(num);
        setName(bookModel.bookname);

        rootview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startActivityForClassNameAndBookId(BookDetailActivity.class, bookModel.bookid);
            }
        });
    }

}
