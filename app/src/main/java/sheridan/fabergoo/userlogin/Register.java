package sheridan.fabergoo.userlogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register extends Fragment {

    private EditText mEdtUsrName, mEdtEmail, mEdtPass1, mEdtPass2, mEdtPhone;
    private Button mBtnRegister;
    private TextView mTvBtnLogin, mTvHeader;
    private FirebaseAuth mFirebaseAuth;
    private NavController mNav;
    private ProgressBar mProgressBar;
    private String uID;
    private FirebaseFirestore mFirebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNav = Navigation.findNavController(view);

        mEdtUsrName = view.findViewById(R.id.edtUsrName);
        mEdtEmail = view.findViewById(R.id.edtEmailRegister);
        mEdtPass1 = view.findViewById(R.id.edtPass1);
        mEdtPass2 = view.findViewById(R.id.edtPass2);
        mEdtPhone = view.findViewById(R.id.edtPhone);
        mBtnRegister = view.findViewById(R.id.btnRegister);
        mTvBtnLogin = view.findViewById(R.id.tvBtnLogin);
        mTvHeader = view.findViewById(R.id.tvHeaderRegister);
        mProgressBar = view.findViewById(R.id.pbFirebaseCreateUser);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        // Navigates straight to Profile fragment if user is already logged-in
        if(mFirebaseAuth.getCurrentUser() != null){
            mNav.navigate(R.id.action_register_to_profile);
        }

        mBtnRegister.setOnClickListener(v -> onRegisterClick());
        mTvBtnLogin.setOnClickListener(v -> mNav.navigate(R.id.action_register_to_login));
    }

    private void onRegisterClick(){
        String email = mEdtEmail.getText().toString().trim();
        String pass1 = mEdtPass1.getText().toString().trim();
        String pass2 = mEdtPass2.getText().toString().trim();
        String uname = mEdtUsrName.getText().toString().trim();
        String phone = mEdtPhone.getText().toString().trim();

        // Input validation
        if(!email.isEmpty() && !pass1.isEmpty() && !pass2.isEmpty()){
            if(pass1.length() > 6){
                if(pass1.equals(pass2)){

                    // Shows loading spinner and hides buttons.
                    mTvBtnLogin.setVisibility(View.GONE);
                    mBtnRegister.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);

                    // Creates a new user in cloud authentication storage.
                    // Listens for when user is successfully added to cloud.
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pass1)
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){

                                    // Gets uID of newly created user.
                                    uID = Objects.requireNonNull(mFirebaseAuth.getCurrentUser())
                                            .getUid();

                                    // Creates reference to "users" collection, creates collection
                                    // if not already existing in db. Adds a document "userID" to
                                    // the collection, and returns its reference.
                                    DocumentReference documentReference = mFirebaseFirestore
                                            .collection("users").document(uID);

                                    // A local instance of document(object) we want to store.
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("uname", uname);
                                    user.put("email", email);
                                    user.put("phone", phone);

                                    // Inserts the local document into the db at documentReference.
                                    documentReference.set(user).addOnSuccessListener(v -> {
                                        Toast.makeText(
                                                this.getContext(),
                                                "User \'" + uname + "\' Created",
                                                Toast.LENGTH_SHORT).show();
                                    });

                                    // Navigates to Profile fragment after user is registered.
                                    mNav.navigate(R.id.action_register_to_profile);

                                }else{
                                    // Error message if creating new user is unsuccessful.
                                    Toast.makeText(
                                            this.getContext(),
                                            "Error: " + Objects.requireNonNull(
                                                    task.getException()
                                            ).getMessage(),
                                            Toast.LENGTH_LONG).show();

                                    // Sets buttons visible again and hides loading spinner.
                                    mTvBtnLogin.setVisibility(View.VISIBLE);
                                    mBtnRegister.setVisibility(View.VISIBLE);
                                    mProgressBar.setVisibility(View.GONE);

                                }
                            });

                }else{
                    mEdtPass2.setError("Passwords must match");
                }
            }else{
                mEdtPass1.setError("Must be longer that 6 characters");
            }
        }else{
            mEdtUsrName.setError("Please fill all fields");
        }

    }
}
