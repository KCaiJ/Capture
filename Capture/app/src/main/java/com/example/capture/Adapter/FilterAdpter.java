package com.example.capture.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.capture.App.App;
import com.example.capture.Bean.PerSettingInfo;
import com.example.capture.R;
import com.example.capture.Util.GetListUtil;
import com.example.capture.Util.PinyinUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * List过滤适配器
 */
public class FilterAdpter extends BaseAdapter implements Filterable {
    private LayoutInflater inflater;
    private MyFilter myFilter;
    private List<PerSettingInfo> Infos;
    private Context context = App.getContext();
    private ArrayList<PerSettingInfo> mOriginalValues;
    private final Object mLock = new Object();

    public FilterAdpter(List<PerSettingInfo> Infos) {
        this.Infos = Infos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Infos.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return Infos.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.fragment_capture_filter_list_layout, null);
        PerSettingInfo info= (PerSettingInfo) getItem(position);
        ImageView imageView= (ImageView) view.findViewById(R.id.fragment_capture_filter_list_layout_img);
        TextView AppName= (TextView) view.findViewById(R.id.fragment_capture_filter_list_layout_appName);
        imageView.setImageDrawable(info.getIcon());
        AppName.setText(info.getAppName());
        return view;
    }

    @Override
    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter();
        }
        return myFilter;
    }

    /**
     * 过滤类
     */
    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            // 持有过滤操作完成之后的数据。该数据包括过滤操作之后的数据的值以及数量。 count:数量 values包含过滤操作之后的数据的值
            FilterResults results = new FilterResults();
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    // 将list集合转换给这个原始数据的ArrayList
                    mOriginalValues = new ArrayList<PerSettingInfo>(Infos);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<PerSettingInfo> list = new ArrayList<PerSettingInfo>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                // 做正式的筛选
                String prefixString = prefix.toString().toLowerCase();
                // 声明一个临时的集合对象 将原始数据赋给这个临时变量
                final ArrayList<PerSettingInfo> values = mOriginalValues;
                final int count = values.size();

                // 新的集合对象
                final ArrayList<PerSettingInfo> newValues = new ArrayList<PerSettingInfo>(count);

                for (int i = 0; i < count; i++) {
                    // 如果姓名的前缀相符或者电话相符就添加到新的集合
                    final PerSettingInfo value = (PerSettingInfo) values.get(i);
                  //  Log.i("coder", "PinyinUtils.getAlpha(value.getUsername())"+ PinyinUtils.getFirstSpell(value.getAppName()));
                    if (PinyinUtils.getFirstSpell(value.getAppName()).startsWith(prefixString) ||value.getAppName().startsWith(prefixString)) {
                        newValues.add(value);
                    }
                }
                // 然后将这个新的集合数据赋给FilterResults对象
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,FilterResults results) {
            // 重新将与适配器相关联的List重赋值一下
            Infos = (List<PerSettingInfo>) results.values;
            GetListUtil.setFilterPacketInfo(Infos);
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}