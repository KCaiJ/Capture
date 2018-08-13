package com.example.capture.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.capture.Adapter.ActivitySettingListAdpter;
import com.example.capture.App.App;
import com.example.capture.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用设置Fragment
 */
public class ActivitySetting extends Fragment{
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= (ListView) view.findViewById(R.id.activity_setting_list);
        listView.setAdapter(new ActivitySettingListAdpter(getList()));
    }

    /**
     * 展示的数据
     * @return
     */
    private  List<Map> getList(){
        List<Map> list=new ArrayList<>();
        Map map=new HashMap();
        map.put("Explain",getContext().getString(R.string.Other));
        map.put("Content","LongJUN");
        list.add(map);
        Map map1=new HashMap();
        map1.put("Explain",getContext().getString(R.string.Edition));
        map1.put("Content","1.0.0");
        list.add(map1);

        Map map2=new HashMap();
        map2.put("Explain",getContext().getString(R.string.Language));
        if (App.getLanguage()) {
            map2.put("Content",getContext().getString(R.string.ch));
        }else{
            map2.put("Content",getContext().getString(R.string.en));
        }
        list.add(map2);
        return list;
    }

}
