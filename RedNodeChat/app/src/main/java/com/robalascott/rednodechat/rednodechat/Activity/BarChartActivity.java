package com.robalascott.rednodechat.rednodechat.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.robalascott.rednodechat.rednodechat.Db.DBHelper;
import com.robalascott.rednodechat.rednodechat.Db.DataType;
import com.robalascott.rednodechat.rednodechat.R;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class BarChartActivity extends AppCompatActivity {
    LineChart lineChart;
    int size = 10;
    private static final String ARGS_CHAT_NAME = "args_chat_name";
    /**
     * Open chat with name
     *
     * @param ctx
     * @param chatName
     */
    public static void open(Context ctx, String chatName) {
        ctx.startActivity(new Intent(ctx, BarChartActivity.class)
                .putExtra(ARGS_CHAT_NAME, chatName));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        lineChart = (LineChart) findViewById(R.id.linechart);
        ArrayList<DataType> list = new ArrayList<DataType>();
        list = getDBhelper(list);
     //   ArrayList<String> xAxe = new ArrayList<String>();
    ;
        ArrayList<Entry> yDevice2 = new ArrayList<Entry>();
        ArrayList<Entry> yDevice3 = new ArrayList<Entry>();
        ArrayList<Entry> yDevice4 = new ArrayList<Entry>();
        ArrayList<Entry> yDevice5 = new ArrayList<Entry>();
        ArrayList<Entry> yDevice6 = new ArrayList<Entry>();
        ArrayList<Entry> yDevice7 = new ArrayList<Entry>();
        float[]sizeList = new float[size];
        Arrays.fill(sizeList,0);
        Iterator itr = list.iterator();

        while(itr.hasNext()) {
            DataType data = (DataType) itr.next();
            if(data.getDevices()==2){
                float float1 =(float)data.getDiff();
                yDevice2.add(new Entry(sizeList[data.getDevices()]++,float1));
            }else if(data.getDevices()==3){
                float float1 =(float)data.getDiff();
                yDevice3.add(new Entry(sizeList[data.getDevices()]++,float1));
            }else if(data.getDevices()==4){
                float float1 =(float)data.getDiff();
                yDevice4.add(new Entry(sizeList[data.getDevices()]++,float1));
            }else if(data.getDevices()==5) {
                float float1 =(float)data.getDiff();
                yDevice5.add(new Entry(sizeList[data.getDevices()]++,float1));
            }else if(data.getDevices()==6) {
                float float1 =(float)data.getDiff();
                yDevice6.add(new Entry(sizeList[data.getDevices()]++,float1));
            }
        }
/*
        String[] xaxes = new String[xAxe.size()];
        Log.e("Test",String.valueOf(xAxe.size()));
        for(int i=0; i<xAxe.size();i++){
            xaxes[i] = xAxe.get(i).toString();
        }
*/
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
       ;
        this.setDeviceLines(yDevice2,2,lineDataSets,Color.BLACK);
        this.setDeviceLines(yDevice3,3,lineDataSets,Color.BLUE);
        this.setDeviceLines(yDevice4,4,lineDataSets,Color.RED);
        this.setDeviceLines(yDevice5,5,lineDataSets,Color.GREEN);
        this.setDeviceLines(yDevice6,6,lineDataSets,Color.LTGRAY);
        this.setDeviceLines(yDevice7,7,lineDataSets,Color.YELLOW);





        LineData data = new LineData(lineDataSets);
        lineChart.setData(data);




    }

    public ArrayList<DataType> getDBhelper( ArrayList<DataType> list){
        DBHelper mydb;
        String Dbase = "DBase";
        mydb = new DBHelper(getApplicationContext(),Dbase);
        list = mydb.getAllDiff(list);
        mydb.close();
        return list;
    }

    public void setDeviceLines(ArrayList<Entry> yDevice ,int place, ArrayList<ILineDataSet> lineDataSets,int color) {
        if(!yDevice.isEmpty()){
            LineDataSet lineDataSet1 = new LineDataSet(yDevice,Integer.toString(place));
            lineDataSet1.setDrawCircles(false);
            lineDataSet1.setColor(color);
            lineDataSets.add(lineDataSet1);
        }
    }
}
