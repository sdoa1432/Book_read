package com.yuexun.book_read.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.control.DataService;
import com.yuexun.book_read.control.SpfControl;
import com.yuexun.book_read.control.event.BookCaseAcquireEvent;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.utils.Utils;
import com.yuexun.book_read.view.adapter.BookCaseListAdapter;
import com.yuexun.book_read.view.custom.BookCaseView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.yuexun.book_read.control.DataService.bookcaseflag;

public class BookCaseFragment extends Fragment {

    private ListView listView;
    private List<BookModel> bookcaseModels = new ArrayList<>();
    private int pageindex = 1;
    private int pagecount = 1;
    private BookCaseListAdapter bookCaseListAdapter;
    private ScrollView scrollView;
    private Button edit;

    public BookCaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_book_case, container, false);

        listView = root.findViewById(R.id.book_case);
        scrollView = root.findViewById(R.id.book_case_scroll);
        edit = root.findViewById(R.id.book_case_edit);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getBookCaseData();
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    private boolean oneflag = true;

    public void getBookCaseData() {
        if (SpfControl.getInstance().getString(DataConstants.SPF_KEY_SESSION).equals("")) {
            //show empty
            if (oneflag) {
                Toast.makeText(this.getContext(), "您未登录，请登录后再试", Toast.LENGTH_LONG).show();
                oneflag = false;
            }
        } else {
            //get net data
            setAdapter(false);
            getbookcasenetdata(pageindex);
        }
    }

    private void getbookcasenetdata(int index) {
        Log.i("yc.zhang", "getbookcasenetdata for " + index);
        Intent intent = new Intent(MyApplication.getContext(), DataService.class);
        intent.putExtra("pageindex", index);
        MyApplication.getContext().startService(intent);
    }

    @Subscribe
    public void onEvent(BookCaseAcquireEvent event) {
        Log.i("yc.zhang", "get  BookCaseAcquireEvent ");
        pageindex = event.pageindex;
        pagecount = event.pagecount;
        handler.sendEmptyMessage(event.acquireFlag);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                edit.setVisibility(View.VISIBLE);
                setAdapter(false);
                Toast.makeText(MyApplication.getContext(), "获取书架成功", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                edit.setVisibility(View.INVISIBLE);
                Toast.makeText(MyApplication.getContext(), "书架为空", Toast.LENGTH_LONG).show();
            } else if (msg.what == 2) {
                edit.setVisibility(View.INVISIBLE);
                Toast.makeText(MyApplication.getContext(), "获取书架失败，请检查网络后重新获取", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void setAdapter(boolean flag) {
        bookcaseModels = DataService.bookcaseModels;
        bookCaseListAdapter = new BookCaseListAdapter(bookcaseModels, MyApplication.getContext(), flag);
        bookCaseListAdapter.setBackListener(BACK);
        Utils.setListHeight(listView, bookCaseListAdapter);
        listView.setAdapter(bookCaseListAdapter);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
        if (flag) {
            edit.setText("退出编辑");
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAdapter(false);
                }
            });
        } else {
            edit.setText("编辑");
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAdapter(true);
                }
            });
        }
    }

    BookCaseView.DelListener BACK = new BookCaseView.DelListener() {
        @Override
        public void onDelSuccee() {
            del.sendEmptyMessage(0);
        }

        @Override
        public void onDelDefeat() {
            del.sendEmptyMessage(1);
        }
    };


    Handler del = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                getbookcasenetdata(1);
                Toast.makeText(MyApplication.getContext(), "删除成功", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                Toast.makeText(MyApplication.getContext(), "删除失败，请稍后重试", Toast.LENGTH_LONG).show();
                setAdapter(false);
            }
        }
    };
}
