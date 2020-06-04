package on.night.ui.login;

// Import statements
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import on.night.R;

/**
 * StartupSplashActivity starts a startup splash animation as the app loads up! Ball coming down
 */
public class StartupSplashActivity extends AppCompatActivity {

    // Final static Keys
    private static final int FROM_STARTUP_SPLASH = 0;

    // Splash Screen Animation
    private Animation splashTopAnimation;
    private ImageView splashImage;
    private static int SPLASH_SCREEN = 5000;

    /**
     * Creates the ball animation coming down upon opening the app.
     * @param savedInstanceState
     */
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

        // Starts a handler that will open the next login activity after 5000ms
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
