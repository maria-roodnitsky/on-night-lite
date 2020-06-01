package on.night.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import on.night.R;

public class TestMapActivity extends AppCompatActivity /*implements OnMapReadyCallback */{

    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);

        // Add the fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    new PlaceholderFragment()).commit();
        }
        // Remove the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



//        MapView mapView = findViewById(R.id.mapview);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);

//        WebView webView = findViewById(R.id.webview);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("https://www.google.com/maps/@38.8864259,-77.2896729,15z");

    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.1, -87.9)));
//
//    }
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        WebView webView = (WebView) findViewById(R.id.mywebview);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://maps.googleapis.com/maps/api/staticmap?
//                ll=36.97,%20-122&lci=bike&z=13&t=p&size=500x500&sensor=true");
//    }

    public static class PlaceholderFragment extends Fragment {
        WebView myBrowser;
        public PlaceholderFragment() {

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            myBrowser = (WebView) rootView.findViewById(R.id.mybrowser);
            myBrowser.loadUrl("file:///android_asset/map.html");
            myBrowser.getSettings().setJavaScriptEnabled(true);

            return rootView;
        }
    }
}
