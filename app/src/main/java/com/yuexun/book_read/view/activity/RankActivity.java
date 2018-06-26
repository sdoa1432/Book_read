package com.yuexun.book_read.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.yuexun.book_read.R;
import com.yuexun.book_read.view.fragment.ManRankFragment;
import com.yuexun.book_read.view.fragment.SynthesizeRankFragment;
import com.yuexun.book_read.view.fragment.WomanRankFragment;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ManRankFragment manRankFragment;
    private WomanRankFragment womanRankFragment;
    private SynthesizeRankFragment synthesizeRankFragment;
    private TabLayout tabLayout;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        viewPager = findViewById(R.id.rank_viewpage);
        tabLayout = findViewById(R.id.rank_tb);

        if (manRankFragment == null)
            manRankFragment = new ManRankFragment();
        if (womanRankFragment == null)
            womanRankFragment = new WomanRankFragment();
        if (synthesizeRankFragment == null)
            synthesizeRankFragment = new SynthesizeRankFragment();

        fragmentList.add(manRankFragment);
        fragmentList.add(womanRankFragment);
        fragmentList.add(synthesizeRankFragment);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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

        inittab();

    }

    private void inittab() {
        mTitleList = new ArrayList<>();
        mTitleList.add("男生榜");
        mTitleList.add("女生榜");
        mTitleList.add("综合榜");
        //设置tablayout模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red));
        tabLayout.setTabTextColors(getResources().getColor(R.color.gray), getResources().getColor(R.color.red));
        tabLayout.setupWithViewPager(viewPager);
    }
}
