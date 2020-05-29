package on.night.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FratDataSource {
    private FirebaseDatabase mDatabase;

    public void open() {
        mDatabase = FirebaseDatabase.getInstance();

    }
}
