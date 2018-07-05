package com.yuexun.book_read.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;
import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.control.callback.OkHttpCallback;
import com.yuexun.book_read.model.BookModel;
import com.yuexun.book_read.utils.DisplayUtils;
import com.yuexun.book_read.utils.Utils;
import com.yuexun.book_read.view.adapter.BookMsgListAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class LibraryActivity extends AppCompatActivity {

    private TagFlowLayout /*type,*/ classify, updateStatus, property, sort, test;
    private Button result_previous, result_next;
    private TextView result_page;
    private ScrollView scrollView;
    private String[] typeValueList;
    //    private String[] typeNumList;
    private String[] classifyValueList;
    private String[] classifyNumList;
    private String[] updateStatusValueList;
    private String[] updateStatusNumList;
    private String[] propertyValueList;
    private String[] propertyNumList;
    private String[] sortValueList;
    private String[] sortNumList;
    private String[] testlist;
    private ListView librarylistview;
    private LayoutInflater mInflater;
    private List<BookModel> bookMsgModels;
    private BookMsgListAdapter bookMsgListAdapter;

    private int pageindex = 1;
    private int pagecount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mInflater = LayoutInflater.from(this);
//        type = findViewById(R.id.library_type);
        classify = findViewById(R.id.library_classify);
        updateStatus = findViewById(R.id.library_updatestatus);
        property = findViewById(R.id.library_property);
        sort = findViewById(R.id.library_sort);
        librarylistview = findViewById(R.id.lirary_list);
        result_next = findViewById(R.id.result_next);
        result_previous = findViewById(R.id.result_previous);
        result_page = findViewById(R.id.result_page);
        scrollView = findViewById(R.id.scrollView);
        test = findViewById(R.id.test);

        testlist = new String[]{"测试1", "测试2", "测试3"};
        TagAdapter<String> testAdapter = new TagAdapter<String>(testlist) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_library_tag, test, false);
                if (position == 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(DisplayUtils.dp2px(MyApplication.getContext(), 44), 0, 0, 0);
                    tv.setLayoutParams(params);
                }
                tv.setText(s);
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                ((TextView) view).setTextColor(getResources().getColor(R.color.red));

            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                ((TextView) view).setTextColor(getResources().getColor(R.color.gray));
            }
        };
        testAdapter.setSelectedList(0);
        test.setAdapter(testAdapter);

        result_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageindex < pagecount)
                    initLibrarylist(++pageindex);
            }
        });
        result_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageindex > 1)
                    initLibrarylist(--pageindex);
            }
        });

        inittips();

        bookMsgModels = new ArrayList<>();
    }

    Handler booklisthandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bookMsgListAdapter = new BookMsgListAdapter(bookMsgModels, MyApplication.getContext());
            Utils.setListHeight(librarylistview, bookMsgListAdapter);
            librarylistview.setAdapter(bookMsgListAdapter);
            result_page.setText(pageindex + "/" + pagecount);
            if (pageindex == pagecount) {
                result_next.setClickable(false);
            } else if (pageindex == 1) {
                result_previous.setClickable(false);
            } else {
                result_next.setClickable(true);
                result_previous.setClickable(true);
            }
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, 0);
                }
            });
        }
    };

    Handler showTips = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            TagAdapter<String> classifyAdapter = new TagAdapter<String>(classifyValueList) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.item_library_tag, test, false);
                    if (position == 0) {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(DisplayUtils.dp2px(MyApplication.getContext(), 44), 0, 0, 0);
                        tv.setLayoutParams(params);
                    }
                    tv.setText(s);
                    return tv;
                }

                @Override
                public void onSelected(int position, View view) {
                    super.onSelected(position, view);
                    ((TextView) view).setTextColor(getResources().getColor(R.color.red));
                }

                @Override
                public void unSelected(int position, View view) {
                    super.unSelected(position, view);
                    ((TextView) view).setTextColor(getResources().getColor(R.color.gray));
                }
            };
            classifyAdapter.setSelectedList(0);
            classify.setAdapter(classifyAdapter);
            classify.setOnTagClickListener(listener);

            TagAdapter<String> scheduleAdapter = new TagAdapter<String>(updateStatusValueList) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.tag_library, updateStatus, false);
                    tv.setText(s);
                    return tv;
                }

                @Override
                public void onSelected(int position, View view) {
                    super.onSelected(position, view);
                    ((TextView) view).setTextColor(getResources().getColor(R.color.red));
                }

                @Override
                public void unSelected(int position, View view) {
                    super.unSelected(position, view);
                    ((TextView) view).setTextColor(getResources().getColor(R.color.gray));
                }
            };
            scheduleAdapter.setSelectedList(0);
            updateStatus.setAdapter(scheduleAdapter);
            updateStatus.setOnTagClickListener(listener);

            TagAdapter<String> propertyAdapter = new TagAdapter<String>(propertyValueList) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.tag_library, property, false);
                    tv.setText(s);
                    return tv;
                }

                @Override
                public void onSelected(int position, View view) {
                    super.onSelected(position, view);
                    ((TextView) view).setTextColor(getResources().getColor(R.color.red));
                }

                @Override
                public void unSelected(int position, View view) {
                    super.unSelected(position, view);
                    ((TextView) view).setTextColor(getResources().getColor(R.color.gray));
                }
            };
            propertyAdapter.setSelectedList(0);
            property.setAdapter(propertyAdapter);
            property.setOnTagClickListener(listener);

            TagAdapter<String> sortAdapter = new TagAdapter<String>(sortValueList) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.tag_library, sort, false);
                    tv.setText(s);
                    return tv;
                }

                @Override
                public void onSelected(int position, View view) {
                    super.onSelected(position, view);
                    ((TextView) view).setTextColor(getResources().getColor(R.color.red));
                }

                @Override
                public void unSelected(int position, View view) {
                    super.unSelected(position, view);
                    ((TextView) view).setTextColor(getResources().getColor(R.color.gray));
                }
            };
            sortAdapter.setSelectedList(0);
            sort.setAdapter(sortAdapter);
            sort.setOnTagClickListener(listener);

            scrollView.scrollTo(0, 0);
            initLibrarylist(pageindex);
        }
    };

    private void inittips() {
        OkHttpUtils.get()
                .url(DataConstants.CONNECT_IP + DataConstants.API_LIBRARY_TITLE)
                .tag(DataConstants.API_LIBRARY_TITLE)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new OkHttpCallback() {
                    @Override
                    public Object disposeResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray classid = jsonObject.getJSONArray("classId");
                        classifyValueList = new String[classid.length()];
                        classifyNumList = new String[classid.length()];
                        for (int i = 0; i < classid.length(); i++) {
                            classifyValueList[i] = classid.getJSONObject(i).optString("name");
                            classifyNumList[i] = classid.getJSONObject(i).optString("classId");
                        }
//                        JSONArray ctype = jsonObject.getJSONArray("ctype");
//                        typeValueList = new String[ctype.length()];
//                        typeNumList = new String[ctype.length()];
//                        for (int i = 0; i < ctype.length(); i++) {
//                            typeValueList[i] = ctype.getJSONObject(i).optString("name");
//                            typeNumList[i] = ctype.getJSONObject(i).optString("ctype");
//                        }
                        JSONArray feeType = jsonObject.getJSONArray("feeType");
                        propertyValueList = new String[feeType.length()];
                        propertyNumList = new String[feeType.length()];
                        for (int i = 0; i < feeType.length(); i++) {
                            propertyValueList[i] = feeType.getJSONObject(i).optString("name");
                            propertyNumList[i] = feeType.getJSONObject(i).optString("feeType");
                        }
                        JSONArray sort = jsonObject.getJSONArray("sort");
                        sortValueList = new String[sort.length()];
                        sortNumList = new String[sort.length()];
                        for (int i = 0; i < sort.length(); i++) {
                            sortValueList[i] = sort.getJSONObject(i).optString("name");
                            sortNumList[i] = sort.getJSONObject(i).optString("sort");
                        }
                        JSONArray updateStatus = jsonObject.getJSONArray("updateStatus");
                        updateStatusValueList = new String[updateStatus.length()];
                        updateStatusNumList = new String[updateStatus.length()];
                        for (int i = 0; i < updateStatus.length(); i++) {
                            updateStatusValueList[i] = updateStatus.getJSONObject(i).optString("name");
                            updateStatusNumList[i] = updateStatus.getJSONObject(i).optString("updateStatus");
                        }
                        showTips.sendEmptyMessage(0);
                        return null;
                    }
                });
    }

    public void initLibrarylist(int page) {
        String classifyvalue = "";
        String updateStatusvalue = "";
        String propertyvalue = "";
        String sortvalue = "";
        if (classify.getSelectedList().iterator().hasNext())
            classifyvalue = classifyNumList[classify.getSelectedList().iterator().next()];
        if (updateStatus.getSelectedList().iterator().hasNext())
            updateStatusvalue = updateStatusNumList[updateStatus.getSelectedList().iterator().next()];
        if (property.getSelectedList().iterator().hasNext())
            propertyvalue = propertyNumList[property.getSelectedList().iterator().next()];
        if (sort.getSelectedList().iterator().hasNext())
            sortvalue = sortNumList[sort.getSelectedList().iterator().next()];
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_LIBRARY)
                .tag(DataConstants.API_LIBRARY)
                .addParams("ctype", "")
                .addParams("classId", classifyvalue)
                .addParams("updateStatus", updateStatusvalue)
                .addParams("feeType", propertyvalue)
                .addParams("sort", sortvalue)
                .addParams("pageIndex", page + "")
                .build()
                .connTimeOut(20 * 1000)
                .execute(new OkHttpCallback() {
                    @Override
                    public Object disposeResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        int code = jsonObject.optInt("code", 0);
                        if (code == 200) {
                            bookMsgModels.clear();
                            pageindex = jsonObject.optInt("page", 1);
                            pagecount = jsonObject.optInt("pages", 1);
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject model = data.getJSONObject(i);
                                BookModel bookModel = new BookModel();
                                bookModel.bookid = model.getInt("bookId");
                                bookModel.bookname = model.getString("bookName");
                                bookModel.bookNum = model.getString("bookNum");
                                bookModel.bookType = model.getString("bookType");
                                bookModel.introduction = model.getString("introduction");
                                bookModel.bookStatus = model.getString("status");
                                bookModel.coverurl = model.getString("url");
                                bookModel.bookupdate = model.getString("bookUpdate");
                                bookModel.updatetime = model.getString("updateTime");
                                bookModel.author = model.getString("author");
                                bookMsgModels.add(bookModel);
                            }
                            booklisthandler.sendEmptyMessage(0);
                        }
                        return null;
                    }
                });
    }

    TagFlowLayout.OnTagClickListener listener = new TagFlowLayout.OnTagClickListener() {
        @Override
        public boolean onTagClick(View view, int position, FlowLayout parent) {
            cilck.removeMessages(0);
            cilck.sendEmptyMessageDelayed(0, 500);
            return false;
        }
    };

    Handler cilck = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pageindex = 1;
            initLibrarylist(pageindex);
        }
    };
}
