package com.ddy.novatehttp.socket;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;


/**
 *
 * Created by Administrator on 2018/4/13 0013.
 */
public class ChatSocket {
    Socket socket;//套接字
    BufferedWriter writer = null;
    BufferedReader reader = null;

    // 建立连接
    @SuppressLint("StaticFieldLeak")
    public void connect(final String ip, final int port) {
        new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    socket = new Socket(ip, port);

                    // 建立连接后获得输出流
                    OutputStream outputStream = socket.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));

                    InputStream is = socket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is, "utf-8"));


                    publishProgress("@Sucess"); // 链接成功
                    try {
                        String line;
                        while ((line = reader.readLine()) != null) {//死循环
                            publishProgress("服务器说：" + line);
                        }
                    } catch (IOException e) {
                        Log.e("ChatSocket", "onProgressUpdate   " + e.getMessage());
                        publishProgress("@数据失败"); //
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    publishProgress("@Failure"); // 链接失败
                    e.printStackTrace();
                }


                // 监听服务器发来的数据

                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                Log.e("ChatSocket", "onProgressUpdate   " + values[0]);

            }
        }.execute();
    }

    public void send(String message) {
        try {
            writer.write(message + "\n");//必须加上换行
            writer.flush();
        } catch (IOException e) {
            Log.e("ChatSocket", "发送失败");
            e.printStackTrace();
        }
    }
}
