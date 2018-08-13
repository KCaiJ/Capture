package com.example.capture.ChidrenFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.capture.Bean.TrafficInfo;
import com.example.capture.Chart.FlowBarChart;
import com.example.capture.Chart.FlowPieChart;
import com.example.capture.R;
import com.example.capture.Util.GetListUtil;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *流量饼状图 Fragmnet
 */
public class TrafficChart extends Fragment {
    private com.github.mikephil.charting.charts.PieChart pie;
    private TextView title;
    private HorizontalBarChart bar;
    private Spinner flow_sp,chart_sp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_traffic_diary_chidren_chart, container, false);
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pie= (com.github.mikephil.charting.charts.PieChart) view.findViewById(R.id.traffic_diary_pie);
        bar= (HorizontalBarChart) view.findViewById(R.id.traffic_diary_bar);
        title= (TextView) view.findViewById(R.id.piechart_title);
        chart_sp= (Spinner) view.findViewById(R.id.piechart_typeSp);
        flow_sp= (Spinner) view.findViewById(R.id.piechart_flowSp);
        chart_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String chartType = (String)chart_sp.getItemAtPosition(position);//图类型
               String flowType = (String) flow_sp.getSelectedItem();            //流量类型
               showChart(chartType,flowType);
           }
           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
        flow_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String flowType = (String)flow_sp.getItemAtPosition(position);    //流量类型
                String chartType = (String) chart_sp.getSelectedItem();            //图类型
                showChart(chartType,flowType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 图形数据和形状设置
     * @param chartType
     * @param flowType
     */
    private void showChart(String chartType,String flowType){
        pie.clear();
        bar.clear();
        DecimalFormat df = new DecimalFormat("#0.00");
        if (chartType.contains(getContext().getString(R.string.Columnar))){
           //柱状图
            bar.setVisibility(View.VISIBLE);
            pie.setVisibility(View.GONE);
            List<TrafficInfo> packs= GetListUtil.getPacketTrafficInfos();
            List<String> xValue=new ArrayList<>();
            List<Float> yValue=new ArrayList<>();
            List<BarEntry> entries =new ArrayList<>();
            String text="";
            if (flowType.contains(getContext().getString(R.string.Receive))){
                for (int i = 0; i <packs.size(); i++) {
                    TrafficInfo info=packs.get(i);
                    if (info.getRx()>0) {
                        yValue.add((float) (info.getRx()));
                        xValue.add(info.getAppNmae());
                    }
                }
                text= getContext().getString(R.string.RxFlow);
            }else{
                if (flowType.contains(getContext().getString(R.string.Send))){
                    for (int i = 0; i <packs.size(); i++) {
                        TrafficInfo info=packs.get(i);
                        if (info.getTx()>0) {
                            yValue.add((float) (info.getTx()));
                            xValue.add(info.getAppNmae());
                        }
                    }
                    text= getContext().getString(R.string.TxFlow);
                }else{
                    for (int i = 0; i <packs.size(); i++) {
                        TrafficInfo info=packs.get(i);
                        if (info.getTotalFlow()>0) {
                            xValue.add(info.getAppNmae());
                            yValue.add((float) (info.getTotalFlow()));
                        }
                    }
                    text= getContext().getString(R.string.TotalFlow);
                }
            }
            for (int i=0;i<yValue.size();i++)
                 entries.add(new BarEntry(i, yValue.get(i)));
            FlowBarChart bars=new FlowBarChart(bar,xValue,entries,text);
            bars.show();
            title.setText(flowType +getContext().getString(R.string.Spend)+ chartType);
        }else {
          //饼图
           List<PieEntry> entries =new ArrayList<>();
           pie.setVisibility(View.VISIBLE);
           bar.setVisibility(View.GONE);
            if (flowType.contains(getContext().getString(R.string.Receive))){
                for (TrafficInfo info:GetListUtil.getPacketTrafficInfos())
                    if (info.getRxf()>=0.3)
                        entries.add(new PieEntry(Float.valueOf(df.format(info.getRx())) , info.getAppNmae()));
            }else{
                if (flowType.contains(getContext().getString(R.string.Send))){
                    for (TrafficInfo info:GetListUtil.getPacketTrafficInfos())
                        if (info.getTxf()>=0.3)
                            entries.add(new PieEntry(Float.valueOf(df.format(info.getTx())) , info.getAppNmae()));
                }
                else{
                    for (TrafficInfo info:GetListUtil.getPacketTrafficInfos())
                        if (info.getTotalFlowf()>=0.3)
                          entries.add(new PieEntry(Float.valueOf(df.format(info.getTotalFlow())) , info.getAppNmae()));
                }
            }
            FlowPieChart pieChart=new FlowPieChart(pie,entries, flowType +getContext().getString(R.string.Spend)+ chartType);
            pieChart.show();
            title.setText( flowType +getContext().getString(R.string.Spend)+ chartType);
        }
    }
}
