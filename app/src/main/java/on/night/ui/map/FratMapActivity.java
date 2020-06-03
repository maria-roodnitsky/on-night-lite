package on.night.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import on.night.R;
import on.night.data.model.FratStructure;
import on.night.data.model.MapToJavascriptStructure;
import on.night.ui.frat.FratHomeActivity;
import on.night.ui.login.LoginActivity;

public class FratMapActivity extends AppCompatActivity{

    public static final String USER = "user";
    private static final int REQUEST_FROM_MAP = 1;
    public static final String GREEK_SPACE = "greekspace";
    private static final String TAG = "FratMapActivity";
    private GoogleMap map;
    private boolean isFratAdmin;
    private PlaceholderFragment placeholderFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);

        //Log.d("firebase", FirebaseDatabase.getInstance().getReference("Users").toString());

        // Add the fragment
        placeholderFragment = new PlaceholderFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    placeholderFragment).commit();
        }
        // Remove the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ///// Button for FratHomeView! //////
        Button fratButton = findViewById(R.id.frat_button);
        Bundle extras = getIntent().getExtras();
        isFratAdmin = extras.getBoolean(LoginActivity.USER_TYPE);

        if (isFratAdmin) {
            fratButton.setVisibility(View.VISIBLE);
        }




    }

    public void onSignOutClicked(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onFratClick(View v) {
        if (isFratAdmin) {
            Intent fratIntent = new Intent(FratMapActivity.this, FratHomeActivity.class);
            fratIntent.putExtra(GREEK_SPACE, getIntent().getStringExtra(LoginActivity.GREEK_SPACE));
            startActivityForResult(fratIntent, REQUEST_FROM_MAP);
        }
    }





    public static class PlaceholderFragment extends Fragment {
        // Cloud Storage
        private FirebaseStorage mStorage;
        private DatabaseReference mDatabaseReference;
        private ArrayList<FratStructure> mFratStructures;
        private File mLocalMap;
        private File mFinalMap;
        private MapToJavascriptStructure mapToJavascriptStructure;

        WebView myBrowser;

        public PlaceholderFragment() {

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            myBrowser = (WebView) rootView.findViewById(R.id.mybrowser);
            myBrowser.loadUrl("https://onnight-1403b.web.app/");


            myBrowser.getSettings().setJavaScriptEnabled(true);

            return rootView;
        }



    }






}
