package on.night.data.services;

// Import statements
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Messaging Service that will be used with Firebase Messaging Cloud
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //
    }












    /////// IMPLEMENT IF HAVE TIME OR EVENTUALLY UNDERSTAND ENOUGH TO DO SO ///////

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance
        // or manage this apps subscriptions on the server side, send the Instance ID token to app
        // server
        sendRegistrationToServer(token);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}
