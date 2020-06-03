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

    public void onRefreshClick(View v) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(placeholderFragment);
        ft.attach(placeholderFragment);
        ft.commit();
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


            // Storage
            mStorage = FirebaseStorage.getInstance();
            StorageReference mapReference = mStorage.getReference().child("map.html");
            mapReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    myBrowser.loadUrl(uri.toString());
                }
            });



            getUpdates();


            myBrowser.getSettings().setJavaScriptEnabled(true);

            return rootView;
        }

        /**
         * Downloads map.html from the cloud storage
         */
        public void downloadMap() throws IOException {
            mStorage = FirebaseStorage.getInstance();
            StorageReference mapReference = mStorage.getReference().child("templateMap.html");
            //            myBrowser.loadUrl("file:///android_asset/map.html");

            // Store in a local file
            mLocalMap = File.createTempFile("map", ".html");

            mapReference.getFile(mLocalMap).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, mLocalMap.getAbsolutePath());
                    // Create the Map Structure that will be turned to javascript
                    createMapStructure();
                    // Now make the new file
                    try {
                        createNewMap();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "download has f a i l e d!");
                }
            });


        }

        /**
         * Gets a list of FratStructures that need to be updated. This will be parsed into javascript
         * code that will be used to create the map.
         */
        public void getUpdates() {
            // Get the database
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("GreekSpaces");

            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Create our AsyncTaskLoader
                    DatabaseMapLoad databaseMapLoad = new DatabaseMapLoad(getContext(), dataSnapshot);
                    mFratStructures = databaseMapLoad.loadInBackground();
                    Log.d(TAG, mFratStructures.toString());
                    // Download the template map
                    try {
                        downloadMap();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void createMapStructure() {
            mapToJavascriptStructure = new MapToJavascriptStructure();
            for (FratStructure fratStructure : mFratStructures) {
                mapToJavascriptStructure.setValue(fratStructure.getFratNickname(), fratStructure.getOpenStatus());
            }
            Log.d(TAG, "MAPSTRUCTURE" + mapToJavascriptStructure.toString());
        }

        public void createNewMap() throws IOException {
            // Readers and Writers
            BufferedReader reader = null;
            BufferedWriter writer = null;

            try {
                mFinalMap = File.createTempFile("finalMap", ".html");
            } catch (IOException e) {
                Log.d(TAG, "Unable to create final map file");
            }
            try {
                reader = new BufferedReader(new FileReader(mLocalMap));
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Temporary file was not found!");
            }
            if (reader == null) {
                Log.d(TAG, "our reader is null");
            }
            try {
                writer = new BufferedWriter(new FileWriter(mFinalMap));
            } catch (IOException e) {
                Log.d(TAG, "Unable to create a Buffered Writer");
            }

            String line = "";
            // Now read html till you find the place to insert javascript
            while (!(line = reader.readLine()).equals("            // MAP CONSTRUCTION HERE")) {
                writer.write(line + "\n");
            }
            writer.write(line + "\n");

            // Insert the javascript
            writer.write(mapToJavascriptStructure.toString() + "\n");

            // Rest of the file
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }
            // Close the writer and reader
            reader.close();
            writer.close();

            // Upload to cloud storage
            Uri file = Uri.fromFile(mFinalMap);
            StorageReference mapReference =

        }

        private class DatabaseMapLoad extends AsyncTaskLoader<ArrayList<FratStructure>> {
            // DataSnapshot for obtaining the data from database
            DataSnapshot dataSnapshot;
            public DatabaseMapLoad(Context context, DataSnapshot dataSnapshot) {
                super(context);
                this.dataSnapshot = dataSnapshot;
            }

            @Nullable
            @Override
            public ArrayList<FratStructure> loadInBackground() {
                try {
                    Log.d(TAG, "" + dataSnapshot);
                    ArrayList<FratStructure> fratStructures = new ArrayList<>();
                    // Iterate through the frats,
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        // Grab our values to create a new FratStructure
                        String name = (String) snapshot.child("Name").getValue();
                        boolean open = (boolean) snapshot.child("Open").getValue();
                        String nickname = (String) snapshot.child("Nickname").getValue();
                        fratStructures.add(new FratStructure(name, open, nickname));
                    }
                    return fratStructures;
                } catch (Exception e) {
                    Log.d(TAG, "Exception has occurred: " + e);
                    e.printStackTrace();
                    return null;
                }
            }
        }





    }






}
