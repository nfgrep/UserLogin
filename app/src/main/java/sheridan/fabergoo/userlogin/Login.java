package sheridan.fabergoo.userlogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import com.google.firebase.auth.FirebaseAuth;

public class Login extends Fragment {

    TextView mTvBtnRegister;
    Button mBtnLogin;
    ProgressBar mPbLogin;
    EditText mEdtEmail, mEdtPass;
    FirebaseAuth mFirebaseAuth;

    NavController mNav;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_login,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNav = Navigation.findNavController(view);

        mTvBtnRegister = view.findViewById(R.id.tvBtnRegister);
        mBtnLogin = view.findViewById(R.id.btnLogin);
        mPbLogin = view.findViewById(R.id.pbFirebaseLogin);
        mPbLogin.setVisibility(View.GONE);

        mTvBtnRegister.setOnClickListener(v -> mNav.navigate(R.id.action_login_to_register));

    }
}
