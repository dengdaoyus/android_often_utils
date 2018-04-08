package com.util.utilslibrary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Description:发送通知的工具类
 * Created by 禽兽先生
 * Created on 2017/10/12
 */

public class NotificationUtil {
    private static NotificationUtil sNotificationUtil;
    private static NotificationManager mNotificationManager;

    private NotificationUtil() {
    }

    public static NotificationUtil getInstance() {
        if (sNotificationUtil == null) {
            synchronized (NotificationUtil.class) {
                if (sNotificationUtil == null) {
                    sNotificationUtil = new NotificationUtil();
                }
            }
        }
        return sNotificationUtil;
    }

    /**
     * Description:发送通知
     * Date:2017/10/13
     *
     * @param id           指定一个id,可以通过这个 id 取消对应通知
     * @param notification 通知对象,通过 Builder.build() 方法获取
     */
    public void showNotification(int id, Notification notification) {
        if (mNotificationManager != null) {
            mNotificationManager.notify(id, notification);
        }
    }

    /**
     * Description:取消指定通知
     * Date:2017/10/13
     */
    public void cancalNotification(int id) {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(id);
        }
    }

    public static class Builder {
        private Context context;
        private int smallIconId = R.drawable.sample_footer_loading; //小图标
        private String title = "测试通知";  //通知标题
        private String text = "测试内容";   //通知内容
        private String tickerText = "测试通知来啦";   //通知提示文字
        private long when = System.currentTimeMillis(); //通知产生时间,显示在右上角
        private String info = "测试 info"; //info 消息,显示在时间下面
        private int defaults = Notification.DEFAULT_VIBRATE;    //通知提醒方式(振动,铃声等)
        private Intent intent = new Intent(Intent.ACTION_DIAL); //点击通知跳转意图

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setSmallIconId(int smallIconId) {
            this.smallIconId = smallIconId;
            return this;
        }

        public Builder setContentTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTicker(String tickerText) {
            this.tickerText = tickerText;
            return this;
        }

        public Builder setWhen(long when) {
            this.when = when;
            return this;
        }

        public Builder setInfo(String info) {
            this.info = info;
            return this;
        }

        public Builder setDefaults(int defaults) {
            this.defaults = defaults;
            return this;
        }

        public Builder setContentIntent(Intent intent) {
            this.intent = intent;
            return this;
        }

        public Notification build() {
            mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mNotificationCompatBuilder = new NotificationCompat.Builder(context);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotificationCompatBuilder
                    //设置通知小 icon
                    .setSmallIcon(smallIconId)
                    //设置通知标题
                    .setContentTitle(title)
                    //设置通知显示内容
                    .setContentText(text)
                    //设置通知提示文字(出现在通知栏,带上升动画效果的文字)
                    .setTicker(tickerText)
                    //通知产生的时间，会在通知信息右上角显示，一般是系统获取到的时间
                    .setWhen(when)
                    //通知 info,显示在时间下面
                    .setContentInfo(info)
//                    //设置该通知优先级
//                    .setPriority(Notification.PRIORITY_DEFAULT)
//                    //设置这个标志当用户单击面板就可以让通知将自动取消
//                    .setAutoCancel(true)
//                    //如果是 true 则表示它为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                    .setOngoing(false)
                    //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                    .setDefaults(defaults)
                    //设置点击通知跳转意图
                    .setContentIntent(resultPendingIntent);
            return mNotificationCompatBuilder.build();
        }
    }
}
