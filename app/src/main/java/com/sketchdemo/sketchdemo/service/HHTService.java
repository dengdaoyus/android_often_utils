//package com.sketchdemo.sketchdemo.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.os.RemoteException;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//
//
//public class HHTService extends Service {
//
//
//    public static final String TAG = "HHTService";
//    final RemoteCallbackList<ITaskCallback> mCallbacks = new RemoteCallbackList<ITaskCallback>();
//
//    public HHTService() {
//    }
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        Log.e(TAG,"onBind-----");
//        // TODO: Return the communication channel to the service.
////        throw new UnsupportedOperationException("Not yet implemented");
//        return  stub;
//
//    }
//    /**
//     * @param commandType
//     */
//    public void func(int commandType, final ITaskCallback callback) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                    try {
//                        callback.actionPerformed(10);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    public void disconnected(){
//
//        for (int i = 0; i <mCallbacks.getRegisteredCallbackCount();i ++){
//            try {
//                mCallbacks.getBroadcastItem(i).disconnected();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {
//        @Override
//        public void registerCallback(ITaskCallback cb) throws RemoteException {
//            if (cb != null){
//                mCallbacks.register(cb);
//            }
//        }
//
//        @Override
//        public void unregisterCallback(ITaskCallback cb) throws RemoteException {
//            if (cb != null) {
//                mCallbacks.unregister(cb);
//            }
//        }
//
//        @Override
//        public void func1(int commandType,ITaskCallback callback) throws RemoteException {
//            func(commandType,callback);
//        }
//    };
//}
//
//
