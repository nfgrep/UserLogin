package sheridan.fabergoo.userlogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Profile extends Fragment {

    TextView mTvUname, mTvEmail, mTvPhone;
    Button mBtnLogout;
    NavController mNav;
    FirebaseAuth mFirebaseAuth;
    String uID;
    FirebaseFirestore mFirebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNav = Navigation.findNavController(view);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        uID = mFirebaseAuth.getCurrentUser().getUid();

        // Creates a reference to an existing document at "uID" in collection "users" from the db
        DocumentReference documentReference =
                mFirebaseFirestore.collection("users").document(uID);

        mTvUname = view.findViewById(R.id.tvUname);
        mTvEmail = view.findViewById(R.id.tvEmail);
        mTvPhone = view.findViewById(R.id.tvPhone);

        // Adds listener on document reference for realtime updating of data in view
        // This callback is fired once upon attaching, and anytime the cloud data is updated
        documentReference.addSnapshotListener((snapshot, e) -> {
            mTvUname.setText(snapshot.getString("uname"));
            mTvEmail.setText(snapshot.getString("email"));
            mTvPhone.setText(snapshot.getString("phone"));
        });





        mBtnLogout = view.findViewById(R.id.btnLogout);
        mBtnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            mNav.navigate(R.id.action_profile_to_login);
        });
    }
}
