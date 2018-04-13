package com.sketchdemo.sketchdemo;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ddy.novatehttp.socket.ChatSocket;
import com.sketchdemo.sketchdemo.activity.BaseActivity;

import butterknife.Bind;

/**
 * Created by Administrator on 2018/4/13 0013.
 */

public class SocketTestActivity extends BaseActivity {


    Button lianji;
    Button send;
    EditText et;
    ChatSocket chatSocket;
    @Override
    protected int getLayoutID() {
        return R.layout.activitysocket;
    }

    @Override
    protected void setTitle() {
        lianji=findViewById(R.id.lianji);
        send=findViewById(R.id.send);
        et=findViewById(R.id.et);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
         chatSocket = new ChatSocket();
    }

    @Override
    protected void setListener() {
        lianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatSocket.connect("127.0.0.1", 10086);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatSocket.send(et.getText().toString());
            }
        });
    }
}
