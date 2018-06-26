package com.yuexun.book_read.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.yuexun.book_read.R;
import com.yuexun.book_read.view.fragment.BookCaseFragment;
import com.yuexun.book_read.view.fragment.CenterFragment;
import com.yuexun.book_read.view.fragment.ChoicenessFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager show;
    private TabLayout select;
    private List<String> mTitleList;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ChoicenessFragment choicenessFragment;
    private BookCaseFragment bookCaseFragment;
    private CenterFragment centerFragment;
    private int pageindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = findViewById(R.id.main_viewpage);
        select = findViewById(R.id.main_tab);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (choicenessFragment == null)
            choicenessFragment = new ChoicenessFragment();
        if (bookCaseFragment == null)
            bookCaseFragment = new BookCaseFragment();
        if (centerFragment == null)
            centerFragment = new CenterFragment();

        if (!fragmentList.contains(choicenessFragment))
            fragmentList.add(choicenessFragment);
        if (!fragmentList.contains(bookCaseFragment))
            fragmentList.add(bookCaseFragment);
        if (!fragmentList.contains(centerFragment))
            fragmentList.add(centerFragment);

        show.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitleList.get(position);
            }
        });
        show.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageindex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        inittab();

    }

    @Override
    protected void onResume() {
        super.onResume();
        show.setCurrentItem(pageindex);
    }

    private void inittab() {
        mTitleList = new ArrayList<>();
        mTitleList.add("精选");
        mTitleList.add("书架");
        mTitleList.add("个人");
        //设置tablayout模式
        select.setLayoutMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        select.addTab(select.newTab().setText(mTitleList.get(0)));
        select.addTab(select.newTab().setText(mTitleList.get(1)));
        select.addTab(select.newTab().setText(mTitleList.get(2)));
        select.setSelectedTabIndicatorColor(getResources().getColor(R.color.red));
        select.setTabTextColors(getResources().getColor(R.color.gray), getResources().getColor(R.color.red));
        select.setupWithViewPager(show);
    }
}
