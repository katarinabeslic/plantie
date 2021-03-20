package com.example.plantie.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.plantie.R;
import com.example.plantie.domen.User;
import com.example.plantie.help.DatabaseBroker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private View view;
    private int RC_SIGN_IN = 1;

    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        btnSignIn = findViewById(R.id.btnSignIn);


        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(singInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            firebaseGoogleAuth(acc);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (ApiException e) {
            firebaseGoogleAuth(null);
            e.printStackTrace();
        }
    }

    private void firebaseGoogleAuth(GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Signed in succesfully!", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Toast.makeText(LoginActivity.this, "Sign in unsuccessful!", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            String username = account.getDisplayName();
            String firstName = account.getGivenName();
            String lastName = account.getFamilyName();
            String email = account.getEmail();
            Uri profilePicture = account.getPhotoUrl();

            User u  = new User();
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setUsername(username);
            u.setEmail(email);
            u.setProfilePicture(profilePicture);

            boolean successful = DatabaseBroker.getInstance(this).addUser(u);

            Toast.makeText(LoginActivity.this, username + " & " + email, Toast.LENGTH_SHORT).show();
        }
    }
}