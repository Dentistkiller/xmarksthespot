package com.yusuf.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView heading;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //type cast variables

        heading = findViewById(R.id.tv);
        logo = findViewById(R.id.imageView);


        //create and set timer

        Timer RunSplash = new Timer();

        //give the timer a task to do
        TimerTask ShowSplash = new TimerTask()
        {
            @Override
            public void run()
            {
                finish();

                //launch 2nd screen > login screen
                //intent service --> create an object of the 2nd screen
                Intent firstIntent = new Intent(MainActivity.this,Login.class);
                startActivity(firstIntent);

            }
        };
        //start the timer
        RunSplash.schedule(ShowSplash,600);
    }
}