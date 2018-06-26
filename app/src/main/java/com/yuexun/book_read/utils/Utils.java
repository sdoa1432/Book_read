package com.yuexun.book_read.utils;

import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.model.BookModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yuexun on 2018/5/23.
 */

public class Utils {

    public static void startActivityForClassNameAndBookId(Class<?> cls, int bookid) {
        Intent intent = new Intent(MyApplication.getContext(), cls);
        intent.putExtra("bookid", bookid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getContext().startActivity(intent);
    }

    public static void startActivityForClassName(Class<?> cls) {
        Intent intent = new Intent(MyApplication.getContext(), cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getContext().startActivity(intent);
    }

    private static int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, MyApplication.getContext().getResources().getDisplayMetrics());
    }

//    private static BookMsgModel jsonToBookInfoModel(JSONObject jsonObject) throws JSONException {
//        return new BookMsgModel(jsonObject.getInt("bookId")
//                , jsonObject.getString("bookName")
//                , jsonObject.getString("bookNum")
//                , jsonObject.getString("bookType")
//                , jsonObject.getString("introduction")
//                , jsonObject.getString("status")
//                , jsonObject.getString("url")
//        );
//    }
//
//    public static void jsonarrayToList(JSONArray jsonArray, List<BookMsgModel> list) throws JSONException {
//        for (int i = 0; i < jsonArray.length(); i++) {
//            BookMsgModel bookMsgModel = Utils.jsonToBookInfoModel(jsonArray.getJSONObject(i));
//            list.add(bookMsgModel);
//        }
//    }

    public static void setListHeight(ListView listview, ListAdapter listAdapter) {
        View itemView = listAdapter.getView(0, null, listview);
        //测量一项的高度
        itemView.measure(0, 0);
        int itemHeight = itemView.getMeasuredHeight(); //一项的高度
        int itemCount = listAdapter.getCount();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * itemCount);
        listview.setLayoutParams(layoutParams);
    }

    public static boolean checkbookidinlist(List<BookModel> list, int bookid) {
        if (list.size() < 1)
            return false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).bookid == bookid) {
                return true;
            }
        }
        return false;
    }
}
