package com.yuexun.book_read.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yuexun.book_read.R;
import com.yuexun.book_read.model.ChapterInfoModel;
import com.yuexun.book_read.view.custom.ChapterView;

import java.util.List;

/**
 * Created by yuexun on 2018/6/13.
 */

public class ChapterListAdapter extends BaseAdapter {

    private List<ChapterInfoModel> chapterInfoModels;
    private Context context;

    public ChapterListAdapter(Context context, List<ChapterInfoModel> chapterInfoModels) {
        this.chapterInfoModels = chapterInfoModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chapterInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.chapter_list_item, null);
            viewHolder.chapterView = convertView.findViewById(R.id.chapter_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.chapterView.setChapterInfoModel(chapterInfoModels.get(position));
        return convertView;
    }


    class ViewHolder {
        public ChapterView chapterView;
    }
}
