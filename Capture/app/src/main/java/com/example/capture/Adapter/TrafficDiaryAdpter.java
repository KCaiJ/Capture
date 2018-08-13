package com.example.capture.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.xml.sax.helpers.ParserAdapter;

import java.util.List;

/**
 * ViewPager 滑动适配器
 */
public class TrafficDiaryAdpter extends FragmentStatePagerAdapter {
    private List<Fragment> list;

    public TrafficDiaryAdpter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        return  list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
