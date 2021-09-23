package com.example.sampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etPhone, etMail, etAddress, etPass, etConfPass;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference ref;
    int maxid = 0;

    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etMail = findViewById(R.id.etMail);
        etAddress = findViewById(R.id.etAddress);
        etPass = findViewById(R.id.etPassword);
        etConfPass = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        person = new Person();

        ref = database.getInstance().getReference().child("User");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxid = (int) snapshot.getChildrenCount();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String mail = etMail.getText().toString();
                String address = etAddress.getText().toString();
                String pass = etPass.getText().toString();
                String confPass = etConfPass.getText().toString();
                if(name.isEmpty() || phone.isEmpty() || mail.isEmpty()|| address.isEmpty() || pass.isEmpty() || confPass.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter all the fields!", Toast.LENGTH_SHORT).show();
                }
                else if(!pass.equals(confPass)){
                    Toast.makeText(RegisterActivity.this, "Your password are not same!", Toast.LENGTH_SHORT).show();
                }
                else{
                    person.setName(name);
                    person.setNumber(phone);
                    person.setMail(mail);
                    person.setAddress(address);
                    ref.child(String.valueOf(maxid+1)).setValue(person);
                    mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Sucessfully Registered!", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}