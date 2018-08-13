package com.example.capture.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.example.capture.Adapter.TrafficDiaryAdpter;
import com.example.capture.ChidrenFragment.TrafficAppFlow;
import com.example.capture.ChidrenFragment.TrafficChart;
import com.example.capture.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 流量日记 Fragment
 */
public class TrafficDiary extends Fragment{
    private ViewPager vp;
    private RadioGroup rg;
    private RadioButton ApplicationFlow;
    private RadioButton piechart;
    private RadioButton WIFINetwork;
    private RadioButton Network;
    private TextView title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_traffic_diary, container, false);
    }

    /*
    ** 初始化控件
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vp = (ViewPager) view.findViewById(R.id.vp);
        rg = (RadioGroup) view.findViewById(R.id.rg);
        ApplicationFlow = (RadioButton) view.findViewById(R.id.applicationFlow);
        piechart = (RadioButton) view.findViewById(R.id.piechart);
        title= (TextView) getActivity().findViewById(R.id.main_title);
        title.setText(getString(R.string.TrafficDiary));
        setData();
        setOnlick();
        ((RadioButton)rg.getChildAt(0)).setChecked(true);  //显示packet Fragment
    }
    /**
     * 监听ViewPager滑动 RadioGroup点击 事件
     */
    private void setOnlick() {
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton)rg.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.applicationFlow:
                        vp.setCurrentItem(0);
                        break;
                    case R.id.piechart:
                        vp.setCurrentItem(1);
                        break;
                }
            }
        });

    }

    /**
     * 配置适配器
     */
    private void setData() {
        List<Fragment> list = new ArrayList<>();
        list.add(new TrafficAppFlow());
        list.add(new TrafficChart());
        vp.setAdapter(new TrafficDiaryAdpter(getActivity().getSupportFragmentManager(),list));
    }

}
