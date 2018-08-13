package com.example.capture.Chart;

import android.content.Context;
import android.graphics.Color;

import com.example.capture.App.App;
import com.example.capture.R;
import com.example.capture.Util.EncodUtils;
import com.example.capture.Util.GetListUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 饼状图 FlowPieChart
 */
public class FlowPieChart {
    private List<PieEntry> entries;  //数据数组
    private String text;        //标题
    private PieChart pieChart;
    private Context content= App.getContext();

    /**
     * 构造函数
     * @param pieChart
     * @param entries
     * @param text
     */
    public FlowPieChart(PieChart pieChart, List<PieEntry> entries, String text){
        this.pieChart=pieChart;
        this.entries=entries;
        this.text=text;
    }

    /**
     * 图形设置
     */
    public void show(){
        pieChart.setExtraOffsets(20, 5, 20, 5);
        pieChart.setEntryLabelTextSize(11f);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setDrawEntryLabels(true);
        pieChart.setHoleRadius(40);
        pieChart.setTransparentCircleRadius(50);
        pieChart.setCenterTextSize(11f);
        Description description=new Description();
        description.setText("");
        pieChart.setBackgroundColor(Color.LTGRAY);
        pieChart.setNoDataText(content.getString(R.string.ChartNoData));
        pieChart.setNoDataTextColor(Color.RED);
        pieChart.setDescription(description);
        pieChart.setCenterText(text+content.getString(R.string.Prompt));
        pieChart.setData(getYData());
        pieChart.animateX(1400);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setWordWrapEnabled(true);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

    }

    /**
     * 数据设置
     * @return
     */
    private PieData getYData() {
        List<Integer> colos=new ArrayList<>();
        for(int i = 0 ; i <entries.size() ;i++){
            int  red = (int) (Math.random()*155);
            int green = (int) (Math.random()*256);
            int  blue = (int) (Math.random()*256);
            colos.add(Color.rgb(red,green,blue));
        }
        PieDataSet dataSet=new PieDataSet(entries,"");
        dataSet.setColors(colos);
        dataSet.setValueLinePart1OffsetPercentage(50.f);
        dataSet.setValueLinePart1Length(0.6f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                Map vules= EncodUtils.getCompany((long) value);
                return vules.get("Num")+(String) vules.get("Company");

            }
        });
        return  data;
    }


}
