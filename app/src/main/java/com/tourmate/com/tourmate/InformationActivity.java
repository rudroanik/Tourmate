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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class InformationActivity extends AppCompatActivity {

    Button save;
    EditText mfullname,memail,mpassword,memergencynumber,maddress;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        save= findViewById(R.id.save);
        mfullname =findViewById(R.id.fullname);
        memail =findViewById(R.id.email);
        mpassword =findViewById(R.id.password);
        memergencynumber =findViewById(R.id.emergencynumber);
        maddress =findViewById(R.id.address);


        mAuth= FirebaseAuth.getInstance();




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                register();
                Intent intent = new Intent(InformationActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
    public void register(){

        final String name= mfullname.getText().toString();
        final String email= memail.getText().toString();
        String password= mpassword.getText().toString();
        final String phone= memergencynumber.getText().toString();
        final String address = maddress.getText().toString();


        if(email.isEmpty()){

            memail.setError("Enter E-mail address");
            memail.requestFocus();
        }
        if(password.isEmpty()){

            mpassword.setError("Enter password");
            mpassword.requestFocus();
        }
        if(password.length()<6){

            mpassword.setError("Enter minimum 6 digit");
            mpassword.requestFocus();
        }
        if(phone.isEmpty()){

            memergencynumber.setError("Enter mobile number ");
            memergencynumber.requestFocus();
        }
        if(phone.length()<11){

            memergencynumber.setError("Enter 11 digit mobile number ");
            memergencynumber.requestFocus();
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( @NonNull Task<AuthResult> task ) {
                if(task.isSuccessful()){

                    User user = new User(name,email,phone,address);
                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( @NonNull Task<Void> task ) {
                            Toast.makeText(InformationActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    Toast.makeText(InformationActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
