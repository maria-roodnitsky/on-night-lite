package on.night.ui.map;

import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import on.night.R;

public class FratMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = FratMapActivity.class.getSimpleName();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve content view that renders the map.
        setContentView(R.layout.activity_frat_map);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//        myRef.setValue("Hello World!");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        // Get SupportMapFragment and register for the callback when map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customize the styling of the base map using a JSON object defined in a raw resource
            // file. This is completely preliminary until we can figure out how to add polygons
            // to the map.

            boolean success = googleMap.setMapStyle(MapStyleOptions
                    .loadRawResourceStyle(this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed womp:(");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        // Position the map's camera at Dartmouth College
        LatLng dartmouth = new LatLng(43.703620, -72.288612);
        mMap.addMarker(new MarkerOptions().position(dartmouth).title("Marker at Dartmouth"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dartmouth));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dartmouth, 17));
    }
}
