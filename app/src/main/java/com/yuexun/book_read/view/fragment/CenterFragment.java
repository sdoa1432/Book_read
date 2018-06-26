package com.yuexun.book_read.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.control.SpfControl;
import com.yuexun.book_read.view.activity.LoginActivity;

/**
 * Created by yuexun on 2018/6/12.
 */

public class CenterFragment extends Fragment {

    private RelativeLayout user;
    private ImageView user_img;
    private TextView user_name;
    private TextView exit;


    public CenterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_center, container, false);

        user = root.findViewById(R.id.user);
        user_img = root.findViewById(R.id.user_img);
        user_name = root.findViewById(R.id.user_name);
        exit = root.findViewById(R.id.exit);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_name.getText().toString().contains("未登录"))
                    startActivity(new Intent(MyApplication.getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SpfControl.getInstance().getString(DataConstants.SPF_KEY_USERNAME).equals("") ||
                        !SpfControl.getInstance().getString(DataConstants.SPF_KEY_HEADERURL).equals("") ||
                        !SpfControl.getInstance().getString(DataConstants.SPF_KEY_SESSION).equals("")) {
                    SpfControl.getInstance().putString(DataConstants.SPF_KEY_SESSION, "");
                    SpfControl.getInstance().putString(DataConstants.SPF_KEY_HEADERURL, "");
                    SpfControl.getInstance().putString(DataConstants.SPF_KEY_USERNAME, "");
                    user_name.setText("未登录");
                    user_img.setImageDrawable(getResources().getDrawable(R.mipmap.user));
                }
            }
        });

        if (!SpfControl.getInstance().getString(DataConstants.SPF_KEY_USERNAME).equals(""))
            user_name.setText(SpfControl.getInstance().getString(DataConstants.SPF_KEY_USERNAME));
        if (!SpfControl.getInstance().getString(DataConstants.SPF_KEY_HEADERURL).equals("")) {
            Glide.with(this)
                    .load(SpfControl.getInstance().getString(DataConstants.SPF_KEY_HEADERURL))
                    .error(R.mipmap.error)
                    .into(user_img);
        }

    }
}
