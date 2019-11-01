package com.quyunshuo.googleadmob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class RewardedAdActivity extends AppCompatActivity implements View.OnClickListener {
    private RewardedAd mRewardedAd;
    private TextView mRequestAdTv;
    private TextView mGoldCoinTv;
    private TextView mShowAd;
    private RewardedAdLoadCallback adLoadCallback;
    private RewardedAdCallback adCallback;
    private int mGoldCoinCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded_ad);
        initView();
    }

    private void initView() {
        mRequestAdTv = findViewById(R.id.request_tv);
        mRequestAdTv.setOnClickListener(this);
        mGoldCoinTv = findViewById(R.id.gold_coin_tv);
        mGoldCoinTv.setText(mGoldCoinCount + "");
        mGoldCoinTv.setOnClickListener(this);
        mShowAd = findViewById(R.id.ad_show_tv);
        mShowAd.setOnClickListener(this);
    }

    /**
     * 初始化激励视频Ad
     */
    private void initRewardedAd() {
        //RewardedAd 是一次性对象。
        //这意味着，在展示激励广告后，就不能再用该对象加载另一个广告了。
        //要请求另一个激励广告，需要创建一个新的 RewardedAd 对象。
        if (mRewardedAd != null) {
            mRewardedAd = null;
        }
        mRewardedAd = new RewardedAd(this, "ca-app-pub-3940256099942544/5224354917");
        //请求回调
        adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                Toast.makeText(RewardedAdActivity.this, "广告已请求", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedAdFailedToLoad(int i) {
                // Ad failed to load.
                // 警告：强烈建议您不要尝试使用 onRewardedAdFailedToLoad() 方法加载新广告。
                // 如果您必须使用 onRewardedAdFailedToLoad() 加载广告，请务必限制广告加载重试次数，
                // 以免在网络连接受限等情况下广告请求连续失败。
            }
        };
        //请求广告
        mRewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    /**
     * 展示广告
     */
    private void showAd() {
        //展示回调
        adCallback = new RewardedAdCallback() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                //在用户因与广告互动而应获得奖励时，系统会调用此方法。
                //可通过 RewardItem 参数的 getType() 和 getAmount() 方法访问为广告单元配置的奖励详细信息。
                mGoldCoinCount = mGoldCoinCount + rewardItem.getAmount();
                mGoldCoinTv.setText(mGoldCoinCount+"");
                Log.d("MiYan", "onUserEarnedReward: "+mGoldCoinCount);
            }

            @Override
            public void onRewardedAdClosed() {
                //在用户点按“关闭”图标或使用“返回”按钮关闭激励广告时，系统会调用此方法。
                //如果应用暂停了音频输出或游戏循环，则非常适合使用此方法恢复这些活动。
                mRewardedAd = null;
            }

            @Override
            public void onRewardedAdFailedToShow(int i) {
                //广告显示失败时，系统会调用此方法
                Toast.makeText(RewardedAdActivity.this, "error:" + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedAdOpened() {
                //在广告开始展示并铺满设备屏幕时，系统会调用此方法。
            }
        };
        mRewardedAd.show(this, adCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_tv:
                initRewardedAd();
                break;
            case R.id.ad_show_tv:
                if (mRewardedAd == null) {
                    Toast.makeText(this, "未请求广告", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mRewardedAd.isLoaded()) {
                    showAd();
                } else {
                    Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
