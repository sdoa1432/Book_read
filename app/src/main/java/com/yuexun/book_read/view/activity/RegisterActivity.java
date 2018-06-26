package com.yuexun.book_read.view.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText user_name;
    private EditText user_password;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_name = findViewById(R.id.user_name);
        user_password = findViewById(R.id.user_password);
        register = findViewById(R.id.user_submit);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = user_name.getEditableText().toString();
                String password = user_password.getEditableText().toString();
                register(name, password);
            }
        });
    }

    private void register(String name, String password) {
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_REGISTER)
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
                        Log.i("yc.zhang", jsonObject.toString());
                        if (jsonObject.optInt("code", 0) == 200)
                            handler.sendEmptyMessage(0);
                        else
                            handler.sendEmptyMessage(1);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e) {

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
            if (msg.what == 0) {
                Toast.makeText(MyApplication.getContext(), "注册成功，请登录", Toast.LENGTH_LONG).show();
                finish();
            } else if (msg.what == 1) {
                Toast.makeText(MyApplication.getContext(), "注册失败，请检查账号及密码后重试", Toast.LENGTH_LONG).show();
            }
        }
    };
}
