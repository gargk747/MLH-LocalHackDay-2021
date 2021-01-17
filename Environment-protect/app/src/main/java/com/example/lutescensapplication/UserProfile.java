package com.example.lutescensapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfile extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = firebaseAuth.getInstance();

        // Bottom navigation bar
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.content, new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new ProfileFragment()).commit();
        }

        // Default home fragment transaction on start.
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "eke");
        ft1.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    // This piece of code handles the taps on the navigation bar
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            // Home fragment transaction
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content, fragment1, "mf");
                            ft1.commit();
                            return true;
                        case R.id.nav_search:
                            // Search fragment transaction
                            SearchFragment fragment2 = new SearchFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, fragment2, "ffs");
                            ft2.commit();
                            return true;
                        case R.id.nav_profile:
                            // Profile fragment transaction
                            ProfileFragment fragment3 = new ProfileFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content, fragment3, "ff");
                            ft3.commit();
                            return true;

                    }

                    return false;
                }
            };

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            //mProfileName.setText(user.getEmail());
        } else {
            startActivity(new Intent(UserProfile.this, Login.class));
            finish();
        }
    }

    protected void onStart(){
        checkUserStatus();
        super.onStart();
    }

    // Open the settings menu in profile screen

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // This bit of code handles the clicks inside the settings menu in profile screen

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        } else if (id == R.id.action_Privacy) {
            startActivity(new Intent(getApplicationContext(), TermsAndConditions.class));
        } else if (id == R.id.action_changePassword){
            final EditText resetEmail = new EditText(this);
            final AlertDialog.Builder changePasswordDialog = new AlertDialog.Builder(this);
            changePasswordDialog.setTitle("Change your password");
            changePasswordDialog.setMessage("Enter your e-mail and you will receive a reset link. Check your spam emails too.");
            changePasswordDialog.setView(resetEmail);
            changePasswordDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    String email = resetEmail.getText().toString();

                    // Display a message if the email field is empty.

                    if(TextUtils.isEmpty(email)){
                        Toast.makeText(UserProfile.this, "It seems the email field was empty..", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Send a reset password email to the user's email address. If the mail is inserted incorrectly, it display
                    // a message and says which type is it.

                    firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserProfile.this, "A reset link has been sent to your e-mail", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserProfile.this, "Error! The reset link has not been sent." + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            changePasswordDialog.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // this empty method is needed to close the reset password dialog.
                }
            });

            changePasswordDialog.create().show();
        }
        return super.onOptionsItemSelected(item);
    }
}

