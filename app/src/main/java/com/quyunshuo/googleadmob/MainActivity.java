package com.quyunshuo.googleadmob;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mInterstitialTv;

    //InterstitialAd在Activity的整个生命周期中，可以使用一个对象来请求并显示多个插页式广告，因此只需构造一次即可。
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInterstitialAd();
        initView();
    }

    private void initView() {
        mInterstitialTv = findViewById(R.id.ad_Interstitial);
        mInterstitialTv.setOnClickListener(this);
    }

    /**
     * 初始化InterstitialAd
     */
    private void initInterstitialAd() {
        //构造插屏广告实例
        mInterstitialAd = new InterstitialAd(this);
        //设置广告ID 此处使用测试ID
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //图片式广告
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/8691691433");  //视频式广告
        //设置广告监听
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // 广告加载完成
                Toast.makeText(MainActivity.this, "InterstitialAd广告加载完成",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // 广告请求失败
                Toast.makeText(MainActivity.this, "广告请求失败 error:" + errorCode,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // 显示广告
            }

            @Override
            public void onAdClicked() {
                // 点击广告
                Toast.makeText(MainActivity.this, "onClick",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                // 用户离开应用程序后
                Log.d("MiYan", "onAdLeftApplication: 离开应用");
            }

            @Override
            public void onAdClosed() {
                // 广告关闭
                // 加载下一个广告
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        //请求广告
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ad_Interstitial:
                if (mInterstitialAd.isLoaded()) {
                    Toast.makeText(this, "InterstitialAd请求成功",
                            Toast.LENGTH_SHORT).show();
                    mInterstitialAd.show();
                } else {
                    Toast.makeText(this, "InterstitialAd请求失败",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在销毁时对广告进行处理
        if (mInterstitialAd != null) {
            mInterstitialAd.setAdListener(null);
            mInterstitialAd = null;
        }
    }
}
