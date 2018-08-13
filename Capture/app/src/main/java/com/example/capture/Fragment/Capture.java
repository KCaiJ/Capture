package com.example.capture.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.capture.Adapter.CaptureListAdpter;
import com.example.capture.App.App;
import com.example.capture.Bean.IPPacket;
import com.example.capture.ChidrenFragment.CaptureOnclick;
import com.example.capture.ChidrenFragment.ListFilterable;
import com.example.capture.R;
import com.example.capture.Service.NetKnightService;
import com.example.capture.Util.AppNameBack;
import com.example.capture.Util.GetListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据包捕获Fragment
 */
public class Capture extends Fragment{
    private ImageView go,goone;
    private ListView listView;
    private  ArrayList<IPPacket>   list=new ArrayList<>();
    private  CaptureListAdpter adpter=new CaptureListAdpter(list) ;
    private  boolean isService=false;
    private  boolean isOne=false;
    private  String OneAppName="";
    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    list.clear();
                    list.addAll(GetListUtil.getIpPackets());
                    adpter.notifyDataSetChanged();
                    break;
            }
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_capture, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        App.setmHandler(mHandler);
        list.addAll(GetListUtil.getIpPackets());
        goone= (ImageView) view.findViewById(R.id.capture_goOneBtn);
        go= (ImageView) view.findViewById(R.id.capture_goBtn);
        listView= (ListView) view.findViewById(R.id.fragmnet_capture_list);
        listView.setAdapter(adpter);

        if (NetKnightService.isRunning){
            isService=true;
            go.setImageResource(R.mipmap.serstop);
            goone.setImageResource(R.mipmap.serstop);
            goone.setVisibility(View.INVISIBLE);
        }else{
            isService = false;
            go.setImageResource(R.mipmap.go);
            goone.setImageResource(R.mipmap.goone);
            go.setVisibility(View.VISIBLE);
            goone.setVisibility(View.VISIBLE);
        }

        /***
         * 选择拦截单一的程序包
         * */
        goone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isService){
                    isOne=true;
                    new ListFilterable(new AppNameBack() {
                        @Override
                        public void appName(String s) {
                            if (s!=null&&!s.equals("")){
                                OneAppName=s;
                                init();
                            }
                        }
                    }).show(getFragmentManager(),"");
                }else{
                    colseService();
                }
            }
        });

        /**
         * 选择拦截全部的程序包
         */
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isService){
                    isOne=false;
                    DialogShow();
                }else{
                    colseService();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (GetListUtil.getIpPackets().get(position)!=null&&GetListUtil.getIpPackets().get(position).getListBagBean()!=null){
                    CaptureOnclick captureOnclick=new CaptureOnclick(GetListUtil.getIpPackets().get(position).getListBagBean(),GetListUtil.getIpPackets().get(position).getPacketInfo().getAppName());
                    captureOnclick.show(getFragmentManager(),"");
                }
            }
        });
    }

    /**
     * 弹出提示框
     */
    private void DialogShow(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.Tip);
        builder.setNegativeButton("NO", null);
        builder.setPositiveButton("Yse", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                init();
            }
        });
        builder.setMessage(R.string.DialogMes);
        builder.show();
    }

    /**
     * 开启服务的权限请求
     */
    private void init() {
        Intent intent = VpnService.prepare(getContext());
        if (intent != null) {
            startActivityForResult(intent, 0);
        } else {
            onActivityResult(0,-1, null);
        }
    }
    /**
     * 开启Service
     * @param request
     * @param result
     * @param data
     */
    public void onActivityResult(int request, int result, Intent data) {
        if (result == -1) {
            Intent intent = new Intent(getActivity(), NetKnightService.class);
            if (isOne){
                if (OneAppName!=null&&!OneAppName.equals("")){
                    intent.putExtra("OneAppName", OneAppName);
                }
            }
            intent.putExtra("isOne", isOne);
            NetKnightService.isRunning = true;
            getActivity().startService(intent);
            isService=true;
            go.setImageResource(R.mipmap.serstop);
            goone.setImageResource(R.mipmap.serstop);
            goone.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * 停止Service
     */
    private void colseService(){
            NetKnightService.isRunning = false;
            isService = false;
            go.setImageResource(R.mipmap.go);
            goone.setImageResource(R.mipmap.goone);
            go.setVisibility(View.VISIBLE);
            goone.setVisibility(View.VISIBLE);
    }
}
