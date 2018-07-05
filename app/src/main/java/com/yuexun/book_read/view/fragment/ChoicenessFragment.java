package com.yuexun.book_read.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bumptech.glide.Glide;
import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.view.activity.BookDetailActivity;
import com.yuexun.book_read.view.activity.ContentActivity;
import com.yuexun.book_read.view.activity.LibraryActivity;
import com.yuexun.book_read.view.activity.RankActivity;
import com.yuexun.book_read.view.activity.SearchActivity;
import com.yuexun.book_read.view.custom.BookCoverView;
import com.yuexun.book_read.view.custom.BookMsgView;
import com.yuexun.book_read.view.custom.OneWordsExplainView;
import com.yuexun.book_read.view.custom.TwoSwitvhButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;
import okhttp3.Call;
import okhttp3.Response;

import static com.yuexun.book_read.utils.Utils.startActivityForClassNameAndBookId;

/**
 * Created by yuexun on 2018/6/12.
 */

public class ChoicenessFragment extends Fragment {

    private Context mContext;
    private TwoSwitvhButton twoSwitvhButton;
    private BGABanner mBanner;
    private BookMsgView hot_bookMsgView, main_recommend1, main_recommend2, main_recommend3;
    private OneWordsExplainView hot_wordsExplainView1, hot_wordsExplainView2, hot_wordsExplainView3, hot_wordsExplainView4,
            hot_wordsExplainView5, hot_wordsExplainView6, first_wordsExplainView1, first_wordsExplainView2, first_wordsExplainView3;
    private BookCoverView bookCoverView1, bookCoverView2, bookCoverView3, active_demand_BookCover_1, active_demand_BookCover_2,
            active_demand_BookCover_3, active_demand_BookCover_4, active_demand_BookCover_5, active_demand_BookCover_6;
    private RelativeLayout ranking;
    private Button library;
    private Button main_search;

    private List<BookModel> mBannerDatas = new ArrayList<>();
    private List<BookModel> mHotDatas = new ArrayList<>();
    private List<BookModel> mBookFirstDatas = new ArrayList<>();
    private List<BookModel> mRecommendDatas = new ArrayList<>();
    private List<BookModel> mActiveDemandDatas = new ArrayList<>();
    private List<String> tips = new ArrayList<>();

    private LoadingDailog load;

