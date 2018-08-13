package com.example.capture.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.capture.Adapter.PacketListviewAdpter;
import com.example.capture.Bean.PerSettingInfo;
import com.example.capture.R;
import com.example.capture.Util.GetListUtil;
import com.example.capture.Util.SortListUtil;

import java.util.List;
/*
** 手机应用 Fragment
 */

public class PacketSetting extends Fragment {
    private ListView packet_lv;
    private Spinner sp;
    private List<PerSettingInfo> packetInfos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_packet_list, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        packet_lv= (ListView) view.findViewById(R.id.packet_lv);
        sp= (Spinner) view.findViewById(R.id.packet_list_layout_sort_sp);
        packetInfos=GetListUtil.getPacketInfoPerSettings();

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String SoreType = (String)sp.getItemAtPosition(position);    //选择的排序类型
                packetInfos=mysort(packetInfos,SoreType);
                packet_lv.setAdapter(new PacketListviewAdpter(packetInfos));
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
    public List<PerSettingInfo> mysort(List<PerSettingInfo> list, String str) {

        String methob="getPermission";
        boolean isasc = false;

        if (str.contains(getContext().getString(R.string.rise))) {
            isasc = true;
        } else if (str.contains(getContext().getString(R.string.drop))) {
            isasc =false ;
        }
        SortListUtil<PerSettingInfo> msList = new SortListUtil<PerSettingInfo>();
        msList.sortByMethod(list,methob,isasc);
        return list;
    }

}
