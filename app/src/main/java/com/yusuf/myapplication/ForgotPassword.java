package com.yusuf.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {


    private EditText editTextEmail_forgotPass;
    private Button btn_resetPassword_forgotPass;
    private ProgressBar progressBar_forgotPass;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail_forgotPass = (EditText) findViewById(R.id.emailAddress_Forgot);
        btn_resetPassword_forgotPass = (Button) findViewById(R.id.btn_resetPass);
        progressBar_forgotPass = (ProgressBar) findViewById(R.id.progressBar_Forgot);

        mAuth = FirebaseAuth.getInstance();

        btn_resetPassword_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = editTextEmail_forgotPass.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail_forgotPass.setError("Email Address is required!");
            editTextEmail_forgotPass.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail_forgotPass.setError("Please provide a valid email address!");
            editTextEmail_forgotPass.requestFocus();
            return;
        }

        progressBar_forgotPass.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Please check your email to reset your password.", Toast.LENGTH_LONG).show();
                    // redirect the login
                    Intent intent = new Intent(ForgotPassword.this, Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgotPassword.this, "Please Try Again!", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}


