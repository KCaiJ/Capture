package com.example.capture.Chart;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.capture.App.App;
import com.example.capture.R;
import com.example.capture.Util.EncodUtils;
import com.example.capture.Util.GetListUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

/**
 * 柱状图 FlowBarChart
 */
public class FlowBarChart {
    private List<BarEntry> entries; //y轴数据
    private List<String> xValue;    //x轴数据
    private String text;        //标题
    private BarChart barChart;
    private Context content= App.getContext();

    /**
     * 构造函数 获取要展示的数据
     * @param barChart
     * @param xValue
     * @param entries
     * @param text
     */
    public FlowBarChart(BarChart barChart, List<String> xValue, List<BarEntry> entries, String text) {
        this.barChart = barChart;
        this.entries = entries;
        this.text=text;
        this.xValue = xValue;
    }

    /**
     * 设置柱状图的具体展现参数
     */
    public  void show(){
        barChart.setTouchEnabled(false);
        barChart.setNoDataText(content.getString(R.string.ChartNoData));
        barChart.setNoDataTextColor(Color.RED);
        barChart.setBackgroundColor(Color.LTGRAY);
        barChart.getAxisRight().setEnabled(true);
        barChart.getAxisLeft().setEnabled(false);
        barChart.setBackgroundColor(Color.LTGRAY);
        barChart.animateXY(1000,2000);
        Description des=new Description();
        des.setText("");
        barChart.setDescription(des);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(xValue.size() , false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int i = (int) value % xValue.size();
                return xValue.get(i);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        YAxis rightAxis = barChart.getAxisRight();
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)value/1024/1024+"MB";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        barChart.setData(BarSetData());

    }

    /**
     * 数据设置
     * @return
     */
    private BarData BarSetData(){
        BarDataSet barDataSet = new BarDataSet(entries,text);
        int  red = (int) (Math.random()*155);
        int green = (int) (Math.random()*256);
        int  blue = (int) (Math.random()*256);
        barDataSet.setColor(Color.rgb(red,green,blue));
        ArrayList<IBarDataSet> threebardata = new ArrayList<>();
        threebardata.add(barDataSet);
        BarData bardata = new BarData(threebardata);
        bardata.setDrawValues(true);
        bardata.setValueTextSize(10f);
        bardata.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                Map vules= EncodUtils.getCompany((long) value);
                return vules.get("Num")+(String) vules.get("Company");
            }
        });
        return  bardata;
    }


}
