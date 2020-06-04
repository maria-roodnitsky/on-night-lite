package on.night.ui.login;

// Import Statements
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import on.night.R;
import on.night.ui.map.FratMapActivity;

/**
 * LoginActivity Class that comes up after the startup splash screen animation.
 * Presented with a login button and login animation
 */
public class LoginActivity extends AppCompatActivity {

    // final static requests, tags, and variables
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1;
    private static final int REQUEST_LOGIN = 2;
    public static final String USER_TYPE = "usertype";
    public static final String GREEK_SPACE = "greekspace";

    // Animation Drawables for cup and button
    private AnimationDrawable buttonAnimation;
    private AnimationDrawable mAnimationDrawable;

    // Firebase authentication
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    /**
     * Creates the view for the Login Activity
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "entetered onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Remove the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Configure sign-in to request user's ID, email address, and basic profile.
        // ID and basic profile are included in Defualt sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build GoogleSignInClient with options specified by GSO.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();


        // Login Button and animation
        final Button loginButton = findViewById(R.id.login);
        buttonAnimation = (AnimationDrawable) loginButton.getBackground();
        buttonAnimation.setEnterFadeDuration(1700);
        buttonAnimation.setExitFadeDuration(1700);

        // Set up cup animation!
        ImageView cupImage = findViewById(R.id.imageView3);
        cupImage.setBackgroundResource(R.drawable.pong_login);
        mAnimationDrawable = (AnimationDrawable) cupImage.getBackground();
    }

    /**
     * On stop method when the login activity stops. Checks for lifecycle
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "entetered onStopWomp()");
    }

    /**
     * Destroys the activity after it is stopped.
     */
    @Override
    protected void onDestroy() {
        Log.d(TAG, "entetered onDestroyWomp()");
        super.onDestroy();
    }

    /**
     * Restarts the activity in the lifecycle
     */
    @Override
    protected void onRestart() {
        Log.d(TAG, "entetered onRestart()");
        super.onRestart();
    }

    /**
     * Checks if user already logged in and if so, we don't go to the login screen.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "entetered onStart()");
        // We check if a user is already singed in and update the UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUiWithUser(currentUser);
    }

    /**
     * On activity result that comes from signing in from google sign in popup
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "entetered onActivityResult()");

        // Result returned from launching the intent from googleSignInClient.getSignInIntent
        if (requestCode == RC_SIGN_IN) {
            // Task returned from call is always completed, no need for a listener. Interesting...
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Gooogle Sign in successful! Y a y, now we authenticate with firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());
                // Since this is google, we must authenticate it still via firebase
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Failed sign In, update UI.
                Log.w(TAG, "Google Sign In Womped", e);
                updateUiWithUser(null);
            }
        }
    }

    /**
     * If on pause we stop the button animation or the cup animation
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "entetered onPause()");

        if (buttonAnimation != null && buttonAnimation.isRunning()){
            buttonAnimation.stop();
        }
    }

    /**
     * Resumes the animations in onResume
     */
    @Override
    protected void onResume() {
        Log.d(TAG, "entetered onResume()");
        super.onResume();
        if (buttonAnimation != null && !buttonAnimation.isRunning()){
            buttonAnimation.start();
        }
    }

    /**
     * Handles firebase authentication with Google!
     * @param idToken
     */
    private void firebaseAuthWithGoogle(String idToken) {

        // Firebase authentication with google authentication
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in Success, update the UI with all of the stuff!
                            Log.d(TAG, "signInWithCredential: success");
                            // Firebase user to update UI
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUiWithUser(user);
                        } else {
                            // If sign in fails, well thats a big womp.
                            Log.w(TAG, "signInWithCredential: failure", task.getException());
                            updateUiWithUser(null);
                        }
                    }
                });
    }

    /**
     * Updates UI with animation and then starts the activity
     * @param user
     */
    private void updateUiWithUser(FirebaseUser user) {
        if (user != null) {
            // Starts the animation and then check if the animation is done
            Log.d(TAG, "starting animation");
            mAnimationDrawable.start();
            checkIfAnimationDone(mAnimationDrawable);
        }
    }

    /**
     * Checks if the animation is done by checking every 300 ms and if so, we will change to the map
     * activity
     * @param anim
     */
    private void checkIfAnimationDone(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
                    Log.d(TAG, "stilling going, fam");
                    checkIfAnimationDone(a);
                } else {
                    Log.d(TAG, "animation stopping");

                    // If animation is done, we put information about the user's frat if an admin
                    // into the intent and then starts the map activity.
                    String uid = mAuth.getCurrentUser().getUid();
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        Intent mapIntent = new Intent(LoginActivity.this, FratMapActivity.class);
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                // Admin frat user!
                                mapIntent.putExtra(USER_TYPE, true);
                                Log.d(TAG, dataSnapshot.child("GreekSpace").getValue().toString());
                                mapIntent.putExtra(GREEK_SPACE, dataSnapshot.child("GreekSpace").getValue().toString());
                            }
                            else {
                                mapIntent.putExtra(USER_TYPE, false);
                            }
                            startActivityForResult(mapIntent, REQUEST_LOGIN);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        }, timeBetweenChecks);
    }

    /**
     * When the Sign in Button is clicked to let the night B e g i n
     * @param v
     */
    public void onSignInClicked(View v) {
        Log.d(TAG, "onClick: entered!");
        switch(v.getId()) {
            case R.id.login:
                signIn();
                break;
        }
    }

    /**
     * Signs in via google sign in client
     */
    private void signIn() {
        // Sign In via google
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}
