package com.example.capture.Activity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capture.Adapter.MainListAdpter;
import com.example.capture.App.App;
import com.example.capture.Bean.ListBean;
import com.example.capture.Fragment.ActivitySetting;
import com.example.capture.Fragment.Language;
import com.example.capture.Fragment.PacketSetting;
import com.example.capture.Fragment.Capture;
import com.example.capture.Fragment.TrafficDiary;
import com.example.capture.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * MainAcitivity
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private DrawerLayout mdrawerLayout;
    private FragmentManager fragmentManager;
    private ListView listview;
    private LinearLayout drawer_left;
    private TextView title;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.setContext(this);
        initLanguage();
        init();
    }
    /**
     * 初始化控件
     */
    private void init() {
        drawerTop();
        title= (TextView) findViewById(R.id.main_title);
        title.setText(getString(R.string.perSetting));
        drawer_left= (LinearLayout) findViewById(R.id.drawer_left);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.drawer_content, new PacketSetting()).commit();
        listview = (ListView) findViewById(R.id.drawer_lv);
        listview.setAdapter(new MainListAdpter(getListBean()));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mdrawerLayout.closeDrawer(drawer_left);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (position)
                {
                    case 0:
                        fragmentTransaction.replace(R.id.drawer_content, new PacketSetting());
                        title.setText(getString(R.string.perSetting));
                        break;
                    case 1:
                        fragmentTransaction.replace(R.id.drawer_content, new Capture());
                        title.setText(getString(R.string.packetGrab));
                        break;
                    case 2:
                        fragmentTransaction.replace(R.id.drawer_content, new TrafficDiary());
                        title.setText(getString(R.string.TrafficDiary));
                        break;
                    case 3:
                        fragmentTransaction.replace(R.id.drawer_content, new ActivitySetting());
                        title.setText(getString(R.string.Setting));
                        break;
                    case 4:
                        new Language().show(getSupportFragmentManager(),"");
                        break;
                    case 5:
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(getString(R.string.Unlogin));
                        builder.setMessage(getString(R.string.Confirmation_of_withdrawal));
                        builder.setPositiveButton(getString(R.string.YesExit), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        });
                        builder.setNegativeButton(getString(R.string.NoExit),null);
                        builder.show();
                        break;
                }
                fragmentTransaction.commit();
            }
        });
    }

    /**
     * 实现drawerLayout和Toobar的联动
     */
    private void drawerTop() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(mtoolbar);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mdrawerLayout, mtoolbar, R.string.drawer_open, R.string.drawer_close);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToggle.syncState();
        mdrawerLayout.addDrawerListener(mToggle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 侧滑bean列表
     * @return
     */
    private List<ListBean> getListBean(){
        List<ListBean> beens=new ArrayList<>();
        String[] strs= new String[]{getString(R.string.perSetting), getString(R.string.packetGrab),getString(R.string.TrafficDiary),getString(R.string.Setting),getString(R.string.LanguageSet), getString(R.string.Unlogin)};
        int[] imgs=new int[]{R.mipmap.knight,R.mipmap.buhuo,R.mipmap.wifi,R.mipmap.settings,R.mipmap.language,R.mipmap.unlogin};
        for (int i=0;i<strs.length;i++){
            ListBean bean=new ListBean();
            bean.setTitle(strs[i]);
            bean.setImg(imgs[i]);
            beens.add(bean);
        }
        return  beens;
    }

    /**
     * 点击两次退出应用
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击两次退出应用
     */
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, getString(R.string.AgainExit), Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * 设置语言
     */
    public void initLanguage(){
        Resources resources=getResources();
        Configuration configuration=resources.getConfiguration();
        if(App.getLanguage()==true){
            configuration.locale= Locale.CHINESE;
        }else {
            configuration.locale=Locale.ENGLISH;
        }
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }

}
