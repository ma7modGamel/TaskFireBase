package com.mgh.taskfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mgh.taskfirebase.FragmentImage.ImageActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    EditText editTextUserName, editTextPassWord;
    ProgressBar progressBarLogin;
    TextView textViewPleaseWait;
    Button btnLogIn;

    LinearLayout linearLayoutContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e(TAG, "onCreate: login" );


        InitializeViews();
        setupWidgetLogin();
        setUpFireBase();
        initVal();
    }

    private void InitializeViews() {

        btnLogIn = findViewById(R.id.btnLoginId);
        editTextUserName = findViewById(R.id.idUserNameLogIn);
        editTextPassWord = findViewById(R.id.idPasswordLogin);
        linearLayoutContent = findViewById(R.id.linearCintentId);
        textViewPleaseWait = findViewById(R.id.plsWaitTVid);
        progressBarLogin = findViewById(R.id.progressparLoginId);
    }

    private void setupWidgetLogin() {
        Log.e(TAG, "setupWidgetLogin: " );
        textViewPleaseWait.setVisibility(View.GONE);
        progressBarLogin.setVisibility(View.GONE);
    }



    //**************************** firebase ************/

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener listener;

    private void setUpFireBase() {
        auth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {

                Log.e(TAG, "onAuthStateChanged: " );
            }
        };

    }
    private void initVal() {

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String USER_LOGIN = editTextUserName.getText().toString().trim();
                String PASSWORD_LOGIN = editTextPassWord.getText().toString().trim();
                if(TextUtils.isEmpty(USER_LOGIN)){
                    Toast.makeText(LoginActivity.this, "ener user name.",
                            Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(PASSWORD_LOGIN)){
                    Toast.makeText(LoginActivity.this, "enter password.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    textViewPleaseWait.setVisibility(View.VISIBLE);
                    progressBarLogin.setVisibility(View.VISIBLE);
                    linearLayoutContent.setVisibility(View.GONE);
                    auth.signInWithEmailAndPassword(USER_LOGIN, PASSWORD_LOGIN)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        FirebaseUser user = auth.getCurrentUser();
                                        Intent intent=new Intent(LoginActivity.this,ImageActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Log.e(TAG, "signInWithEmail:success" + user + "  " + user.getUid());
                                        textViewPleaseWait.setVisibility(View.GONE);
                                        progressBarLogin.setVisibility(View.GONE);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        textViewPleaseWait.setVisibility(View.GONE);
                                        progressBarLogin.setVisibility(View.GONE);
                                        linearLayoutContent.setVisibility(View.VISIBLE);


                                    }

                                    // ...
                                }
                            });



                }
            }
        });

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, ImageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listener != null) {
            auth.removeAuthStateListener(listener);
        }
    }

}
