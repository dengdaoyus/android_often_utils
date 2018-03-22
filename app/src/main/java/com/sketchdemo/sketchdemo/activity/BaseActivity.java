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
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        onCreateActivity(savedInstanceState);
    }

    protected void onCreateActivity(Bundle bundle) {
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        setTitle();
        initData();
        setListener();
    }

    protected abstract int getLayoutID();

    protected abstract void setTitle();

    protected abstract void initData();

    protected abstract void setListener();

}
