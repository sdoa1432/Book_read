package com.yuexun.book_read.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yuexun.book_read.R;


/**
 * Created by yuexun on 2018/5/16.
 */

public class TwoSwitvhButton extends LinearLayout {

    private View root;
    private Button left, right;
    private int type = 0;

    public TwoSwitvhButton(Context context) {
        super(context);
    }

    @SuppressLint("ResourceType")
    public TwoSwitvhButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.button_twoswitch, this);
        root = getRootView();
        left = (Button) findViewById(R.id.two_switch_left_button);
        right = (Button) findViewById(R.id.two_switch_right_button);
        left.setBackground(getResources().getDrawable(R.drawable.background_twoswitch_leftbutton_shape_red));
        left.setTextColor(R.color.white);
        right.setBackground(getResources().getDrawable(R.drawable.background_twoswitch_rightbutton_shape_white));
        right.setTextColor(R.color.red);
        setLeftText("男生");
        setRightText("女生");
        setBackground(R.drawable.background_twoswitch);
        setLeftListener(null);
        setRightListener(null);
    }

    public TwoSwitvhButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBackground(@ColorRes int resId) {
        root.setBackground(getResources().getDrawable(resId));
    }

    public void setRightListener(final Listener listener) {
        right.setOnClickListener(new OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                type = 1;
                left.setBackground(getResources().getDrawable(R.drawable.background_twoswitch_leftbutton_shape_white));
                left.setTextColor(R.color.red);
                right.setBackground(getResources().getDrawable(R.drawable.background_twoswitch_rightbutton_shape_red));
                right.setTextColor(R.color.white);
                if (listener != null)
                    listener.onClick(v);
            }
        });
    }

    public void setLeftListener(final Listener listener) {
        left.setOnClickListener(new OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                type = 0;
                left.setBackground(getResources().getDrawable(R.drawable.background_twoswitch_leftbutton_shape_red));
                left.setTextColor(R.color.white);
                right.setBackground(getResources().getDrawable(R.drawable.background_twoswitch_rightbutton_shape_white));
                right.setTextColor(R.color.red);
                if (listener != null)
                    listener.onClick(v);
            }
        });
    }

    public void setRightText(String s) {
        right.setText(s);
    }

    public void setLeftText(String s) {
        left.setText(s);
    }

    public int getType() {
        return type;
    }

    public interface Listener {
        void onClick(View v);
    }

}
