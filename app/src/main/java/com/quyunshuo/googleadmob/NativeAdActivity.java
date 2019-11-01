package com.quyunshuo.googleadmob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

public class NativeAdActivity extends AppCompatActivity {
    private AdLoader mAdLoader;
    private FrameLayout mAdRoot;  //广告的容器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);
        initView();
        initNativeAd();
    }

    private void initView() {
        mAdRoot = findViewById(R.id.ad_root);
    }

    /**
     * 初始化NativeAd
     */
    private void initNativeAd() {
        //构建可加载统一原生广告的 AdLoader
        mAdLoader = new AdLoader
                .Builder(this, "ca-app-pub-3940256099942544/2247696110")
                //调用此方法会将 AdLoader 配置为请求统一原生广告。当广告成功加载后，会调用监听器对象的 onUnifiedNativeAdLoaded() 方法
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        displayUnifiedNativeAd(mAdRoot, unifiedNativeAd);
                    }
                })
                //AdListener 对象处理原生广告的方式与处理横幅广告和插页式广告的方式之间有一个重要的区别。
                //由于 AdLoader 自己就有因具体格式而异的监听器（即 UnifiedNativeAd.OnUnifiedNativeAdLoadedListener）
                //可在广告加载时使用，因此当原生广告成功加载时，并不会调用 AdListener 中的 onAdLoaded() 方法
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                                build())
                .build();
        //构建完 AdLoader 后，就可以使用它来加载广告了。加载广告有 loadAd() 和 loadAds() 两种方法。
        //loadAds() 方法目前仅适用于 AdMob 广告。对于参与中介的广告，请改为使用 loadAd()
        //loadAd()  方法为单个广告发送请求
        //loadAds() 方法为多个广告（最多 5 个）发送请求
        mAdLoader.loadAd(new AdRequest.Builder().build());
//        mAdLoader.loadAds(new AdRequest.Builder().build(), 3);

    }

    /**
     * 填充广告布局
     *
     * @param parent 广告容器
     * @param ad     广告
     */
    private void displayUnifiedNativeAd(ViewGroup parent, UnifiedNativeAd ad) {

        // Inflate a layout and add it to the parent ViewGroup.
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        UnifiedNativeAdView adView = (UnifiedNativeAdView) inflater
                .inflate(R.layout.ad_unified_nativa_layout, null);

        // Locate the view that will hold the headline, set its text, and call the
        // UnifiedNativeAdView's setHeadlineView method to register it.
        TextView headlineView = adView.findViewById(R.id.ad_title);
        headlineView.setText(ad.getHeadline());
        adView.setHeadlineView(headlineView);

        // Repeat the above process for the other assets in the UnifiedNativeAd
        // using additional view objects (Buttons, ImageViews, etc).


        // If the app is using a MediaView, it should be
        // instantiated and passed to setMediaView. This view is a little different
        // in that the asset is populated automatically, so there's one less step.
        MediaView mediaView = (MediaView) adView.findViewById(R.id.ad_img);
        adView.setMediaView(mediaView);

        // Call the UnifiedNativeAdView's setNativeAd method to register the
        // NativeAdObject.
        adView.setNativeAd(ad);

        // Ensure that the parent view doesn't already contain an ad view.
        parent.removeAllViews();

        // Place the AdView into the parent.
        parent.addView(adView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
