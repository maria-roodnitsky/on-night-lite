package on.night.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


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
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import on.night.R;
import on.night.data.model.LoggedInUser;
import on.night.ui.login.LoginViewModel;
import on.night.ui.login.LoginViewModelFactory;

import java.net.URI;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import on.night.R;
import on.night.ui.map.FratMapActivity;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1;
    private static final int REQUEST_LOGIN = 2;
    private LoginViewModel loginViewModel;
    private AnimationDrawable buttonAnimation;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private AnimationDrawable mAnimationDrawable;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





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

        setContentView(R.layout.activity_login);
//        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);



        final Button loginButton = findViewById(R.id.login);
//        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        Objects.requireNonNull(getSupportActionBar()).hide();

        buttonAnimation = (AnimationDrawable) loginButton.getBackground();
        buttonAnimation.setEnterFadeDuration(1700);
        buttonAnimation.setExitFadeDuration(1700);

        // Set up cup animation!
        ImageView cupImage = findViewById(R.id.imageView3);
        cupImage.setBackgroundResource(R.drawable.pong_login);
        mAnimationDrawable = (AnimationDrawable) cupImage.getBackground();


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // We check if a user is already singed in and update the UI accordingly
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUiWithUser(currentUser);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
     * Handles firebase authentication with Google!
     * @param idToken
     */
    private void firebaseAuthWithGoogle(String idToken) {
        // Progress Bar question mark?

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in Success, ipdate the UI with all of the stuff!
                            Log.d(TAG, "signInWithCredential: success");
                            Toast.makeText(LoginActivity.this, "Authentication Success!",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUiWithUser(user);
                        } else {
                            // If sign in fails, well thats a big womp.
                            Log.w(TAG, "signInWithCredential: failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Womp",
                                    Toast.LENGTH_SHORT).show();
                            updateUiWithUser(null);

                        }
                    }
                });
    }


    private void updateUiWithUser(FirebaseUser model) {
        if (model != null) {
            String welcome = getString(R.string.welcome) + model.getDisplayName();
            // TODO : initiate successful logged in experience
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

            // Play cup animation!
            mAnimationDrawable.start();

            checkIfAnimationDone(mAnimationDrawable);

        }
    }

    private void checkIfAnimationDone(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
                    checkIfAnimationDone(a);
                } else {
                    Intent mapIntent = new Intent(LoginActivity.this, FratMapActivity.class);
                    startActivityForResult(mapIntent, REQUEST_LOGIN);
                }
            }
        }, timeBetweenChecks);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (buttonAnimation != null && buttonAnimation.isRunning()){
            buttonAnimation.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (buttonAnimation != null && !buttonAnimation.isRunning()){
            buttonAnimation.start();
        }
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

    private void signIn() {
        // Sign In via google
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}
