package com.example.capture.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.capture.Activity.MainActivity;
import com.example.capture.App.App;
import com.example.capture.R;

import java.util.Locale;

/**
 * 语言设置
 */
public class Language extends DialogFragment {
    private RadioGroup radioGroup;
    private Button yesBtn,noBtn;
    private Locale locale=Locale.SIMPLIFIED_CHINESE;
    private Language self;
    private boolean what;
    public Language(){
        self=this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmeng_language, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroup=(RadioGroup)view.findViewById(R.id.fragmeng_language_rg);
        yesBtn=(Button)view.findViewById(R.id.fragmeng_language_YesBtn);
        noBtn=(Button)view.findViewById(R.id.fragmeng_language_NoBtn);
        if(App.getLanguage()){
            view.findViewById(R.id.fragmeng_language_ch).performClick();
        }else {
            view.findViewById(R.id.fragmeng_language_en).performClick();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.fragmeng_language_ch:
                        what=true;
                        break;
                    case R.id.fragmeng_language_en:
                        what=false;
                        break;
                }
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAppLanguage();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismiss();
            }
        });
    }
    /**
     * 更改应用语言
     */
    public void changeAppLanguage() {
        App.setLanguage(what);
        Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        dismiss();
    }
}
