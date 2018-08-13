package com.example.capture.ChidrenFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.capture.Adapter.SecondCaptureListAdpter;
import com.example.capture.Bean.BagBean;
import com.example.capture.R;

import java.util.List;

/**
 * 数据包详细信息对话框
 */
public class CaptureOnclick extends DialogFragment{
    private List<BagBean> beanList;
    private ListView listView;
    private ImageView backBtn;
    private Spinner sp;
    private CaptureOnclick myself;
    private  String appName;
    private TextView textView;
    public CaptureOnclick ( List<BagBean> beanList,String appName){
        myself=this;
        this.beanList=beanList;
        this.appName=appName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_capture_list_onclick_layout, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= (ListView) view.findViewById(R.id.fragmnet_capture_list_onclick_listview);
        backBtn= (ImageView) view.findViewById(R.id.fragmnet_capture_list_onclick_backBtn);
        textView= (TextView) view.findViewById(R.id.fragmnet_capture_list_onclick_title);
        sp= (Spinner) view.findViewById(R.id.fragmnet_capture_list_onclick_sp);
        final SecondCaptureListAdpter adpter=new SecondCaptureListAdpter(beanList,0);
        listView.setAdapter(adpter);
        textView.setText(appName);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myself.dismiss();
            }
        });
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adpter.setType(position);
                listView.setAdapter(adpter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
