package com.yusuf.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    //variables
    EditText txtEmail;
    EditText txtPassword;
    EditText txtConfirmPassword;
    FirebaseAuth mAuth;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        //type cast each variable
        txtEmail = findViewById(R.id.edEmail);
        txtPassword = findViewById(R.id.edPasswordreg);
        txtConfirmPassword = findViewById(R.id.edConfirm);
        btnRegister = findViewById(R.id.buttonReg);
    }

    //register method
    public void registerOnClick (View view)
    {
        //if the user clicks this button --> get the values --> pass into variables
        if (view.getId() == R.id.buttonReg)
        {
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String confirmPassword = txtConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter an email address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Confirm your password", Toast.LENGTH_SHORT).show();
                return;
            }

            //ranger checks --> minimum 6
            if (password.length() <6 || confirmPassword.length() <6)
            {
                Toast.makeText(this, "Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                return;
            }
            if (confirmPassword.equals(password))
            {
                //bring in firebase
                //create this user
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Register.this,"reg Successful", Toast.LENGTH_SHORT).show();
                            //allow them to login
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(Register.this,"Reg failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }

    }

    public  void  CancelClick(View view)
    {
        Toast.makeText(Register.this, "reg cancelled",Toast.LENGTH_SHORT).show();
        txtEmail.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }

    public void Back2Login(View view)
    {
        Intent b2l = new Intent(Register.this,Login.class);
        startActivity(b2l);
    }


}