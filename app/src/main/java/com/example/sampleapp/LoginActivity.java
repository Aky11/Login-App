package com.example.sampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText email,pass;
    private Button btnSignin;
    private TextView tvNewUser;
    private CheckBox checkBox;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email=findViewById(R.id.userMail);
        pass=findViewById(R.id.userPassword);
        btnSignin=findViewById(R.id.btnLogin);
        tvNewUser = findViewById(R.id.tvNewUser);
        checkBox = findViewById(R.id.checkBox);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = email.getText().toString();
                String password = pass.getText().toString();
                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter all the fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Welcome "+user.getEmail(), Toast.LENGTH_SHORT).show();
                                openMainScreen();
                            }else{
                                Toast.makeText(LoginActivity.this, "Signin Failure", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(LoginActivity.this, "User authenticated please wati...", Toast.LENGTH_SHORT).show();
            openMainScreen();
        }
    }

    void openMainScreen(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}