package com.yuexun.book_read.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yuexun.book_read.R;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.view.custom.BookCaseView;
import com.yuexun.book_read.view.custom.BookMsgView;

import java.util.List;

/**
 * Created by yuexun on 2018/6/15.
 */

public class BookCaseListAdapter extends BaseAdapter {

    private List<BookModel> bookModels;
    private Context context;
    private boolean delflag;
//    private View.OnClickListener listener;

    public BookCaseListAdapter(List<BookModel> bookModels, Context context, boolean delflag/*, View.OnClickListener listener*/) {
        this.bookModels = bookModels;
        this.context = context;
        this.delflag = delflag;
//        this.listener = listener;
    }

    @Override
    public int getCount() {
        return bookModels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.book_case_list_item, null);
            viewHolder.bookCaseView = convertView.findViewById(R.id.item_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (bookModels.size() > 0) {
            viewHolder.bookCaseView.setBookCaseModel(bookModels.get(position), delflag/*, listener*/);
            viewHolder.bookCaseView.setdelListener(delListener);
        }
        return convertView;

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private BookCaseView.DelListener delListener;

    public void setBackListener(BookCaseView.DelListener delListener) {
        this.delListener = delListener;
    }

    public void removeListener() {
        this.delListener = null;
    }


    class ViewHolder {
        public BookCaseView bookCaseView;
    }
}
