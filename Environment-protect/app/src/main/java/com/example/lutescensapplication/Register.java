package com.example.lutescensapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText mNickname, mEmail, mPassword, mConfirmPassword;
    Button mRegisterBtn;
    CheckBox mCheckBox;
    TextView mRegisterToLogin, mGdprAndTerms;
    FirebaseAuth fAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNickname = findViewById(R.id.nickname);
        mEmail = findViewById(R.id.emailAddress);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mRegisterToLogin = findViewById(R.id.registerToLogin);
        mCheckBox = findViewById(R.id.gdprAndTermsCheckBox);
        mGdprAndTerms = findViewById(R.id.gdprAndTerms);


        // I get an instance of the database from FireBase to perform actions on the database

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.registerProgressBar);

        // If the user is already logged in, then the app goes straight to the Home page.

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), UserProfile.class));
            finish();
        }

        // Code to register the user into the database after successful registration

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    final String nickname = mNickname.getText().toString().trim();
                    String email = mEmail.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();
                    String confirmPassword = mConfirmPassword.getText().toString().trim();

                    progressBar.setVisibility(View.VISIBLE);

                    // Here the code register the user data onto the database. If is a correct registration,
                    // the user goes to the Home page. If something else occurred, the app displays an "Error!" message

                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = fAuth.getCurrentUser();
                                // Get user email and uid from auth
                                String email = user.getEmail();
                                String uid = user.getUid();
                                // Store user info into the realtime database by using Hashmap
                                HashMap<Object, String> hashMap = new HashMap<>();
                                // Insert info in the HasMap
                                hashMap.put("email", email);
                                hashMap.put("uid", uid);
                                hashMap.put("nickname", nickname);
                                hashMap.put("image", "");

                                // FireBase database instance
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                // Path to store user data (named "Users")
                                DatabaseReference reference = database.getReference("Users");
                                // Put data within HashMap in the database
                                reference.child(uid).setValue(hashMap);


                                Toast.makeText(Register.this, "User created.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), UserProfile.class));
                                finish();
                            } else {
                                Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

        // This method allow the user to visualise the screen with all information related to GDPR, Terms and Conditions of use of the application

        mGdprAndTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TermsAndConditions.class));
            }
        });

        // This method brings the user to the Login screen when the click on "Already registered? Click here!" text in login screen.

        mRegisterToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

    }

    boolean validate() {
        String nickname = mNickname.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();

        // I check that nickname, email and password are filled up correctly with these if statements.
        // In order: check that nickname is not empty, checks that nickname has a length between 6 and 12 characters, that email is not empty,
        // that password is not empty, that password is between 6 and 12 characters, that confirm password is not empty,
        // that confirm password matches with password and privacy policy and conditions of use box is ticked.

        if (TextUtils.isEmpty(nickname)) {
            mNickname.setError("A nickname is required");
            return false;
        } else if (nickname.length() < 6 || nickname.length() > 12) {
            mNickname.setError("Sorry, the nickname must be between 6 and 12 characters");
            return false;
        } else if (TextUtils.isEmpty(email)) {
            mEmail.setError("An e-mail is required");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            mPassword.setError(("A password is required"));
            return false;
        } else if (password.length() < 6 || password.length() > 12) {
            mPassword.setError("Sorry, password must be between 6 and 12 characters");
            return false;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            mPassword.setError(("Please, confirm your password"));
            return false;
        } else if (!confirmPassword.equals(password)) {
            mConfirmPassword.setError("Password not matching");
            return false;
        } else if (!mCheckBox.isChecked()) {
            mCheckBox.setError("You have to agree to GDPR and Terms and Conditions of the application");
            return false;
        }
        return true;
    }
}