    public ChoicenessFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_choiceness, container, false);

        mContext = getContext();
        twoSwitvhButton = root.findViewById(R.id.title_two_button);
        mBanner = root.findViewById(R.id.main_banner);
        hot_bookMsgView = root.findViewById(R.id.main_hot_bookmsg);
        hot_wordsExplainView1 = root.findViewById(R.id.hot_one_explain_1);
        hot_wordsExplainView2 = root.findViewById(R.id.hot_one_explain_2);
        hot_wordsExplainView3 = root.findViewById(R.id.hot_one_explain_3);
        hot_wordsExplainView4 = root.findViewById(R.id.hot_one_explain_4);
        hot_wordsExplainView5 = root.findViewById(R.id.hot_one_explain_5);
        hot_wordsExplainView6 = root.findViewById(R.id.hot_one_explain_6);
        bookCoverView1 = root.findViewById(R.id.book_first_1);
        bookCoverView2 = root.findViewById(R.id.book_first_2);
        bookCoverView3 = root.findViewById(R.id.book_first_3);
        first_wordsExplainView1 = root.findViewById(R.id.book_first_4);
        first_wordsExplainView2 = root.findViewById(R.id.book_first_5);
        first_wordsExplainView3 = root.findViewById(R.id.book_first_6);
        main_recommend1 = root.findViewById(R.id.main_recommend1);
        main_recommend2 = root.findViewById(R.id.main_recommend2);
        main_recommend3 = root.findViewById(R.id.main_recommend3);
        active_demand_BookCover_1 = root.findViewById(R.id.active_demand_BookCover_1);
        active_demand_BookCover_2 = root.findViewById(R.id.active_demand_BookCover_2);
        active_demand_BookCover_3 = root.findViewById(R.id.active_demand_BookCover_3);
        active_demand_BookCover_4 = root.findViewById(R.id.active_demand_BookCover_4);
        active_demand_BookCover_5 = root.findViewById(R.id.active_demand_BookCover_5);
        active_demand_BookCover_6 = root.findViewById(R.id.active_demand_BookCover_6);
        ranking = root.findViewById(R.id.ranking);
        library = root.findViewById(R.id.library);
        main_search = root.findViewById(R.id.main_search);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        load = new LoadingDailog.Builder(this.getContext())
                .setMessage("加载中...")
                .setCancelable(false)
                .setCancelOutside(false)
                .create();

        main_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyApplication.getContext(), SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                startActivity(new Intent(MyApplication.getContext(), ContentActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyApplication.getContext(), RankActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyApplication.getContext(), LibraryActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        //banner
        mBanner.setTransitionEffect(TransitionEffect.Alpha);
        mBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                startActivityForClassNameAndBookId(BookDetailActivity.class, ((BookModel) model).bookid);
            }
        });
        mBanner.setAdapter(new BGABanner.Adapter<ImageView, BookModel>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable BookModel model, int position) {
                Glide.with(mContext)
                        .load(model.disseminateurl)
                        .error(R.mipmap.error)
                        .into(itemView);
            }
        });


        downloadData(twoSwitvhButton.getType() + "");
        twoSwitvhButton.setRightListener(new TwoSwitvhButton.Listener() {
            @Override
            public void onClick(View v) {
                downloadData("1");
            }
        });
        twoSwitvhButton.setLeftListener(new TwoSwitvhButton.Listener() {
            @Override
            public void onClick(View v) {
                downloadData("0");
            }
        });

    }


    private void downloadData(String type) {
//        if (!load.isShowing() && load != null)
//            load.show();
        OkHttpUtils.getInstance().cancelTag(DataConstants.API_MAIN);
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_MAIN)
                .addParams("sex", type)
                .tag(DataConstants.API_MAIN)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject responsejson = new JSONObject(s);
                        int code = responsejson.optInt("code", 0);
                        Log.i("yc.zhang", "code : " + code);
                        if (code == 200) {
                            mBannerDatas.clear();
                            mHotDatas.clear();
                            mBookFirstDatas.clear();
                            mRecommendDatas.clear();
                            mActiveDemandDatas.clear();
                            JSONArray data = responsejson.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject dataJSONObject = data.getJSONObject(i);
                                JSONArray content = dataJSONObject.optJSONArray("content");
                                switch (dataJSONObject.getString("name")) {
                                    case "":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.disseminateurl = jsonObject.getString("cover");
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            mBannerDatas.add(bookModel);
                                        }
                                        break;
                                    case "重磅推荐":
                                    case "热门推荐":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            bookModel.bookname = jsonObject.getString("bookName");
                                            bookModel.bookNum = jsonObject.getString("bookNum");
                                            bookModel.bookType = jsonObject.getString("bookType");
                                            bookModel.introduction = jsonObject.getString("introduction");
                                            bookModel.bookStatus = jsonObject.getString("status");
                                            bookModel.coverurl = jsonObject.getString("url");
                                            mHotDatas.add(bookModel);
                                        }
                                        break;
                                    case "新书抢鲜":
                                    case "潜力新作":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            bookModel.bookname = jsonObject.getString("bookName");
                                            bookModel.bookNum = jsonObject.getString("bookNum");
                                            bookModel.bookType = jsonObject.getString("bookType");
                                            bookModel.introduction = jsonObject.getString("introduction");
                                            bookModel.bookStatus = jsonObject.getString("status");
                                            bookModel.coverurl = jsonObject.getString("url");
                                            mBookFirstDatas.add(bookModel);
                                        }
                                        break;
                                    case "主编推荐":
                                    case "主编力推":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            bookModel.bookname = jsonObject.getString("bookName");
                                            bookModel.bookNum = jsonObject.getString("bookNum");
                                            bookModel.bookType = jsonObject.getString("bookType");
                                            bookModel.introduction = jsonObject.getString("introduction");
                                            bookModel.bookStatus = jsonObject.getString("status");
                                            bookModel.coverurl = jsonObject.getString("url");
                                            mRecommendDatas.add(bookModel);
                                        }
                                        break;
                                    case "畅销精品":
                                    case "火热畅销":
                                        for (int j = 0; j < content.length(); j++) {
                                            JSONObject jsonObject = content.getJSONObject(j);
                                            BookModel bookModel = new BookModel();
                                            bookModel.bookid = jsonObject.getInt("bookId");
                                            bookModel.bookname = jsonObject.getString("bookName");
                                            bookModel.bookNum = jsonObject.getString("bookNum");
                                            bookModel.bookType = jsonObject.getString("bookType");
                                            bookModel.introduction = jsonObject.getString("introduction");
                                            bookModel.bookStatus = jsonObject.getString("status");
                                            bookModel.coverurl = jsonObject.getString("url");
                                            mActiveDemandDatas.add(bookModel);
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
//                        if (load != null && load.isShowing())
//                            load.dismiss();
                    }

                    @Override
                    public void onResponse(Call call, Object o) {

                    }
                });
    }

    Handler show = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mBanner.setAutoPlayAble(mBannerDatas.size() > 1);
            tips.clear();
            for (int i = 0; i < mBannerDatas.size(); i++)
                tips.add("");
            mBanner.setData(mBannerDatas, tips);
            //hot
            int index = -1;
            hot_bookMsgView.setBookInfoModel(mHotDatas.get(++index));
            hot_wordsExplainView1.setBookInfoModel(mHotDatas.get(++index));
            hot_wordsExplainView2.setBookInfoModel(mHotDatas.get(++index));
            hot_wordsExplainView3.setBookInfoModel(mHotDatas.get(++index));
            hot_wordsExplainView4.setBookInfoModel(mHotDatas.get(++index));
            hot_wordsExplainView5.setBookInfoModel(mHotDatas.get(++index));
            hot_wordsExplainView6.setBookInfoModel(mHotDatas.get(++index));

            //book_first
            index = -1;
            bookCoverView1.setBookInfoModel(mBookFirstDatas.get(++index));
            bookCoverView2.setBookInfoModel(mBookFirstDatas.get(++index));
            bookCoverView3.setBookInfoModel(mBookFirstDatas.get(++index));
            first_wordsExplainView1.setBookInfoModel(mBookFirstDatas.get(++index));
            first_wordsExplainView2.setBookInfoModel(mBookFirstDatas.get(++index));
            first_wordsExplainView3.setBookInfoModel(mBookFirstDatas.get(++index));

            //Recommend
            index = -1;
            main_recommend1.setBookInfoModel(mRecommendDatas.get(++index));
            main_recommend2.setBookInfoModel(mRecommendDatas.get(++index));
            main_recommend3.setBookInfoModel(mRecommendDatas.get(++index));

            //activedemand
            index = -1;
            active_demand_BookCover_1.setBookInfoModel(mActiveDemandDatas.get(++index));
            active_demand_BookCover_2.setBookInfoModel(mActiveDemandDatas.get(++index));
            active_demand_BookCover_3.setBookInfoModel(mActiveDemandDatas.get(++index));
            active_demand_BookCover_4.setBookInfoModel(mActiveDemandDatas.get(++index));
            active_demand_BookCover_5.setBookInfoModel(mActiveDemandDatas.get(++index));
            active_demand_BookCover_6.setBookInfoModel(mActiveDemandDatas.get(++index));

//            if (load != null && load.isShowing())
//                load.dismiss();
        }
    };
}
