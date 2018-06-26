package com.yuexun.book_read.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.control.SpfControl;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText user_name;
    private EditText user_password;
    private Button login;
    private TextView register;
    private ImageView iv_clean_phone;
    private ImageView clean_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_name = findViewById(R.id.user_name);
        user_password = findViewById(R.id.user_password);
        login = findViewById(R.id.user_login);
        register = findViewById(R.id.user_register);
        iv_clean_phone = findViewById(R.id.iv_clean_phone);
        clean_password = findViewById(R.id.clean_password);

        user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone.getVisibility() == View.GONE) {
                    iv_clean_phone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_phone.setVisibility(View.GONE);
                }
            }
        });

        user_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && clean_password.getVisibility() == View.GONE) {
                    clean_password.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    clean_password.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(MyApplication.getContext(), R.string.please_input_limit_pwd, Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    user_password.setSelection(s.length());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = user_name.getEditableText().toString();
                String password = user_password.getEditableText().toString();
                login(name, password);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyApplication.getContext(), RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    private void login(String name, String password) {
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_LOGIN)
                .addParams("username", name)
                .addParams("password", password)
                .tag(DataConstants.API_BOOK_CONTENT)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.optInt("code", 0) == 200) {
                            Log.i("yc.zhang", jsonObject.toString());
                            SpfControl.getInstance().putString(DataConstants.SPF_KEY_SESSION, jsonObject.getString("session"));
                            SpfControl.getInstance().putString(DataConstants.SPF_KEY_HEADERURL, jsonObject.getJSONObject("result").getString("header"));
                            SpfControl.getInstance().putString(DataConstants.SPF_KEY_USERNAME, jsonObject.getJSONObject("result").getString("username"));
                            handler.sendEmptyMessage(0);
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("yc.zhang", e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Object o) {

                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("yc.zhang", msg.what + "");
            if (msg.what == 0) {
                Toast.makeText(MyApplication.getContext(), "登录成功", Toast.LENGTH_LONG).show();
                finish();
            } else if (msg.what == 1) {
                Toast.makeText(MyApplication.getContext(), "登录失败，请检查账号及密码后重试", Toast.LENGTH_LONG).show();
            }
        }
    };
}
