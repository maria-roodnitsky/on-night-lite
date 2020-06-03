package on.night.ui.frat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import on.night.R;
import on.night.data.model.FratStructure;
import on.night.ui.login.LoginActivity;
import on.night.ui.map.FratMapActivity;

public class FratHomeActivity extends AppCompatActivity {

    // Tag
    private final String TAG = "FratHomeActivity";

    // Database Variables
    private DatabaseReference mDatabaseReference;

    // Views
    private TextView mFratTitle;
    private Switch mOnSwitch;

    // Frat Structure
    private FratStructure mFratStructure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frat_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // TODO: Look into FCM.

        // Get our extras from intent
        Bundle extras = getIntent().getExtras();

        // Sets up database
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFratStructure = new FratStructure(extras.getString(FratMapActivity.GREEK_SPACE), false,
                extras.getString(FratMapActivity.GREEK_SPACE));

        // Grab views
        mFratTitle = findViewById(R.id.frat_title);
        mOnSwitch = findViewById(R.id.on_switch);


        loadCurrentStatus();
        // Frat Title and Current Status Update



        // On Switch Listener
        mOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if it is on, isChecked will be on, in that case, we want the database to know
                // that the toggle for this frat is now on!

                updateStatusDatabase(isChecked);
                Log.d(TAG, "we stored");

//                if (isChecked) {
//                    // TODO: Write to database that this is happening!
//
//
//
//
//
//                } else {
//                    // TODO: Write to database that we are off.
//                }
            }
        });



    }


    /**
     * Helper method to load the current open status of the Greek Space
     */
    private void loadCurrentStatus() {
        // Get the specific child of the database we want to work with. Get a single value event listener
        mDatabaseReference = mDatabaseReference.child("GreekSpaces").child(mFratStructure.getFratName());
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Use an AsyncTaskLoader to load from database
                DatabaseFratLoad databaseFratLoad = new DatabaseFratLoad(getApplicationContext(), dataSnapshot);
                mFratStructure = databaseFratLoad.loadInBackground();
                Log.d(TAG, mFratStructure.toString());

                // Now that mFratStructure has been updated, let us quickly update our switch.
                mOnSwitch.setChecked(mFratStructure.getOpenStatus());
                Log.d("nickname", mFratStructure.getFratNickname());
                mFratTitle.setText(mFratStructure.getFratNickname());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Helper method to update database
     * @param isChecked
     */
    private void updateStatusDatabase (boolean isChecked) {
        mFratStructure.setOpenStatus(isChecked);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GreekSpaces").child(mFratStructure.getFratName());

        // AsyncTaskLoader to store data
        new DatabaseFratStore(getApplicationContext()).loadInBackground();

    }

    /**
     * AsyncTaskLoader to separately get this data for loading the switch to the correct position
     */
    private class DatabaseFratLoad extends AsyncTaskLoader<FratStructure> {
        // DataSnapshot for obtaining the data from database
        DataSnapshot dataSnapshot;
        public DatabaseFratLoad(Context context, DataSnapshot dataSnapshot) {
            super(context);
            this.dataSnapshot = dataSnapshot;
        }

        @Nullable
        @Override
        public FratStructure loadInBackground() {
            try {
                Log.d(TAG, "" + dataSnapshot);
                // Grab our values to create a new FratStructure
                String name = (String) dataSnapshot.child("Name").getValue();
                boolean open = (boolean) dataSnapshot.child("Open").getValue();
                String nickname = (String) dataSnapshot.child("Nickname").getValue();

                return new FratStructure(name, open, nickname);
            } catch (Exception e) {
                Log.d(TAG, "Exception has occurred: " + e);
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * AsyncTaskLoader for storing Frat status in the database
     */
    private class DatabaseFratStore extends AsyncTaskLoader<Void> {
        public DatabaseFratStore(@NonNull Context context) {
            super(context);
        }

        @Nullable
        @Override
        public Void loadInBackground() {
            try {
                mDatabaseReference.child("Open").setValue(mFratStructure.getOpenStatus());
            } catch (Exception e) {
                Log.d(TAG, "Exception occured in storing in the database!");
            }
            return null;
        }
    }

}
