package com.example.capture.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capture.App.App;
import com.example.capture.R;

import java.util.List;
import java.util.Map;

/**
 * 应用设置ListView适配器
 */
public class ActivitySettingListAdpter extends BaseAdapter{
    private List<Map> list;
    private LayoutInflater inflater;
    private Context context=App.getContext();

    public ActivitySettingListAdpter(List<Map> list) {
        this.list = list;
        this.inflater= LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return  list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.fragment_setting_list_layout,null);
        TextView explain= (TextView) view.findViewById(R.id.activity_setting_list_explain);
        TextView content= (TextView) view.findViewById(R.id.activity_setting_list_content);
        Map map=list.get(position);
        explain.setText(map.get("Explain").toString());
        content.setText(map.get("Content").toString());
        return view;
    }
}
