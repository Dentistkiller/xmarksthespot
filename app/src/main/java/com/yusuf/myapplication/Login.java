package com.yusuf.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView login;
    EditText userName;
    EditText password;
    Button loginBtn;
    Button regBTN;
    Button newPass;
    ImageView img;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //type casting

        login = findViewById(R.id.textView2);
        userName = findViewById(R.id.edUsername);
        password = findViewById(R.id.edPassword);
        loginBtn = findViewById(R.id.button);
        regBTN = findViewById(R.id.RegBtn);
        img = findViewById(R.id.imageView);

        mAuth = FirebaseAuth.getInstance();

    }

    public void loginButton (View view)
    {
        // btnclick events --> paras of view class
        //Toast.makeText(this,"You are offline try again later",Toast.LENGTH_SHORT).show();
        //getemail and password from the edit texts

        try {
            String email = userName.getText().toString().trim();
            String pWord = password.getText().toString().trim();

            //housekeeping
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter your email to login", Toast.LENGTH_SHORT).show();
                userName.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(pWord)) {
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
                password.requestFocus();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, pWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Loging in...", Toast.LENGTH_SHORT).show();
                        ;

                        //clear text boxes
                        userName.setText("");
                        password.setText("");
                        userName.requestFocus();

                        //take to next screen --> about screen
                        Intent intent = new Intent(Login.this, MyMap.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Login.this, "Login failed ", Toast.LENGTH_SHORT).show();
                        userName.setText("");
                        password.setText("");
                        userName.requestFocus();
                    }
                }
            });
        } catch (Exception ex)
        {
            Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT).show();
        }
    }
    public  void Reg(View view)
    {
        Intent reg = new Intent(Login.this, Register.class);
        startActivity(reg);

    }

    public  void forgot (View view)
    {
        Intent forgot = new Intent(Login.this,ForgotPassword.class);
        startActivity(forgot);
    }

}