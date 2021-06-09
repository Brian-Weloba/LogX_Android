package com.saturdev.logx;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class LogInActivity extends AppCompatActivity {
    @BindView(R.id.emailLogIn)
    EditText mEmail;
    @BindView(R.id.passwordLogIn)
    EditText mPassword;
    @BindView(R.id.logInButton)
    Button mLoginBT;
    @BindView(R.id.noAccountText)
    TextView mNoAccount;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mEmail.setText(getIntent().getStringExtra("email"));

        mNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignInActivity.class));
            }
        });

        mLoginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail() && validatePassword()) {
                    String email = mEmail.getEditableText().toString();
                    String password = mPassword.getEditableText().toString();
                    signIn(email, password);
                }
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor prefEditor = sharedPref.edit();
                            prefEditor.putInt("isLogged", 1);
                            prefEditor.apply();
                        } else {
                            Timber.tag(TAG).w(task.getException(), "signInWithEmail:failure");
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Boolean validateEmail() {
        String value = mEmail.getEditableText().toString();
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (value.isEmpty()) {
            mEmail.setError("Field cannot be empty");
            mEmail.requestFocus();
            return false;
        } else if (!value.matches(emailPattern)) {
            mEmail.setError("Invalid email address");
            mEmail.requestFocus();
            return false;
        } else {
            mEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = mPassword.getEditableText().toString();
        String passwordVal =
//                "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" //+      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                "(?=\\S+$)" +           //no white spaces
//                ".{4,}" +               //at least 4 characters
//                "$"
                ;

        if (value.isEmpty()) {
            mPassword.setError("Field cannot be empty");
            mPassword.requestFocus();
            return false;
        }
//        else if (!value.matches(passwordVal)) {
//            mPassword.setError("Password Requirements not met");
//            mPassword.requestFocus();
//            return false;
//        }
        else if (value.length() >= 15) {
            mPassword.setError("Password too long");
            mPassword.requestFocus();
            return false;
        } else {
            mPassword.setError(null);
            return true;
        }
    }
}