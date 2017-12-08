package test.hk.com.KkDriving.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import test.hk.com.KkDriving.R;


public class SpashActivity extends Activity {


    private static final int ANIMATION_DURATION = 2000;
    private static final float SCALE_END = 1.13F;



    ImageView mIvSplash;


    TextView mSplashAppName;

    TextView Version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spashl);

        mSplashAppName = (TextView) findViewById(R.id.splash_app_name);
        Version = (TextView) findViewById(R.id.Version);
        mIvSplash = (ImageView) findViewById(R.id.iv_splash);
        Version.setText("V"+getVersion());
        animateImage();
    }

    private void animateImage() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mIvSplash, "scaleX", 0.5f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mIvSplash, "scaleY", 0.5f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DURATION);
        set.play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity( new Intent(SpashActivity.this,SelectActivity.class));
                SpashActivity.this.finish();
            }
        });
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager =SpashActivity.this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(SpashActivity.this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
