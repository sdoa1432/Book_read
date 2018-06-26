package com.yuexun.book_read.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuexun.book_read.R;
import com.yuexun.book_read.model.ChapterInfoModel;

/**
 * Created by yuexun on 2018/5/21.
 */

public class ChapterView extends RelativeLayout {

    private TextView chapter_name, chapter_time;
    private ImageView chapter_new, chapter_vip;

    public ChapterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_new_chapter, this);

        chapter_name = findViewById(R.id.chapter_name);
        chapter_time = findViewById(R.id.chapter_time);
        chapter_vip = findViewById(R.id.chapter_vip);

    }

    public void setVip(boolean b) {
        if (b) {
            chapter_vip.setVisibility(VISIBLE);
        } else {
            chapter_vip.setVisibility(GONE);
        }
    }

    public void setchaptername(String s) {
        chapter_name.setText(s);
    }

    public void setchaptertime(String s) {
        chapter_time.setText(s);
    }

    public void setChapterInfoModel(ChapterInfoModel chapterInfoModel){
        setchaptername(chapterInfoModel.chapterTitle);
        setVip(chapterInfoModel.isVip);
    }

}
