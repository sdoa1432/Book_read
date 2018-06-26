package com.yuexun.book_read.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.control.SpfControl;
import com.yuexun.book_read.control.callback.OkHttpCallback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;

import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private TagFlowLayout history, recommend;
    private Button change, delete;
    private ImageView search, back;
    private EditText search_edit;
    private String[] historylist;
    private Set<String> historysave;
    private String[] tips;
    private String[] showtips = new String[3];
    private LayoutInflater mInflater;
    private TagAdapter<String> recommendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mInflater = LayoutInflater.from(this);

        history = findViewById(R.id.search_history_flow);
        recommend = findViewById(R.id.search_recommend_flow);
        search = findViewById(R.id.search);
        change = findViewById(R.id.change);
        delete = findViewById(R.id.delete);
        back = findViewById(R.id.back);
        search_edit = findViewById(R.id.search_edit);

    }

    @Override
    protected void onStart() {
        super.onStart();

        initHistoryData();
        history.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                historysave.add(historylist[position]);
                SpfControl.getInstance().putStringSet(DataConstants.SPF_KEY_HISTORY, historysave);
                Intent intent = new Intent(MyApplication.getContext(), ResultActivity.class);
                intent.putExtra("keyword", historylist[position]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        });
        recommend.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                historysave.add(showtips[position]);
                SpfControl.getInstance().putStringSet(DataConstants.SPF_KEY_HISTORY, historysave);
                Intent intent = new Intent(MyApplication.getContext(), ResultActivity.class);
                intent.putExtra("keyword", showtips[position]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = search_edit.getText().toString();
                if (keyword.equals("")) {
                    Toast.makeText(MyApplication.getContext(),"输入为空，请输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                historysave.add(keyword);
                SpfControl.getInstance().putStringSet(DataConstants.SPF_KEY_HISTORY, historysave);
                Intent intent = new Intent(MyApplication.getContext(), ResultActivity.class);
                intent.putExtra("keyword", keyword);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settip();
                recommendAdapter = new TagAdapter<String>(showtips) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView tv = (TextView) mInflater.inflate(R.layout.flow_item, recommend, false);
                        tv.setText(s);
                        return tv;
                    }
                };
                recommend.setAdapter(recommendAdapter);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historysave.clear();
                SpfControl.getInstance().putStringSet(DataConstants.SPF_KEY_HISTORY, historysave);
                initHistoryData();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        downloadData();
    }

    private void initHistoryData() {
        historysave = SpfControl.getInstance().getStringSet(DataConstants.SPF_KEY_HISTORY);
        if (historysave != null) {
            historylist = new String[historysave.size()];
            historysave.toArray(historylist);
        } else {
            historylist = new String[0];
        }
        history.setAdapter(new TagAdapter<String>(historylist) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.flow_item, history, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    private void downloadData() {
        OkHttpUtils.get()
                .url(DataConstants.CONNECT_IP + DataConstants.API_SEARCH_HOT)
                .tag(DataConstants.API_SEARCH_HOT)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new OkHttpCallback() {
                    @Override
                    public Object disposeResponse(Response response) throws Exception {
                        String s = response.body().string();
//                        Log.i("yc.zhang", s);
                        JSONObject responsejson = new JSONObject(s);
                        int code = responsejson.optInt("code", 0);
//                        Log.i("yc.zhang", "code : " + code);
                        if (code == 200) {
                            JSONArray jsonArray = responsejson.getJSONArray("data");
                            tips = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tips[i] = jsonArray.getJSONObject(i).optString("name");
                            }
                            show.sendEmptyMessage(0);
                        }
                        return null;
                    }
                });
    }

    private int tipoffset;

    Handler show = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tipoffset = 0;
            settip();
            recommendAdapter = new TagAdapter<String>(showtips) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.flow_item, recommend, false);
                    tv.setText(s);
                    return tv;
                }
            };
            recommend.setAdapter(recommendAdapter);
        }
    };

    private void settip() {
        for (int x = 0; x < 3; x++) {
            if (x + tipoffset < tips.length) {
                showtips[x] = tips[x + tipoffset];
            } else if (x + tipoffset == tips.length) {
                showtips[x] = tips[0];
            } else if (x + tipoffset > tips.length) {
                showtips[x] = tips[1];
            }
        }
        if (3 + tipoffset < tips.length) {
            tipoffset += 3;
        } else if (3 + tipoffset == tips.length) {
            tipoffset = 0;
        } else if (2 + tipoffset == tips.length) {
            tipoffset = 1;
        } else if (1 + tipoffset == tips.length) {
            tipoffset = 2;
        }

    }

}
