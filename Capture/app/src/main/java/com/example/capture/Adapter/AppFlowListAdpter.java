package com.example.capture.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.capture.App.App;
import com.example.capture.Bean.TrafficInfo;
import com.example.capture.R;
import com.example.capture.Util.EncodUtils;
import com.example.capture.Util.GetListUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 获取手机应用花费的流量字节数ListView适配器
 */
public class AppFlowListAdpter extends BaseAdapter {
    private List<TrafficInfo> list;
    private LayoutInflater inflater;
    private Context context= App.getContext();
    public AppFlowListAdpter(List<TrafficInfo> list) {
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
        View view=inflater.inflate(R.layout.fragment_traffic_diary_chidren_appflow_list_layout,null);
        TrafficInfo info= (TrafficInfo) getItem(position);
        ImageView img= (ImageView) view.findViewById(R.id.traffic_list_img);
        TextView appName= (TextView) view.findViewById(R.id.traffic_diart_listview_appName);
        TextView RxFlow= (TextView) view.findViewById(R.id.traffic_diart_listview_RxFlow);
        ProgressBar RxProgress= (ProgressBar) view.findViewById(R.id.traffic_diart_listview_RxProgress);
        TextView TxFlow= (TextView) view.findViewById(R.id.traffic_diart_listview_TxFlow);
        ProgressBar TxProgress= (ProgressBar) view.findViewById(R.id.traffic_diart_listview_TxProgress);
        TextView TotalFlow= (TextView) view.findViewById(R.id.traffic_diart_listview_TotalFlow);
        ProgressBar TotalProgress= (ProgressBar) view.findViewById(R.id.traffic_diart_listview_TotalProgress);

        img.setImageDrawable(info.getInco());
        appName.setText(info.getAppNmae());

        DecimalFormat df = new DecimalFormat("#0.00");
        Map vules= EncodUtils.getCompany(info.getRx());
        RxFlow.setText(vules.get("Num")+(String) vules.get("Company")+"\t("+df.format(info.getRxf())+"%)");

        vules= EncodUtils.getCompany(info.getTx());

        TxFlow.setText(vules.get("Num")+(String) vules.get("Company")+"\t("+df.format(info.getTxf())+"%)");

        vules= EncodUtils.getCompany(info.getTotalFlow());
        TotalFlow.setText(vules.get("Num")+(String) vules.get("Company")+"\t("+df.format(info.getTotalFlowf())+"%)");

        RxProgress.setMax(100);
        TxProgress.setMax(100);
        TotalProgress.setMax(100);
        RxProgress.setProgress((int) info.getRxf());
        TxProgress.setProgress((int) info.getTxf());
        TotalProgress.setProgress((int) info.getTotalFlowf());
        return  view;
    }
}
