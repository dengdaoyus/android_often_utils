//package com.sketchdemo.sketchdemo.service;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.IBinder;
//import android.os.RemoteException;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.sketchdemo.sketchdemo.R;
//import com.sketchdemo.sketchdemo.activity.BaseActivity;
//
///**
// * Created by Administrator on 2018/4/16 0016.
// */
//
//public class AidlTestActivity extends BaseActivity {
//    TextView textView;
//
//    @Override
//    protected int getLayoutID() {
//        return R.layout.activity_aidl;
//    }
//
//    @Override
//    protected void setTitle() {
//        textView = findViewById(R.id.textview);
//        Button button1 = findViewById(R.id.button);
//        Button button2 = findViewById(R.id.button2);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                //在5.0及以上版本必须要加上这个
//                intent.setPackage("service.hht.com.serviceapplication");
//                intent.setAction("com.hht.service");
//                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//                textView.setText("正在启动服务运行的服务！");
//            }
//        });
//
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (mService != null) {
//                        mService.func1(10, mCallback);
//                    }
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                    textView.setText("未检测到正在运行的服务！");
//                }
//            }
//        });
//
//
//    }
//
//    private ITaskCallback mCallback = new ITaskCallback.Stub() {
//        public void actionPerformed(int id) {
////            textView.setText("通过服务回调获取 id："+id);
//            Log.e("TAG", "通过服务回调获取 id：" + id);
//        }
//
//        public void disconnected() {
//
//        }
//    };
//
//    IMyAidlInterface mService;
//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            textView.setText("服务已连接！");
//            mService = IMyAidlInterface.Stub.asInterface(service);
//            try {
//                mService.registerCallback(mCallback);
//            } catch (RemoteException e) {
//
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            textView.setText("服务已断开！");
//            try {
//                mService.unregisterCallback(mCallback);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            mService = null;
//        }
//    };
//
//
//    @Override
//    protected void initView() {
//
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//    @Override
//    protected void setListener() {
//
//    }
//}
