package com.example.capture.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capture.App.App;
import com.example.capture.Bean.IPPacket;
import com.example.capture.R;
import com.example.capture.Util.EncodUtils;
import com.example.capture.Util.GetListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据包捕获显示ListView适配器
 */
public class CaptureListAdpter extends BaseAdapter{
    private ArrayList<IPPacket> list;
    private LayoutInflater inflater;
    private Context context= App.getContext();
    public CaptureListAdpter(ArrayList<IPPacket> list) {
        this.list = list;
        this.inflater= LayoutInflater.from(context);
    }

    public void setIPpacketList(ArrayList<IPPacket> list2) {
        if (list != null) {
            list = (ArrayList<IPPacket>) list2.clone();
            notifyDataSetChanged();
        }
    }
    public void clearIPpacketList() {
        if (list != null) {
            list.clear();
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list==null? 0 :list.size();
    }

    @Override
    public Object getItem(int position) {
        return list==null? null :list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list==null? -1 :position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view=inflater.inflate(R.layout.fragment_capture_list_layout,null);
        final IPPacket ipPacket=list.get(position);
        ImageView imageView= (ImageView) view.findViewById(R.id.fragmnet_capture_list_layout_img);
        TextView AppName= (TextView) view.findViewById(R.id.fragmnet_capture_list_layout_AppName);
        TextView IPandType= (TextView) view.findViewById(R.id.fragmnet_capture_list_layout_IPandType);
        TextView Date= (TextView) view.findViewById(R.id.fragmnet_capture_list_layout_date);
        TextView packetSize= (TextView) view.findViewById(R.id.fragmnet_capture_list_layout_packetSize);
        imageView.setImageDrawable(ipPacket.getPacketInfo().getIcon());
        AppName.setText(ipPacket.getPacketInfo().getAppName());
        IPandType.setText(ipPacket.getToIPAdd().toString().replace("/","")+":"+ipPacket.getDestinationPort()+"\t\t"+ipPacket.getPacketType());
        Date.setText(ipPacket.getDate());
        Map map= EncodUtils.getCompany(ipPacket.getSize());
        packetSize.setText(map.get("Num")+" "+map.get("Company"));
        return view;
    }
}
