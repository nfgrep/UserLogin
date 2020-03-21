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
import com.google.firebase.firestore.ListenerRegistration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Profile extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController mNav = Navigation.findNavController(view);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Gets the uID of current logged-in user, this uID is the key used to retrieve data from db
        String uID = firebaseAuth.getCurrentUser().getUid();

        // Creates a reference to an existing document at "uID" in collection "users" from the db
        DocumentReference documentReference =
                firebaseFirestore.collection("users").document(uID);

        TextView mTvUname = view.findViewById(R.id.tvUname);
        TextView mTvEmail = view.findViewById(R.id.tvEmail);
        TextView mTvPhone = view.findViewById(R.id.tvPhone);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        // Adds listener on document reference for realtime updating of data in view
        // This callback is fired once upon attaching, and anytime the cloud data is updated

        ListenerRegistration listenerRegistration =
                documentReference.addSnapshotListener( (snapshot, e) -> {
                    assert snapshot != null;
                    mTvUname.setText(snapshot.getString("uname"));
                    mTvEmail.setText(snapshot.getString("email"));
                    mTvPhone.setText(snapshot.getString("phone"));
                });


        // Signs user out and navigates to login fragment.
        // Removes listener to stop from trying to update data after user is logs-out.
        btnLogout.setOnClickListener(v -> {
            listenerRegistration.remove();
            FirebaseAuth.getInstance().signOut();
            mNav.navigate(R.id.action_profile_to_login);
        });
    }
}
