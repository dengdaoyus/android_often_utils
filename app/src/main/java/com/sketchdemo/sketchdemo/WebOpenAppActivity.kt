package com.sketchdemo.sketchdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebResourceRequest
import kotlinx.android.synthetic.main.activity_webopenapp.*
import com.sketchdemo.sketchdemo.R.id.webView
import android.webkit.WebView
import android.webkit.WebViewClient


@SuppressLint("Registered")
/**
 * Created by Administrator on 2018/4/8 0008.
 */
class WebOpenAppActivity : AppCompatActivity() {
    val SCHEME_WTAI = "wtai://wp/"
    val SCHEME_WTAI_MC = "wtai://wp/mc;"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webopenapp)
        webView.loadUrl("file:///android_asset/webopenapp.html")

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (view?.isPrivateBrowsingEnabled!!) return false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if (request?.url?.path?.startsWith(SCHEME_WTAI)!!) {
//                        if (request.url?.path?.startsWith(SCHEME_WTAI_MC)!!) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(WebView.SCHEME_TEL + request!!.url?.path?.substring(SCHEME_WTAI_MC.length)))
                    startActivity(intent)
                    return true
//                        }
//
//                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

    }

    /**
     *


    1，系统默认应用
    Safari http://  
    maps http://maps.google.com  
    Phone tel://  
    SMS sms://  
    Mail mailto://  
    iBooks ibooks://  
    App Store itms-apps://itunes.apple.com  
    Music music://  
    Videos videos://  

    2，常用第三方软件
    QQ mqq://  
    微信 weixin://  
    腾讯微博 TencentWeibo://  
    淘宝 taobao://  
    支付宝 alipay://  
    微博 sinaweibo://  
    weico微博 weico://  
    QQ浏览器 mqqbrowser:// com.tencent.mttlite
    uc浏览器 dolphin:// com.dolphin.browser.iphone.chinese
    欧朋浏览器 ohttp:// com.oupeng.mini
    搜狗浏览器 SogouMSE:// com.sogou.SogouExplorerMobile
    百度地图 baidumap:// com.baidu.map
    Chrome googlechrome://  
    优酷 youku://  
    京东 openapp.jdmoble://  
    人人 renren://  
    美团 imeituan://  
    1号店 wccbyihaodian://  
    我查查 wcc://  
    有道词典 yddictproapp://  
    知乎 zhihu://  
    点评 dianping://  
    微盘 sinavdisk://  
    豆瓣fm doubanradio://  
    网易公开课 ntesopen://  
    名片全能王 camcard://  
    QQ音乐 qqmusic://  
    腾讯视频 tenvideo://  
    豆瓣电影 doubanmovie://  
    网易云音乐 orpheus://  
    网易新闻 newsapp://  
    网易应用 apper://  
    网易彩票 ntescaipiao://  
    有道云笔记 youdaonote://  
    多看 duokan-reader://  
    全国空气质量指数 dirtybeijing://  
    百度音乐 baidumusic://  
    下厨房 xcfapp://
     */


}