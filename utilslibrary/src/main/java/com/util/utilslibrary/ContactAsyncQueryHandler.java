package com.util.utilslibrary;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.util.utilslibrary.bean.ContactListBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 异步加载通讯录工具
 * Created by  on 17-4-12.
 */

public class ContactAsyncQueryHandler extends AsyncQueryHandler {

    private List<ContactListBean> listPhone = new ArrayList<>();
    private OnQueryListener mOnQueryListener;

    public ContactAsyncQueryHandler(ContentResolver cr, OnQueryListener listener) {
        super(cr);
        this.mOnQueryListener = listener;
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst(); // 游标移动到第一项
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                ContactListBean info = new ContactListBean();
                String name = cursor.getString(1);
                String number = cursor.getString(2);
                info.setPhoneName(name);
                info.setPhoneNumber(number);
                listPhone.add(info);
            }

        }
        if (mOnQueryListener != null) {
            mOnQueryListener.onQueryComplete(listPhone);
        }
        super.onQueryComplete(token, cookie, cursor);
    }

    public interface OnQueryListener {
        void onQueryComplete(List<ContactListBean> contactList);
    }
}
