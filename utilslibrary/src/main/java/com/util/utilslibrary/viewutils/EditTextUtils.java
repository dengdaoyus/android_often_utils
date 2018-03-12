package com.util.utilslibrary.viewutils;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * EditTextUtils 工具类
 * Created by Administrator on 2017/1/19 0019.
 */

public class EditTextUtils {

    private static EditTextUtils editTextUtils;

    public static EditTextUtils getEditTextUtils() {
        return editTextUtils == null ? editTextUtils = new EditTextUtils() : editTextUtils;
    }

    //禁止EditText输入空格
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" "))
                    return "";
                else
                    return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    //禁止EditText输入特殊字符
    public static void setEditTextInhibitInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`\\[\\]/~￥（）——+|{}【】‘；：”“’#*]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    ///限制回车
    public static void setEditTextInputEbter(EditText editText) {
        //这里是限制回车！
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }

    //限制字数和空客
    public static void setEditTextInputNuamber(final EditText changeUserName, final int num,
                                               final Context context, final String type, final TextView textView) {
        changeUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = changeUserName.getText();
                if (type.equals("yes")) {
                    if (s.toString().contains(" ")) {
                        String[] str = s.toString().split(" ");
                        String str1 = "";
                        for (String aStr : str) {
                            str1 += aStr;
                        }
                        changeUserName.setText(str1);
                    }
                }

                int len = editable.length();
                if (textView != null) {
                    textView.setText(MessageFormat.format("{0}/{1}", len, num));
                }
                if (len > num) {
                    Toast.makeText(context, "输入最大为" + (num) + "个字哟！", Toast.LENGTH_SHORT).show();
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, num);
                    changeUserName.setText(newStr);
                    editable = changeUserName.getText();
                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //EditText竖直方向是否可以滚动
    public static boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;
        return scrollDifference != 0 && ((scrollY > 0) || (scrollY < scrollDifference - 1));
    }
}
