package pos.jgeraldo.com.openflightsandroidsample.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.databinding.ActivitySplashBinding;

public class SplashActivity extends Activity implements Animation.AnimationListener{


    private ActivitySplashBinding binding;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_splash);

        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        Interpolator animInterpolator = new DecelerateInterpolator();
        logoAnim.setInterpolator(animInterpolator);
        logoAnim.setAnimationListener(this);
        binding.ivSplash.requestLayout();
        binding.ivSplash.setAnimation(logoAnim);
        logoAnim.start();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent i = new Intent(SplashActivity.this,
            MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}