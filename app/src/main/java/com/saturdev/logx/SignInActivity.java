package com.saturdev.logx;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.nameSignUp)
    EditText nameET;
    @BindView(R.id.emailSignUp)
    EditText emailET;
    @BindView(R.id.passwordSignUp)
    EditText passwordET;
    @BindView(R.id.SignUpbutton)
    Button signUpBT;
    @BindView(R.id.loginButton)
    TextView mLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);
        int number = sharedPref.getInt("isLogged", 0);

        mAuth = FirebaseAuth.getInstance();

        if (number == 0) {
            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SignInActivity.this, LogInActivity.class);
                    startActivity(intent);
                }
            });

            signUpBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateEmail() && validateUsername() && validatePassword()) {
                        String email = emailET.getEditableText().toString();
                        String username = nameET.getEditableText().toString();
                        String password = passwordET.getEditableText().toString();
                        createAccount(email, password, username);
                    }
                }
            });
        }else{
            Intent intent = new Intent(SignInActivity.this, HomeDevicesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void createAccount(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Timber.d("createUserWithEmail:success");
                            Intent intent = new Intent(SignInActivity.this, LogInActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Timber.tag(TAG).w(task.getException(), "createUserWithEmail:failure");
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        if (user != null) {
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Timber.d("User profile updated.");
                            }
                        }
                    });
        }
    }

    private Boolean validateEmail() {
        String value = emailET.getEditableText().toString();
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (value.isEmpty()) {
            emailET.setError("Field cannot be empty");
            emailET.requestFocus();
            return false;
        } else if (!value.matches(emailPattern)) {
            emailET.setError("Invalid email address");
            emailET.requestFocus();
            return false;
        } else {
            emailET.setError(null);
            return true;
        }
    }

    private Boolean validateUsername() {
        String value = nameET.getEditableText().toString();
        String noWhiteSpace = "(?=\\s+$)";

        if (value.isEmpty()) {
            nameET.setError("Field cannot be empty");
            emailET.requestFocus();
            return false;
        } else if (value.length() >= 15) {
            nameET.setError("Username too long");
            nameET.requestFocus();
            return false;
        } else {
            nameET.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = passwordET.getEditableText().toString();
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
            passwordET.setError("Field cannot be empty");
            passwordET.requestFocus();
            return false;
        }
//        else if (!value.matches(passwordVal)) {
//            passwordET.setError("Password Requirements not met");
//            passwordET.requestFocus();
//            return false;
//        }
        else if (value.length() >= 15) {
            passwordET.setError("Password too long");
            passwordET.requestFocus();
            return false;
        } else {
            passwordET.setError(null);
            return true;
        }
    }
}