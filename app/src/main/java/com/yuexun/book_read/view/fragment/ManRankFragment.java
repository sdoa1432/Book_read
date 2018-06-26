package com.yuexun.book_read.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.view.custom.BookCoverView;
import com.yuexun.book_read.view.custom.RankListItemView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ManRankFragment extends Fragment {

    private BookCoverView rank1, rank2, rank3;
    private RankListItemView rank4, rank5, rank6, rank7, rank8;
    private List<BookModel> manRankList = new ArrayList<>();

    public ManRankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_man_rank, container, false);
        rank1 = root.findViewById(R.id.man_rank_item_1);
        rank2 = root.findViewById(R.id.man_rank_item_2);
        rank3 = root.findViewById(R.id.man_rank_item_3);
        rank4 = root.findViewById(R.id.man_rank_item_4);
        rank5 = root.findViewById(R.id.man_rank_item_5);
        rank6 = root.findViewById(R.id.man_rank_item_6);
        rank7 = root.findViewById(R.id.man_rank_item_7);
        rank8 = root.findViewById(R.id.man_rank_item_8);

        downloaddata();

        return root;
    }

    private void downloaddata() {
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_RANK)
                .addParams("sex", 0 + "")
                .tag(DataConstants.API_RANK + 0)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject responsejson = new JSONObject(s);
                        int code = responsejson.optInt("code", 0);
                        if (code == 200) {
                            manRankList.clear();
                            JSONArray res = responsejson.optJSONArray("res");
                            for (int j = 0; j < res.length(); j++) {
                                JSONObject jsonObject = res.getJSONObject(j);
                                BookModel bookModel = new BookModel();
                                bookModel.bookid = jsonObject.getInt("bookId");
                                bookModel.bookname = jsonObject.getString("bookName");
                                bookModel.bookNum = jsonObject.getString("bookNum");
                                bookModel.bookType = jsonObject.getString("bookType");
                                bookModel.introduction = jsonObject.getString("introduction");
                                bookModel.bookStatus = jsonObject.getString("status");
                                bookModel.coverurl = jsonObject.getString("url");
                                manRankList.add(bookModel);
                            }
                            show.sendEmptyMessage(0);
                        }
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("yc.zhang", "code : " + call.request().toString() + " " + e + " " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Object o) {

                    }
                });
    }

    Handler show = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int index = -1;
            rank1.setBookInfoModel(manRankList.get(++index));
            rank2.setBookInfoModel(manRankList.get(++index));
            rank3.setBookInfoModel(manRankList.get(++index));
            rank4.setBookInfoModel(manRankList.get(++index),index+1);
            rank5.setBookInfoModel(manRankList.get(++index),index+1);
            rank6.setBookInfoModel(manRankList.get(++index),index+1);
            rank7.setBookInfoModel(manRankList.get(++index),index+1);
            rank8.setBookInfoModel(manRankList.get(++index),index+1);

        }
    };


}
