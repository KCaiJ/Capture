package com.example.capture.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.capture.App.App;
import com.example.capture.Bean.PerSettingInfo;
import com.example.capture.R;
import com.example.capture.SQL.SQL;

import java.util.List;

/**
 * 手机应用权限设置ListView适配器，获取手机应用的包名，应用名及图标
 */
public class PacketListviewAdpter extends BaseAdapter {
    private List<PerSettingInfo> list;
    private LayoutInflater inflater;
    private Context context=App.getContext();

    public PacketListviewAdpter(List<PerSettingInfo> list) {
        this.list = list;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return  list.size();
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
        View view=inflater.inflate(R.layout.fragment_packet_list_layout,null);
        final PerSettingInfo info= (PerSettingInfo) getItem(position);
        ImageView img= (ImageView) view.findViewById(R.id.packet_list_layout_img);
        TextView appName= (TextView) view.findViewById(R.id.appName);
        TextView packName= (TextView) view.findViewById(R.id.packetName);
        final Spinner permissions= (Spinner) view.findViewById(R.id. packet_list_layout_sp);
        img.setImageDrawable(info.getIcon());
        appName.setText(info.getAppName());
        packName.setText(info.getPacketName());
        permissions.setSelection(info.getPermission(),true);
        permissions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                info.setPermission(position);
                SQL sql= App.getPerDb();
                SQLiteDatabase db=sql.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", info.getAppName());
                values.put("per", position);
                db.replace("perSetting",null,values);
                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}
