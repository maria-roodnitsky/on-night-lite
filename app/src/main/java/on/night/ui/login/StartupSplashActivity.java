package on.night.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import on.night.R;

public class StartupSplashActivity extends AppCompatActivity {

    private static final int FROM_STARTUP_SPLASH = 0;

    // Splash Screen Animation
    private Animation splashTopAnimation;
    private ImageView splashImage;
    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_splash);
        // Remove the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Spalsh Screen animations
        splashTopAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_top_animation);
        splashImage = findViewById(R.id.splash_image);
        splashImage.setAnimation(splashTopAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartupSplashActivity.this, LoginActivity.class);
                startActivityForResult(intent, FROM_STARTUP_SPLASH);
                finish();
            }
        }, SPLASH_SCREEN);



    }
}
