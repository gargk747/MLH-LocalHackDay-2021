package com.example.lutescensapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterTxt, mForgotTxt;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.emailLogin);
        mPassword = findViewById(R.id.passwordLogin);
        progressBar = findViewById(R.id.loginProgressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);
        mRegisterTxt = findViewById(R.id.registerTxt);
        mForgotTxt = findViewById(R.id.forgotTxt);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                // These If statements check that email and password field are not empty.

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                // This is simply a progress bar that activates when the user tries to access to the application.
                // It improves the user experience, giving him an idea that some process is running and they have to wait.

                progressBar.setVisibility(View.VISIBLE);

                // This piece of code authenticate the user by using email and password. If email and password are inserted correctly,
                // the user will go the the home screen and a message "Logged in successfully" will be displayed. Otherwise,
                // a message "Error!" with the following error written will be showed and the progress bar will disappear.

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = fAuth.getCurrentUser();

                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                // Get user email and uid from auth
                                String email = user.getEmail();
                                String uid = user.getUid();
                                // Store user info into the realtime database by using Hashmap
                                HashMap<Object, String> hashMap = new HashMap<>();
                                // Insert info in the HasMap
                                hashMap.put("email", email);
                                hashMap.put("uid", uid);
                                hashMap.put("nickname", "");
                                hashMap.put("image", "");

                                // FireBase database instance
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                // Path to store user data (named "Users")
                                DatabaseReference reference = database.getReference("Users");
                                // Put data within HashMap in the database
                                reference.child(uid).setValue(hashMap);
                            }


                            Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),UserProfile.class));
                            finish();
                        }else {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        // This method brings the user to the Register screen when the click on "Not registered yet? Click on me!" text in login screen.

        mRegisterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });

        mForgotTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // This code creates an alert dialog with an edit text views where the user can write in his email.

                final EditText resetEmail = new EditText(view.getContext());
                final AlertDialog.Builder resetPasswordDialog = new AlertDialog.Builder(view.getContext());
                resetPasswordDialog.setTitle("Reset your password");
                resetPasswordDialog.setMessage("Enter your e-mail and you will receive a reset link. Check your spam emails too.");
                resetPasswordDialog.setView(resetEmail);
                resetPasswordDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String email = resetEmail.getText().toString();

                        // Display a message if the email field is empty.

                        if(TextUtils.isEmpty(email)){
                            Toast.makeText(Login.this, "It seems the email field was empty..", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Send a reset password email to the user's email address. If the mail is inserted incorrectly, it display
                        // a message and says which type is it.

                        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "A reset link has been sent to your e-mail", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! The reset link has not been sent." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                resetPasswordDialog.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // this empty method is needed to close the reset password dialog.
                    }
                });

                resetPasswordDialog.create().show();

            }
        });

    }
}
