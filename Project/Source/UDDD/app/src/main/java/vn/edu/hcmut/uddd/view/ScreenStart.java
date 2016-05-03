package vn.edu.hcmut.uddd.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import vn.edu.hcmut.uddd.R;

public class ScreenStart extends Activity {

        private TextView appName;
        private ImageView mImageView;
        private Animation mFadeIn;
        private Animation mFadeInScale;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.activity_screen_start);

            this.mImageView = (ImageView) findViewById(R.id.start_imageView_idic_logo);
            this.appName = (TextView) findViewById(R.id.start_textView_app_name);
            this.mFadeIn = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in);
            this.mFadeInScale = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in_scale);

            this.appName.setTypeface(Typeface.createFromAsset(getAssets(), "light.ttf"));
            this.mFadeIn.setDuration(500);
            this.mFadeInScale.setDuration(2000);

            this.mImageView.startAnimation(this.mFadeIn);
            this.mFadeIn.setAnimationListener(new FadeInAnimation());
            this.mFadeInScale.setAnimationListener(new FadeInScaleAnimation());
        }

        private class FadeInAnimation implements Animation.AnimationListener{

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mImageView.startAnimation(ScreenStart.this.mFadeInScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        }

        private class FadeInScaleAnimation implements Animation.AnimationListener{

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(ScreenStart.this, ScreenHome.class);
                ScreenStart.this.startActivity(intent);
                ScreenStart.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        }
    }
