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

public class Register extends Fragment {

    EditText mEdtUsrName, mEdtEmail, mEdtPass1, mEdtPass2, mEdtPhone;
    Button mBtnRegister;
    TextView mTvBtnLogin, mTvHeader;
    FirebaseAuth mFirebaseAuth;
    NavController mNav;
    ProgressBar mProgressBar;

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
        mEdtEmail = view.findViewById(R.id.edtEmail);
        mEdtPass1 = view.findViewById(R.id.edtPass1);
        mEdtPass2 = view.findViewById(R.id.edtPass2);
        mEdtPhone = view.findViewById(R.id.edtPhone);
        mBtnRegister = view.findViewById(R.id.btnRegister);
        mTvBtnLogin = view.findViewById(R.id.tvBtnLogin);
        mTvHeader = view.findViewById(R.id.tvRegisterHeader);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressBar = view.findViewById(R.id.pbFirebaseCreateUser);

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

        if(!email.isEmpty() && !pass1.isEmpty() && !pass2.isEmpty()){
            if(pass1.length() > 6){
                if(pass1.equals(pass2)){
                    mTvBtnLogin.setVisibility(View.GONE);
                    mBtnRegister.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);

                    mFirebaseAuth.createUserWithEmailAndPassword(email,pass1)
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    Toast.makeText(
                                            this.getContext(),
                                            "User" + "NAME" + "Created",
                                            Toast.LENGTH_SHORT).show();
                                    mNav.navigate(R.id.action_register_to_profile);

                                }else{
                                    Toast.makeText(
                                            this.getContext(),
                                            "Error: " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();

                                }
                            });

                }else{
                    mEdtPass2.setError("Passwords must match");
                }
            }else{
                mEdtPass1.setError("Must be longer that 6 characters");
            }
        }else{
            mTvHeader.setError("Please fill all fields");
        }

    }
}
