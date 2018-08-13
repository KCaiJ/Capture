package com.example.capture.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capture.App.App;
import com.example.capture.Bean.ListBean;
import com.example.capture.R;

import java.util.List;

/**
 * 主界面侧滑栏ListView 适配器
 */
public class MainListAdpter extends BaseAdapter {
    private List<ListBean> list;
    private LayoutInflater inflater;
    private Context context= App.getContext();

    public MainListAdpter(List<ListBean> list) {
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
        View view=inflater.inflate(R.layout.activity_drawer_list_layout,null);
        ListBean bean=list.get(position);
        ImageView img= (ImageView) view.findViewById(R.id.activity_list_ch_img);
        TextView title= (TextView) view.findViewById(R.id.activity_list_ch_title);
        img.setImageResource(bean.getImg());
        title.setText(bean.getTitle());
        return view;
    }
}
