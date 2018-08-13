package com.example.capture.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capture.App.App;
import com.example.capture.Bean.BagBean;
import com.example.capture.Bean.IPPacket;
import com.example.capture.R;

import java.util.List;
public class SecondCaptureListAdpter  extends BaseAdapter{
    private List<BagBean> list;
    private LayoutInflater inflater;
    private Context context= App.getContext();
    private  int type;
    public SecondCaptureListAdpter(List<BagBean> list,int type) {
        this.type=type;
        this.list = list;
        list.size();
        this.inflater= LayoutInflater.from(context);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.fragment_capture_list_onclick_list_layout,null);
        TextView date= (TextView) view.findViewById(R.id.fragment_capture_list_onclick_list_layout_date);
        TextView text= (TextView) view.findViewById(R.id.fragment_capture_list_onclick_list_layout_text);
        BagBean bagBean= (BagBean) getItem(position);
        if (bagBean.getMode()>0){
            if (bagBean.getMode()==BagBean.CAPTURE_RECEIVED){
                date.setText("<---"+"\t"+bagBean.getDate());
                text.setTextColor(Color.RED);
            }else{
                date.setText("--->"+"\t"+bagBean.getDate());
                text.setTextColor(Color.BLUE);
            }
            if (type==1) {
                text.setText(bagBean.getConver16HexStr());
            }else{
                text.setText(bagBean.getReslutStr());
            }
        }else {
            text.setText(R.string.NoData);
        }
        return view;
    }
}
