package com.example.capture.ChidrenFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.capture.Adapter.AppFlowListAdpter;
import com.example.capture.Bean.TrafficInfo;
import com.example.capture.R;
import com.example.capture.Util.GetListUtil;
import com.example.capture.Util.SortListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 流量统计 Fragment
 */
public class TrafficAppFlow extends Fragment {
    private ListView appFlow;
    private Spinner spinner;
    private List<TrafficInfo> packetTrafficInfos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_traffic_diary_chidren_appflow, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        packetTrafficInfos =new ArrayList<>();
        appFlow = (ListView) view.findViewById(R.id.appflow_lv);
        spinner= (Spinner) view.findViewById(R.id._traffic_diary_sort_sp);
        packetTrafficInfos = GetListUtil.getPacketTrafficInfos();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String SoreType = (String)spinner.getItemAtPosition(position);    //选择的排序类型
                packetTrafficInfos =mysort(packetTrafficInfos,SoreType);
                appFlow.setAdapter(new AppFlowListAdpter(packetTrafficInfos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 调用排序方法
     * @param list
     * @param str
     * @return
     */
    public List<TrafficInfo> mysort(List<TrafficInfo> list, String str) {
        String methob="getTotalFlow";
        boolean isasc = true;
        if (str.contains(getContext().getString(R.string.total)) ) {
            methob="getTotalFlow";
        } else if (str.contains(getContext().getString(R.string.Receive)) ) {
            methob="getRx";
        } else if (str.contains(getContext().getString(R.string.Send))) {
            methob="getTx";
        }
        if (str.contains(getContext().getString(R.string.rise))) {
            isasc = false;
        } else if (str.contains(getContext().getString(R.string.drop))) {
            isasc =true ;
        }
        SortListUtil<TrafficInfo> msList = new SortListUtil<TrafficInfo>();
        msList.sortByMethod(list,methob,isasc);
        return list;
    }

}
