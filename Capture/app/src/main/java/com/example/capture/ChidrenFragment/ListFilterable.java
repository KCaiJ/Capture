package com.example.capture.ChidrenFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.capture.Adapter.FilterAdpter;
import com.example.capture.R;
import com.example.capture.Util.AppNameBack;
import com.example.capture.Util.GetListUtil;

/**
 * 列表筛选Fragment
 */
public class ListFilterable extends DialogFragment{
    private EditText et_filter;
    private FilterAdpter adapter;
    private ListView lv_list;
    private Button btn;
    private  AppNameBack appNameBack;
    private ListFilterable self;

    public ListFilterable(AppNameBack appNameBack){
        self=this;
        this.appNameBack=appNameBack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_capture_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        GetListUtil.setFilterPacketInfo(GetListUtil.getPacketInfoPerSettings());
        super.onViewCreated(view, savedInstanceState);
        btn= (Button) view.findViewById(R.id.fragmnet_capture_filter_btn);
        et_filter = (EditText)view. findViewById(R.id.fragmnet_capture_filter_ed);
        lv_list = (ListView) view.findViewById(R.id.fragmnet_capture_filter_list);
        adapter = new FilterAdpter(GetListUtil.getPacketInfoPerSettings());
        lv_list.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_filter.getText().toString().equals("")&&et_filter.getText().toString()!=null){
                    appNameBack.appName(et_filter.getText().toString());
                    self.dismiss();
                }else{
                    Toast.makeText(getContext(), R.string.selectApp, Toast.LENGTH_SHORT).show();
                }
            }
        });

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   et_filter.setText(GetListUtil.getFilterPacketInfo().get(position).getAppName());
            }
        });


        et_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(et_filter.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
