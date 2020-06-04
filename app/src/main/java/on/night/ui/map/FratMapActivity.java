package on.night.ui.map;

// Import Statements
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;

import on.night.R;
import on.night.ui.frat.FratHomeActivity;
import on.night.ui.login.LoginActivity;

/**
 * FratMapActivity displays the map that is seen for both admin and normal users.
 */
public class FratMapActivity extends AppCompatActivity{

    // Final static requests, keys, and tags.
    private static final int REQUEST_FROM_MAP = 1;
    public static final String GREEK_SPACE = "greekspace";
    private static final String TAG = "FratMapActivity";

    // Map to display and boolean to check if frat admin.
    private GoogleMap map;
    private boolean isFratAdmin;
    private PlaceholderFragment placeholderFragment;

    /**
     * Creates the Map activity with the placeholder fragment.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);


        // Add the fragment
        placeholderFragment = new PlaceholderFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    placeholderFragment).commit();
        }

        // Remove the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Button for FratHomeView if a frat admin
        Button fratButton = findViewById(R.id.frat_button);
        Bundle extras = getIntent().getExtras();
        isFratAdmin = extras.getBoolean(LoginActivity.USER_TYPE);
        if (isFratAdmin) {
            fratButton.setVisibility(View.VISIBLE);
        }

    }

    /**
     * On click method for when sign out is clicked
     * @param v
     */
    public void onSignOutClicked(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * If an admin, on click for frat home activity
     * @param v
     */
    public void onFratClick(View v) {
        if (isFratAdmin) {
            Intent fratIntent = new Intent(FratMapActivity.this, FratHomeActivity.class);
            fratIntent.putExtra(GREEK_SPACE, getIntent().getStringExtra(LoginActivity.GREEK_SPACE));
            startActivityForResult(fratIntent, REQUEST_FROM_MAP);
        }
    }

    /**
     * Fragment class within the FratMapActivity to hold the WebView
     */
    public static class PlaceholderFragment extends Fragment {

        // Webview for our map
        WebView myBrowser;

        /**
         * Default empty constructor
         */
        public PlaceholderFragment() {

        }

        /**
         * Creates the view with our webview inside the fragment
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return
         */
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Sets javascript enabled for the map
            myBrowser = rootView.findViewById(R.id.mybrowser);
            myBrowser.loadUrl("https://onnight-1403b.web.app/");
            myBrowser.getSettings().setJavaScriptEnabled(true);

            return rootView;
        }
    }
}
