package com.tourmate.com.tourmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText memail,mpassword;
    Button next;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        next= findViewById(R.id.next);

        mAuth= FirebaseAuth.getInstance();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                userLogIn();


            }
        });




    }


    private void userLogIn() {

        String email = memail.getText().toString();
        String password = mpassword.getText().toString();

        if (email.isEmpty()){
            memail.setError("Enter Valid E-mail");
            memail.requestFocus();
        }
        if (password.isEmpty()){
            mpassword.setError("Enter Valid Password");
            mpassword.requestFocus();
        }
        if (password.length()<6){
            mpassword.setError("Enter Valid minimu 6 digit password");
            mpassword.requestFocus();
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( @NonNull Task<AuthResult> task ) {
               if(task.isSuccessful()){
                   Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                   startActivity(intent);
                   finish();
               }
               else {
                   Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
            }
        });

    }
}
