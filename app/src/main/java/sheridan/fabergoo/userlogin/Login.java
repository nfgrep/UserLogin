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

import java.util.Objects;

public class Login extends Fragment {

    private TextView mTvBtnRegister, mTvHeaderLogin;
    private Button mBtnLogin;
    private ProgressBar mPbLogin;
    private EditText mEdtEmail, mEdtPass;
    private FirebaseAuth mFirebaseAuth;

    private NavController mNav;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_login,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNav = Navigation.findNavController(view);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mTvBtnRegister = view.findViewById(R.id.tvBtnRegister);
        mTvHeaderLogin = view.findViewById(R.id.tvHeaderLogin);
        mBtnLogin = view.findViewById(R.id.btnLogin);
        mPbLogin = view.findViewById(R.id.pbFirebaseLogin);
        mEdtEmail = view.findViewById(R.id.edtEmailLogin);
        mEdtPass = view.findViewById(R.id.edtPassLogin);

        mTvBtnRegister.setOnClickListener(v -> mNav.navigate(R.id.action_login_to_register));
        mBtnLogin.setOnClickListener(v -> {
            String email = mEdtEmail.getText().toString().trim();
            String pass = mEdtPass.getText().toString().trim();

            if(!email.isEmpty() && !pass.isEmpty()){
                if(pass.length() > 6){
                    mTvBtnRegister.setVisibility(View.GONE);
                    mBtnLogin.setVisibility(View.GONE);
                    mPbLogin.setVisibility(View.VISIBLE);

                    mFirebaseAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    Toast.makeText(
                                            this.getContext(),
                                            "Log in Successful",
                                            Toast.LENGTH_SHORT).show();

                                    mNav.navigate(R.id.action_login_to_profile);

                                   }else{
                                    Toast.makeText(
                                            this.getContext(), "Error: " +
                                                    Objects.requireNonNull(
                                                            task.getException()).getMessage(),
                                            Toast.LENGTH_LONG).show();

                                    mTvBtnRegister.setVisibility(View.VISIBLE);
                                    mBtnLogin.setVisibility(View.VISIBLE);
                                    mPbLogin.setVisibility(View.GONE);
                                }
                            });

                }else{
                    mEdtPass.setError("Must be longer that 6 characters");
                }
            }else{
                mEdtEmail.setError("Please fill all fields");
            }
        });

    }
}
