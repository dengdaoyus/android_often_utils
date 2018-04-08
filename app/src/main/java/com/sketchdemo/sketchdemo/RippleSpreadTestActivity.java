package com.sketchdemo.sketchdemo;

import android.view.View;
import android.widget.Button;

import com.sketchdemo.sketchdemo.activity.BaseActivity;
import com.util.utilslibrary.widget.WhewView;


/**
 * 仿支付宝咻一咻
 * Created by Administrator on 2018/3/22 0022.
 */

public class RippleSpreadTestActivity extends BaseActivity {


    WhewView whanView;

    Button button;

    @Override
    protected int getLayoutID() {
        return R.layout.avtivity_ripplespread;
    }

    @Override
    protected void setTitle() {

    }

    @Override
    protected void initView() {
        whanView= (WhewView) findViewById(R.id.whenView);
        button= (Button) findViewById(R.id.button);
        if (!whanView.isStarting()) {
            whanView.start();
        }

    }

    @Override
    protected void initData() {


    }

    @Override
    protected void setListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!whanView.isStarting()) {
                    whanView.start();
                } else {
                    whanView.stop();
                }
            }
        });
    }
}
