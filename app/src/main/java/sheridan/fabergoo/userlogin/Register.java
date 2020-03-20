package sheridan.fabergoo.userlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText mEdtUsrName, mEdtEmail, mEdtPass1, mEdtPass2, mEdtPhone;
    Button mBtnRegister;
    TextView mTvBtnLogin;
    FirebaseAuth mFirebaseAuth;
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEdtUsrName = findViewById(R.id.edtUsrName);
        mEdtEmail = findViewById(R.id.edtEmail);
        mEdtPass1 = findViewById(R.id.edtPass1);
        mEdtPass2 = findViewById(R.id.edtPass2);
        mEdtPhone = findViewById(R.id.edtPhone);
        mBtnRegister = findViewById(R.id.btnLogin);
        mTvBtnLogin = findViewById(R.id.tvLogin);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mBtnRegister.setOnClickListener(v -> onRegisterClick());

    }

    private void onRegisterClick(){
        String email = mEdtEmail.getText().toString().trim();
        String pass1 = mEdtPass1.getText().toString().trim();
        String pass2 = mEdtPass2.getText().toString().trim();

        if(!email.isEmpty() && !pass1.isEmpty() && !pass2.isEmpty()){
            if(pass1.length() > 6){
                if(pass1.equals(pass2)){

                    //mProgressbar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pass1)
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){

                                    Toast.makeText(
                                            Register.this,
                                            "User" + "NAME" + "Created",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    //Send user to main activity: Fragments?
                                }else{

                                }
                            });

                }else{
                    mEdtPass2.setError("Passwords must match");
                }
            }else{
                mEdtPass1.setError("Must be longer that 6 characters");
            }
        }else{
            mBtnRegister.setError("Please fill all fields");
        }

    }
}
