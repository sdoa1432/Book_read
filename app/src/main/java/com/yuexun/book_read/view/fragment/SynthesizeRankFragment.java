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
import com.yuexun.book_read.view.custom.RankListItemView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by yuexun on 2018/5/29.
 */

public class SynthesizeRankFragment extends Fragment {


    private RankListItemView rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8, rank9, rank10;
    private List<BookModel> clickRankList = new ArrayList<>();
    private List<BookModel> collectRankList = new ArrayList<>();
    private List<BookModel> rewardRankList = new ArrayList<>();
    private List<BookModel> wordsRankList = new ArrayList<>();
    private List<BookModel> newBookRankList = new ArrayList<>();
    private List<BookModel> overBookRankList = new ArrayList<>();

    public SynthesizeRankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_synthesize_rank, container, false);
        rank1 = root.findViewById(R.id.synthesize_rank_item_1);
        rank2 = root.findViewById(R.id.synthesize_rank_item_2);
        rank3 = root.findViewById(R.id.synthesize_rank_item_3);
        rank4 = root.findViewById(R.id.synthesize_rank_item_4);
        rank5 = root.findViewById(R.id.synthesize_rank_item_5);
        rank6 = root.findViewById(R.id.synthesize_rank_item_6);
        rank7 = root.findViewById(R.id.synthesize_rank_item_7);
        rank8 = root.findViewById(R.id.synthesize_rank_item_8);
        rank9 = root.findViewById(R.id.synthesize_rank_item_9);
        rank10 = root.findViewById(R.id.synthesize_rank_item_10);

        downloaddata();

        return root;
    }

    private void downloaddata() {
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_RANK)
                .addParams("sex", 2 + "")
                .tag(DataConstants.API_RANK + 2)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject responsejson = new JSONObject(s);
                        int code = responsejson.optInt("code", 0);
                        if (code == 200) {
                            clickRankList.clear();
                            collectRankList.clear();
                            rewardRankList.clear();
                            wordsRankList.clear();
                            newBookRankList.clear();
                            overBookRankList.clear();
                            JSONArray res = responsejson.optJSONArray("res");
                            for (int i = 0; i < res.length(); i++) {
                                JSONObject dataJSONObject = res.getJSONObject(i);
                                JSONArray content = dataJSONObject.optJSONArray("content");
                                switch (dataJSONObject.optString("name")) {
                                    case "点击排行榜":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            bookModel.bookname = jsonObject.getString("bookName");
                                            bookModel.bookNum = jsonObject.getString("bookNum");
                                            bookModel.bookType = jsonObject.getString("bookType");
                                            bookModel.introduction = jsonObject.getString("introduction");
                                            bookModel.bookStatus = jsonObject.getString("status");
                                            clickRankList.add(bookModel);
                                        }
                                        break;
                                    case "打赏排行榜":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            bookModel.bookname = jsonObject.getString("bookName");
                                            bookModel.bookNum = jsonObject.getString("bookNum");
                                            bookModel.bookType = jsonObject.getString("bookType");
                                            bookModel.introduction = jsonObject.getString("introduction");
                                            bookModel.bookStatus = jsonObject.getString("status");
                                            rewardRankList.add(bookModel);
                                        }
                                        break;
                                    case "收藏排行榜":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            bookModel.bookname = jsonObject.getString("bookName");
                                            bookModel.bookNum = jsonObject.getString("bookNum");
                                            bookModel.bookType = jsonObject.getString("bookType");
                                            bookModel.introduction = jsonObject.getString("introduction");
                                            bookModel.bookStatus = jsonObject.getString("status");
                                            collectRankList.add(bookModel);
                                        }
                                        break;
                                    case "字数排行榜":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            bookModel.bookname = jsonObject.getString("bookName");
                                            bookModel.bookNum = jsonObject.getString("bookNum");
                                            bookModel.bookType = jsonObject.getString("bookType");
                                            bookModel.introduction = jsonObject.getString("introduction");
                                            bookModel.bookStatus = jsonObject.getString("status");
                                            wordsRankList.add(bookModel);
                                        }
                                        break;
                                    case "新书排行榜":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            bookModel.bookname = jsonObject.getString("bookName");
                                            bookModel.bookNum = jsonObject.getString("bookNum");
                                            bookModel.bookType = jsonObject.getString("bookType");
                                            bookModel.introduction = jsonObject.getString("introduction");
                                            bookModel.bookStatus = jsonObject.getString("status");
                                            newBookRankList.add(bookModel);
                                        }
                                        break;
                                    case "完本排行榜":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            bookModel.bookname = jsonObject.getString("bookName");
                                            bookModel.bookNum = jsonObject.getString("bookNum");
                                            bookModel.bookType = jsonObject.getString("bookType");
                                            bookModel.introduction = jsonObject.getString("introduction");
                                            bookModel.bookStatus = jsonObject.getString("status");
                                            overBookRankList.add(bookModel);
                                        }
                                        break;
                                }
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
            rank1.setBookInfoModel(clickRankList.get(++index), index + 1);
            rank2.setBookInfoModel(clickRankList.get(++index), index + 1);
            rank3.setBookInfoModel(clickRankList.get(++index), index + 1);
            rank4.setBookInfoModel(clickRankList.get(++index), index + 1);
            rank5.setBookInfoModel(clickRankList.get(++index), index + 1);
            rank6.setBookInfoModel(clickRankList.get(++index), index + 1);
            rank7.setBookInfoModel(clickRankList.get(++index), index + 1);
            rank8.setBookInfoModel(clickRankList.get(++index), index + 1);
            rank9.setBookInfoModel(clickRankList.get(++index), index + 1);
            rank10.setBookInfoModel(clickRankList.get(++index), index + 1);

        }
    };


}
