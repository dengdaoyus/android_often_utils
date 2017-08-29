package com.sketchdemo.sketchdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final String ASSET_SCHEME = "file:///android_asset/";
    static final String FILE_SCHEME = "file:///";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mContext = MainActivity.this;
//
//        // String uri = "http://myimagetest.immouo.com/diary/pic/a77738c7-b979-4b70-b119-8f7f364dcde8.jpg";
//        String uri = "http://myimagetest.immouo.com/diary/pic/fb654c1f-6239-4ee0-a375-6f5344cee92f.jpg";
//        final SketchImageView sketchImageView = (SketchImageView) findViewById(R.id.sketchimage);
//
//        sketchImageView.setDisplayListener(new DisplayListener() {
//            @Override
//            public void onStarted() {
//
//            }
//
//            @Override
//            public void onError(ErrorCause errorCause) {
//
//            }
//
//            @Override
//            public void onCompleted(Drawable drawable, ImageFrom imageFrom, ImageAttrs imageAttrs) {
//
//
//                setParams(sketchImageView, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//                sketchImageView.setZoomEnabled(true);
//                sketchImageView.getImageZoomer().setReadMode(true);
//                sketchImageView.setBlockDisplayLargeImageEnabled(true);
//                sketchImageView.setZoomEnabled(false);
//            }
//
//
//            @Override
//            public void onCanceled(CancelCause cancelCause) {
//
//            }
//        });
//        sketchImageView.displayImage(uri);
//
//    }
//
//    public void setParams(View view, int widthPixel, int heightPixel) {
//        int width = DisplayUtils.getScreenWidth(mContext);
//        Log.e("size", "widthPixel:" + widthPixel);
//        Log.e("size", "heightPixel:" + heightPixel);
//        Log.e("size", "width   " + width + "    height  " + (int) (width / (widthPixel / (float) heightPixel)));
//        int height = (int) (width / (widthPixel / (float) heightPixel));
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
//        params.width = width;
//        params.height = height;
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();

        Intent intent = getIntent();
        //获得Intent的Action
        String action = intent.getAction();
        //获得Intent的MIME type
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            //我们这里处理所有的文本类型
            if (type.startsWith("text/")) {
                //处理获取到的文本，这里我们用TextView显示
                handleSendText(intent);
            }
            //图片的MIME type有 image/png , image/jepg, image/gif 等，
            else if (type.startsWith("image/")) {
                //处理获取到图片，我们用ImageView显示
                handleSendImage(intent);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                //处理多张图片，我们用一个GridView来显示
                handleSendMultipleImages(intent);
            }
        }
    }


    /**
     * 用TextView显示文本
     * 可以打开一般的文本文件
     *
     * @param intent
     */
    private void handleSendText(Intent intent) {
        TextView textView = new TextView(this);

        //一般的文本处理，我们直接显示字符串
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            textView.setText(sharedText);
            Log.e("share","文本内容："+sharedText);
        }

        //文本文件处理，从Uri中获取输入流，然后将输入流转换成字符串
        Uri textUri =  intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (textUri != null) {
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(textUri);
                textView.setText(inputStream2Byte(inputStream));
                Log.e("share","文本内容："+inputStream2Byte(inputStream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //设置给Activity
       // setContentView(textView);
    }


    /**
     * 将输入流转换成字符串
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private String inputStream2Byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len = -1;

        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        bos.close();

        //指定编码格式为UIT-8
        return new String(bos.toByteArray(), "UTF-8");
    }


    /**
     * 用ImageView显示单张图片
     *
     * @param intent
     */
    private void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(imageUri);
            Log.e("share","单张图片："+imageUri);
           // setContentView(imageView);
        }
    }


    /**
     * 用GridView显示多张图片
     *
     * @param intent
     */
    private void handleSendMultipleImages(Intent intent) {
        final ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            GridView gridView = new GridView(this);
            //设置item的宽度
            gridView.setColumnWidth(130);
            //设置列为自动适应
            gridView.setNumColumns(GridView.AUTO_FIT);
            gridView.setAdapter(new GridAdapter(this, imageUris));

            //setContentView(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {

                    //点击GridView的item 可以分享图片给其他应用
                    //这里可以参考http://blog.csdn.net/xiaanming/article/details/9395991
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, imageUris.get(position));
                    intent.setType("image/*");
                    startActivity(Intent.createChooser(intent, "共享图片"));
                }
            });

        }
    }

    /**
     * 重写BaseAdapter
     *
     * @author xiaanming
     */
    public class GridAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<Uri> list;

        public GridAdapter(Context mContext, ArrayList<Uri> list) {
            this.list = list;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageURI(list.get(position));
            return imageView;
        }
    }


}
