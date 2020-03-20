package sheridan.fabergoo.userlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText mEdtUsrName, mEdtEmail, mEdtPass, mEdtPhone;
    Button mBtnRegister;
    TextView mTvBtnLogin;
    FirebaseAuth mFirebaseAuth;
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
