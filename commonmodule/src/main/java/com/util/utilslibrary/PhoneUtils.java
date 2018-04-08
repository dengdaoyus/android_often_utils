package com.util.utilslibrary;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.util.utilslibrary.bean.ContactListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作手机工具
 * Created by Administrator on 2017/5/3 0003.
 */

public class PhoneUtils {
    private final String TAG = getClass().getName();
    static PhoneUtils instance;

    public synchronized static PhoneUtils getInstance() {
        if (instance == null) {
            instance = new PhoneUtils();
        }

        return instance;
    }

    /**
     * 根据电话号码获取联系人姓名
     *
     * @param phoneNum
     * @return
     */
    public String getContactNameFromPhoneBook(Context context,String phoneNum) {
        String contactName = "";
        Cursor cursor =context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
                new String[]{phoneNum}, null);
        if (null != cursor && cursor.moveToFirst()) {
            contactName = cursor
                    .getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            cursor.close();
        }
        return contactName;

    }

    /**
     * 分页查询系统联系人信息
     *
     * @param pageSize      每页最大的数目
     * @param currentOffset 当前的偏移量
     * @return
     */
    public List<ContactListBean> getContactsByPage(Context context,int pageSize, int currentOffset) {
        List<ContactListBean> list = new ArrayList<>();
        Cursor cursor = null;
        //定义常量，节省重复引用的时间
        Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        String DISPLAY_NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

        //String contactId;
        String displayName;
        String displayPhone;
        //生成ContentResolver对象
        ContentResolver contentResolver =context.getContentResolver();
        Log.e("wangle ", "start   " + new java.util.Date(System.currentTimeMillis()));
        try {
            String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key"};
            // 获取手机联系人
            cursor = contentResolver.query(CONTENT_URI, projection, null, null, " sort_key COLLATE LOCALIZED asc limit " + pageSize + " offset " + currentOffset);
        } catch (Exception sort_key) {
            Log.e(TAG, "sort_key is error");
            try {
                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.DATA1, "phonebook_label"};
                cursor = contentResolver.query(CONTENT_URI, projection, null, null, " sort_key COLLATE LOCALIZED asc limit " + pageSize + " offset " + currentOffset);
            } catch (Exception phonebook_label) {
                Log.e(TAG, "phonebook_label is error");
            }
        }

        // 无联系人直接返回
        if (!cursor.moveToFirst()) {//moveToFirst定位到第一行
            return null;
        }

        do {

            displayName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
            displayPhone = cursor.getString(cursor.getColumnIndex(NUMBER));
            ContactListBean info = new ContactListBean();
            info.setPhoneName(displayName);
            info.setPhoneNumber(displayPhone);
            list.add(info);
        } while (cursor.moveToNext());
//        try{
//            String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                    ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key"};
//            cursor = MyApp.getInstance().getContentResolver().query(CONTENT_URI, projection, null, null, " sort_key COLLATE LOCALIZED asc limit " + pageSize + " offset " + currentOffset);
//        }catch (Exception sort_key){
//            Log.e(TAG,"sort_key is error");
//            try{
//                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.DATA1, "phonebook_label"};
//                cursor = MyApp.getInstance().getContentResolver().query(CONTENT_URI, projection, null, null, "phonebook_label COLLATE LOCALIZED asc limit " + pageSize + " offset " + currentOffset);
//            }catch (Exception phonebook_label){
//                Log.e(TAG,"phonebook_label is error");
//            }
//        }
//
//        if (cursor != null) {
//
//            while (cursor.moveToNext()) {
//                String contactName = cursor.getString(0);
//                String phoneNumber = cursor.getString(1);
//                PhoneInfo info = new PhoneInfo(contactName, phoneNumber);
//                lists.add(info);
//                info = null;
//            }
//            cursor.close();
//        }
        cursor.close();
        return list;
    }

    /**
     * 调用系统短信发送界面
     *
     * @param context
     * @param number
     */
    public void sendMsg(Context context, String number,String msg) {

        Uri smsToUri = Uri.parse("smsto:" + number);

        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

        intent.putExtra("sms_body", msg);

        context.startActivity(intent);
    }


}
