package com.sketchdemo.sketchdemo.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;


/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */

public abstract  class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateActivity(savedInstanceState);
    }

    protected void onCreateActivity(Bundle bundle) {
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        setTitle();
        initView();
        initData();
        setListener();
    }

    protected abstract int getLayoutID();

    protected abstract void setTitle();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setListener();

}
