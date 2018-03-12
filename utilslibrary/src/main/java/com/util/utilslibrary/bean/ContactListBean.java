package com.util.utilslibrary.bean;

/**
 * 手机通讯录bean
 * Created by Administrator on 2017/5/3 0003.
 */
public class ContactListBean {
    private String phoneName;
    private String phoneNumber;

    public static ContactListBean getInstance() {
        return SingleInstanceHolder.sInstance;
    }

    private static class SingleInstanceHolder {

        private static ContactListBean sInstance = new ContactListBean();
    }


    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}